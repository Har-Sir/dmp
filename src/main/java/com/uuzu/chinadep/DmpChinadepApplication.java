package com.uuzu.chinadep;

import com.uuzu.chinadep.listener.MacStatisticListener;
import com.uuzu.common.redis.RedisLocker;
import com.uuzu.common.redis.RedissonConnector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by lixing on 2017/3/16.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableCaching
@EnableRabbit
public class DmpChinadepApplication {

    @Bean
    public ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        return threadPoolTaskExecutor;
    }

    @Bean
    public MacStatisticListener macStatisticListener(){

        return new MacStatisticListener();
    }

    @Bean
    public RedisLocker redisLocker(){
        return new RedisLocker();
    }
    @Bean
    public RedissonConnector redissonConnector(){
        return new RedissonConnector();
    }

    public static void main(String[] args){
        SpringApplication.run(DmpChinadepApplication.class, args);
    }

}
