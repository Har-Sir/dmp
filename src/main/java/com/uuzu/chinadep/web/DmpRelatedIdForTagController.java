package com.uuzu.chinadep.web;

import com.alibaba.fastjson.JSON;
import com.uuzu.chinadep.pojo.DmpRelatedIdForTagResponse;
import com.uuzu.common.es.EsIdMapping;
import com.uuzu.chinadep.pojo.IdTypeConstant;
import com.uuzu.chinadep.service.RelatedIdForTagService;
import com.uuzu.chinadep.service.httpapi.EsIdMappingHttpService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.chinadep.utils.CommonUtils;
import com.uuzu.chinadep.utils.MD5Utils;
import com.uuzu.chinadep.utils.MessageConvert;
import com.uuzu.common.es.EsIdMappingNew;
import com.uuzu.common.pojo.DmpRequest;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@Api(description = "上报换标签接口")
public class DmpRelatedIdForTagController {

    @Autowired
    UserService userService;

    @Autowired
    RelatedIdForTagService relatedIdForTagService;

    @Autowired
    EsIdMappingHttpService esIdMappingHttpService;

    @PostMapping("/dmp/relatedIdforTag")
    @ApiOperation(value = "上报换标签接口" , notes = "上报换标签接口")
    @ApiImplicitParam(name = "dmpRequest", value = "请求参数", required = true, dataType = "DmpRequest", paramType = "body")
    public ResponseEntity<DmpRelatedIdForTagResponse> relatedIdforTag(@RequestBody @Validated DmpRequest dmpRequest) throws Exception {
        RetState retState = null;
        DmpRelatedIdForTagResponse dmpResponse = new DmpRelatedIdForTagResponse();
        try {
            dmpResponse.setBusiSerialNo(dmpRequest.getBusiSerialNo());
            dmpResponse.setUserId(dmpRequest.getUserId());

            //query mob_user by userId
            String token = "";
            try {
                token = userService.queryTokenByUserId(dmpRequest.getUserId());
            }catch (NullPointerException e){
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "invalid user");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }catch (Exception e){
                userService.removeUserTokenCache();
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "service error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }

            // decode
            retState = MessageConvert.decode(dmpRequest, token);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, retState.getMsg());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }
            String exid = dmpRequest.getExid();
            String[] exids = exid.split(",");
            // begin to add into elasticSearch
            addElasticSearch(exids, dmpRequest.getIdType(), dmpRequest.getUserId());
            log.info("relatedIdforTag, save into elasticSearch, exids=[{}], idType=[{}], userId=[{}]", JSON.toJSON(exids), dmpRequest.getIdType(), dmpRequest.getUserId());
            // end to add into elasticSearch

            if (exids.length < 2 || StringUtils.isBlank(exids[0]) || StringUtils.isBlank(exids[1])) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, "exid must contains two types of data at least");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }

            if (!CommonUtils.isMobileNO(exids[1])) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, "phone is not correct in exid");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }

            //get response data
            retState = relatedIdForTagService.relatedIdForTag(dmpRequest.getIdType(), exids[0], dmpRequest.getDataRange(), dmpResponse);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL, retState.getMsg());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }

            // encode
            retState = MessageConvert.encode(dmpResponse, token);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, retState.getMsg());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }

            // set success flag
            dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
            return ResponseEntity.ok(dmpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        dmpResponse.setRespState(DmpResponse.RespState.ERROR_OTHER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
    }


    /**
     * log user request into elasticSearch
     * @param exids
     * @param idType
     * @param userId
     */
    private void addElasticSearch(String[] exids, String idType, String userId) {
        if(IdTypeConstant.IMEI_PHONE_LON_LAT_ADDRESS.equals(idType)){
            EsIdMappingNew esIdMappingNew = new EsIdMappingNew();
            esIdMappingNew.setMac(exids[0]);
            esIdMappingNew.setPhone(exids[1]);
            esIdMappingNew.setFirst_level(exids[2]);
            esIdMappingNew.setSecond_level(exids[3]);
            esIdMappingNew.setPlace_name(exids[4]);
            esIdMappingNew.setAddress(exids[5]);
            esIdMappingNew.setLatitude(exids[6]);
            esIdMappingNew.setLongitude(exids[7]);
            esIdMappingNew.setUserId(userId);
            esIdMappingHttpService.saveEsIdMappingNew(esIdMappingNew);

        }else{
            EsIdMapping esIdMapping = new EsIdMapping();
            // imei phone
            if (IdTypeConstant.IMEI_PHONE_LON_LAT.equals(idType)) {
                esIdMapping.setImei_idfa(exids[0]);
                esIdMapping.setEncryptionImei(MD5Utils.encode(exids[0]));
            } else {
                esIdMapping.setImei_idfa(null);
                esIdMapping.setEncryptionImei(null);
            }
            esIdMapping.setMac(null);
            esIdMapping.setEncryptionMac(null);
            esIdMapping.setUserId(userId);
            esIdMapping.setPhone(exids.length < 2 ? null : exids[1]);
            esIdMapping.setLongitude(exids.length < 3 ? null : exids[2]);
            esIdMapping.setLatitude(exids.length < 4 ? null : exids[3]);
            esIdMappingHttpService.saveEsIdMapping(esIdMapping);
        }

    }
}
