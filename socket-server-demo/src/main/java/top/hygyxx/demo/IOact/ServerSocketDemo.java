package top.hygyxx.demo.IOact;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/20
 * 异步线程优化阻塞BIO
 * Time:20:24
 */
public class ServerSocketDemo {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();//监听客户端连接（连接阻塞）
                System.out.println("=====Port Number: =====" + socket.getPort());
                executorService.execute(new SocketThread(socket));//异步线程池处理IO
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
