package com.uuzu.chinadep.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.DmpTask;
import com.uuzu.chinadep.repository.DmpTaskRepository;
import com.uuzu.chinadep.service.httpapi.FastDFSService;
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
public class O2OStatisticListener {
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    FastDFSService fastDFSService;

    /**
     * 对日志进行消费，获取任务处理结果
     *
     * @param logMessage
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "dmpO2OStatisticMessage", type = ExchangeTypes.FANOUT),
            value = @Queue(value = "O2Olog")))
    public synchronized void o2oStatistic(String logMessage) throws Exception {
        try {
//            {
//                "userid": "89",
//                "storeid": "10",
//                "hour": "15",
//                "dataPath": "http://xxxxxx"
//            }
            JsonNode jsonNode = mapper.readTree(logMessage);
            String path = jsonNode.get("dataPath").asText();

            byte[] resultByte = fastDFSService.download("o2ostatistic", path, "");
            String result = new String(resultByte, "UTF-8");

            MediaType mediaType = MediaType.parse("application/json;charset=utf-8");


            String resultData = "{\"json_data\":" + result + ",\"secret_key\":\"Yz_Portrait_upload\"}";
            log.error(resultData);
            RequestBody body = RequestBody.create(mediaType, resultData);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://Test.soundtooth.cn/upload/YZLocData").post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            log.error(res);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
