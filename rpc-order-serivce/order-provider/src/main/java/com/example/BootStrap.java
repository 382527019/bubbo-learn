package com.example;

import com.example.service.OrderServiceImpl;
import com.example.service.RpcProxyServer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:15:47
 */
@Configurable
@ComponentScan("com.example")
public class BootStrap {
    public static void main(String[] args) {
//        generalAct();
        ApplicationContext context = new AnnotationConfigApplicationContext(BootStrap.class);

    }

    static void generalAct() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        RpcProxyServer rpcProxyServer = new RpcProxyServer();
        rpcProxyServer.publisher(orderService, 8080);//发布
    }

}
