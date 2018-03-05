package com.uuzu.chinadep.jobo;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

@Slf4j
//@DisallowConcurrentExecution //当前任务不允许并发执行
public class SampleJobA implements BaseJob {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String[] keys = jobDataMap.getKeys();
        for (String key : keys){
            System.err.print(key);
        }

        log.info("dmp-chinadep 测试定时任务...");
    }
}

