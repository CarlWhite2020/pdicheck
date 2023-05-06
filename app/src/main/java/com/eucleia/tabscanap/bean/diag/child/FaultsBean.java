package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;

public class FaultsBean implements Serializable {
    /**
     * fault_code : P0401
     * fault_description : EGR Valve A Flow Insufficient Detected
     * fault_status :
     */

    private String fault_code;
    private String fault_description;
    private String fault_status;

    public String getFault_code() {
        return fault_code;
    }

    public void setFault_code(String fault_code) {
        this.fault_code = fault_code;
    }

    public String getFault_description() {
        return fault_description;
    }

    public void setFault_description(String fault_description) {
        this.fault_description = fault_description.replace("\n","");
    }

    public String getFault_status() {
        return fault_status;
    }

    public void setFault_status(String fault_status) {
        this.fault_status = fault_status;
    }
}