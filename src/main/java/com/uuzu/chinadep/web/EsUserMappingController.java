//package com.uuzu.chinadep.web;
//
//import com.uuzu.chinadep.exception.HBaseNoDataException;
//import com.uuzu.chinadep.pojo.DmpEquipmentData;
//import com.uuzu.chinadep.pojo.RpDeviceAllInfo;
//import com.uuzu.chinadep.service.BigDataService;
//import com.uuzu.chinadep.utils.MD5Utils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Slf4j
//@Controller
////@ApiIgnore
//@Api(description = "dmp-chinadep OpenApi")
//@RequestMapping("dmpEsData")
//public class EsUserMappingController {
//
//
//    @PostMapping("save")
//    @ApiOperation(value = "dmp es 数据准备", notes = "数据准备")
//    @ApiImplicitParam(name = "esUserMappings", value = "请求参数", required = true, dataType = "List<EsUserMapping>", paramType = "body")
//    public ResponseEntity<String> saveEsUserMapping(@RequestBody List<DmpEquipmentData> dmpEquipmentData) {
//        try {
//            int i = this.esUserMappingService.saveEsUserMapping(dmpEquipmentData);
//            System.err.println("apiPostNum:" + dmpEquipmentData.size() + "LL---------" + "insertNum:" + i + "LL");
//
//
//            return ResponseEntity.ok("新增数据:" + i + "条");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//
//    }
//
//    @PostMapping("save1")
//    @ApiOperation(value = "dmp es 数据准备", notes = "数据准备")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "type", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "imei_idfa", value = "imei_idfa", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "mac", value = "mac", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "phone", value = "phone", required = true, dataType = "string", paramType = "query")
//    })
//    public ResponseEntity<String> saveEsUserMapping1(@RequestParam(value = "id") String id,
//                                                     @RequestParam(value = "imei_idfa") String imei_idfa,
//                                                     @RequestParam(value = "mac") String mac,
//                                                     @RequestParam(value = "phone") String phone) {
//        try {
//            DmpEquipmentData esUserMapping = new DmpEquipmentData();
//            esUserMapping.setId(id);
//            esUserMapping.setImei_idfa(imei_idfa);
//            esUserMapping.setMac(mac);
//            esUserMapping.setPhone(phone);
//            esUserMapping.setEncryptionImei(MD5Utils.encode(imei_idfa));
//            esUserMapping.setEncryptionMac(MD5Utils.encode(mac));
//            esUserMapping.setEncryptionPhone(MD5Utils.encode(phone));
//            esUserMapping.setUpdateTime(new Date(1513312309));
//            this.esUserMappingRepository.save(esUserMapping);
//
//            return ResponseEntity.ok("新增数据:" + 1 + "条");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//
//    }
//
//    @PostMapping("queryEs")
//    @ApiOperation(value = "es数据查询", notes = "es数据查询")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type", value = "type", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "value", value = "value", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "string", paramType = "query")
//
//    })
//    public ResponseEntity<String> queryEs(@RequestParam(value = "type") String type,
//                                          @RequestParam(value = "value") String value,
//                                          @RequestParam(value = "password") String password) {
//        if (password.equals("mob_zhoujin")) {
//            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//            boolQueryBuilder.must(QueryBuilders.matchQuery(type, value));
//
//            SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                    .withQuery(boolQueryBuilder)
//                    .build();
//            Page<DmpEquipmentData> search = esUserMappingRepository.search(searchQuery);
//            List result = new ArrayList();
//            for (DmpEquipmentData esUserMapping : search) {
//                result.add(esUserMapping.toString());
//            }
//            return ResponseEntity.ok(result.toString());
//        }
//        return null;
//    }
//
//    @Autowired
//    BigDataService bigDataService;
//
//    @PostMapping("queryHbase")
//    @ApiOperation(value = "通过deviceid查询hbase", notes = "通过deviceid查询hbase")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "deviceId", value = "deviceId", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "string", paramType = "query")
//
//    })
//    public ResponseEntity<String> queryHbase(
//            @RequestParam(value = "deviceId") String deviceId,
//            @RequestParam(value = "password") String password) throws HBaseNoDataException {
//        if (password.equals("mob_zhoujin")) {
//            RpDeviceAllInfo rpDeviceAllInfo = bigDataService.getBeanByParam("rp_device_profile_info", "cf", deviceId);
//            return ResponseEntity.ok(rpDeviceAllInfo.toString());
//        }
//        return null;
//    }
//
//
//}
