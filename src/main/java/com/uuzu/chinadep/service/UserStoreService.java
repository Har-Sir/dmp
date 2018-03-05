package com.uuzu.chinadep.service;

import com.uuzu.chinadep.mapper.UserStoreMapper;
import com.uuzu.chinadep.pojo.UserStore;
import com.uuzu.chinadep.pojo.UserStoreQueryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author jiangll
 *
 */
@Transactional
@Service
public class UserStoreService {

	@Autowired
	private UserStoreMapper userStoreMapper;

	/**
	 * 添加
	 * @param userStore
	 */
	public boolean add(UserStore userStore) {
		userStore.setUpdatetime(new Date());
		return userStoreMapper.insert(userStore)==0?false:true;
	}

	/**
	 * 修改
	 * @param userStore
	 * @return
	 */
	public boolean update(UserStore userStore) {
		return userStoreMapper.updateById(userStore) == 1 ? true:false;
	}
	/**
	 * 根据用户和店铺检索
	 * @param userid
	 * @param storeid
	 * @return
	 */
	public UserStore selectCountByUserAndStore(String userid, String storeid) {
		return userStoreMapper.selectCountByUserAndStore(userid,storeid);
	}

	/**
	 * 获取去重的用户店铺列表
	 * @return
	 */
	public List<UserStore> selectDedupUser() {
		return userStoreMapper.selectDedupUser();
	}

	/**
	 * 总数
	 * @param queryBean
	 * @return
	 */
    public int total(UserStoreQueryBean queryBean) {
		return userStoreMapper.selectTotalByQueryBean(queryBean);
    }

	public List<UserStore> list(UserStoreQueryBean queryBean) {
		return userStoreMapper.selectListByQueryBean(queryBean);
	}
}
