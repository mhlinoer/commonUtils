package com.linoer.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linoer.app.concurrent.MyThreadInterface;
import com.linoer.app.model.OptionModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommonHelper {
    private final static Logger log = LoggerFactory.getLogger(CommonHelper.class);

    /**
     * 获取当前时间戳字符串
     *
     * @return 时间戳
     */
    public static String getCurrentDateTimeStr() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 对象转map
     *
     * @param obj 待转对象
     * @return map
     */
    public static Map convertToMap(Object obj) {
        try {
            if (obj instanceof Map) {
                return (Map) obj;
            }
            Map returnMap = BeanUtils.describe(obj);
            returnMap.remove("class");
            return returnMap;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.getMessage();
        }
        return null;
    }

    /**
     * 对命令行进行解析
     *
     * @param args         命令行参数
     * @param optionModels 参数详情
     * @return 解析后的参数
     * @throws ParseException Base for Exceptions thrown during parsing of a command-line
     */
    public static CommandLine getOptionsCommandLine(String[] args, List<OptionModel> optionModels) throws ParseException {
        Options options = new Options();
        optionModels.forEach(
                optionModel -> {
                    Option option = new Option(
                            optionModel.getAbbreviatedName(),
                            optionModel.getFullName(),
                            optionModel.isHasArg(),
                            optionModel.getDescription()
                    );
                    option.setRequired(optionModel.isRequired());
                    options.addOption(option);
                }
        );
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    /**
     * 优雅关闭线程池
     *
     * @param threadPool 线程池
     * @param alias      标记
     */
    private void shutdownThreadPool(ExecutorService threadPool, String alias) {
        log.info("Start to shutdown the thead pool: {}", alias);

        threadPool.shutdown(); // 使新任务无法提交.
        try {
            // 等待未完成任务结束
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // 取消当前执行的任务
                log.warn("Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");

                // 等待任务取消的响应
                if (!threadPool.awaitTermination(30, TimeUnit.SECONDS))
                    log.error("Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
            }
        } catch (InterruptedException ie) {
            // 重新取消当前线程进行中断
            threadPool.shutdownNow();
            log.error("The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconcistent state. Please check the biz logs.");

            // 保留中断状态
            Thread.currentThread().interrupt();
        }

        log.info("Finally shutdown the thead pool: {}", alias);
    }

    /**
     * 添加线程关闭hook
     *
     * @param threadApp 线程
     * @param alias     名字
     */
    public void addClosedThreadHook(MyThreadInterface threadApp, String alias) {
        Runtime.getRuntime().addShutdownHook(new Thread(alias + "-shutdown-hook") {
            @Override
            public void run() {
                threadApp.stop();
            }
        });
    }

    /**
     * 获取偏移的日期
     *
     * @param offset
     * @return
     */
    private static String getOffsetDayTime(Integer offset, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, offset);
        Date time = c.getTime();
        return sdf.format(time);
    }

    /**
     * 通用请求格式转换，前端请求为json或者form格式时，都能讲请求参数解析出来
     *
     * @param httpServletRequest request
     * @return request params
     */
    public static Map<String, String> commonHttpRequestParamConvert(HttpServletRequest httpServletRequest) {
        Map<String, String> params = new HashMap<>();
        try {
            Map<String, String[]> requestParams = httpServletRequest.getParameterMap();
            if (requestParams != null && !requestParams.isEmpty()) {
                requestParams.forEach((key, value) -> params.put(key, value[0]));
            } else {
                StringBuilder paramSb = new StringBuilder();
                try {
                    String str = "";
                    BufferedReader br = httpServletRequest.getReader();
                    while ((str = br.readLine()) != null) {
                        paramSb.append(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("httpServletRequest get requestBody error, cause : {}", e);
                }
                if (paramSb.length() > 0) {
                    JSONObject paramJsonObject = JSON.parseObject(paramSb.toString());
                    if (paramJsonObject != null && !paramJsonObject.isEmpty()) {
                        paramJsonObject.forEach((key, value) -> params.put(key, String.valueOf(value)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("commonHttpRequestParamConvert error, cause :{}", e);
        }
        return params;
    }

    /**
     * 获取当前jar所在目录
     *
     * @return 当前jar绝对路径
     */
    public static String getLocalJarPath() {
        String jarPath = CommonHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.endsWith(".jar")) {
            jarPath = jarPath.substring(0, jarPath.lastIndexOf("/"));
        }
        return jarPath;
    }

    /**
     * 根据base路径和相对路径，拼接成绝对路径
     *
     * @param base /home
     * @param dir  ./conf/conf.ini     ../conf/conf.ini
     * @return 绝对路径
     */
    public static String generatorRealPath(String base, String dir) {
        try {
            if (!base.endsWith("/")) {
                base = base + "/";
            }
            if (dir.startsWith("./")) {
                return base + dir.substring(2);
            }
            // 检测有几层上级目录
            String[] splitDir = dir.split("\\.\\./");
            // 检测base有几层目录
            String[] splitBase = base.split("/");
            // base的目录还不如dir的上层目录多，肯定有问题
            if (splitBase.length < splitDir.length) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            // 拼凑前半部分目录
            for (int i = 1; i < splitBase.length - splitDir.length + 1; i++) {
                stringBuilder.append("/");
                stringBuilder.append(splitBase[i]);
            }
            stringBuilder.append("/").append(dir.substring(3 * (splitDir.length - 1)));
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件写入工具
     * 按行写入
     *
     * @param fileName 文件名
     * @param content  内容
     */
    public static void writeStrLinesToFile(String fileName, List<String> content) {
        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bfw = Files.newBufferedWriter(filePath);
            content.forEach(
                    con -> {
                        try {
                            bfw.write(con);
                            bfw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断两个时间是否为同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        return isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);
    }

    public static List<String> readFirstAndLastLine(File file) throws IOException {
        return readFirstAndLastLine(file, "utf-8");
    }

    /**
     * 获取文件的最后一行
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<String> readFirstAndLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        List<String> lineDataList = new ArrayList<>(2);
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(0);
            String firstLine = raf.readLine();
            if (firstLine == null) {
                firstLine = raf.readLine();
            }
            lineDataList.add(firstLine);
            long len = raf.length();
            if (len == 0L) {
                return null;
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    lineDataList.add(new String(bytes));
                } else {
                    lineDataList.add(new String(bytes, charset));
                }
                return lineDataList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("error :" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取请求中的ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    assert inet != null;
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get ip addr from request failed:" + e.getMessage());
        }
        return ipAddress;
    }

    /**
     * 按对象key值区分
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
