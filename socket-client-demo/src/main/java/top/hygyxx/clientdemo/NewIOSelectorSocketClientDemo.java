package top.hygyxx.clientdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/21
 * Time:22:14
 */
public class NewIOSelectorSocketClientDemo {
    static Selector selector;
    public static void main(String[] args) {
        try {
            selector = Selector.open();//1.多路复用器
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);//配置非阻塞（默认阻塞）
            socketChannel.connect((new InetSocketAddress("localhost",8080)));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);//2.注册连接事件
            while (true) {
                selector.select();//选择阻塞机制
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();//移除，防止重复key处理
                    if (selectionKey.isConnectable()) {//事件监听
                        handleConnect(selectionKey);//2.连接处理
                    }else if (selectionKey.isReadable()){//事件监听
                        handleRead(selectionKey);//缓冲区读入
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnect(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel =(SocketChannel) selectionKey.channel();
        if (socketChannel.isConnectionPending()){
            socketChannel.finishConnect();
        }
        socketChannel.configureBlocking(false);//非阻塞
        socketChannel.write(ByteBuffer.wrap("=====我是客户端，给你发信息".getBytes()));
        socketChannel.register(selector,SelectionKey.OP_READ);//注册读入事件
    }
    private static void handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel =(SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        System.out.println("=====客户端收到信息=====："+ new String(byteBuffer.array()));
    }
}
