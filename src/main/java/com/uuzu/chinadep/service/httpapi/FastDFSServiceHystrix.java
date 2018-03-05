package com.uuzu.chinadep.service.httpapi;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by lixing on 2017/6/20.
 * 断路器
 */
@Component
public class FastDFSServiceHystrix implements FastDFSService {

    @Override
    public String uploadAppend(String user_id, String path, MultipartFile files) {
        return null;
    }

    @Override
    public String upload(String user_id, String upload_dir, MultipartFile files) {
        return null;
    }

    public byte[] download(String user_id, @RequestParam(value = "path") String path,
                           String download_name) {
        return null;
    }
}
