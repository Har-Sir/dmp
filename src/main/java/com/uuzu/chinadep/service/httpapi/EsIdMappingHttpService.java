package com.uuzu.chinadep.service.httpapi;

import com.uuzu.common.es.DmpEquipmentData;
import com.uuzu.common.es.EsIdMapping;
import com.uuzu.common.es.EsIdMappingNew;
import com.uuzu.common.es.IdMappingPage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhoujin
 */
@FeignClient(name = "mobesservice", fallback = EsIdMappingHttpHystrix.class)
@Component
public interface EsIdMappingHttpService {

    @RequestMapping(method = RequestMethod.POST, path = "/elasticsearch/getIdMapping")
    IdMappingPage getIdMapping(@RequestParam("key") String key,
                               @RequestParam("value") String value,
                               @RequestParam("startPage") int startPage,
                               @RequestParam("size") int size);

    @RequestMapping(method = RequestMethod.POST, value  = "/elasticsearch/saveEsIdMapping",consumes = "application/json")
    String saveEsIdMapping(@RequestBody EsIdMapping esIdMapping);

    @RequestMapping(method = RequestMethod.POST, value  = "/elasticsearch/saveEsIdMappingNew",consumes = "application/json")
    String saveEsIdMappingNew(@RequestBody EsIdMappingNew esIdMappingNew);
}
