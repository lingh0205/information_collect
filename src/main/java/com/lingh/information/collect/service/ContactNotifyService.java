package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.ContactNotify;

import java.util.List;

public interface ContactNotifyService {

    List<ContactNotify> queryNotifyConfigByAccountId(Long accountId);

}
