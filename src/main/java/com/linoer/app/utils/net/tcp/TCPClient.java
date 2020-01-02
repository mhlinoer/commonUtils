package com.linoer.app.utils.net.tcp;

import com.alibaba.fastjson.JSONObject;
import com.linoer.app.utils.CommonHelper;

import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    public static void tcpSend(String host, Integer port) throws Exception {
        System.out.println("client start sending ...");
        Socket client = new Socket(host, port);
        OutputStream os = client.getOutputStream();
        String msg = String.format("{'user':'tom','msg':%s}", CommonHelper.getCurrentDateTimeStr());
        JSONObject jsonObject = JSONObject.parseObject(msg);
        os.write(jsonObject.toJSONString().getBytes());
        System.out.println("send message:" + msg);
        os.close();
        client.close();
    }
}
