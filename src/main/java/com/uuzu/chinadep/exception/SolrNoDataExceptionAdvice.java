package com.uuzu.chinadep.exception;

import com.uuzu.common.pojo.DmpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by zhoujin on 2017/7/5.
 * <p>
 * Solr没有查询到数据
 */
@Slf4j
@RestControllerAdvice
public class SolrNoDataExceptionAdvice {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SolrNoDataException.class)
    public DmpResponse SolrNoDataExceptionHandle(SolrNoDataException exception) {
        log.info("solr没有查询到数据", exception.getMessage());
        String message = exception.getMessage();

        return new DmpResponse().failure(DmpResponse.RespState.ERROR_RESPONSE_NULL.desc,
                DmpResponse.RespState.ERROR_RESPONSE_NULL.code, message);
    }


}
