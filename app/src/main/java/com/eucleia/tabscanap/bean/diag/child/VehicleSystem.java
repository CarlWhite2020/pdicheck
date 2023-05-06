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
public class VehicleSystem implements Serializable {

    public static final int HISTORY_TYPE = 0;
    public static final int CURRENT_TYPE = 1;

    private String diagnostic_path;
    private List<FaultsBean> faults_current;
    private List<FaultsBean> faults_historyBean;
    private List<Information> information;
    private String system_description;

    public void setDiagnostic_path(String diagnostic_path) {
        this.diagnostic_path = diagnostic_path;
    }

    public String getDiagnostic_path() {
        return diagnostic_path;
    }

    public void setFaults_current(List<FaultsBean> faults_current) {
        this.faults_current = faults_current;
    }

    public List<FaultsBean> getFaults_current() {
        return faults_current;
    }

    public void setFaults_historyBean(List<FaultsBean> faults_historyBean) {
        this.faults_historyBean = faults_historyBean;
    }

    public List<FaultsBean> getFaults_historyBean() {
        return faults_historyBean;
    }

    public void setInformation(List<Information> information) {
        this.information = information;
    }

    public List<Information> getInformation() {
        return information;
    }

    public void setSystem_description(String system_description) {
        this.system_description = system_description;
    }

    public String getSystem_description() {
        return system_description;
    }

}