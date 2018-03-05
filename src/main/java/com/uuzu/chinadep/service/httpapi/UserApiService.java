package com.uuzu.chinadep.service.httpapi;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lixing on 2017/6/20.
 */
@FeignClient(name = "MOBUSER" , fallback = UserApiServiceHystrix.class)
@Component
public interface UserApiService {

    @RequestMapping(method = RequestMethod.GET, path = "/openApi/mob-user/queryUser")
    @ResponseBody
    String queryUser(@RequestParam(value = "params")String params,
                           @RequestParam(value = "projectId")String projectId,
                             @RequestParam(value = "columns")String[] columns);

}
