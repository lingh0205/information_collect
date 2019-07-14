package com.lingh.information.collect.dao.entity;

import java.io.Serializable;
import java.util.Date;

public class ContactNotify implements Serializable {
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Long accountId;

    private String notifyType;

    private String notifyValue;

    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType == null ? null : notifyType.trim();
    }

    public String getNotifyValue() {
        return notifyValue;
    }

    public void setNotifyValue(String notifyValue) {
        this.notifyValue = notifyValue == null ? null : notifyValue.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}