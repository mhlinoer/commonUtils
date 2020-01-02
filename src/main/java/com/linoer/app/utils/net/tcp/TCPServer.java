package com.linoer.app.utils.net.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;

public class TCPServer {

//    /**
//     * 多线程
//     * @param port
//     * @throws IOException
//     */
//    public static void multiTcpServer(Integer port) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(port);
//        while (true) {
//            System.out.println("Waiting for connection...");
//            final Socket activeSocket = serverSocket.accept();
//
//            System.out.println("Received connection from " + activeSocket);
//            Runnable runnable = () -> handleClientRequest(activeSocket);
//            ExecutorService executorService = Executors.newFixedThreadPool(8);
//            Future future = executorService.submit(new Thread(runnable));
//            try {
//                future.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 单线程
     * @param port
     * @throws IOException
     */
    public static void tcpServer(Integer port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Waiting for connection...");
            final Socket activeSocket = serverSocket.accept();

            System.out.println("Received connection from " + activeSocket);
            Runnable runnable = () -> handleClientRequest(activeSocket);
            new Thread(runnable).start();
        }
    }

    private static void handleClientRequest(Socket socket){
        try{
            BufferedReader socketReader = null;
            BufferedWriter socketWriter = null;
            socketReader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));

            String inMsg;
            while ((inMsg = socketReader.readLine()) != null) {
                System.out.println("Received msg: " + inMsg);
                socketWriter.write(inMsg);
                socketWriter.write("\n");
                socketWriter.flush();
            }
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
