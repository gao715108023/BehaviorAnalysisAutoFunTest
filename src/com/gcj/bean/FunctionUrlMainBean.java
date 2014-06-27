package com.gcj.bean;

/**
 * Created by gaochuanjun on 14-2-17.
 */
public class FunctionUrlMainBean {

    private String funType;
    private int clientType;
    private String baseUrl;
    private String parserName;
    private String params;
    private String paramsGet;

    public String getFunType() {
        return funType;
    }

    public void setFunType(String funType) {
        this.funType = funType;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getParserName() {
        return parserName;
    }

    public void setParserName(String parserName) {
        this.parserName = parserName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParamsGet() {
        return paramsGet;
    }

    public void setParamsGet(String paramsGet) {
        this.paramsGet = paramsGet;
    }
}
