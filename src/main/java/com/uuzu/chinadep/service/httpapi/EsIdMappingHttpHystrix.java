package com.uuzu.chinadep.service.httpapi;

import com.uuzu.common.es.DmpEquipmentData;
import com.uuzu.common.es.EsIdMapping;
import com.uuzu.common.es.EsIdMappingNew;
import com.uuzu.common.es.IdMappingPage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


/**
 * @author zhoujin
 * 断路器
 */
@Component
public class EsIdMappingHttpHystrix implements EsIdMappingHttpService {

    @Override
    public IdMappingPage getIdMapping(String key, String value, int startPage, int size) {
        return null;
    }

    @Override
    public String saveEsIdMapping(EsIdMapping esIdMapping) {
        return null;
    }

    @Override
    public String saveEsIdMappingNew(EsIdMappingNew esIdMappingNew) {
        return null;
    }


}
