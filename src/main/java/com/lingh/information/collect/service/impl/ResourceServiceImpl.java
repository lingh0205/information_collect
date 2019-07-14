package com.lingh.information.collect.service.impl;

import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.dao.mapping.ResourceMapper;
import com.lingh.information.collect.service.ResourceService;
import com.lingh.information.collect.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    ResourceMapper resourceMapper;

    @Override
    public Resource selectByUrl(String url) {
        return resourceMapper.selectByUrl(url);
    }

    @Override
    public List<Resource> queryActiveResource() {
        Date date = new Date();
        return resourceMapper.queryActiveResource(date);
    }

    @Override
    public void updateNextExecTime(Resource resource) {
        Date date = new Date();
        Long period = resource.getPeriod() * 1000;
        Date after = DateUtil.after(date, period);
        resource.setLastExecuteTime(after);
        LOGGER.info("Update Resource {} Last Execute Time to {}.", resource.getUrl(), after);
        resourceMapper.updateByPrimaryKey(resource);
    }

    @Override
    public Resource queryById(Long resourceId) {
        return resourceMapper.selectByPrimaryKey(resourceId);
    }
}
