package com.uuzu.chinadep.web;

import com.uuzu.chinadep.exception.SolrNoDataException;
import com.uuzu.chinadep.repository.AdTagsRepository;
import com.uuzu.chinadep.service.PersonaService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.common.pojo.AdTags;
import com.uuzu.common.pojo.DmpRequest;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.pojo.RetState;
import com.uuzu.common.utils.DateTimeUtil;
import com.uuzu.common.utils.JsonUtil;
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

import java.util.Date;


/*
 * Created by zhoujin on 2017/7/14
 * 广告部门接口对接，返回用户全标签
*/


@Slf4j
@Controller
@Api(description = "用户标签查询")
public class DmpTagsController {



    @Autowired
    UserService userService;

    @Autowired
    PersonaService personaService;

    @Autowired
    AdTagsRepository adTagsRepository;

    @PostMapping("/dmptags")
    @ApiOperation(value = "用户标签查询" , notes = "全标签返回")
    @ApiImplicitParam(name = "dmpRequest", value = "请求参数", required = true, dataType = "DmpRequest", paramType = "body")
    public ResponseEntity<DmpResponse> post(@RequestBody @Validated DmpRequest dmpRequest) throws SolrNoDataException {
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
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "会员服务异常");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }
            String adtagstr = dmpRequest.getDataRange();
            if("{}".equals(adtagstr)){
                dmpResponse.setRespState(DmpResponse.RespState.NULL_DATARANGE, DmpResponse.RespState.NULL_DATARANGE.desc);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }
            AdTags adTags = JsonUtil.JsonToModel(adtagstr,AdTags.class);
            adTags.setCreatAt(DateTimeUtil.getFormatDateTime(new Date()));
            // 存入mongo
            adTagsRepository.save(adTags);

            //获取数据
            retState = personaService.getAllTags(dmpRequest.getIdType(), dmpRequest.getExid(), dmpRequest.getDataRange(), dmpResponse);

            if (!retState.getFlag()) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL, retState.getMsg());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            }

            // set success flag
            dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
            return ResponseEntity.ok(dmpResponse);
        } catch (Exception e) {
            if (e instanceof SolrNoDataException) {
                throw new SolrNoDataException(dmpRequest.getBusiSerialNo());
            }
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_RESPONSE_NULL);
            e.printStackTrace();
        } finally {
            if (StringUtils.isBlank(dmpResponse.getResCode())) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_OTHER);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
    }
}
