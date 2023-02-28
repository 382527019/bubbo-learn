package com.example.handler;

import com.example.RpcNetTransport;
import com.example.RpcRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:16:47
 */
//实现 InvocationHandler动态代理
@Component
public class RemoteInvocationHandler implements InvocationHandler {
    @Value("${me.host}")
    private String host;
    @Value("${me.port}")
    private int port;


    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public RemoteInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //建立远程连接
        RpcNetTransport rpcNetTransport = new RpcNetTransport(host, port);
//        Socket socket = rpcNetTransport.newSocket();
        System.out.println("====客户端建立远程连接");
//        传数据{类名、方法名、参数、参数类型}
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());//类名
        rpcRequest.setMethodName(method.getName());//方法名
        rpcRequest.setArgs(args);//参数
        rpcRequest.setTypes(method.getParameterTypes());//参数类型

        return rpcNetTransport.send(rpcRequest);//发送
    }
}
