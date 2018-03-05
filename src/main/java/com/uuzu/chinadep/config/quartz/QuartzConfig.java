package com.uuzu.chinadep.config.quartz;


import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;


@Configuration
public class QuartzConfig {

    @Value("${quartz.schedulerName}")
    private String schedulerName;
    @Value("${org.quartz.jobStore.isClustered}")
    private String isClustered;
    @Value("${org.quartz.jobStore.clusterCheckinInterval}")
    private String clusterCheckinInterval;
    @Value("${org.quartz.jobStore.misfireThreshold}")
    private String misfireThreshold;
    @Value("${org.quartz.jobStore.maxMisfiresToHandleAtATime}")
    private String maxMisfiresToHandleAtATime;
    @Value("${org.quartz.jobStore.driverDelegateClass}")
    private String driverDelegateClass;
    @Value("${org.quartz.jobStore.class}")
    private String jobStore_class;
    @Value("${org.quartz.jobStore.tablePrefix}")
    private String tablePrefix;
    @Value("${org.quartz.jobStore.dataSource}")
    private String dataSource;
    @Value("${org.quartz.dataSource.qzDS.connectionProvider.class}")
    private String connectionProvider_class;
    @Value("${org.quartz.dataSource.qzDS.driver}")
    private String driver;
    @Value("${org.quartz.dataSource.qzDS.URL}")
    private String URL;
    @Value("${org.quartz.dataSource.qzDS.user}")
    private String user;
    @Value("${org.quartz.dataSource.qzDS.password}")
    private String password;
    @Value("${org.quartz.dataSource.qzDS.maxConnections}")
    private String maxConnections;
    @Value("${org.quartz.scheduler.instanceId}")
    private String instanceId;
    @Value("${org.quartz.scheduler.instanceName}")
    private String instanceName;
    @Value("${org.quartz.scheduler.rmi.export}")
    private String export;
    @Value("${org.quartz.scheduler.rmi.proxy}")
    private String proxy;
    @Value("${org.quartz.scheduler.wrapJobExecutionInUserTransaction}")
    private String wrapJobExecutionInUserTransaction;
    @Value("${org.quartz.threadPool.class}")
    private String threadPool_class;
    @Value("${org.quartz.threadPool.threadCount}")
    private String threadCount;
    @Value("${org.quartz.threadPool.threadPriority}")
    private String threadPriority;
    @Value("${org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread}")
    private String threadsInheritContextClassLoaderOfInitializingThread;



    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName(schedulerName);
        factory.setApplicationContextSchedulerContextKey(schedulerName);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.isClustered",isClustered);
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval",clusterCheckinInterval);
        properties.setProperty("org.quartz.jobStore.misfireThreshold",misfireThreshold);
        properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime",maxMisfiresToHandleAtATime);
        properties.setProperty("org.quartz.jobStore.driverDelegateClass",driverDelegateClass);
        properties.setProperty("org.quartz.jobStore.class",jobStore_class);
        properties.setProperty("org.quartz.jobStore.tablePrefix",tablePrefix);
        properties.setProperty("org.quartz.jobStore.dataSource",dataSource);
        properties.setProperty("org.quartz.dataSource.qzDS.connectionProvider.class",connectionProvider_class);
        properties.setProperty("org.quartz.dataSource.qzDS.driver",driver);
        properties.setProperty("org.quartz.dataSource.qzDS.URL",URL);
        properties.setProperty("org.quartz.dataSource.qzDS.user",user);
        properties.setProperty("org.quartz.dataSource.qzDS.password",password);
        properties.setProperty("org.quartz.dataSource.qzDS.maxConnections",maxConnections);
        properties.setProperty("org.quartz.scheduler.instanceId",instanceId);
        properties.setProperty("org.quartz.scheduler.instanceName",instanceName);
        properties.setProperty("org.quartz.scheduler.rmi.export",export);
        properties.setProperty("org.quartz.scheduler.rmi.proxy",proxy);
        properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction",wrapJobExecutionInUserTransaction);
        properties.setProperty("org.quartz.threadPool.class",threadPool_class);
        properties.setProperty("org.quartz.threadPool.threadCount",threadCount);
        properties.setProperty("org.quartz.threadPool.threadPriority",threadPriority);
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread",threadsInheritContextClassLoaderOfInitializingThread);
        propertiesFactoryBean.setProperties(properties);
        //propertiesFactoryBean.setLocation(new ClassPathResource(classPathResource));
        propertiesFactoryBean.afterPropertiesSet();
        return  propertiesFactoryBean.getObject();
    }

    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

}