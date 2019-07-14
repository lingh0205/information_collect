package com.lingh.information.collect.dao.mapping;

import com.lingh.information.collect.dao.entity.Content;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKeyWithBLOBs(Content record);

    int updateByPrimaryKey(Content record);

    int countByUrl(String link);

    List<Content> queryActiveProperContents(@Param("resourceId") Long resourceId, @Param("subscribeTime") Date subscribeTime);

    List<Content> queryByIdList(@Param("items") List<Long> subList);

}