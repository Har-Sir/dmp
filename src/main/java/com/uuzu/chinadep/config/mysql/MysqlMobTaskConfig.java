package com.uuzu.chinadep.config.mysql;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by lixing on 2017/6/28.
 */
@Configuration
@MapperScan(basePackages = "com.uuzu.chinadep.taskmapper",sqlSessionFactoryRef = "mobTaskSqlSessionFactory")
public class MysqlMobTaskConfig {

    @Value("${mybatis.mappertask-locations}")
    private String mappertaskLocations;
    @Value("${mybatis.typetask-aliases-package}")
    private String typetaskAliasesPackage;

    @ConfigurationProperties("spring.datasource.druid.mysql2")
    @Bean
    public DataSource mysqlTaskDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "mobTaskTransactionManager")
    public DataSourceTransactionManager rdsTransactionManager() {
        return new DataSourceTransactionManager(mysqlTaskDataSource());
    }

    @Bean(name = "mobTaskSqlSessionFactory")
    public SqlSessionFactory rdsSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mysqlTaskDataSource());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(this.mappertaskLocations));
        sessionFactory.setTypeAliasesPackage(typetaskAliasesPackage);
        return sessionFactory.getObject();
    }

}
