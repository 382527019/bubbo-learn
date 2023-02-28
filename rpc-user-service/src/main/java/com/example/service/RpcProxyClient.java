package com.example.service;

import com.example.handler.RemoteInvocationHandler;

import java.lang.reflect.Proxy;
/**
 * Created with IDEA
 * author:YunGui Hhuang
 * Date:2022/11/27
 * Time:16:19
 */
public class RpcProxyClient {

    /**
     * 动态代理执行 Proxy.newProxyInstance(lassLoader loader,
     *                                    Class<?>[] interfaces,
     *                                    InvocationHandler h)
     *  InvocationHandler 动态代理实现的接口
     * @param interfaceCls 代理类加载器
     * @param host
     * @param port
     * @param <T>
     * @return
     */
    public <T> T clientProxy(final Class<T> interfaceCls, final String host, final int port) {
        System.out.println("=====动态代理执行："+interfaceCls.toString());
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, new RemoteInvocationHandler(host,port));
    }

    /*public <T> T clientProxy(final Class<T> interfaceCls, final String host, final int port) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[]{interfaceCls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //TODO 反射调用 处理加工
                return "这是客户端代理处理结果";
            }
        });
    }*/
}
