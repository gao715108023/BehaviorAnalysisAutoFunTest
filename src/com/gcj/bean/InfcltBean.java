package com.gcj.bean;

import java.util.Date;

public class InfcltBean {

    private Date updateTime;

    private int clientType;

    private String url;

    public InfcltBean(Date updateTime, int clientType, String url) {
        this.updateTime = updateTime;
        this.clientType = clientType;
        this.url = url;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
