package com.uuzu.chinadep.service.httpapi;

import org.springframework.stereotype.Component;

/**
 * Created by lixing on 2017/6/20.
 * 断路器
 */
@Component
public class UserApiServiceHystrix implements UserApiService {

    /**
     * 调用接口失败会进入断路器代码逻辑
     *
     * @param params
     * @param columns
     * @return
     */
    @Override
    public String queryUser(String params, String projectId, String[] columns) {
        return "-9999";
    }
}
