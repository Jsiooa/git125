package com.yc.jdkproxy;

import com.yc.staticproxy.OrderBiz;
import com.yc.staticproxy.OrderBizImpl;
import com.yc.staticproxy.StaticProxy;

public class Test2 {
    public static void main(String[] args) {
        //设置代理生成的字节码 保存到当前目录
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        JdkProxyTool jpt=new JdkProxyTool(new OrderBizImpl());
        OrderBiz ob= (OrderBiz) jpt.createProxy();
//        System.out.println("生成的代理类:"+ob);

        ob.addOrder(1,100);
        ob.findOrder();
    }
}
