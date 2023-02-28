package com.example.service;

import com.example.annotation.RemoteService;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:15:41
 */
@RemoteService//spring扫描到这个注解后自动发布服务
public class OrderServiceImpl implements IOrderService{
    @Override
    public String selectOrderList() {
        System.out.println("=====调用成功：执行接口方法"+this.getClass().toString());
        return "查询到的订单列表";
    }

    @Override
    public String orderById(String id) {
        return "查询到的订单";
    }
}
