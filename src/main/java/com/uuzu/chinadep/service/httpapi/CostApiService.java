package com.uuzu.chinadep.service.httpapi;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by lixing on 2017/6/20.
 */
@FeignClient(name = "MOBUSER" , fallback = CostApiServiceHystrix.class)
@Component
@CacheConfig(cacheNames = "cost")
public interface CostApiService {

    @RequestMapping(method = RequestMethod.POST, path = "openApi/cost/queryByCostID")
    @Cacheable(value = "queryByCostID",keyGenerator = "aggKeyGenerator")
    String queryByCostID(@RequestParam(value = "id") String id);

}
