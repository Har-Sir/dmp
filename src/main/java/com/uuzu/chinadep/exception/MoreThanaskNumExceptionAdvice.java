package com.uuzu.chinadep.exception;

import com.uuzu.chinadep.service.codis.CodisService;
import com.uuzu.common.exception.MoreThanaskNumException;
import com.uuzu.common.pojo.DmpResponse;
import com.uuzu.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/3.
 */
@Slf4j
@RestControllerAdvice
public class MoreThanaskNumExceptionAdvice {

    @Autowired
    private JavaMailSender javaMailSender;
    /*@Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;*/
    @Autowired
    private CodisService codisService;

    @Value("${dmp.mail_users}")
    private String mailUser;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = MoreThanaskNumException.class)
    public DmpResponse moreThanaskNumExceptionHandle(MoreThanaskNumException exception){
        String message = exception.getMessage();
        String[] splitMessage = StringUtils.split(message, ":");
        log.error(message);
        String emailLock = this.codisService.get(splitMessage[1] + "lock");
        if(!StringUtils.equals(emailLock,"1")){
            try {
                MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

                StringBuilder builder = new StringBuilder("用户:"+splitMessage[1]+"于");
                builder.append(DateUtil.dateToString(new Date(), DateUtil.DATETIME_PATTERN));
                builder.append("超出openpi调用次数限制");
                mimeMessageHelper.setFrom(fromEmail,"游族MOB事业部");
                mimeMessageHelper.setTo(mailUser.split(","));
                mimeMessageHelper.setSubject("API调用限制邮件通知");
                mimeMessageHelper.setText(builder.toString());
                this.javaMailSender.send(mimeMessage);

                this.codisService.set(splitMessage[1] + "lock","1",nowToZero());
            } catch (Exception e) {
                log.error("提醒邮件发送失败{}",e);
                this.codisService.set(splitMessage[1] + "lock","0",nowToZero());
            }
        }

        DmpResponse dmpResponse = new DmpResponse();

        dmpResponse.setRespState(DmpResponse.RespState.APIASK_NUM);
        return dmpResponse;
    }

    private Integer nowToZero(){
        DateTime zero = new DateTime().millisOfDay().withMaximumValue(); //每天零点
        DateTime now = new DateTime();
        Period period = new Period(now,zero);
        return period.getHours()*3600 + period.getMinutes()*60 + period.getSeconds();
    }

}
