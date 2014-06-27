package com.gcj.bean;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-20
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class FunctionDayBean {

    private String userid;

    private Date updateTime;

    private String versionId;

    private String md5;

    private String url;

    private String count;

    public FunctionDayBean(String userid, Date updateTime, String versionId, String md5, String count) {
        this.userid = userid;
        this.updateTime = updateTime;
        this.versionId = versionId;
        this.md5 = md5;
        this.count = count;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
