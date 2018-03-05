package com.uuzu.chinadep.service.httpapi;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhoujin on 2017/8/18.
 */
@Component
public class TagsApiServiceHystrix implements TagsService{

    @Override
    public String tagIf(List<String> list, String target) {
        return null;
    }
    @Override
    public String tagIf2(List<String> list, String target) {
        return null;
    }
}
