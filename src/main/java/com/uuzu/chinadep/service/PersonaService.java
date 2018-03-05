package com.uuzu.chinadep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.RpDeviceAllInfo;
import com.uuzu.chinadep.service.codis.CodisService;
import com.uuzu.chinadep.service.httpapi.CostApiService;
import com.uuzu.chinadep.service.httpapi.EsService;
import com.uuzu.chinadep.service.httpapi.TagsService;
import com.uuzu.chinadep.utils.ArithUtil;
import com.uuzu.chinadep.config.ParamConfig.DataServeBase;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import com.uuzu.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by lixing on 2017/6/19.
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "bigData")
public class PersonaService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String HBASE_TABLE_NAME_NEW = "rp_device_profile_info";
    private static String HBASE_TABLE_SCORE = "rp_device_score_info";
    private static String HBASE_FAMILY_NAME = "cf";

    @Autowired
    BigDataService bigDataService;

    @Autowired
    CostApiService costApiService;

    @Autowired
    CodisService codisService;

    @Autowired
    TagsService tagsService;

    @Autowired
    EsService esService;

    /**
     * 获取标签值
     *
     * @param idType
     * @param exid
     * @param dataRange
     * @param dmpResponse
     * @return
     * @throws Exception
     */
    public RetState invoke(String idType, String exid, String dataRange, DmpResponse dmpResponse) throws Exception {
        // 获取IdType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null) {
            return new RetState(false, "unknown idType");
        }

        // 获取KeyTypes
        String[] keys = dataRange.split("\\|@\\|");
        if (keys.length == 0) {
            return new RetState(false, "dataRange is null.");
        }
        DataServeBase.KeyType[] keyTypes = new DataServeBase.KeyType[keys.length];
        for (int i = 0; i < keys.length; i++) {
            DataServeBase.KeyType keyType = getDemoBasicidKeyType(keys[i]);
            if (keyType == null) {
                return new RetState(false, "unknown key:" + keys[i]);
            }
            keyTypes[i] = keyType;
        }

        // dataServe调用
        String key = esService.queryDeviceId(it.code,it.key, exid);
        if (key == null) {
            return new RetState(false, "not data found");
        }

        if (StringUtils.isNotBlank(key)) {

            RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_NAME_NEW, HBASE_FAMILY_NAME, key);
            if (null == rpDeviceAllInfo) {
                return new RetState(false, "not data found");
            }

            Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);

            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            for (DataServeBase.KeyType keyType : keyTypes) {
                dataMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
            }

            String data = JsonUtil.ModelToJson(dataMap);
            dmpResponse.setDataRange(data);
            return new RetState(true, "ok");
        }
        return new RetState(false, "not data found");

    }


    /**
     * 获取标签值  V3  细分到D013个性标签
     *
     * @param idType
     * @param exid
     * @param dataRange
     * @param dmpResponse
     * @return
     * @throws Exception
     */
    public RetState invokeV3(String idType, String exid, String dataRange, DmpResponse dmpResponse) throws Exception {
        // 通过计费ID获取需要查询的标签值
        String cost = costApiService.queryByCostID(dataRange);
        if (StringUtils.isEmpty(cost)) {
            //throw new NullPointerException("cost_id不存在");
            return new RetState(false, "dataRange对应的计费ID不存在");
        }
        //System.err.println(cost);
        JsonNode jsonNode = OBJECT_MAPPER.readTree(cost);
        String tagsStr = jsonNode.get("tags").asText();


        // 获取IdType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null) {
            return new RetState(false, "unknown idType");
        }

        // 获取KeyTypes
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


        List<DataServeBase.KeyType> tmp = new ArrayList<DataServeBase.KeyType>();
        for (DataServeBase.KeyType str : keyTypes) {
            if (str != null) {
                tmp.add(str);
            }
        }
        keyTypes = tmp.toArray(new DataServeBase.KeyType[0]);


        // dataServe调用
        String key = esService.queryDeviceId(it.code,it.key, exid);
        if (key == null) {
            return new RetState(false, "not data found");
        }

        if (StringUtils.isNotBlank(key)) {

            RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_NAME_NEW, HBASE_FAMILY_NAME, key);
            if (null == rpDeviceAllInfo) {
                return new RetState(false, "not data found");
            }

            Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);

            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            for (DataServeBase.KeyType keyType : keyTypes) {
                dataMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
            }

            //如果有D013的标签
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
        return new RetState(false, "not data found");

    }


    /**
     * 获取标签值  V4  细分到D013个性标签 加权重
     *
     * @param idType
     * @param exid
     * @param dataRange
     * @param dmpResponse
     * @return
     * @throws Exception
     */
    public RetState invokeV4(String idType, String exid, String dataRange, DmpResponse dmpResponse) throws Exception {
        // 通过计费ID获取需要查询的标签值
        String cost = costApiService.queryByCostID(dataRange);
        if (StringUtils.isEmpty(cost)) {
            //throw new NullPointerException("cost_id不存在");
            return new RetState(false, "dataRange对应的计费ID不存在");
        }
        JsonNode jsonNode = OBJECT_MAPPER.readTree(cost);
        //获取计费id对应的标签
        String tagsStr = jsonNode.get("tags").asText();


        // 获取IdType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null) {
            return new RetState(false, "unknown idType");
        }

        // 获取KeyTypes
        String[] keys = tagsStr.split("\\|@\\|");
        if (keys.length == 0) {
            return new RetState(false, "dataRange is null.");
        }
        DataServeBase.KeyType[] keyTypes = new DataServeBase.KeyType[keys.length];
        String D013TagsStr = "";
        String D030TagsStr = "";
        String D032TagsStr = "";
        List<String> D013Tags = new ArrayList<>();//个性标签
        List<String> D030Tags = new ArrayList<>();//行业标签
        List<String> D032Tags = new ArrayList<>();//金融划窗标签
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].contains("D030")) {
                D030TagsStr = keys[i].split(":")[1];
                D030Tags = Arrays.asList(D030TagsStr.split("\\|"));
            } else if (keys[i].contains("D032")) {
                D032TagsStr = keys[i].split(":")[1];
                D032Tags = Arrays.asList(D032TagsStr.split("\\|"));
            } else if (keys[i].contains("D013")) {
                D013TagsStr = keys[i].split(":")[1];
                D013Tags = Arrays.asList(D013TagsStr.split("\\|"));
            } else {
                DataServeBase.KeyType keyType = getDemoBasicidKeyType(keys[i]);
                if (keyType == null) {
                    return new RetState(false, "unknown key:" + keys[i]);
                }
                keyTypes[i] = keyType;
            }
        }


        List<DataServeBase.KeyType> tmp = new ArrayList<DataServeBase.KeyType>();
        for (DataServeBase.KeyType str : keyTypes) {
            if (str != null) {
                tmp.add(str);
            }
        }
        keyTypes = tmp.toArray(new DataServeBase.KeyType[0]);


        // dataServe调用 获取es中该设备号的deviceid
        String key = esService.queryDeviceId(it.code,it.key, exid);
        //String key = "e52169734156a0ccae9a8d7459b4ec0469489de5";


        if (key == null) {
            return new RetState(false, "not data found");
        }

        if (StringUtils.isNotBlank(key)) {
            //通过deviceid从hbase中获取该设备号的信息
            RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_NAME_NEW, HBASE_FAMILY_NAME, key);
            if (null == rpDeviceAllInfo) {
                return new RetState(false, "not data found");
            }

            Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);

            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            for (DataServeBase.KeyType keyType : keyTypes) {
                dataMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
            }

            //如果有D013的标签
            //hbase中数据有的个性标签有权重，(如果没有权重，默认权重都为0，by小桢桢)
            StringBuffer D013Result = new StringBuffer();//D013拼接字符串

            if (D013Tags.size() > 0) {
                String personTags = rpDeviceAllInfo.getTags();
                if (StringUtils.isNotBlank(personTags)) {
                    //eg:211,846,755,138,468,290,608,649,538,832,289,616,375,471,199,719,207,172,259,695,545,367,837=0.2823,0.1002,0.0955,0.0861,0.0601,0.0578,0.0529,0.0528,0.0434,0.0409,0.0348,0.0347,0.0298,0.0277,0.0277,0.023,0.0228,0.022,0.0214,0.0213,0.0193,0.0191,0.019
                    String[] personTag = personTags.split("=");//personTag[0]为个性标签，personTag[1]为权重


                    if (personTag.length == 2) {
                        String[] tags = personTag[0].split(",");//标签数组
                        String[] weights = personTag[1].split(",");//权重数组
                        Map<String, String> tagsMap = new HashMap();
                        //合并成map，key为标签，value为权重
                        for (int i = 0; i < tags.length; i++) {
                            tagsMap.put(tags[i], weights[i]);
                        }

                        List<String> bigtags = new ArrayList<String>();//一二级标签存放list
                        for (String tag : D013Tags) {
                            //非一二级标签
                            if (!StringUtils.contains(tag, "A") && !StringUtils.contains(tag, "B")) {
                                if (tagsMap.containsKey(tag)) {
                                    D013Result.append(tag + ":" + tagsMap.get(tag) + "|");
                                } else {
                                    D013Result.append(tag + ":0|");
                                }
                            } else {
                                bigtags.add(tag);
                            }
                        }
                        String bigtagsStr = "";

                        if (bigtags.size() > 0) {
                            //调用user接口，获取一二级标签下各有那些三级标签
                            bigtagsStr = tagsService.tagIf2(bigtags, rpDeviceAllInfo.getTags()).replaceAll("\"", "");

                            // A02:15;A06:798,93;B1S:278;C01:0
                            String[] tt = bigtagsStr.split(";");
                            for (String tt1 : tt) {
                                //A06:798,93
                                String[] bigtagss = tt1.split(":");
                                if ("0".equals(bigtagss[1])) {
                                    D013Result.append(bigtagss[0] + ":0|");
                                } else {
                                    String[] taggs = bigtagss[1].split(",");

                                    double s = 0.0;
                                    for (String tagg : taggs) {
                                        s += Double.parseDouble(tagsMap.get(tagg));
                                    }
                                    D013Result.append(bigtagss[0] + ":" + ArithUtil.div(s, taggs.length) + "|");
                                }
                            }
                        }
                    } else {
                        //该设备号没有带权重值
                        for (String tag : D013Tags) {
                            D013Result.append(tag + ":0|");
                        }
                    }
                    dataMap.put("D013", D013Result.substring(0, D013Result.length() - 1));
                } else {
                    for (String tag : D013Tags) {
                        D013Result.append(tag + ":null|");
                    }
                    dataMap.put("D013", D013Result.substring(0, D013Result.length() - 1));
                }
            }

            if (D030Tags.size() > 0) {
                StringBuffer D030Result = new StringBuffer();//D013拼接字符串

                //hbase中D030的值
                String D030ResultStr = rpDeviceAllInfo.getInstalled_cate_tag();
                if (StringUtils.isNotBlank(D030ResultStr)) {
                    //D030TagsArray[0]为标签数组，D030TagsArray[1]为权重数据
                    String[] D030Array = D030ResultStr.split("=");
                    String[] D030TagsArray = D030Array[0].split(",");
                    String[] D030WeightArray = D030Array[1].split(",");
                    //合并成map，key为标签，value为权重
                    Map<String, String> D030Map = new HashMap();
                    for (int i = 0; i < D030TagsArray.length; i++) {
                        D030Map.put(D030TagsArray[i], D030WeightArray[i]);
                    }
                    for (String tag : D030Tags) {
                        if (D030Map.containsKey(tag)) {
                            D030Result.append(tag + ":" + D030Map.get(tag) + "|");
                        } else {
                            D030Result.append(tag + ":0|");
                        }
                    }
                    dataMap.put("D030", D030Result.substring(0, D030Result.length() - 1));
                } else {
                    for (String tag : D030Tags) {
                        D030Result.append(tag + ":null|");
                    }
                    dataMap.put("D030", D030Result.substring(0, D030Result.length() - 1));
                }
            }

            if (D032Tags.size() > 0) {
                StringBuffer D032Result = new StringBuffer();//D032拼接字符串
                //hbase中D032的值
                String D032ResultStr = rpDeviceAllInfo.getFinance_action();
                if (StringUtils.isNotBlank(D032ResultStr)) {
                    //D030TagsArray[0]为标签数组，D030TagsArray[1]为权重数据
                    String[] D032Array = D032ResultStr.split("=");
                    String[] D032TagsArray = D032Array[0].split(",");
                    String[] D032WeightArray = D032Array[1].split(",");
                    Map<String, String> D032Map = new HashMap();
                    //合并成map，key为标签，value为权重
                    for (int i = 0; i < D032TagsArray.length; i++) {
                        D032Map.put(D032TagsArray[i], D032WeightArray[i]);
                    }
                    for (String tag : D032Tags) {
                        if (D032Map.containsKey(tag)) {
                            D032Result.append(tag + ":" + D032Map.get(tag) + "|");
                        } else {
                            D032Result.append(tag + ":0|");
                        }
                    }
                    dataMap.put("D032", D032Result.substring(0, D032Result.length() - 1));
                } else {
                    for (String tag : D032Tags) {
                        D032Result.append(tag + ":null|");
                    }
                    dataMap.put("D032", D032Result.substring(0, D032Result.length() - 1));
                }
            }

            String data = JsonUtil.ModelToJson(dataMap);
            dmpResponse.setDataRange(data);
            return new RetState(true, "ok");
        }
        return new RetState(false, "not data found");

    }

    /**
     * 设备特征识别
     *
     * @param idType
     * @param exid
     * @param dataRange
     * @param dmpResponse
     * @return
     * @throws IOException
     */
    public RetState invokeEasy(String idType, String exid, String dataRange, DmpResponse dmpResponse) throws Exception {

        // 获取IdType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null) {
            return new RetState(false, "unknown idType");
        }
        // 获取KeyTypes

        /**
         * eg:
         * "dataRange":"D004:0|1|@|D005:2|@|D013:4|12|345&525",
         */

        String[] keys = dataRange.split("\\|@\\|");
        if (keys.length == 0) {
            return new RetState(false, "dataRange is null.");
        }

        DataServeBase.KeyType[] keyTypes = new DataServeBase.KeyType[keys.length];
        String[] keyVals = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String parts[] = keys[i].split(":");
            if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
                return new RetState(false, "unknown key:" + keys[i]);
            }

            DataServeBase.KeyType keyType = getDemoBasicidKeyType(parts[0].trim());
            if (keyType == null) {
                return new RetState(false, "unknown key:" + keys[i]);
            }
            keyTypes[i] = keyType;
            keyVals[i] = parts[1].trim();
        }
        // dataServe调用

        String key = esService.queryDeviceId(it.code,it.key, exid);
        if (key == null) {
            return new RetState(false, "not data found");
        }

        RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_NAME_NEW, HBASE_FAMILY_NAME, key);
        if (rpDeviceAllInfo == null) {
            return new RetState(false, "not data found");
        }

        Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (DataServeBase.KeyType keyType : keyTypes) {
            dataMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
        }


        // match process
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            String ktCode = keyTypes[i].code;
            sb.append(ktCode + ":");
            List<String> vparts = new ArrayList<String>();
            String value = dataMap.get(keyTypes[i].code) == null ? "" : (String) dataMap.get(keyTypes[i].code);
            if (ktCode.equals("D013")) {
                vparts = Arrays.asList(value.split("=")[0].split(","));
            } else {
                vparts.add(value);
            }

            String[] kparts = keyVals[i].split("\\||&");
            String kpartsplits = StringUtils.replacePattern(keyVals[i], "[^\\|&]", "");

            if (kparts.length - kpartsplits.length() != 1) {
                return new RetState(false, "data format error:" + keyVals[i]);
            }

            boolean ret = vparts.contains(kparts[0]);
            for (int k = 1; k < kparts.length; k++) {
                if (kpartsplits.toCharArray()[k - 1] == '|') {
                    if (ret) break;
                    ret = ret || vparts.contains(kparts[i]);
                } else if (kpartsplits.toCharArray()[k - 1] == '&') ret = ret && vparts.contains(kparts[i]);
            }
            sb.append(ret ? 1 : 0);

            if (i < keys.length - 1) {
                sb.append("|@|");
            }
        }

        String data = sb.toString();
        dmpResponse.setDataRange(data);

        return new RetState(true, "ok.");

    }

    /**
     * 黑名单用户打分
     *
     * @param idType
     * @param exid
     * @param dataRange
     * @param dmpResp
     * @return
     */
    public RetState invokeNormal(String idType, String exid, String dataRange, DmpResponse dmpResp) throws Exception {
        // 获取IdType
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (it == null) {
            return new RetState(false, "unknown idType");
        }
        // 获取KeyTypes
        String[] keys = dataRange.split("\\|@\\|");
        if (keys.length == 0) {
            return new RetState(false, "dataRange is null.");
        }
        DataServeBase.KeyType[] keyTypes = new DataServeBase.KeyType[keys.length];
        for (int i = 0; i < keys.length; i++) {
            DataServeBase.KeyType keyType = DataServeBase.getScoreKT(keys[i]);
            if (keyType == null) {
                return new RetState(false, "unknown key:" + keys[i]);
            }
            keyTypes[i] = keyType;
        }

        String key = esService.queryDeviceId(it.code,it.key, exid);
        if (key == null) {
            return new RetState(false, "not data found");
        }

        RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam(HBASE_TABLE_SCORE, HBASE_FAMILY_NAME, key);
        if (rpDeviceAllInfo == null) {
            return new RetState(false, "not data found");
        }

        Map<String, String> rpDeviceAllInfoMap = JsonUtil.JsonToMap(rpDeviceAllInfo);

        Map<String, Object> retMap = new LinkedHashMap<>();
        for (DataServeBase.KeyType keyType : keyTypes) {
            retMap.put(keyType.code, rpDeviceAllInfoMap.get(keyType.key));
        }

        String data = JsonUtil.ModelToJson(retMap);
        dmpResp.setDataRange(data);
        return new RetState(true, "ok");

    }

    public RetState getAllTags(String idType, String exid, String dataRange, DmpResponse dmpResp) throws Exception {
        DataServeBase.IdType it = DataServeBase.getIdType(idType);
        if (!StringUtils.equals(it.code, "010217")) {
            exid = DigestUtils.md5Hex(exid);
        }
        String tags = codisService.get(exid);
        if (tags == null) {
            return new RetState(false, "查询结果为空");
        }
        dmpResp.setDataRange(JsonUtil.ModelToJson(tags));
        return new RetState(true, "ok");
    }


    private DataServeBase.KeyType getDemoBasicidKeyType(String keyCode) {
        if (DataServeBase.getDemoKT(keyCode) != null) return DataServeBase.getDemoKT(keyCode);
        //if (DataServeBase.getBasicidKT(keyCode) != null) return DataServeBase.getBasicidKT(keyCode);
        return null;
    }
}
