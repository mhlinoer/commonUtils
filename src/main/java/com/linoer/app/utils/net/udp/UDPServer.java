package com.linoer.app.utils.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {
    private DatagramSocket ds;

    public UDPServer(Integer port) throws SocketException {
        this.ds = new DatagramSocket(port);
    }

    public void openServer() throws IOException {
        while (true){
            byte [] buff = new byte [1024];
            DatagramPacket dp = new DatagramPacket(buff,buff.length);
            ds.receive(dp);
            String recStr = new String(dp.getData(), 0 , dp.getLength());
            System.out.println("received msg:" + recStr);
        }
    }

    public void close(){
        if (ds != null){
            ds.close();
        }
    }

//    public static void main(String[] args) {
//        UDPServer udpServer = null;
//        try {
//            udpServer = new UDPServer(3020);
//            udpServer.openServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if (udpServer != null){
//                udpServer.close();
//            }
//        }
//    }
}
