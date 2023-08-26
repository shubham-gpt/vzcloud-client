package com.vzcloud.entity;

import java.util.Date;

/**
 * @version V2.3
 * @ClassName:SystemInfo.java
 * @author: http://www.wgstart.com
 * @date: November 16, 2019
 * @Description: View system information
 * @Copyright: 2017-2022 www.wgstart.com. All rights reserved.
 */
public class SystemInfo extends BaseEntity {


    /**
     *
     */
    private static final long serialVersionUID = 879979812204191283L;


    /**
     * host name
     */
    private String hostname;

    /**
     * System version information
     */
    private String version;

    /**
     * System version detailed information
     */
    private String versionDetail;

    /**
     * Memory usage rate
     */
    private Double memPer;

    /**
     * Core(Nuclear number)
     */
    private String cpuCoreNum;

    /**
     * CPU usage rate
     */
    private Double cpuPer;

    /**
     * CPU model information
     */
    private String cpuXh;


    /**
     * Host status, 1 normal, 2 offline
     */
    private String state;


    /**
     * Creation time
     */
    private Date createTime;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDetail() {
        return versionDetail;
    }

    public void setVersionDetail(String versionDetail) {
        this.versionDetail = versionDetail;
    }


    public String getCpuCoreNum() {
        return cpuCoreNum;
    }

    public void setCpuCoreNum(String cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCpuXh() {
        return cpuXh;
    }

    public void setCpuXh(String cpuXh) {
        this.cpuXh = cpuXh;
    }

    public Double getMemPer() {
        return memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
    }

    public Double getCpuPer() {
        return cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}