package com.lingh.information.collect.dao.mapping;

import com.lingh.information.collect.dao.entity.ContactNotify;

import java.util.List;

public interface ContactNotifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContactNotify record);

    int insertSelective(ContactNotify record);

    ContactNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContactNotify record);

    int updateByPrimaryKey(ContactNotify record);

    List<ContactNotify> queryNotifyConfigByAccountId(Long accountId);

}