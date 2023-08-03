package com.yc.staticproxy;

public class StaticProxy implements OrderBiz{
    //目标类引用，利用setXXX,或者构造方法  注入
    private OrderBiz orderBiz;

    public StaticProxy(OrderBiz orderBiz) {
        this.orderBiz = orderBiz;
    }

    @Override
    public void addOrder(int pid, int num) {
        //前置增强
        showHello();
        this.orderBiz.addOrder(pid,num);

        showBye();
        //后置增强
    }

    @Override
    public void findOrder() {
        this.orderBiz.findOrder();
    }


    public void showHello(){
        System.out.println("hello");

    }

    public void showBye(){
        System.out.println("bye");
    }
}
