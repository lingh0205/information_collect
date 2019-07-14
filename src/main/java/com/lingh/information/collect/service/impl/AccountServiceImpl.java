package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.Account;
import com.lingh.information.collect.dao.mapping.AccountMapper;
import com.lingh.information.collect.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    AccountMapper accountMapper;

    @Override
    public List<Account> queryActiveAccount() {
        return accountMapper.queryActiveAccount();
    }
}
