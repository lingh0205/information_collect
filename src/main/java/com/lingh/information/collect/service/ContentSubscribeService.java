package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.Account;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.ContentSubscribe;
import com.lingh.information.collect.dao.entity.Resource;

import java.util.List;

public interface ContentSubscribeService {

    void saveContentSubscribeList(Account account, Resource resource, List<Content> contentList, boolean success);

    List<ContentSubscribe> queryRetryContents(Long accountId, Long resourceId);

    void updateContentSubscribeList(List<ContentSubscribe> subList, boolean success);

}
