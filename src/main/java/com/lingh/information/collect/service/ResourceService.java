package com.lingh.information.collect.service;

import com.lingh.information.collect.dao.entity.Resource;

import java.util.List;

public interface ResourceService {

    Resource selectByUrl(String url);

    List<Resource> queryActiveResource();

    void updateNextExecTime(Resource resource);

    Resource queryById(Long resourceId);

}
