package top.hygyxx.clientdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/21
 * Time:22:31
 */
public class NewIOSocketClientDemo {

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost",8080));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("======我是SocketChannel客户端".getBytes());
            byteBuffer.flip();//翻转
            socketChannel.write(byteBuffer);

            //读取数据
            byteBuffer.clear();
            int count = socketChannel.read(byteBuffer);
            if ( count> 0) {
                System.out.println("=====收到服务端数据====="+new String(byteBuffer.array()));
            }else System.out.println("=====没收到数据");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
