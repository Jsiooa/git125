package com.yc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class logTest2 {
    public static void main(String[] args) {
        Logger logger= LoggerFactory.getLogger(logTest2.class);
        logger.error("error");
        logger.info("info");
        logger.trace("trace");
        logger.debug("debug");
        logger.warn("warn");
    }
}
