package com.uuzu.chinadep.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.uuzu.chinadep.pojo.UserDevice;
import com.uuzu.chinadep.pojo.UserJsonDevice;
import com.uuzu.chinadep.pojo.UserStore;
import com.uuzu.chinadep.service.codis.CodisService;
import com.uuzu.chinadep.service.httpapi.FastDFSService;
import com.uuzu.chinadep.utils.ConstantConfig;
import com.uuzu.chinadep.utils.DateUtils;
import com.uuzu.common.pojo.DmpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zhongqi on 2017/9/1.
 */
@Service
@Slf4j
public class CustomerDeviceService {
    @Autowired
    private FastDFSService   fastDFSService;
    @Autowired
    private UserStoreService userStoreService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String failedCode = "000001";
    private static final String TIMESTAMP = "timeStamp";
    private static final String DEVICE = "device";
    private static final Long time = 1483200000L;

    @Autowired
    private CodisService codisService;

    public ResponseEntity<DmpResponse> customerDevice(UserDevice userdevice) throws IOException {
        DmpResponse dmpResponse = new DmpResponse();
        try {
            UserStore user = null;
            //codis缓存的加入
            String json = codisService.get(ConstantConfig.PROJECT_NAME + "-" + ConstantConfig.PROJECT_O2O +
                    "-userStoreRegister_" + userdevice.getUserid() + "-" + userdevice.getStoreid());
            if (StringUtils.isNotBlank(json)) {
                user = JSON.parseObject(json, UserStore.class);
            } else {
                user = userStoreService.selectCountByUserAndStore(userdevice.getUserid(), userdevice.getStoreid());
                if (user == null) {
                    log.info("用户或店铺不存在,userId:[{}],updateTime:[{}]",userdevice.getUserid(),userdevice.getUpdatetime());
                    dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG,"用户或店铺不存在");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
                }
                codisService.set(ConstantConfig.PROJECT_NAME + "-" + ConstantConfig.PROJECT_O2O +
                        "-userStoreRegister_" + user.getUserid() + "-" + user.getStoreid(), JSON.toJSONString(user));
            }
            //检查数据
            if (!dataCheck(userdevice)) {
                log.info("请求参数错误，文件信息:[{}]",userdevice.toString());
                dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
            }
            StringBuffer totaldata = getDate(userdevice);
            MultipartFile mockMultipartFile = new MockMultipartFile("files", "userDeviceRealtime.txt", "multipart/form-data", new ByteArrayInputStream(totaldata.toString().getBytes()));
            String path = "/user/mobeye/clientdata/o2o/" + userdevice.getUserid() + "/" + DateUtils.getDateBySecond(userdevice.getUpdatetime()) + "/userDeviceRealtime.txt";
            String returnPath = fastDFSService.uploadAppend("user_device_mac", path, mockMultipartFile);

            //判断dfs返回结果
            if (returnPath.equals(failedCode)) {//文件上传失败
                log.info("文件上传失败[{}],path:[{}],文件信息为:[{}],文件大小为[{}]",returnPath,path,userdevice.toString(),mockMultipartFile.getSize());
                dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
                return ResponseEntity.ok(dmpResponse);

            } else {//文件上传成功
                log.info("文件上传成功,path:[{}]",path);
                dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
                return ResponseEntity.ok(dmpResponse);
            }
        }catch(NullPointerException e){
            log.error("文件上传空值异常:[{}],失败文件信息为:[{}]",e ,userdevice.toString());
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG,"上传数据格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);

        }catch(JSONException e){
            log.error("文件上传解析json异常:[{}],失败文件信息为:[{}]",e ,userdevice.toString());
            dmpResponse.setRespState(DmpResponse.RespState.ERROR_REQUEST_ARG,"上传数据格式错误");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dmpResponse);
        }catch (Exception e) {
            log.error("文件上传失败异常:[{}],失败文件信息为:[{}]",e ,userdevice.toString());
            dmpResponse.setRespState(DmpResponse.RespState.SUCCESS);
            return ResponseEntity.ok(dmpResponse);
        }
    }

    public boolean dataCheck(UserDevice user) {
        String mat = "^[[0-9]*[1-9][0-9]*]{10}$";
        if(!(user.getUpdatetime().matches(mat))){
            return false;
        }else  if ( Long.parseLong(user.getUpdatetime()) < time ) {
            return false;
        } else if (StringUtils.isBlank(user.getContent()) || user.getContent().equals("[{}]")||user.getContent().equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    public StringBuffer getDate(UserDevice userdevice) throws IOException{
        JSONArray jarr = JSONArray.parseArray(userdevice.getContent());
        StringBuffer totaldata = new StringBuffer("");
        for (Iterator iterator = jarr.iterator(); iterator.hasNext(); ) {
            JSONObject data = (JSONObject) iterator.next();
            String clienttime = data.get(TIMESTAMP).toString();
            String device = data.get(DEVICE).toString();
            UserJsonDevice ujd = new UserJsonDevice();
            ujd.setUserid(userdevice.getUserid());
            ujd.setStoreid(userdevice.getStoreid());
            ujd.setData_type(userdevice.getData_type());
            ujd.setEncrypt_type(userdevice.getEncrypt_type());
            ujd.setDevice(device);
            ujd.setClienttime(clienttime);
            ujd.setUpdatetime(userdevice.getUpdatetime());
            ujd.setProcesstime(Long.toString(System.currentTimeMillis() / 1000));
            String json = OBJECT_MAPPER.writeValueAsString(ujd) + "\n";
            totaldata.append(json);
         }
        return totaldata;
    }

}
