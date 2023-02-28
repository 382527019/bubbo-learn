package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:16:58
 */
//RPC连接通道
public class RpcNetTransport {
    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Socket newSocket(){
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    //发送
   public Object send(RpcRequest request){
        Socket socket = newSocket();
       System.out.println("=====发送："+socket.toString());

       //IO操作
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);//序列化
            System.out.println("=====序列化");
            objectOutputStream.flush();
            System.out.println("=====反序列化");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            System.out.println("=====服务端返回结果："+object);
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
//            objectOutputStream.close();
        }
        return socket;
    }
}
