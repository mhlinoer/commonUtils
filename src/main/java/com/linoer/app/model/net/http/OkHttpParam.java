package com.linoer.app.model.net.http;

import okhttp3.MediaType;

/**
 * okhttp封装的请求类
 */
public class OkHttpParam {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 接口URL
     */
    private String apiUrl;

    /**
     * 接口路径
     */
    private String apiPath;

    /**
     * 读取超时时间
     */
    private int readTimeout = 30 * 1000;

    /**
     * 写入超时时间
     */
    private int writeTimeout = 30 * 1000;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 2 * 1000;

    /**
     * 编码类型
     */
    private MediaType mediaType = MEDIA_TYPE_JSON;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
