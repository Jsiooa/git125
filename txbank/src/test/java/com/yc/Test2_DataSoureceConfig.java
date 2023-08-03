package com.yc;
import com.alibaba.druid.pool.DruidDataSource;
import com.yc.configs.Config;
import com.yc.configs.DataSourceConfig;
import junit.framework.TestCase;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class,Config.class})
@Log4j2
public class Test2_DataSoureceConfig extends TestCase {
    @Autowired
    private DataSourceConfig dsc;

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("dataSource")
    private DataSource ds;


    @Autowired
    @Qualifier("dbcpDataSource")
    private DataSource dbcpDataSource;

    @Autowired
    @Qualifier("druidDataSource")
    private DataSource druidDattaSource;

    @Autowired
    private TransactionManager tx;

    @Test
    public void testTransactionManager(){
        log.info(tx);
    }

    @Test
    public void testDBCPConnection() throws SQLException {
        Assert.assertNotNull(ds.getConnection());
        Connection con=ds.getConnection();
        log.info(con);
    }

    @Test
    public void testDbcpDataSource() throws SQLException {
        Assert.assertNotNull(druidDattaSource.getConnection());
        Connection con=ds.getConnection();
        log.info(con);
    }
    @Test
    public void testDruidDataSource() throws SQLException {
        Assert.assertNotNull(druidDattaSource.getConnection());
        Connection connection=druidDattaSource.getConnection();
        log.info(connection+"\t"+((DruidDataSource)druidDattaSource).getInitialSize());
    }


    @Test
    public void testPropertySource(){
        Assert.assertEquals("root",dsc.getUsername());

        log.info(dsc.getUsername());
    }
    @Test
    public void testEnvironment(){
        log.info(environment.getProperty("jdbc.password")+"\t"+environment.getProperty("user.home"));
    }



}
