package com.example.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:21:54
 */
@Target(ElementType.TYPE)//注解范围
@Retention(RetentionPolicy.RUNTIME)//运行时用
@Component
public @interface RemoteService {
}
