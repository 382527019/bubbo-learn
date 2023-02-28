package com.example.handler;

import com.example.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:17:33
 */
public class ProcessorHandler implements Runnable {
    private Socket socket;

    private Object service;

    public ProcessorHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
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

            Object invoke = invoke(rpcRequest);//反射调用
            System.out.println("=====服务端收到客户端请求处理后："+invoke);

            System.out.println("=====发送给客户端数据，序列化");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(invoke);//返回给客户端
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }finally {
//            objectInputStream.close();
        }

    }
//动态代理 反射执行
    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(rpcRequest.getClassName());
        Method method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getTypes());
        System.out.println("====动态代理 反射执行====类名："+clazz.getName()+"====方法："+method.getName()+"=====参数"+rpcRequest.getArgs().toString());
        return method.invoke(service, rpcRequest.getArgs());
    }
}
