package top.hygyxx.demo.actSerializable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/23
 * Time:23:24
 */
//对象序列化
public class SocketClient {

    public static void main(String[] args) {
        try {
            User user = new User();
            user.setName("用户名");
            Socket socket = new Socket("localhost", 8080);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(user);//序列化
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
