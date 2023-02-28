package com.example.annatation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:23:30
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)//运行时用
@Component
public @interface Refernce {
}
