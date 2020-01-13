package com.linoer.app.utils.net.http;

import com.alibaba.fastjson.JSON;
import com.linoer.app.model.net.http.OkHttpParam;
import com.linoer.app.model.net.http.OkHttpResult;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于okhttp的通用请求类
 */
public class OkHttpClients {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpClients.class);

    /**
     * get请求
     */
    public static <T> OkHttpResult<T> get(OkHttpParam restParam, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null) {
            url = url + restParam.getApiPath();
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求json数据
     */
    public static <T> OkHttpResult<T> post(OkHttpParam restParam, String reqJsonData, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null) {
            url = url + restParam.getApiPath();
        }
        RequestBody body = RequestBody.create(restParam.getMediaType(), reqJsonData);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求map数据
     */
    public static <T> OkHttpResult<T> post(OkHttpParam restParam, Map<String, String> parms, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null) {
            url = url + restParam.getApiPath();
        }
        FormBody.Builder builder = new FormBody.Builder();

        if (parms != null) {
            for (Map.Entry<String, String> entry : parms.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        FormBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * 返回值封装成对象
     */
    private static <T> OkHttpResult<T> exec(
            OkHttpParam restParam,
            Request request,
            Class<T> tClass) throws Exception {

        OkHttpResult<? extends Object> clientResult = exec(restParam, request);
        String result = clientResult.getResult();
        int status = clientResult.getStatus();

        T t = null;
        if (status == 200) {
            if ("".equalsIgnoreCase(result)) {
                t = JSON.parseObject(result, tClass);
            }
        } else {
            try {
                result = JSON.parseObject(result, String.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new OkHttpResult<>(clientResult.getStatus(), result, t);
    }

    /**
     * 执行方法
     */
    private static OkHttpResult exec(
            OkHttpParam restParam,
            Request request) throws Exception {

        OkHttpResult result = null;

        OkHttpClient client = null;
        ResponseBody responseBody = null;
        try {
            client = new OkHttpClient();

            client.newBuilder()
                    .connectTimeout(restParam.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(restParam.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(restParam.getWriteTimeout(), TimeUnit.MILLISECONDS);
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseBody = response.body();
                if (responseBody != null) {
                    String responseString = responseBody.string();
                    result = new OkHttpResult<>(response.code(), responseString, null);
                }
            } else {
                logger.error("http get request error:" + response.code());
                throw new Exception(response.message());
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
            if (client != null) {
                client.dispatcher().executorService().shutdown();   //清除并关闭线程池
                client.connectionPool().evictAll();                 //清除并关闭连接池
                try {
                    if (client.cache() != null) {
                        Objects.requireNonNull(client.cache()).close();                         //清除cache
                    }
                } catch (IOException e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
        return result;
    }

}
