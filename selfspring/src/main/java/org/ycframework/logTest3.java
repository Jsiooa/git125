package org.ycframework;

import com.yc.MyConfig;
import org.ycframework.context.YcAnnotationConfigApplicationContext;
import org.ycframework.context.YcApplicationContext;

public class logTest3 {
    public static void main(String[] args) {
        YcApplicationContext ac=new YcAnnotationConfigApplicationContext(MyConfig.class);
//        UserBiz ub=(UserBiz)ac.getBean("userBizImpl");
//        ub.add("张三");
//        Logger logger= LoggerFactory.getLogger(logTest3.class);
//        logger.error("error");
//        logger.info("info");
//        logger.trace("trace");
//        logger.debug("debug");
//        logger.warn("warn");
    }
}
