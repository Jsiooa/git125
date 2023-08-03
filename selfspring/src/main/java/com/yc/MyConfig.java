package com.yc;

import org.ycframework.annotation.YcComponentScan;
import org.ycframework.annotation.YcConfiguration;
import org.ycframework.annotation.YcpropertySource;

@YcConfiguration
@YcComponentScan(basePackages = {"com.yc","com.yc2"})
@YcpropertySource(value = "db.properties")
public class MyConfig {
}
