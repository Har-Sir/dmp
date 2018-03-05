package com.uuzu.chinadep.service.codis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by lixing on 2017/3/23.
 *  对codis操作简单封装
 */
@Service
public class CodisService {

    /*@Autowired
    private JedisResourcePool jedisResourcePool;*/

    @Autowired(required = false)
    private JedisPool jedisPool;

    private <T> T execute(Function<T, Jedis> fun) {
        Jedis jedis = null;
        try {
            // 从连接池中获取到jedis对象
            jedis = jedisPool.getResource();
            return fun.callback(jedis);
        }  finally {
            if (null != jedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                jedis.close();
            }
        }
    }
    /**
     * 执行SET操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute(new Function<String, Jedis>() {
            @Override
            public String callback(Jedis e) {
                return e.set(key, value);
            }
        });
    }
    /**
     * 指定GET操作
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        return this.execute(new Function<String, Jedis>() {
            @Override
            public String callback(Jedis e) {
                return e.get(key);
            }
        });
    }
    /**
     * 指定DEL操作
     *
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new Function<Long, Jedis>() {
            @Override
            public Long callback(Jedis e) {
                return e.del(key);
            }
        });
    }
    /**
     * 设置生存时间，单位为秒
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<Long, Jedis>() {
            @Override
            public Long callback(Jedis e) {
                return e.expire(key, seconds);
            }
        });
    }
    /**
     * 执行SET操作，并且设置生存时间，单位为秒
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String, Jedis>() {
            @Override
            public String callback(Jedis e) {
                String str = e.set(key, value);
                //设置生存时间
                e.expire(key, seconds);
                return str;
            }
        });
    }

}
