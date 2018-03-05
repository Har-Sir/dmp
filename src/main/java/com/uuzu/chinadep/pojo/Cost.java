package com.uuzu.chinadep.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


/**
 * Created by zhoujin on 2017/8/8.
 */

@Data
public class Cost {

    @Id
    @JsonIgnore
    private ObjectId id;
    private String cost_id;//计费ID，唯一的
    private String model_type;//模式
    private String tags;//标签
//    private String normal_tags;//普通标签
//    private String specific_tags;//个性标签
    private Double price;//价格
    private String user_id;//用户id
    private String createAt;

}
