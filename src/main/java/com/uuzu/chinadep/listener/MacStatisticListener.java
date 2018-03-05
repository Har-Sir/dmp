package com.uuzu.chinadep.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.DmpTask;
import com.uuzu.chinadep.repository.DmpTaskRepository;
import com.uuzu.common.redis.RedisLocker;
import com.uuzu.common.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhoujin on 2017/8/14.
 */
@Service
@Slf4j
@RefreshScope
public class MacStatisticListener {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String LOCK_NAME = "dmpMacStatisticLock";

    @Autowired
    private RedisLocker redisLocker;
    @Autowired
    private DmpTaskRepository dmpTaskRepository;

    /**
     * 对日志进行消费，获取任务处理结果
     *
     * @param logMessage
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "dmpMacStatisticMessage", type = ExchangeTypes.FANOUT),
            value = @Queue(value = "maclog")))
    public synchronized void macStatistic(String logMessage) throws Exception {
        String data = "";
        String busiserialno = "";
        String accessKey = "";
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(logMessage);
            String code = jsonNode.get("code").asText();
            busiserialno = jsonNode.get("busiSerialNo").asText();
            data = jsonNode.get("data").toString();
            accessKey = jsonNode.get("accessKey").toString().replace("\"","");
            DmpTask dmpTask = dmpTaskRepository.findByBusiserialno(busiserialno);
            dmpTask.setOutput(data);
            dmpTask.setUpdateAt(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrDate(), DateTimeUtil.DAY_FORMAT_MINUTUES));
            if ("0000".equals(code)) {//成功
                dmpTask.setState(1);
            } else {//失败
                dmpTask.setState(2);
            }
            doNumberCounter(dmpTask, new AtomicInteger());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                String url  = "http://test-mac-mapping.advertsales.cn/api/pushResult?accessKey="+accessKey+"&data="+data+"&requestId="+busiserialno;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                log.error(result);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }


    /**
     * 分布式插入锁
     *
     * @param dmpTask
     * @param numberCounter
     * @throws Exception
     */
    private void doNumberCounter(DmpTask dmpTask, AtomicInteger numberCounter) throws Exception {
        redisLocker.lock(LOCK_NAME, () -> {
            this.dmpTaskRepository.save(dmpTask);
            numberCounter.set(0);
            return null;
        });
    }

}
