package com.vzcloud.entity;

import java.util.Date;

/**
 * @version V2.3
 * @ClassName:LogInfo.java
 * @author: wgcloud
 * @date: November 16, 2019
 * @Description: Log information
 * @Copyright: 2017-2022 www.wgstart.com. All rights reserved.
 */
public class LogInfo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1565538727002722890L;

    /**
     * host name
     */
    private String hostname;

    /**
     * describe
     */
    private String infoContent;

    /**
     * 0 success, 1 failure
     */
    private String state;


    /**
     * Creation time
     */
    private Date createTime;


    public String getHostname() {
        return hostname;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    public String getInfoContent() {
        return infoContent;
    }


    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}