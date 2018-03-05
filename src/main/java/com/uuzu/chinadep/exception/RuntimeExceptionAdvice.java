package com.uuzu.chinadep.exception;

import com.uuzu.common.pojo.DmpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by lixing on 2017/6/5.
 */
@Slf4j
@RestControllerAdvice
public class RuntimeExceptionAdvice {


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = RuntimeException.class)
    public DmpResponse moreThanaskNumExceptionHandle(RuntimeException exception){
        DmpResponse dmpResponse = new DmpResponse();
        dmpResponse.setRespState(DmpResponse.RespState.APIASK_NUM,exception.getMessage());
        return dmpResponse;
    }

}
