package com.uuzu.chinadep.web;

import com.uuzu.chinadep.service.MacService;
import com.uuzu.chinadep.service.httpapi.UserService;
import com.uuzu.common.pojo.DmpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by zhoujin on 2017/8/8.
 */
@Slf4j
@Controller
@Api(description = "投户外合作需求接口")
public class DmpMacControl {


    @Autowired
    UserService userService;


    @Autowired
    MacService macService;


    @PostMapping("/dmpmacs")
    @ApiOperation(value = "投户外合作需求接口" , notes = "投户外合作需求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "user_id", dataType = "String", required = true, value = "用户名"),
            @ApiImplicitParam(paramType = "form", name = "cost_id", dataType = "String", required = true, value = "计费id"),
            @ApiImplicitParam(paramType = "form", name = "accessKey", dataType = "String", required = true, value = "accessKey"),
            @ApiImplicitParam(paramType = "form", name = "files", dataType = "File", required = true, value = "文件上传")
    })
    public ResponseEntity<DmpResponse> mac(@RequestParam(value = "user_id") String user_id,
                                           @RequestParam(value = "cost_id") String cost_id,
                                           @RequestParam(value = "accessKey") String accessKey,
                                           @RequestParam("files") MultipartFile multipartFile) throws IOException {
        DmpResponse dmpResponse = new DmpResponse();
        try {

            //请求mob_user接口，获取用户对应的asc_token
            String token = "";
            try {
                token = userService.queryTokenByUserId(user_id);
            } catch (NullPointerException e) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "无效的会员");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dmpResponse);
            } catch (Exception e) {
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_AUTHRORITY, "会员服务异常");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
            }
            String busiSerialNo = macService.invoke(cost_id, user_id, accessKey, multipartFile);
            dmpResponse.setBusiSerialNo(busiSerialNo);

        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof NullPointerException){
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG);
                dmpResponse.setDataRange(e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_OTHER);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dmpResponse);
        }

        dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
        return ResponseEntity.ok(dmpResponse);
    }
}
