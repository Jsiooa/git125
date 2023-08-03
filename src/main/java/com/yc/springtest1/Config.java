package com.yc.springtest1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.yc.springtest1.biz","com.yc.springtest1.dao"})
public class Config {

}
