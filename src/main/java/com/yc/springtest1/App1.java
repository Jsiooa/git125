package com.yc.springtest1;


import com.yc.springtest1.biz.UserBiz;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App1 {

    public static void main(String[] args) {
        ApplicationContext container=new AnnotationConfigApplicationContext(com.yc.springtest1.Config.class);
//        ApplicationContext ac=new AnnotationConfigApplicationContext(com.yc.springtest1.Config.class);


        UserBiz ub=(UserBiz) container.getBean("userBizImpl");
        ub.add("王五");
    }
}
