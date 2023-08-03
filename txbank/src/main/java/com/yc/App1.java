package com.yc;

import com.yc.biz.AccountBiz;
import com.yc.configs.Config;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class App1 extends TestCase {

    @Autowired
    private AccountBiz accountBiz;

}
