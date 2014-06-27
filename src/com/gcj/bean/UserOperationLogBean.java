package com.gcj.bean;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-20
 * Time: 下午1:35
 * To change this template use File | Settings | File Templates.
 */
public class UserOperationLogBean {

    private Date updateTime;

    private String userId;

    private String p;

    public UserOperationLogBean(Date updateTime, String userId, String p) {
        this.updateTime = updateTime;
        this.userId = userId;
        this.p = p;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
