package com.lingh.information.collect.dao.mapping;

import com.lingh.information.collect.dao.entity.ContentSubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentSubscribeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContentSubscribe record);

    int insertSelective(ContentSubscribe record);

    ContentSubscribe selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContentSubscribe record);

    int updateByPrimaryKey(ContentSubscribe record);

    List<ContentSubscribe> queryRetry(@Param("accountId") Long accountId, @Param("resourceId")Long resourceId);

    void updateSuccessStatus(Long id);

    void updateRetry(Long id);

}