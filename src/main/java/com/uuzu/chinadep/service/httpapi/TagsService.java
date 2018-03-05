package com.uuzu.chinadep.service.httpapi;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by zhoujin on 2017/8/18.
 */
@FeignClient(name = "MOBUSER" , fallback = TagsApiServiceHystrix.class)
@Component
@CacheConfig(cacheNames = "tag")
public interface TagsService {

    @RequestMapping(method = RequestMethod.POST, path = "/openApi/mapping/tagif")
    @Cacheable(value = "tagIf",keyGenerator = "aggKeyGenerator")
    String tagIf(@RequestParam(value = "list") List<String> list,@RequestParam(value = "target") String target);


    @RequestMapping(method = RequestMethod.POST, path = "/openApi/mapping/tagif2")
    @Cacheable(value = "tagIf2",keyGenerator = "aggKeyGenerator")
    String tagIf2(@RequestParam(value = "list") List<String> list,@RequestParam(value = "target") String target);
}


