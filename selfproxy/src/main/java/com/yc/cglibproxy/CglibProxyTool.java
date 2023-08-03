package com.yc.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyTool implements MethodInterceptor {
    private Object target;

    public CglibProxyTool(Object target) {
        this.target = target;
    }

    public Object createProxy(){
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        Object proxy=enhancer.create();
        return proxy;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().startsWith("add")){
            //前置增强
            showHello();
        }

        Object returnValue=method.invoke(target,objects);
        return returnValue;
    }
    public void showHello(){
        System.out.println("hello");

    }
}
