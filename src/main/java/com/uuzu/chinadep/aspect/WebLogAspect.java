package com.uuzu.chinadep.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuzu.chinadep.service.UserApiaskNumService;
import com.uuzu.common.pojo.DmpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lixing on 2017/3/31.
 * 日志会通过消息队列来异步处理
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Autowired
    private JavaMailSender javaMailSender;


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static ThreadLocal<String> apiName = new ThreadLocal<>();
    private static ThreadLocal<String> strategyId = new ThreadLocal<>();

    @Autowired
    private UserApiaskNumService userApiaskNumService;


    @Pointcut("execution(public * com.uuzu.chinadep.web..*.*(..))")
    public void webLog() {
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        startTime.set(System.currentTimeMillis());
        Map<String, Object> requestMessage = new LinkedHashMap<>();
        String controllName = joinPoint.getSignature().getDeclaringTypeName() + "." + (joinPoint.getSignature().getName());
        Object[] par = joinPoint.getArgs();
        requestMessage.put("url", request.getRequestURL().toString());
        requestMessage.put("method", request.getMethod());
        requestMessage.put("ipaddress", request.getRemoteAddr());
        requestMessage.put("controller", controllName);
        if (!StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpMacControl.mac")
                && !StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpIdMappingController.idmapping")){
            requestMessage.put("paremeter", par);
        } else if (StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpIdMappingController.idmapping")) {
            Map<String, String[]> paramMap = request.getParameterMap();
            Map<String, String> requestMap = new HashMap<>();
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                requestMap.put(entry.getKey(), entry.getValue()[0]);
            }
            requestMessage.put("paremeter", requestMap);
        }
        apiName.set(controllName);

        if (StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpTagVerifyController.post")
                || StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpController.qrydataV2")
                || StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpController.qrydataV3")
                || StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpController.qrydataV4")
                || StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpRelatedIdForTagController.relatedIdforTag")) {
            DmpRequest dmpRequest = (DmpRequest) par[0];
            if (null != dmpRequest) strategyId.set(dmpRequest.getDataRange());
            //针对不同用户，限制不同调用限制
            if (!ArrayUtils.contains(userApiaskNumService.getWhiteUserIds(), dmpRequest.getUserId())) {
                userApiaskNumService.doApiServiceNum(dmpRequest.getUserId());
            }
        } else if(StringUtils.equals(controllName, "com.uuzu.chinadep.web.DmpIdMappingController.idmapping")) {
            Map<String, String[]> paramMap = request.getParameterMap();
            String userId = paramMap.get("user_id")[0];
            // defined in server-log
            strategyId.set("mob_dmp_idmapping" + userId);
            //针对不同用户，限制不同调用限制
            if (!ArrayUtils.contains(userApiaskNumService.getWhiteUserIds(), userId)) {
                userApiaskNumService.doApiServiceNum(userId);
            }
        }

        log.info(OBJECT_MAPPER.writeValueAsString(requestMessage));

    }


    @Value("${dmp.mail_users}")
    private String mailUser;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        Map<String, Object> responseMessage = new LinkedHashMap<>();
        responseMessage.put("controller", apiName.get());
        responseMessage.put("strategyId", strategyId.get());
        responseMessage.put("responseParemeter", ret);
        responseMessage.put("executionTime", System.currentTimeMillis() - startTime.get());

        //添加接口500邮件报警
//        if((((ResponseEntity) ret).getStatusCode()).value()==500){
//            //异步发送告警
//            threadPoolTaskExecutor.execute(() -> {
//                try {
//                    SimpleMailMessage message = new SimpleMailMessage();
//                    message.setFrom(fromEmail);
//                    message.setTo(mailUser.split(","));
////                    message.setTo(mailUser); //自己给自己发送邮件
//                    message.setSubject("dmp服务接口500异常报警");
//                    message.setText("dmp接口名称："+apiName.get()+"   异常信息"+ret.toString());
//                    javaMailSender.send(message);
//                } catch (Exception e) {
//                    log.error("发送邮件消息失败=exception:{}", e);
//                }
//            });
//        }


        log.info(OBJECT_MAPPER.writeValueAsString(responseMessage));
        startTime.remove();
        apiName.remove();
        strategyId.remove();
    }


}
