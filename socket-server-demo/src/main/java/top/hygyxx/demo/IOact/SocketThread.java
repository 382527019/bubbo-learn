package top.hygyxx.demo.IOact;

import java.io.*;
import java.net.Socket;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/20
 * 异步线程处理IO流
 * Time:23:25
 */
public class SocketThread implements Runnable{
    private final Socket socket;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader bufferedReader = null;//输入流
        try {
            //读
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientStrInfo = bufferedReader.readLine();//读取客户端的一行数据
            System.out.println("=====The Client message was received: =====\n"+clientStrInfo);

            //写
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("=====I got the message:=====\n");//以换行符说明服务端已发送完成，客户端不再监听
            bufferedWriter.flush();

            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
