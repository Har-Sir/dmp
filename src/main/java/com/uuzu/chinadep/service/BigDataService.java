package com.uuzu.chinadep.service;

import com.jthink.spring.boot.starter.hbase.api.HbaseTemplate;
import com.uuzu.chinadep.exception.HBaseNoDataException;
import com.uuzu.chinadep.pojo.RpDeviceAllInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhoujin on 2017/6/22.
 */
@Slf4j
@Transactional
@Service
//@CacheConfig(cacheNames = "bigData")
public class BigDataService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    /**
     * 根据表和rowKey获取满足条件的实体
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public RpDeviceAllInfo getBeanByParam(String tableName, String family, String rowKey) throws HBaseNoDataException {
        return this.getByHbase(tableName, family, rowKey);
    }

    /**
     * 从hbase中获取需要的数据
     *
     * @param tableName
     * @param family
     * @param rowKey
     * @return
     */
    public RpDeviceAllInfo getByHbase(String tableName, String family, String rowKey) throws HBaseNoDataException {
        RpDeviceAllInfo rpDeviceAllInfo = hbaseTemplate.get(tableName, rowKey, (result, arg1) -> {
            if (!result.isEmpty()) {
                RpDeviceAllInfo rpDeviceAllInfo1 = new RpDeviceAllInfo();
                rpDeviceAllInfo1.setImei_idfa(Bytes.toString(result.getValue(family.getBytes(), "imei_idfa".getBytes())));
                rpDeviceAllInfo1.setIp(Bytes.toString(result.getValue(family.getBytes(), "ip".getBytes())));
                rpDeviceAllInfo1.setMac(Bytes.toString(result.getValue(family.getBytes(), "mac".getBytes())));
                rpDeviceAllInfo1.setOpenudid(Bytes.toString(result.getValue(family.getBytes(), "openudid".getBytes())));
                rpDeviceAllInfo1.setAdsid(Bytes.toString(result.getValue(family.getBytes(), "adsid".getBytes())));
                rpDeviceAllInfo1.setAndroidid(Bytes.toString(result.getValue(family.getBytes(), "androidid".getBytes())));
                rpDeviceAllInfo1.setPhone(Bytes.toString(result.getValue(family.getBytes(), "phone".getBytes())));
                rpDeviceAllInfo1.setAgebin(Bytes.toString(result.getValue(family.getBytes(), "agebin".getBytes())));
                rpDeviceAllInfo1.setCell_factory(Bytes.toString(result.getValue(family.getBytes(), "cell_factory".getBytes())));
                rpDeviceAllInfo1.setCity(Bytes.toString(result.getValue(family.getBytes(), "city".getBytes())));
                rpDeviceAllInfo1.setCountry(Bytes.toString(result.getValue(family.getBytes(), "country".getBytes())));
                rpDeviceAllInfo1.setEdu(Bytes.toString(result.getValue(family.getBytes(), "edu".getBytes())));
                rpDeviceAllInfo1.setGender(Bytes.toString(result.getValue(family.getBytes(), "gender".getBytes())));
                rpDeviceAllInfo1.setIncome(Bytes.toString(result.getValue(family.getBytes(), "income".getBytes())));
                rpDeviceAllInfo1.setKids(Bytes.toString(result.getValue(family.getBytes(), "kids".getBytes())));
                rpDeviceAllInfo1.setModel_level(Bytes.toString(result.getValue(family.getBytes(), "model_level".getBytes())));
                rpDeviceAllInfo1.setProvince(Bytes.toString(result.getValue(family.getBytes(), "province".getBytes())));
                rpDeviceAllInfo1.setSegment(Bytes.toString(result.getValue(family.getBytes(), "segment".getBytes())));
                rpDeviceAllInfo1.setTot_install_apps(Bytes.toString(result.getValue(family.getBytes(), "tot_install_apps".getBytes())));
                rpDeviceAllInfo1.setTags(Bytes.toString(result.getValue(family.getBytes(), "tags".getBytes())));
                rpDeviceAllInfo1.setProfile_score(Bytes.toString(result.getValue(family.getBytes(), "profile_score".getBytes())));
                rpDeviceAllInfo1.setBehavior_score(Bytes.toString(result.getValue(family.getBytes(), "behavior_score".getBytes())));
                rpDeviceAllInfo1.setPositive_action_score(Bytes.toString(result.getValue(family.getBytes(), "positive_action_score".getBytes())));
                rpDeviceAllInfo1.setActive_score(Bytes.toString(result.getValue(family.getBytes(), "active_score".getBytes())));
                rpDeviceAllInfo1.setSummary_score(Bytes.toString(result.getValue(family.getBytes(), "summary_score".getBytes())));

                rpDeviceAllInfo1.setModel(Bytes.toString(result.getValue(family.getBytes(), "model".getBytes())));
                rpDeviceAllInfo1.setCarrier(Bytes.toString(result.getValue(family.getBytes(), "carrier".getBytes())));
                rpDeviceAllInfo1.setNetwork(Bytes.toString(result.getValue(family.getBytes(), "network".getBytes())));
                rpDeviceAllInfo1.setScreensize(Bytes.toString(result.getValue(family.getBytes(), "screensize".getBytes())));
                rpDeviceAllInfo1.setSysver(Bytes.toString(result.getValue(family.getBytes(), "sysver".getBytes())));
                rpDeviceAllInfo1.setCity_level(Bytes.toString(result.getValue(family.getBytes(), "city_level".getBytes())));
                rpDeviceAllInfo1.setPermanent_country(Bytes.toString(result.getValue(family.getBytes(), "permanent_country".getBytes())));
                rpDeviceAllInfo1.setPermanent_province(Bytes.toString(result.getValue(family.getBytes(), "permanent_province".getBytes())));
                rpDeviceAllInfo1.setPermanent_city(Bytes.toString(result.getValue(family.getBytes(), "permanent_city".getBytes())));
                rpDeviceAllInfo1.setOccupation(Bytes.toString(result.getValue(family.getBytes(), "occupation".getBytes())));
                rpDeviceAllInfo1.setHouse(Bytes.toString(result.getValue(family.getBytes(), "house".getBytes())));
                rpDeviceAllInfo1.setRepayment(Bytes.toString(result.getValue(family.getBytes(), "repayment".getBytes())));
                rpDeviceAllInfo1.setCar(Bytes.toString(result.getValue(family.getBytes(), "car".getBytes())));
                rpDeviceAllInfo1.setWorkplace(Bytes.toString(result.getValue(family.getBytes(), "workplace".getBytes())));
                rpDeviceAllInfo1.setResidence(Bytes.toString(result.getValue(family.getBytes(), "residence".getBytes())));
                rpDeviceAllInfo1.setMarried(Bytes.toString(result.getValue(family.getBytes(), "married".getBytes())));
                rpDeviceAllInfo1.setInstalled_cate_tag(Bytes.toString(result.getValue(family.getBytes(), "installed_cate_tag".getBytes())));
                rpDeviceAllInfo1.setFinance_time(Bytes.toString(result.getValue(family.getBytes(), "finance_time".getBytes())));
                rpDeviceAllInfo1.setFinance_action(Bytes.toString(result.getValue(family.getBytes(), "finance_action".getBytes())));

                return rpDeviceAllInfo1;
            }
            return null;
        });
        return rpDeviceAllInfo;
    }

}
