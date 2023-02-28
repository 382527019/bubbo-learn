package com.example.annotation;

import com.example.handler.AnnotationProcessorHandler;
import com.example.handler.ProcessorHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:22:39
 */
//spring容器启动完成后会监听到一个ContextRedfreshdEvent事件
@Component
public class SocketServerInitial implements ApplicationListener<ContextRefreshedEvent> {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("=====开始监听" + "=====端口：" + 8888);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();//监听请求
                System.out.println("=====收到请求" + socket);
                executorService.execute(new AnnotationProcessorHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
