package com.linoer.app.utils.net.http;

import com.linoer.app.model.net.http.HttpHeaderConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 通用http请求类
 */
public class CommonCloseableHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(CommonCloseableHttpClient.class);
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 发送HttpGet请求
     * @param url
     * @return
     */
    public static HttpResponse doGet(String url, Map<String,String> parameter , Map<String,String> header) throws URISyntaxException {
        // url不为空
//        Preconditions.checkArgument(!StringUtils.isEmpty(url));
        URI uri = getUriBuilder(url,parameter).build();
        logger.info("debug doGet uri :" + uri.getPath());
        HttpGet httpget = new HttpGet(uri);

        // 给http请求添加head信息
        addHeader(httpget,header);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return response;
    }

    /**
     * 发送HttpPost请求，参数为parameter
     * @return
     */
    public static String doPost(String url, Map<String,Object> parameter, Map<String, String> header) throws Exception{
        // url不为空
//        Preconditions.checkArgument(!StringUtils.isEmpty(url));

        HttpPost httppost = new HttpPost(url);

        if(header.containsKey("Content-Type")){
            switch (header.get("Content-Type")){
                // form表单提交数据
                case HttpHeaderConstants.CONTENT_TYPE_FORM:
                    UrlEncodedFormEntity entity = getFormEntity(parameter);
                    entity.setContentType(HttpHeaderConstants.CONTENT_TYPE_FORM);
                    httppost.setEntity(entity);
                    break;

                // json表单提交数据
                case HttpHeaderConstants.CONTENT_TYPE_JSON:
                    StringEntity stringEntity = getJsonEntity(parameter);
                    httppost.setEntity(stringEntity);
                    break;
            }
        }

        // 给请求添加head信息
        addHeader(httppost,header);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = Objects.requireNonNull(response).getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity1);
            System.out.println(result);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URIBuilder getUriBuilder(String url, Map<String, String> parameter) throws URISyntaxException {
        /*
         * 由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
         */
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> list = new LinkedList<>();
        if(!CollectionUtils.isEmpty(Collections.singleton(parameter))){
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                BasicNameValuePair param = new BasicNameValuePair(entry.getKey(),entry.getValue());
                list.add(param);
            }
        }
        uriBuilder.setParameters(list);
        return uriBuilder;
    }

    private static void addHeader(HttpRequestBase httpRequestBase, Map<String, String> header){
//        Preconditions.checkNotNull(httpRequestBase);
        if(!CollectionUtils.isEmpty(Collections.singleton(header))){
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpRequestBase.addHeader(entry.getKey(),entry.getValue());
            }
        }
    }

    private static UrlEncodedFormEntity getFormEntity(Map<String, Object> map){
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if(!CollectionUtils.isEmpty(Collections.singleton(map))){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        return new UrlEncodedFormEntity(formparams, Consts.UTF_8);
    }

    private static StringEntity getJsonEntity(Map<String, Object> map){
        com.alibaba.fastjson.JSONObject jsonParam = new com.alibaba.fastjson.JSONObject();
        if(!CollectionUtils.isEmpty(Collections.singleton(map))){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonParam.put(entry.getKey(),entry.getValue());
            }
        }
        return new StringEntity(jsonParam.toJSONString(), ContentType.APPLICATION_JSON);
    }
}
