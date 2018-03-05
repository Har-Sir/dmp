package com.uuzu.chinadep.service.httpapi;

import com.uuzu.chinadep.pojo.Cost;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by lixing on 2017/6/20.
 * 断路器
 */
@Component
public class CostApiServiceHystrix implements CostApiService {

    @Override
    public String queryByCostID(String id) {
        return null;
    }
}
