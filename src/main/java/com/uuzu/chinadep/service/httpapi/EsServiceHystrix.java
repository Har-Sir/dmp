package com.uuzu.chinadep.service.httpapi;

import com.uuzu.common.es.AdDeviceRiskWhiteList;
import org.springframework.stereotype.Component;


/**
 * @author zhoujin
 *         断路器
 */
@Component
public class EsServiceHystrix implements EsService {

    @Override
    public String queryDeviceId(String code, String key, String value) {
        return null;
    }

    @Override
    public AdDeviceRiskWhiteList getAdDeviceRishWhiteList(String code, String key, String value) {
        return null;
    }

}
