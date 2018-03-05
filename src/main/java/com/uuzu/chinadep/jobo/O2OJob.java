package com.uuzu.chinadep.jobo;

import com.uuzu.common.utils.DateTimeUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by zhoujin on 2017/9/6.
 */
public class O2OJob implements BaseJob {

    //O2O商业地理，每小时定时起任务
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String busiSerialNo = "chinadmp_" + new Date().getTime() + "_O2O_Commercial_Geography";
        String projectName = "chinadmp";
        String businessName = "O2O_Commercial_Geography";
        String shellDir = "/home/dmpots/taskScheduleForMobEye/tasks/chinadmp/dmp_o2o_meeting.sh";
        //String shellDir = "/home/dba/taskScheduleSystem/tasks/cluster_32_160/chinadmp/dmp_o2o_meeting.sh";
        //String clusterName = "bd04-067";
        String dateStr = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrDate(), DateTimeUtil.DAY_FORMAT_SECOND);
        String reqData = "{\\\"userid\\\":\\\"89\\\",\\\"storeid\\\":\\\"10\\\",\\\"date\\\":\\\"" + dateStr + "\\\"}";
        try {
            reqData = URLEncoder.encode(reqData,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 20170925 by zhongqi
         */

        String createUser = "lixing,zhoujin";
        String url = "http://10.5.34.55:8090/scheduler/submitTask?busiSerialNo=" + busiSerialNo + "&projectName=" + projectName +
                "&businessName=" + businessName + "&shellDir=" + shellDir + "&reqData=" + reqData+"&createUser="+createUser+"&needMail=2";

 //       String url = "http://10.5.34.55:8090/scheduler/newTask?busiSerialNo=" + busiSerialNo + "&projectName=" + projectName +"&businessName=" + businessName + "&shellDir=" + shellDir + "&reqData=" + reqData;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
