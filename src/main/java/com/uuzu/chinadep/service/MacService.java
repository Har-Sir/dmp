package com.uuzu.chinadep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.pojo.DmpTask;
import com.uuzu.chinadep.repository.DmpTaskRepository;
import com.uuzu.chinadep.service.httpapi.CostApiService;
import com.uuzu.chinadep.service.httpapi.FastDFSService;
import com.uuzu.chinadep.config.ParamConfig.DataServeBase;
import com.uuzu.common.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by zhoujin on 2017/8/9.
 */
@Slf4j
@Service
public class MacService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    DmpTaskRepository dmpTaskRepository;

    @Autowired
    CostApiService costApiService;

    @Autowired
    FastDFSService fastDFSService;

    public String invoke(String cost_id, String user_id,String accessKey, MultipartFile multipartFile) throws IOException {
        String a = "ok";
        String str = "";
        StringBuffer sb = new StringBuffer();
        System.out.println(a);
        BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
        while ((str = br.readLine()) != null) {
            str = str.replace("\n", "");
            sb.append(str);
        }

        MultipartFile mockMultipartFile = new MockMultipartFile("files", multipartFile.getOriginalFilename(), "multipart/form-data", new ByteArrayInputStream(sb.toString().getBytes()));

        String busiSerialNo = "chinadmp_" + new Date().getTime() + "_mac_statistics_" + user_id;
        busiSerialNo = DigestUtils.md5Hex(busiSerialNo);

        // 计费ID
        String cost = costApiService.queryByCostID(cost_id);
        if (StringUtils.isEmpty(cost)) {
            throw new NullPointerException("cost_id不存在");
        }
        //System.err.println(cost);
        JsonNode jsonNode = OBJECT_MAPPER.readTree(cost);
        String tagsStr = jsonNode.get("tags").asText();
        String tagsJson = "";
        if (jsonNode.get("tags_type").asInt() == 1) {
            String[] tags = tagsStr.split(",");

            String[] tagsVar = new String[tags.length];
            for (int i = 0; i < tags.length; i++) {
                tagsVar[i] = DataServeBase.getDemoKT(tags[i]).key + " " + tags[i];
            }
            for (String s : tagsVar) {
                tagsJson += s + ",";
            }
        } else if (jsonNode.get("tags_type").asInt() == 2) {
            tagsJson = tagsStr;
        }

        // 调用小明的文件服务
        String pathStr = fastDFSService.upload(user_id, null, mockMultipartFile);//文件地址

        JsonNode pathNode = OBJECT_MAPPER.readTree(pathStr);
        String path = pathNode.get("resMsg").asText();
        //path = "http://10.5.1.45:20100/mobeye/download?user_id=xiaoling@uuzu.com&path=" + path;
        path = "http://upload.pmp.appgo.cn/mobeye/download?user_id=xiaoling@uuzu.com&path=" + path;

        String reqData = "{\\\\\"tags_type\\\\\":\\\\\"" + jsonNode.get("tags_type").asInt() + "\\\\\",\\\\\"busiSerialNo\\\\\":\\\\\"" + busiSerialNo + "\\\\\",\\\\\"path\\\\\":\\\\\"" + path + "\\\\\",\\\\\"tags\\\\\":\\\\\"" + tagsJson.substring(0, tagsJson.length() - 1) + "\\\\\",\\\\\"accessKey\\\\\":\\\\\"" + accessKey + "\\\\\"}";
        reqData = URLEncoder.encode(reqData,"utf-8");

        /**
         * 20170925 by zhongqi
         */

        String createUser = "zhoujin,lixing";

        String url = "http://10.5.34.55:8090/scheduler/submitTask?busiSerialNo=" + busiSerialNo + "&projectName=chinadmp&businessName=mac_statistics&shellDir=/home/dmpots/taskScheduleForMobEye/tasks/chinadmp/dmp_mac_statistic.sh&reqData=" + reqData+"&createUser="+createUser+"&needMail=2";
        //String url = "http://10.5.34.55:8090/scheduler/newTask?busiSerialNo=" + busiSerialNo + "&projectName=chinadmp&businessName=mac_statistics&shellDir=/home/dba/taskScheduleSystem/tasks/cluster_32_160/chinadmp/dmp_mac_statistic.sh&reqData=" + reqData;
        //String url = "http://10.5.34.55:8090/scheduler/newTask?busiSerialNo=" + busiSerialNo + "&projectName=chinadmp&businessName=mac_statistics&shellDir=/home/dba/taskScheduleSystem/tasks/cluster_1_2/chinadmp/dmp_mac_statistic.sh&clusterName=bd04-067&reqData=" + reqData;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        JsonNode res = OBJECT_MAPPER.readTree(response.body().string());
        Integer responeCode = res.get("responeCode").asInt();
        String responeMessage =res.get("responeMessage").asText();
        if (responeCode != 200){
            throw new IOException("Unexpected Msg:" + responeMessage);
        }
        DmpTask dmpTask = new DmpTask();
        dmpTask.setBusiserialno(busiSerialNo);
        dmpTask.setCreateAt(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrDate(), DateTimeUtil.DAY_FORMAT_MINUTUES));
        dmpTask.setInputPath(path);
        dmpTask.setState(0);//状态为1表示成功，后期可以写成enum
        dmpTask.setCost_id(cost_id);
        dmpTaskRepository.save(dmpTask);

        return busiSerialNo;
    }


    public ByteArrayOutputStream parse(InputStream in) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

}
