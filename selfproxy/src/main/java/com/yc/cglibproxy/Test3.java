package com.yc.cglibproxy;

public class Test3 {
    public static void main(String[] args) {
        CglibProxyTool jpt=new CglibProxyTool(new OrderBizImpl());

        OrderBizImpl ob= (OrderBizImpl) jpt.createProxy();
        System.out.println("生成的代理对象:"+ob.toString());
        ob.findOrder();
        ob.addOrder(1,99);
    }
}
