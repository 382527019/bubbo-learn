package com.example;

import com.example.service.IOrderService;
import com.example.service.RpcProxyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:16:19
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
//        generalAct();
    }

    static void generalAct() {
        RpcProxyClient rpcProxyClient = new RpcProxyClient();
        //动态代理调用 IOrderService
        IOrderService orderService = rpcProxyClient.clientProxy(IOrderService.class, "localhost", 8080);
        System.out.println("=====拿到执行结果：" + orderService.selectOrderList());
        System.out.println("=====拿到执行结果：" + orderService.orderById("id"));
    }
}
