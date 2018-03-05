package com.uuzu.chinadep.web;


import com.uuzu.chinadep.pojo.UserDevice;
import com.uuzu.chinadep.service.CustomerDeviceService;
import com.uuzu.common.pojo.DmpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


/**
 * Created by zhongqi on 2017/8/22.
 */
@RestController
@RequestMapping("/customerDevice")
@Api(description = "用户实时设备号")
public class CustomerDeviceController {

    @Autowired
    private CustomerDeviceService customerDeviceService;

    @ApiOperation("用户实时设备号接口数据存储")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "storeid", value = "店铺ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "data_type", value = "数据类型", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "encrypt_type", value = "加密", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "数据", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "updatetime", value = "数据传输时间", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<DmpResponse> list(UserDevice userdevice) throws IOException {
        return customerDeviceService.customerDevice(userdevice);
    }
}
