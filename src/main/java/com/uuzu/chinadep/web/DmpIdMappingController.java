package com.uuzu.chinadep.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.IdMappingInfo;
import com.uuzu.chinadep.service.IdMappingService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.common.codec.Aes;
import com.uuzu.common.codec.BaseConfig;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Slf4j
@Controller
@Api(description = "单条实时的关联ID查询服务")
public class DmpIdMappingController {

    @Autowired
    IdMappingService idMappingService;
    @Autowired
    UserService userService;

    @ApiOperation("单条实时的关联ID查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiSerialNo", value = "业务流水号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "inIdType", value = "输入的id类型", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "exid", value = "输入的外部id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "outIdType", value = "输出的id类型", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "user_id", value = "dmp用户id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "extranum", value = "额外返回的个数", dataType = "int", paramType = "query")
    })
    @PostMapping("/dmp/idmapping")
    @ResponseBody
    public ResponseEntity<IdMappingInfo> idmapping(@RequestParam(value = "busiSerialNo") String busiSerialNo,
                                            @RequestParam(value = "inIdType") String inIdType,
                                            @RequestParam(value = "exid") String exid,
                                            @RequestParam(value = "outIdType") String outIdType,
                                            @RequestParam(value = "user_id") String user_id,
                                            @RequestParam(value = "extranum", required = false)Integer extranum) throws IOException {

        IdMappingInfo dmpResponse = new IdMappingInfo();
        dmpResponse.setBusiSerialNo(busiSerialNo);
        dmpResponse.setOutIdType(outIdType);
        dmpResponse.setUserId(user_id);
        //请求mob_user接口，获取用户对应的asc_token
        String token = "";
        try {
            token = userService.queryTokenByUserId(user_id);
        }catch (NullPointerException e){
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "invalid user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
        }catch (Exception e){
            userService.removeUserTokenCache();
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "service error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
        }

        if (StringUtils.isBlank(token)) {
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, "user token is empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
        }

        try {
            ObjectMapper OBJECT_MAPPER = new ObjectMapper();
            Aes.AesToken at = OBJECT_MAPPER.readValue(token, Aes.AesToken.class);
            exid = Aes.decodeBase64(exid, at, BaseConfig.DEFAULT_CHARSET);
            RetState result = idMappingService.idMapping(inIdType, exid, outIdType, extranum, dmpResponse, at);
            if (!result.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL, result.getMsg());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }
            dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
            return ResponseEntity.ok(dmpResponse);
        } catch (Exception e) {
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, "exId must encode by AES");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
        }

    }
}
