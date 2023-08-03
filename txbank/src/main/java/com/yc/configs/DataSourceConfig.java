package com.yc.configs;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:db.properties")
@Data
@Log4j2
public class DataSourceConfig {

    @Value("root")
    private String username;
    @Value("aaaaaa")
    private String password;
    @Value("jdbc:mysql://localhost:3306/testbank?serverTimezone=UTC")
    private String url;
    @Value("com.mysql.cj.jdbc.Driver")
    private String driverclass;

    @Value("#{T(Runtime).getRuntime().availableProcessors()*2}")
    private int cpuCount;

    @Bean
    public TransactionManager dataSourceTransactionManager(@Autowired @Qualifier(value="druidDataSource")DataSource ds){
        DataSourceTransactionManager tx=new DataSourceTransactionManager();
        tx.setDataSource(ds);
        return tx;
    }

    @Bean(initMethod = "init",destroyMethod = "close")
    public DruidDataSource druidDataSource() {
        DruidDataSource dds=new DruidDataSource();
        dds.setUrl(url);
        dds.setUsername(username);
        dds.setPassword(password);
        dds.setDriverClassName(driverclass);


        log.info("配置druid的连接池大小:"+cpuCount);
        dds.setInitialSize(cpuCount);
        dds.setMaxActive(cpuCount*2);
        return dds;
    }
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setDriverClassName(driverclass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;

    }

    @Bean
    public DataSource dbcpDataSource(){
        BasicDataSource dataSource=new BasicDataSource();
        dataSource.setDriverClassName(driverclass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;

    }
}
