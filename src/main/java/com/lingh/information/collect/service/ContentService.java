package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.ContentSubscribe;

import java.util.Date;
import java.util.List;

public interface ContentService {

    void saveContentList(List<Content> contentList);

    List<Content> queryActiveProperContents(Long resourceId, Date subscribeTime);

    List<Content> queryByIdList(List<ContentSubscribe> subList);

}
