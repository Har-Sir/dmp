package com.uuzu.chinadep.service.httpapi;

import com.uuzu.common.es.AdDeviceRiskWhiteList;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoujin
 */
@FeignClient(name = "mobesservice", fallback = EsServiceHystrix.class)
@Component
public interface EsService {

    @RequestMapping(method = RequestMethod.POST, path = "/elasticsearch/queryDeviceId")
    String queryDeviceId(@RequestParam("code") String code,
                         @RequestParam("key") String key,
                         @RequestParam("value") String value);

    @RequestMapping(method = RequestMethod.POST, path = "/elasticsearch/getAdDeviceRishWhiteList")
    AdDeviceRiskWhiteList getAdDeviceRishWhiteList(@RequestParam("code") String code,
                                                   @RequestParam("key") String key,
                                                   @RequestParam("value") String value);

}
