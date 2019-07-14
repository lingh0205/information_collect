package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.Account;

import java.util.List;

public interface AccountService {

    List<Account> queryActiveAccount();

}
