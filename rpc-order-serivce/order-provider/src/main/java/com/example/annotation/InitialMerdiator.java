package com.example.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:22:18
 */
//BeanPostProcessor
// 称Bean后置处理器，它是Spring中定义的接口，
// 在Spring容器的创建过程中（具体为Bean初始化前后）会回调BeanPostProcessor中定义的两个方法
//postProcessAfterInitialization 方法的返回值会被Spring容器作为处理后的Bean注册到容器中
@Component
public class InitialMerdiator implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //自定义服务发布注解
        if (bean.getClass().isAnnotationPresent(RemoteService.class)) {
            //拿到bean的方法
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                //组织key
                String key = bean.getClass().getInterfaces()[0].getName() + "." + method.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.map.put(key, beanMethod);
            }
        }
        //处理后的Bean注册到容器中
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
