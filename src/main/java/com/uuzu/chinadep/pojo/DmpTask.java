package com.uuzu.chinadep.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;


/**
 * Created by zhoujin on 2017/8/8.
 */
@Data
public class DmpTask {

    @Id
    private String ID;
    private String busiserialno;
    private String inputPath;
    private String output;
    private int state;//0未执行，1成功 2失败
    private String createAt;
    private String updateAt;
    private String cost_id;


}
