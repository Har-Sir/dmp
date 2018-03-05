package com.uuzu.chinadep.service.httpapi;

import com.uuzu.chinadep.config.FeignMultipartSupportConfig;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhoujin on 2017/8/9.
 */
@FeignClient(name = "MOBTASK",configuration = FastDFSService.FeignMultipartSupportConfig.class ,fallback = FastDFSServiceHystrix.class)
@Component
public interface FastDFSService{


    @RequestMapping(method = RequestMethod.POST, path = "/mobeye/upload-append", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadAppend(@RequestParam(value = "user_id") String user_id,
                        @RequestParam(value = "path") String path,
                        @RequestPart(value = "files") MultipartFile files);

    @RequestMapping(method = RequestMethod.POST, path = "/mobeye/upload2",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestParam(value = "user_id") String user_id,
                  @RequestParam(value = "upload_dir") String upload_dir,
                  @RequestPart(value = "files") MultipartFile files);

    @RequestMapping(method = RequestMethod.POST,path = "/mobeye/download")
    public byte[] download(@RequestParam(value = "user_id") String user_id,
                           @RequestParam(value = "path") String path,
                           @RequestPart(value = "download_name") String download_name);


    public class FeignMultipartSupportConfig {

        @Bean
        @Primary
        @Scope("prototype")
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder();
        }

        @Bean
        public feign.Logger.Level multipartLoggerLevel() {
            return feign.Logger.Level.FULL;
        }
    }


}
