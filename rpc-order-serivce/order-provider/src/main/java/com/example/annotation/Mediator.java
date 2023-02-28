package com.example.annotation;

import com.example.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:21:58
 */
public class Mediator {

    private volatile static Mediator instance;
    //存储发布的服务实例
    public  static Map<String,BeanMethod> map = new ConcurrentHashMap<>();

    private Mediator(){};

    public static Mediator getInstance(){
        if (instance==null){
            synchronized(Mediator.class){
                if (instance==null){
                    instance = new Mediator();
                }
            }
        }
        return instance;
    }

    public Object processor(RpcRequest rpcRequest){
        String key = rpcRequest.getClassName() + "." + rpcRequest.getMethodName();//key
        BeanMethod beanMethod = map.get(key);//得到服务能调用的方法
        if (beanMethod==null){
            return null;
        }
        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        try {
            System.out.println("=====执行服务方法："+method); //执行
            return method.invoke(bean, rpcRequest.getArgs());
        } catch (IllegalAccessException |InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

}
