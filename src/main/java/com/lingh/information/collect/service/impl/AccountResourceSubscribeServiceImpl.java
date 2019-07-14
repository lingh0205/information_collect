package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.AccountResourceSubscribe;
import com.lingh.information.collect.dao.mapping.AccountResourceSubscribeMapper;
import com.lingh.information.collect.service.AccountResourceSubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountResourceSubscribeServiceImpl implements AccountResourceSubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    AccountResourceSubscribeMapper accountResourceSubscribeMapper;

    @Override
    public List<AccountResourceSubscribe> querySubscribeResources(Long accountId) {
        return accountResourceSubscribeMapper.querySubscribeResources(accountId);
    }

    @Override
    public void updateLastSubscribeTime(AccountResourceSubscribe subscribe) {
        accountResourceSubscribeMapper.updateByAccountAndResource(subscribe);
    }
}
