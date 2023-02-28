package top.hygyxx.demo.actSerializable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/23
 * Time:23:23
 */
//反序列化
public class MyServerSocket {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user = (User)objectInputStream.readObject();
            System.out.println(user);//User{name='用户名'}
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
