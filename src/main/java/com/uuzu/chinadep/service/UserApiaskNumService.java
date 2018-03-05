package com.uuzu.chinadep.service;

import com.uuzu.chinadep.repository.UserApiaskNumRepository;
import com.uuzu.chinadep.service.codis.CodisService;
import com.uuzu.common.exception.MoreThanaskNumException;
import com.uuzu.common.pojo.UserApiaskNum;
import com.uuzu.common.redis.RedisLocker;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lixing on 2017/6/4.
 */
@Service
@RefreshScope
public class UserApiaskNumService {

    private static final String LOCK_NAME = "dmpaskNumCount";
    private static AtomicInteger askCountNum = new AtomicInteger();

    @Autowired
    private CodisService codisService;
    @Autowired
    private RedisLocker redisLocker;
    @Autowired
    private UserApiaskNumRepository userApiaskNumRepository;

    public String[] getUserIds() {
        return userIds;
    }
    public String[] getWhiteUserIds() {
        return whiteUserIds;
    }
    @Value("${dmp.userIds}")
    private String[] userIds;
    @Value("${dmp.whiteUserIds}")
    private String[] whiteUserIds;
    @Value("${dmp.counterNumber}")
    private int COUNTERNUMBER;


    /**
     * 增加指定用户的调用次数限制，超出限制调用后，会抛出异常
     * @param userId
     * @return
     */
    public synchronized void doApiServiceNum(String userId) throws Exception {
        final String[] countNum = {codisService.get(userId)};
        if(StringUtils.isNotBlank(countNum[0]) && Integer.valueOf(countNum[0]) < COUNTERNUMBER){
            throw new MoreThanaskNumException("超出调用次数限制,停止提供服务,用户Id:"+userId);
        } else {
            askCountNum.getAndIncrement(); //调用次数加一
            if(askCountNum.get() >= COUNTERNUMBER){
                redisLocker.lock(LOCK_NAME,()->{
                    //countNum = codisService.get(userId);
                    //第一次调用获取到限制调用次数
                    if(StringUtils.isBlank(countNum[0])){
                        UserApiaskNum one = userApiaskNumRepository.findOne(userId);
                        if(null != one){
                            String num = one.getNum();
                            this.codisService.set(userId,num,nowToZero());
                            countNum[0] = num;
                        }else {
                            throw new RuntimeException("没有设置访问限制次数");
                        }

                    }

                    //总次数扣除调用量
                    Integer i = Integer.valueOf(countNum[0]) - askCountNum.get();
                    if(i < 0){
                        throw new MoreThanaskNumException("超出调用次数限制,停止提供服务,用户Id:"+userId);
                    }

                    this.codisService.set(userId, i.toString(),nowToZero()); //更新并设置新的过期时间
                    askCountNum.set(0);
                    return null;
                });
            }


        }


    }


    private Integer nowToZero(){
        DateTime zero = new DateTime().millisOfDay().withMaximumValue(); //每天零点
        DateTime now = new DateTime();
        Period period = new Period(now,zero);
        return period.getHours()*3600 + period.getMinutes()*60 + period.getSeconds();
    }



}
