package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.AccountResourceSubscribe;
import java.util.List;

public interface AccountResourceSubscribeService {

    List<AccountResourceSubscribe> querySubscribeResources(Long accountId);

    void updateLastSubscribeTime(AccountResourceSubscribe subscribe);

}
