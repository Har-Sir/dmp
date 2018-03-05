package com.uuzu.chinadep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.IdTypeConstant;
import com.uuzu.chinadep.pojo.RpDeviceAllInfo;
import com.uuzu.chinadep.service.httpapi.CostApiService;
import com.uuzu.chinadep.service.httpapi.EsService;
import com.uuzu.chinadep.service.httpapi.TagsService;
import com.uuzu.chinadep.config.ParamConfig.DataServeBase;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import com.uuzu.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@CacheConfig(cacheNames = "bigData")
public class RelatedIdForTagService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static String HBASE_TABLE_NAME_NEW = "rp_device_profile_info";
    private static String HBASE_FAMILY_NAME = "cf";

    @Autowired
    BigDataService bigDataService;

    @Autowired
    CostApiService costApiService;

    @Autowired
    TagsService tagsService;

    @Autowired
    EsService esService;

    public RetState relatedIdForTag(String idType, String exid, String dataRange, DmpResponse dmpResponse) throws Exception {

        // query costId by dataRange
        String cost = costApiService.queryByCostID(dataRange);
        if (StringUtils.isEmpty(cost)) {
            return new RetState(false, "costId not found");
        }

        JsonNode jsonNode = OBJECT_MAPPER.readTree(cost);
        String tagsStr = jsonNode.get("tags").asText();
        // get idType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null || (!IdTypeConstant.IMEI_PHONE_LON_LAT.equals(it.code)&& !IdTypeConstant.IMEI_PHONE_LON_LAT_ADDRESS.equals(it.code))) {
            return new RetState(false, "unsupported idType");
        }

        // get keyTypes
        String[] keys = tagsStr.split("\\|@\\|");
        if (keys.length == 0) {
            return new RetState(false, "dataRange is null.");
        }

        DataServeBase.KeyType[] keyTypes = new DataServeBase.KeyType[keys.length];
        String D013TagsStr = "";
        List<String> D013Tags = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            if (!keys[i].contains("D013")) {
                DataServeBase.KeyType keyType = getDemoBasicidKeyType(keys[i]);
                if (keyType == null) {
                    return new RetState(false, "unknown key:" + keys[i]);
                }
                keyTypes[i] = keyType;
            } else {
                D013TagsStr = keys[i].split(":")[1];
                D013Tags = Arrays.asList(D013TagsStr.split("\\|"));
            }
        }

        List<DataServeBase.KeyType> tmp = new ArrayList<>();
        for (DataServeBase.KeyType str : keyTypes) {
            if (str != null) {
                tmp.add(str);
            }
        }
        keyTypes = tmp.toArray(new DataServeBase.KeyType[0]);

        // dataServe
        String key = esService.queryDeviceId(it.code,it.key, exid);
        if (StringUtils.isBlank(key)) {
            return new RetState(false, "not data found");
        }
        RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_NAME_NEW, HBASE_FAMILY_NAME, key);
        if (null == rpDeviceAllInfo) {
            return new RetState(false, "not data found");
        }

        Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);
        Map<String, Object> dataMap = new LinkedHashMap<>();
        for (DataServeBase.KeyType keyType : keyTypes) {
            dataMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
        }
        //if has D013,package
        List<String> bigtags = new ArrayList<>();
        if (D013Tags.size() > 0) {
            String personTags = rpDeviceAllInfo.getTags();
            String[] tags = personTags.split(",");
            Arrays.sort(tags);
            StringBuffer D013Result = new StringBuffer();
            for (String tag : D013Tags) {
                if (!StringUtils.contains(tag, "A") && !StringUtils.contains(tag, "B")) {
                    if (Arrays.binarySearch(tags, tag) >= 0) {
                        D013Result.append(tag + ":1,");
                    } else {
                        D013Result.append(tag + ":0,");
                    }
                } else {
                    bigtags.add(tag);
                }
            }
            String bigtagsStr = "";

            if (bigtags.size() > 0) {
                bigtagsStr = tagsService.tagIf(bigtags, rpDeviceAllInfo.getTags());
                D013Result.append(bigtagsStr.replaceAll("\"", ""));
                dataMap.put("D013", D013Result);
            } else {
                dataMap.put("D013", D013Result.substring(0, D013Result.length() - 1));
            }
        }
        String data = JsonUtil.ModelToJson(dataMap);
        dmpResponse.setDataRange(data);

        return new RetState(true, "ok");

    }

    private DataServeBase.KeyType getDemoBasicidKeyType(String keyCode) {
        if (DataServeBase.getDemoKT(keyCode) != null) return DataServeBase.getDemoKT(keyCode);
        return null;
    }

}
