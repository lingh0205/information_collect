package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.ContentSubscribe;
import com.lingh.information.collect.dao.mapping.ContentMapper;
import com.lingh.information.collect.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    ContentMapper contentMapper;

    @Override
    public void saveContentList(List<Content> contentList) {
        contentList.forEach(content -> {
            int count = contentMapper.countByUrl(content.getLink());
            if (count <= 0){
                contentMapper.insertSelective(content);
            }else {
                LOGGER.info("SKIP URL : {}", content.getLink());
            }
        });
    }

    @Override
    public List<Content> queryActiveProperContents(Long resourceId, Date subscribeTime) {
        return contentMapper.queryActiveProperContents(resourceId, subscribeTime);
    }

    @Override
    public List<Content> queryByIdList(List<ContentSubscribe> subList) {
        List<Long> list = subList.stream().map(ContentSubscribe::getContentId).collect(Collectors.toList());
        return contentMapper.queryByIdList(list);
    }
}
