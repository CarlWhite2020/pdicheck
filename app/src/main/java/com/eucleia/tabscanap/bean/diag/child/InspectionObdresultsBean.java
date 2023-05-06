/**
 * Copyright 2019 bejson.com
 */
package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-11-20 8:42:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InspectionObdresultsBean implements Serializable {

    private int faults_total = 0;
    private List<Iupr> iupr;
    private List<Livedata> livedata;
    private String mil_on_mileage;
    private String mil_status;
    private String protocol;
    private int qualify = 0;
    private List<Readiness> readiness;
    private List<VehicleSystem> system;
    private String strCaption;


    private int mil_status_key_on_int=1;
    private int mil_status_engine_start_int=0;
    private int mil_status_int=0;


    public int getFaults_total() {
        return faults_total;
    }

    public void setFaults_total(int faults_total) {
        this.faults_total = faults_total;
    }

    public List<Iupr> getIupr() {
        return iupr;
    }

    public void setIupr(List<Iupr> iupr) {
        this.iupr = iupr;
    }

    public List<Livedata> getLivedata() {
        return livedata;
    }

    public void setLivedata(List<Livedata> livedata) {
        this.livedata = livedata;
    }

    public String getMil_on_mileage() {
        return mil_on_mileage;
    }

    public void setMil_on_mileage(String mil_on_mileage) {
        this.mil_on_mileage = mil_on_mileage;
    }

    public String getMil_status() {
        return mil_status;
    }

    public void setMil_status(String mil_status) {
        this.mil_status = mil_status;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getQualify() {
        return qualify;
    }

    public void setQualify(int qualify) {
        this.qualify = qualify;
    }

    public List<Readiness> getReadiness() {
        return readiness;
    }

    public void setReadiness(List<Readiness> readiness) {
        this.readiness = readiness;
    }

    public List<VehicleSystem> getSystem() {
        return system;
    }

    public void setSystem(List<VehicleSystem> system) {
        this.system = system;
    }

    public String getStrCaption() {
        return strCaption;
    }

    public void setStrCaption(String strCaption) {
        this.strCaption = strCaption;
    }

    public int getMil_status_key_on_int() {
        return mil_status_key_on_int;
    }

    public void setMil_status_key_on_int(int mil_status_key_on_int) {
        this.mil_status_key_on_int=mil_status_key_on_int;
    }

    public int getMil_status_engine_start_int() {
        return mil_status_engine_start_int;
    }

    public void setMil_status_engine_start_int(int mil_status_engine_start_int) {
        this.mil_status_engine_start_int=mil_status_engine_start_int;
    }

    public int getMil_status_int() {
        return mil_status_int;
    }

    public void setMil_status_int(int mil_status_int) {
        this.mil_status_int=mil_status_int;
    }
}