package com.uuzu.chinadep.web;

import com.uuzu.chinadep.exception.HBaseNoDataException;
import com.uuzu.chinadep.exception.SolrNoDataException;
import com.uuzu.chinadep.service.PersonaService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.chinadep.utils.MessageConvert;
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

/**
 * Created by zhoujin on 2017/6/26.
 */

@Slf4j
@Controller
@Api(description = "用户画像")
public class DmpAbnormalController {

    @Autowired
    UserService userService;

    @Autowired
    PersonaService personaService;


    @PostMapping("/abnormal")
    @ApiOperation(value = "黑名单用户打分" , notes = "黑名单用户打分  confluence uri: http://c.mob.com/display/MDS/MobDeviceAbnormal")
    @ApiImplicitParam(name = "dmpRequest", value = "请求参数", required = true, dataType = "DmpRequest", paramType = "body")
    public ResponseEntity<DmpResponse> post(@RequestBody @Validated DmpRequest dmpRequest) throws Exception {
        RetState retState = null;
        DmpResponse dmpResponse = new DmpResponse();
        try {
            // 解析报文
            dmpResponse.setBusiSerialNo(dmpRequest.getBusiSerialNo());

            //请求mob_user接口，获取用户对应的asc_token
            String token = "";
            try {
                token = userService.queryTokenByUserId(dmpRequest.getUserId());
            }catch (NullPointerException e){
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "无效的会员");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }catch (Exception e){
                //调用错误，清除缓存
                userService.removeUserTokenCache();
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "会员服务异常");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }
            //String token = "{\"key_algorithm\":\"AES\", \"cipher_algorithm\":\"AES/CBC/PKCS5Padding\", \"key\":\"1234667890123456\", \"iv_parameter\":\"1234567892546398\"}";


            // 解密
            retState = MessageConvert.decode(dmpRequest, token);
            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG, retState.getMsg());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }

            //获取数据
            retState = personaService.invokeNormal(dmpRequest.getIdType(), dmpRequest.getExid(), dmpRequest.getDataRange(), dmpResponse);


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
            if (e instanceof SolrNoDataException) {
                throw new SolrNoDataException(dmpRequest.getBusiSerialNo());
            }else if(e instanceof HBaseNoDataException){
                throw new HBaseNoDataException(dmpRequest.getBusiSerialNo());
            }
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL);
            //e.printStackTrace();
        } finally {
            if (StringUtils.isBlank(dmpResponse.getResCode())) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_OTHER);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
    }
}
