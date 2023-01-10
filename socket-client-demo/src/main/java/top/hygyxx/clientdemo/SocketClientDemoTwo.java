package top.hygyxx.clientdemo;

import java.io.*;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/20
 * Time:20:52
 */
public class SocketClientDemoTwo {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8080);
            //写
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("=====I'm the client-One and I've sent a message.=====\n");
            bufferedWriter.flush();

            //读
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverInfo = bufferedReader.readLine();
            System.out.println("===== server return info :=====\n"+serverInfo);

            bufferedWriter.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (socket!=null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
