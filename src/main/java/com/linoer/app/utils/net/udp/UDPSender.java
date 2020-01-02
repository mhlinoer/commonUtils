package com.linoer.app.utils.net.udp;

import com.linoer.app.utils.CommonHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPSender {
    private int remotePort = 8088;
    private InetAddress address;

    public UDPSender(String ip, Integer port) throws UnknownHostException {
        this.remotePort = port;
        this.address = InetAddress.getByName(ip);
    }

    public void send() {
        String jsonStringMessage = String.format("{'user':'jerry','msg':%s}", CommonHelper.getCurrentDateTimeStr());
        try (DatagramSocket ds = new DatagramSocket()) {
            //发送数据报
            byte[] buffer = jsonStringMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, remotePort);
            ds.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            new UDPSender("localhost", 3020).send("aaaaaa");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }
}
