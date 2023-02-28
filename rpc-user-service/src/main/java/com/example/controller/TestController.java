package com.example.controller;

import com.example.annatation.Refernce;
import com.example.service.IOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:23:28
 */
@RestController
public class TestController {

    @Refernce
    IOrderService orderService;

    @GetMapping("/test")
    String get() {
        return orderService.selectOrderList();
    }
}
