package com.lingh.information.collect.dao.entity;

import java.io.Serializable;
import java.util.Date;

public class AccountResourceSubscribe implements Serializable {
    private Long accountId;

    private Long resourceId;

    private Date lastSubscribeTime;

    public Date getLastSubscribeTime() {
        return lastSubscribeTime;
    }

    public void setLastSubscribeTime(Date lastSubscribeTime) {
        this.lastSubscribeTime = lastSubscribeTime;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}