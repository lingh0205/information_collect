package com.lingh.information.collect.dao.mapping;

import com.lingh.information.collect.dao.entity.AccountResourceSubscribe;

import java.util.List;

public interface AccountResourceSubscribeMapper {

    int insert(AccountResourceSubscribe record);

    int insertSelective(AccountResourceSubscribe record);

    List<AccountResourceSubscribe> querySubscribeResources(Long accountId);

    void updateByAccountAndResource(AccountResourceSubscribe subscribe);

}