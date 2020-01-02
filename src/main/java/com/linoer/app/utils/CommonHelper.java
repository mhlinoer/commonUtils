package com.linoer.app.utils;

import com.linoer.app.concurrent.MyThreadInterface;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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
     * 参考方法，对命令行进行解析
     *
     * @param args 命令行参数
     * @return 解析后的参数
     * @throws ParseException Base for Exceptions thrown during parsing of a command-line
     */
    public CommandLine getOptionsCommandLine(String[] args) throws ParseException {
        Options options = new Options();

        Option option = new Option("n", "name", true, "the name of this agent");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("f", "conf-file", true, "specify a conf file");
        option.setRequired(true);
        options.addOption(option);

        option = new Option(null, "no-reload-conf", false, "do not reload " +
                "conf file if changed");
        options.addOption(option);

        option = new Option("h", "help", false, "display help text");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();

        return parser.parse(options, args);
    }

    /**
     * 优雅关闭线程池
     *
     * @param threadPool 线程池
     * @param alias 标记
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
     * @param threadApp 线程
     * @param alias 名字
     */
    public void addClosedThreadHook(MyThreadInterface threadApp, String alias){
        Runtime.getRuntime().addShutdownHook(new Thread( alias + "-shutdown-hook") {
            @Override
            public void run() {
                threadApp.stop();
            }
        });
    }

}
