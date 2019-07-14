package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.ContactNotify;
import com.lingh.information.collect.dao.mapping.ContactNotifyMapper;
import com.lingh.information.collect.service.ContactNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactNotifyServiceImpl implements ContactNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactNotifyServiceImpl.class);

    @Autowired
    ContactNotifyMapper contactNotifyMapper;

    @Override
    public List<ContactNotify> queryNotifyConfigByAccountId(Long accountId) {
        return contactNotifyMapper.queryNotifyConfigByAccountId(accountId);
    }

}
