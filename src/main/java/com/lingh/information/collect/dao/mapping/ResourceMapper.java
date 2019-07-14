package com.lingh.information.collect.dao.mapping;

import com.lingh.information.collect.dao.entity.Resource;

import java.util.Date;
import java.util.List;

public interface ResourceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);

    Resource selectByUrl(String url);

    List<Resource> queryActiveResource(Date date);

}