package com.yc.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyTool implements InvocationHandler {

    //目标类
    private Object target;

    public JdkProxyTool(Object obj) {
        this.target = obj;
    }
    //生成代理对象的方法
    public Object createProxy(){
        Object proxy= Proxy.newProxyInstance(JdkProxyTool.class.getClassLoader(),
                                                target.getClass().getInterfaces(),
                                                this);
        return proxy;
    }

    //挡在主程序中调用生成的Proxy中的方法，会自动回调这个invoke(),在这个invoke（）加入增强 切莫这些功能
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().startsWith("add")){
            //前置增强
            showHello();
        }

        Object returnValue=method.invoke(target,args);
        return returnValue;
    }

    public void showHello(){
        System.out.println("hello");

    }

    public void showBye(){
        System.out.println("bye");
    }
}
