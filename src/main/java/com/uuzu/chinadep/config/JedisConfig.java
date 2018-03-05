package com.uuzu.chinadep.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Created by lixing on 2017/4/1.
 */
@Configuration
@RefreshScope
public class JedisConfig {

    @Value("${jodis.maxTotal}")
    private Integer MAXTOTAL;
    @Value("${jodis.maxIdle}")
    private Integer MAXIDLE;
    @Value("${jodis.minIdle}")
    private Integer MINIDLE;
    @Value("${jodis.maxWaitMillis}")
    private Integer MAXWAITMILLIS;
    @Value("${jodis.lvsAddr}")
    private String LVSADDR;
    @Value("${jodis.lvsPort}")
    private Integer LVSPORT;

    @Bean
    @RefreshScope
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(MAXTOTAL);
        jedisPoolConfig.setMaxIdle(MAXIDLE);
        jedisPoolConfig.setMinIdle(MINIDLE);
        jedisPoolConfig.setMaxWaitMillis(MAXWAITMILLIS);
        //jedisPoolConfig.setTestOnBorrow(Boolean.TRUE);
        //jedisPoolConfig.setTestOnReturn(Boolean.TRUE);
        return jedisPoolConfig;
    }

    @Bean
    @RefreshScope
    public JedisPool jedisPool (JedisPoolConfig jedisPoolConfig){
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,LVSADDR,LVSPORT);
        return jedisPool;
    }





}
