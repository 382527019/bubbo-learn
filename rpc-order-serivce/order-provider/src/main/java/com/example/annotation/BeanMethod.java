package com.example.annotation;

import java.lang.reflect.Method;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:21:58
 */
public class BeanMethod {

    private Object bean;
    private Method method;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
