package com.uuzu.chinadep.service.httpapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoujin on 2017/6/27.
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    UserApiService userApiService;

    @Cacheable(value = "queryTokenByUserId",keyGenerator = "aggKeyGenerator")
    public String queryTokenByUserId(String userId) throws IOException {
        Map<String,Object> params = new HashMap<>();
        params.put("api_userid",userId);
        String[] fileds = {"user_token"};
        String token = userApiService.queryUser(JsonUtil.ModelToJson(params),"2",fileds);
        JsonNode jsonNode = OBJECT_MAPPER.readTree(token);
        return jsonNode.get("user_token").asText();
    }


    @CacheEvict(value = {"queryTokenByUserId"},allEntries = true)
    public void removeUserTokenCache(){
        log.warn("用户token缓存数据清除中...");
    }


}
