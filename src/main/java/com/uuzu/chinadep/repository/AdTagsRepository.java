package com.uuzu.chinadep.repository;

import com.uuzu.common.pojo.AdTags;
import com.uuzu.common.pojo.UserApiaskNum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/6/3.
 */
@Repository
public interface AdTagsRepository extends MongoRepository<AdTags,String> {
}
