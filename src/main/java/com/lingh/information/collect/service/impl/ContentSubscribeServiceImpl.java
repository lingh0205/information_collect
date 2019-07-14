package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.Account;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.ContentSubscribe;
import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.dao.mapping.ContentSubscribeMapper;
import com.lingh.information.collect.service.ContentSubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentSubscribeServiceImpl implements ContentSubscribeService {

    @Autowired
    ContentSubscribeMapper contentSubscribeMapper;

    @Override
    public void saveContentSubscribeList(Account account, Resource resource, List<Content> contentList, boolean success) {
        for (Content content : contentList) {
            ContentSubscribe subscribe = new ContentSubscribe();
            subscribe.setAccountId(account.getId());
            subscribe.setContentId(content.getId());
            subscribe.setContentTitle(content.getTitle());
            subscribe.setResourceId(resource.getId());
            subscribe.setRetryTime((byte)0);
            if (success) {
                subscribe.setSendStatus((byte) 1);
            }else {
                subscribe.setSendStatus((byte)0);
            }
            contentSubscribeMapper.insertSelective(subscribe);
        }
    }

    @Override
    public List<ContentSubscribe> queryRetryContents(Long accountId, Long resourceId) {
        return contentSubscribeMapper.queryRetry(accountId, resourceId);
    }

    @Override
    public void updateContentSubscribeList(List<ContentSubscribe> subList, boolean success) {
        subList.forEach(subscribe -> {
            if (success){
                contentSubscribeMapper.updateSuccessStatus(subscribe.getId());
            }else {
                contentSubscribeMapper.updateRetry(subscribe.getId());
            }
        });
    }

}
