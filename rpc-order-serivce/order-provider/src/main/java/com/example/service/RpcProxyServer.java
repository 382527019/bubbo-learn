package com.example.service;

import com.example.handler.ProcessorHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:16:16
 */
public class RpcProxyServer {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public void publisher(Object service, int port) {
        System.out.println("=====开始监听"+service.toString()+"=====端口："+port);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();//监听请求
                System.out.println("=====收到请求"+socket);
                executorService.execute(new ProcessorHandler(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
