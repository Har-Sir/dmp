package com.uuzu.chinadep.pojo;

import lombok.Data;

/**
 * Created by zhoujin on 2017/6/22.
 */
@Data
public class RpDeviceAllInfo {
    private String imei_idfa;
    private String ip;
    private String mac;
    private String openudid;
    private String adsid;
    private String androidid;
    private String phone;

    private String profile_score;
    private String behavior_score;
    private String positive_action_score;
    private String active_score;
    private String summary_score;

    private String tags;
    private String tot_install_apps;
    private String model_level;
    private String cell_factory;
    private String income;
    private String kids;
    private String city;
    private String country;
    private String edu;
    private String segment;
    private String agebin;
    private String gender;
    private String province;

    private String model;//设备型号
    private String carrier;//运营商代码
    private String network;//网络
    private String screensize;//分辨率
    private String sysver;//操作系统
    private String city_level;//城市等级
    private String permanent_country;//常驻国家
    private String permanent_province;//常驻省份
    private String permanent_city;//常驻城市
    private String occupation;//职业
    private String house;//房产情况
    private String repayment;//偿还能力
    private String car;//车产情况
    private String workplace;//工作地
    private String residence;//居住地
    private String married;//婚姻状况

    private String installed_cate_tag;//行业分类下app在装个数
    private String finance_time;//金融划窗标签更新时间
    private String finance_action;//金融划窗标签
    
}
