package com.eucleia.tabscanap.bean.diag.child;


/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.model.local.db
 * 文件名:  DTC
 * 作者 :   <sen>
 * 时间 :  下午4:56 2017/3/24.
 * 描述 : 已测车型-故障码 bean对象
 */
public class DTC {

    private Long taskId;
    private String troubleTimeKey; // 时间Key
    private String troubleSystemName;// 系统名称
    private long troubleSystemNo; // 相同系统名称、key序列号如:A 序列号1;A 序列号2;B 序列号1 ...
    private String troubleCode; // 故障码
    private String troubleStatus;// 故障码状态
    private String troubleDesc;// 故障码描述

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTroubleTimeKey() {
        return this.troubleTimeKey;
    }

    public void setTroubleTimeKey(String troubleTimeKey) {
        this.troubleTimeKey = troubleTimeKey;
    }

    public String getTroubleSystemName() {
        return this.troubleSystemName;
    }

    public void setTroubleSystemName(String troubleSystemName) {
        this.troubleSystemName = troubleSystemName;
    }

    public long getTroubleSystemNo() {
        return this.troubleSystemNo;
    }

    public void setTroubleSystemNo(long troubleSystemNo) {
        this.troubleSystemNo = troubleSystemNo;
    }

    public String getTroubleCode() {
        return this.troubleCode;
    }

    public void setTroubleCode(String troubleCode) {
        this.troubleCode = troubleCode;
    }

    public String getTroubleStatus() {
        return this.troubleStatus;
    }

    public void setTroubleStatus(String troubleStatus) {
        this.troubleStatus = troubleStatus;
    }

    public String getTroubleDesc() {
        return this.troubleDesc;
    }

    public void setTroubleDesc(String troubleDesc) {
        this.troubleDesc = troubleDesc;
    }


}
