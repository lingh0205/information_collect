package com.lingh.information.collect.engine.task;

import cn.hutool.core.collection.CollectionUtil;
import com.lingh.information.collect.dao.entity.Content;
import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.parser.Parser;
import com.lingh.information.collect.parser.ParserFactory;
import com.lingh.information.collect.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ResourceTask implements Callable<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceTask.class);

    private final Resource resource;

    private UUID traceId;

    private String url;

    private Parser parser;

    private ContentService contentService;

    public ResourceTask(Resource resource) {
        this.resource = resource;
        this.setUrl(resource.getUrl());
        this.setParser(ParserFactory.getParser(resource.getParser()));
    }

    @Override
    public Boolean call() {
        Boolean success = Boolean.FALSE;
        try {
            long startTime = System.currentTimeMillis();
            LOGGER.info("[{}]Start to download content from : {} at {}.", traceId, resource.getUrl(), startTime);
            List<Content> contentList = parser.parser(this.getTraceId(), resource);
            if (CollectionUtil.isEmpty(contentList)){
                LOGGER.info("Task : [{}] found zero content at {}.", traceId, System.currentTimeMillis());
            }
            contentService.saveContentList(contentList);
            success = Boolean.TRUE;
            LOGGER.info("Finished Download content from : {} cost {}.", resource.getUrl(), System.currentTimeMillis() - startTime);
        }catch (IOException e){
            LOGGER.error("Parser url : {} found Exception.", url, e);
            success = Boolean.FALSE;
        }catch (Exception e){
            LOGGER.error("Failed to parser url : {}.", e);
            success = Boolean.FALSE;
        }
        return success;
    }

    public UUID getTraceId() {
        return traceId;
    }

    public void setTraceId(UUID traceId) {
        this.traceId = traceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public Resource getResource() {
        return resource;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public ContentService getContentService() {
        return contentService;
    }
}
