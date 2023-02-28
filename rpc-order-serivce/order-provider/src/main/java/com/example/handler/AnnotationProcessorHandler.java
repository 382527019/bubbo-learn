package com.example.handler;

import com.example.RpcRequest;
import com.example.annotation.Mediator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:22:43
 */
public class AnnotationProcessorHandler implements Runnable{
    private Socket socket;

    public AnnotationProcessorHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //IO操作
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            System.out.println("=====拿到客户端数据，反序列化");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest =(RpcRequest) objectInputStream.readObject();//反序列化

            Mediator mediator = Mediator.getInstance();//反射调用
            Object processor = mediator.processor(rpcRequest);
            System.out.println("=====服务端收到客户端请求处理后："+processor);
            System.out.println("=====发送给客户端数据，序列化");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(processor);//返回给客户端
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
//            objectInputStream.close();
        }

    }
}
