package com.uuzu.chinadep.mapper;

import com.uuzu.chinadep.pojo.UserStore;
import com.uuzu.chinadep.pojo.UserStoreQueryBean;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserStoreMapper extends Mapper<UserStore> {

    UserStore selectCountByUserAndStore(@Param("userid") String userid, @Param("storeid") String storeid);

    int updateById(UserStore userStore);

    List<UserStore> selectDedupUser();

    int selectTotalByQueryBean(UserStoreQueryBean queryBean);

    List<UserStore> selectListByQueryBean(UserStoreQueryBean queryBean);
}