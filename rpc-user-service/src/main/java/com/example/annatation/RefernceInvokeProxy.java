package com.example.annatation;

import com.example.handler.RemoteInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:23:34
 */
//BeanPostProcessor 后置处理器
@Component
public class RefernceInvokeProxy implements BeanPostProcessor {

    /** 路由处理器*/
    @Autowired
    RemoteInvocationHandler remoteInvocationHandler;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            //扫描注解，对@Refernce注解设置代理 Proxy.newProxyInstance
            if (field.isAnnotationPresent(Refernce.class)) {
               Object proxy= Proxy.newProxyInstance(field.getType().getClassLoader(), new Class<?>[]{field.getType()}, remoteInvocationHandler);
                field.setAccessible(true);
                try {
                    //对加@Refernce注解设置代理，实现inovcationHandler
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        return bean;
    }
}
