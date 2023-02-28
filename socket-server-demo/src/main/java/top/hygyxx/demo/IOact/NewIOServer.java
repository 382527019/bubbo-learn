package top.hygyxx.demo.IOact;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/21
 * NOI 管道channel非阻塞 ，io数据放缓冲区处理
 * Time:22:14
 */
public class NewIOServer {
    //非阻塞IO ServerSocketChannel//Channel Buffer Selector
    public static void main(String[] args) {
        try {
            ServerSocketChannel  severSocketChannel = ServerSocketChannel.open();
            severSocketChannel.configureBlocking(false);//配置非阻塞（默认阻塞）
            severSocketChannel.socket().bind(new InetSocketAddress(8080));

            while (true){
                SocketChannel socketChannel = severSocketChannel.accept();
                if (socketChannel!=null){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//分配缓冲区
                    socketChannel.read(byteBuffer);//数据读到缓冲区
                    System.out.println("=====Server receive info："+new String(byteBuffer.array()));
                    byteBuffer.flip();//翻转
                    socketChannel.write(byteBuffer);//写出
                }else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("=====连接未就绪=====");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
