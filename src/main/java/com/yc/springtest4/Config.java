package com.yc.springtest4;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
//@PropertySource(value = "classpath:db.properties")
@PropertySource(value ="file:D:\\源辰上课\\idea\\test\\gitTest01\\src\\main\\resources\\db.properties")
public class Config {
}
