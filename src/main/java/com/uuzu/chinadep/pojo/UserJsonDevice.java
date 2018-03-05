package com.uuzu.chinadep.pojo;


import lombok.Data;


/**
 * Created by jiangll on 2017/5/24.
 */
@Data
public class UserJsonDevice {
    private String userid ;
    private String storeid ;
    private String data_type;
    private String encrypt_type;
    private String device;
    private String clienttime;
    private String updatetime;
    private String processtime;


}
