package com.uuzu.chinadep.taskpojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class JobAndTrigger implements Serializable{
    private String JOB_NAME;
    private String JOB_GROUP;
    private String JOB_CLASS_NAME;
    private String TRIGGER_NAME;
    private String TRIGGER_GROUP;
    private BigInteger REPEAT_INTERVAL;
    private BigInteger TIMES_TRIGGERED;
    private String CRON_EXPRESSION;
    private String TIME_ZONE_ID;
    private String DESCRIPTION = "无任务描述信息";
    private String IS_DURABLE;
    private String REQUESTS_RECOVERY;
    private String TRIGGER_STATE;
    private byte[] JOB_DATA;
    private String STRING_JOB_DATA = "未知任务参数数据";

    private long NEXT_FIRE_TIME;
    private long PREV_FIRE_TIME;

    public String getTRIGGER_STATE() {
        return TRIGGER_STATE;
    }
    public void setTRIGGER_STATE(String TRIGGER_STATE) {
        switch (TRIGGER_STATE){
            case "WAITING" : this.TRIGGER_STATE = "1";
                break;

            case "PAUSED" : this.TRIGGER_STATE = "2";
                break;

            case "ACQUIRED" : this.TRIGGER_STATE = "3";
                break;

            case "BLOCKED" : this.TRIGGER_STATE = "4";
                break;

            case "ERROR" : this.TRIGGER_STATE = "5";
                break;

            default: this.TRIGGER_STATE = "6";
        }

    }

    public String getSTRING_JOB_DATA() {
        return STRING_JOB_DATA;
    }

    public void setSTRING_JOB_DATA(String job_data) {
            this.STRING_JOB_DATA = job_data;
    }
}
