package com.uuzu.chinadep.web;

import com.uuzu.chinadep.config.ParamConfig.DataServeBase;
import com.uuzu.chinadep.service.httpapi.EsService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.chinadep.utils.MessageConvert;
import com.uuzu.common.es.AdDeviceRiskWhiteList;
import com.uuzu.common.pojo.DmpRequest;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import com.uuzu.common.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhoujin
 */
@Slf4j
@Controller
@Api(description = "广告设备白名单风险评分")
public class AdDeviceRiskWhiteListControl {
    @Autowired
    UserService userService;

    @Autowired
    EsService esService;

    /**
     * 标签查询接口，计费
     *
     * @param dmpRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/getAdDeviceRishWhiteList")
    @ApiOperation(value = "广告设备白名单风险评分", notes = "广告设备白名单风险评分")
    @ApiImplicitParam(name = "dmpRequest", value = "请求参数", required = true, dataType = "DmpRequest", paramType = "body")
    public ResponseEntity<DmpResponse> getAdDeviceRishWhiteList(@RequestBody @Validated DmpRequest dmpRequest) throws Exception {
        RetState retState = null;

        DmpResponse dmpResponse = new DmpResponse();
        try {
            // 解析报文
            dmpResponse.setBusiSerialNo(dmpRequest.getBusiSerialNo());

            //请求mob_user接口，获取用户对应的asc_token
            String token = "";
            try {
                token = userService.queryTokenByUserId(dmpRequest.getUserId());
            } catch (NullPointerException e) {
                log.error(e.getMessage(), e);
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "无效的会员");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                //调用错误，清除缓存
                userService.removeUserTokenCache();
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "会员服务异常");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }


            // 解密
            retState = MessageConvert.decode(dmpRequest, token);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, retState.getMsg());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }

            // 获取IdType
            DataServeBase.IdType it = DataServeBase.getIdType(dmpRequest.getIdType());
            if (it == null) {
                retState = new RetState(false, "unknown idType");
            }
            AdDeviceRiskWhiteList adDeviceRishWhiteList = esService.getAdDeviceRishWhiteList(it.code, it.key, dmpRequest.getExid());
            if (adDeviceRishWhiteList == null) {
                retState = new RetState(false, "not data found");
            }else{
                dmpResponse.setDataRange(JsonUtil.ModelToJson(adDeviceRishWhiteList));
                retState = new RetState(true, "ok");
            }

            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL, retState.getMsg());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }

            // 加密
            retState = MessageConvert.encode(dmpResponse, token);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, retState.getMsg());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }

            // set success flag
            dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
            return ResponseEntity.ok(dmpResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        dmpResponse.setRespState(DmpResponse.RespState.ERROR_OTHER);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
    }
}
