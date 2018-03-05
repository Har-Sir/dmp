package com.uuzu.chinadep.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.uuzu.chinadep.jobo.BaseJob;
import com.uuzu.chinadep.taskpojo.JobAndTrigger;
import com.uuzu.chinadep.service.IJobAndTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/job")
public class JobController {

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @PostMapping(value = "/addjob")
    public void addjob(@RequestParam(value = "jobClassName") String jobClassName,
                       @RequestParam(value = "jobGroupName") String jobGroupName,
                       @RequestParam(value = "cronExpression") String cronExpression,
                       @RequestParam(value = "requestRecovery") Integer requestRecovery,
                       @RequestParam(value = "storeDurably") Integer storeDurably,
                       @RequestParam(value = "description") String description,
                       @RequestParam(value = "jobData", required = false) String jobData) {
        JobDataMap jobDataMap = new JobDataMap();
        try {
            boolean requestRecoveryFlag = true;
            boolean storeDurablyFlag = true;
            if (requestRecovery == 0) {
                requestRecoveryFlag = false;
            }
            if (storeDurably == 0) {
                storeDurablyFlag = false;
            }

            if(StringUtils.isNotBlank(jobData)){
                jobDataMap = OBJECT_MAPPER.readValue(jobData, JobDataMap.class);
            }

            addJob(jobClassName, jobGroupName, cronExpression, requestRecoveryFlag, storeDurablyFlag, description, jobDataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addJob(String jobClassName,
                       String jobGroupName,
                       String cronExpression,
                       boolean requestRecovery,
                       boolean storeDurably,
                       String description,
                       JobDataMap jobDataMap) throws Exception {

        // 通过SchedulerFactory获取一个调度器实例
        Scheduler sched = schedulerFactoryBean.getScheduler();
        // 启动调度器
        sched.start();
        //构建job信息
        JobDetail jobDetail = JobBuilder.
                newJob(getClass(jobClassName).
                        getClass()).
                withIdentity(jobClassName, jobGroupName).
                requestRecovery(requestRecovery).
                storeDurably(storeDurably).usingJobData(jobDataMap).
                withDescription(description).
                build();
        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();
        try {
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            log.info("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
    }


    @PostMapping(value = "/pausejob")
    public ResponseEntity<Map<String, Object>> pausejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            jobPause(jobClassName, jobGroupName);
            map.put("statusText", "暂停成功");
            map.put("status", "200");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    public void jobPause(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        Scheduler sched = schedulerFactoryBean.getScheduler();
        sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @PostMapping(value = "/resumejob")
    public ResponseEntity<Map<String, Object>> resumejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            jobresume(jobClassName, jobGroupName);
            map.put("statusText", "恢复任务成功");
            map.put("status", "200");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }

    public void jobresume(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        Scheduler sched = schedulerFactoryBean.getScheduler();
        sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @PostMapping(value = "/reschedulejob")
    public ResponseEntity<Map<String, Object>> rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
                                                             @RequestParam(value = "jobGroupName") String jobGroupName,
                                                             @RequestParam(value = "cronExpression") String cronExpression) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            boolean b = jobreschedule(jobClassName, jobGroupName, cronExpression);
            if (b) {
                map.put("statusText", "更新任务成功");
                map.put("status", "200");
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    public boolean jobreschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            Date date = sched.rescheduleJob(triggerKey, trigger);
            if (null != date) {
                return true;
            }
        } catch (SchedulerException e) {
            log.info("更新定时任务失败" + e);
            throw new Exception("更新定时任务失败");
        }
        return false;
    }


    @PostMapping(value = "/deletejob")
    public ResponseEntity<Map<String, Object>> deletejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            boolean b = jobdelete(jobClassName, jobGroupName);
            if (b) {
                map.put("statusText", "删除任务成功");
                map.put("status", "200");
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    public boolean jobdelete(String jobClassName, String jobGroupName) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        Scheduler sched = schedulerFactoryBean.getScheduler();
        sched.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        boolean b = sched.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        sched.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        return b;
    }


    @GetMapping(value = "/queryjob")
    public Map<String, Object> queryjob(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
        for (JobAndTrigger jobAndTrigger1 : jobAndTrigger.getList()) {
            try {
                Object o = ByteToObject(jobAndTrigger1.getJOB_DATA());
                JobDataMap jobDataMap = (JobDataMap) o;
                jobAndTrigger1.setSTRING_JOB_DATA(OBJECT_MAPPER.writeValueAsString(jobDataMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;
    }

    public Object ByteToObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            // bytearray to object
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);

            obj = oi.readObject();

        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                bi.close();
                oi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }

}
