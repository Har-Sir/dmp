package com.uuzu.chinadep.service;

import com.alibaba.fastjson.JSON;
import com.uuzu.chinadep.config.ParamConfig.DataServeBase;
import com.uuzu.chinadep.pojo.IdMappingInfo;
import com.uuzu.chinadep.pojo.IdTypeConstant;
import com.uuzu.chinadep.service.httpapi.EsIdMappingHttpService;
import com.uuzu.chinadep.utils.MD5Utils;
import com.uuzu.common.codec.Aes;
import com.uuzu.common.codec.BaseConfig;
import com.uuzu.common.es.DmpEquipmentData;
import com.uuzu.common.es.IdMappingPage;
import com.uuzu.common.pojo.RetState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IdMappingService {

    @Autowired
    EsIdMappingHttpService esIdMappingHttpService;

    public RetState idMapping(String inIdType, String exId,
                              String outIdType, Integer extraNum, IdMappingInfo dmpResponse, Aes.AesToken at) {
        // check inIdType valid
        DataServeBase.IdType init = DataServeBase.getIdType(inIdType);
        if (init == null) {
            return new RetState(false, "unknown inIdType");
        }
        // check outIdType valid
        DataServeBase.IdType outIt = DataServeBase.getIdType(outIdType);
        if (outIt == null) {
            return new RetState(false, "unknown outIdType");
        }

        // check extraNum valid
        if (null != extraNum && (extraNum > 10 || extraNum < 0)) {
            return new RetState(false, "extraNum must less than 10");
        }

        // when inIdType is idfa or mac use md5 value to query es
        if (IdTypeConstant.IDFA.equals(init.code)) {
            init.key = DataServeBase.getIdType(IdTypeConstant.ENCRYPTION_IMEI).key;
            exId = MD5Utils.encode(exId);
        } else if (IdTypeConstant.MAC.equals(init.code)) {
            init.key = DataServeBase.getIdType(IdTypeConstant.ENCRYPTION_MAC).key;
            exId = MD5Utils.encode(exId);
        }

        try {
            // query es compare with termQuery
//            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//            SearchQuery searchQuery = nativeSearchQueryBuilder
//                    .withQuery(matchQuery(init.key, exId))
////                    .withQuery(wildcardQuery(init.key, "*" + exId + "*"))
////                    .withSearchType(SearchType.QUERY_AND_FETCH)
//                    .build();
//            searchQuery.setPageable(new PageRequest(0, 11));
//            Page<DmpEquipmentData> search = dmpEquipmentDataRepository.search(searchQuery);
            IdMappingPage idMapping = esIdMappingHttpService.getIdMapping(init.key, exId, 0, 11);


//            System.out.println("init.key:" + init.key + "--exid:" + exId + "--search.getTotalElements():" + search.getTotalElements());
            if (idMapping == null || idMapping.getPage() == null || idMapping.getPage().size() == 0) {
                return new RetState(false, "no data found");
            }
//            System.out.println("init.key:" + init.key + "--exid:" + exId + "--fuzzy query:" + JSON.toJSONString(search.getContent()));
            DmpEquipmentData result = idMapping.getPage().get(0);
            String mapId = getValueByOutIdType(outIt.code, result);

            if (StringUtils.isNotBlank(mapId))
                dmpResponse.setMapId(Aes.encodeBase64(mapId, at, BaseConfig.DEFAULT_CHARSET));

            if (extraNum != null && extraNum > 0) {
                log.info("start to handle setExtraId");
                long startTime = System.currentTimeMillis();
                setExtraId(idMapping.getPage(), extraNum, outIt, at, dmpResponse, init, exId,idMapping.getTotalPage());
                log.info("finish to handle setExtraId, cost time= " + (System.currentTimeMillis() - startTime) + "ms");
            }

            return new RetState(true, "success");
        } catch (Exception e) {
            log.error("exception, message:{" + e.getMessage() + "}", e);
            return new RetState(false, "unknown error");
        }

    }

    /**
     * get value by idTypeCode
     *
     * @param idTpyeCode
     * @param result
     * @return
     */
    private String getValueByOutIdType(String idTpyeCode, DmpEquipmentData result) {
        switch (idTpyeCode) {
            case IdTypeConstant.MAC:
                return result.getMac();
            case IdTypeConstant.IMEI_IDFA:
                return result.getImei_idfa();
            case IdTypeConstant.ENCRYPTION_MAC:
                return result.getEncryptionMac();
            case IdTypeConstant.ENCRYPTION_IMEI:
                return result.getEncryptionImei();
            case IdTypeConstant.IDFA:
                return result.getImei_idfa();
            case IdTypeConstant.PHONE:
                return "";
            case IdTypeConstant.ENCRYPTION_PHONE:
                return "";
            case IdTypeConstant.ANDROID_ID:
                return result.getAndroidid();
        }
        return "";
    }


    /**
     * set extraId
     *
     * @param extraNum
     * @param outIt
     * @param at
     * @param dmpResponse
     * @param init
     */
    private void setExtraId(List<DmpEquipmentData> search, int extraNum, DataServeBase.IdType outIt,
                            Aes.AesToken at, IdMappingInfo dmpResponse, DataServeBase.IdType init, String exId,int totalPage) {
        String extraId;
        // query data of phone is empty  num=extraNum
        ArrayList<String> resultSet = new ArrayList<>();
        // search phone is empty
        for (int i = 1; i < search.size(); i++) {
            if (resultSet.size() >= extraNum) {
                break;
            }
            DmpEquipmentData dmpEquipmentData = search.get(i);
            if (StringUtils.isBlank(dmpEquipmentData.getPhone())) {
                try {
                    String value = getValueByOutIdType(outIt.code, dmpEquipmentData);
                    // value not empty Aes encode
                    if (StringUtils.isNotBlank(value))
                        resultSet.add(Aes.encodeBase64(value, at, BaseConfig.DEFAULT_CHARSET));
                } catch (Exception e) {
                    continue;
                }
            }
        }
        if (resultSet.size() < extraNum) {
            for (int i = 1; i < totalPage; i++) {
                // 防止index.max_result_window溢出
                if (i > 900) break;

                if (resultSet.size() >= extraNum) {
                    break;
                }
                IdMappingPage idMapping = esIdMappingHttpService.getIdMapping(init.key, exId, i, 11);

                for (int j = 0; j < idMapping.getPage().size(); j++) {
                    if (resultSet.size() >= extraNum) {
                        break;
                    }
                    DmpEquipmentData dmpEquipmentData = idMapping.getPage().get(j);
                    if (StringUtils.isBlank(dmpEquipmentData.getPhone())) {
                        try {
                            String value = getValueByOutIdType(outIt.code, dmpEquipmentData);
                            // value not empty Aes encode
                            if (StringUtils.isNotBlank(value))
                                resultSet.add(Aes.encodeBase64(value, at, BaseConfig.DEFAULT_CHARSET));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(resultSet)) {
            extraId = JSON.toJSONString(resultSet);
            dmpResponse.setExtraId(extraId);
        }
    }


}
