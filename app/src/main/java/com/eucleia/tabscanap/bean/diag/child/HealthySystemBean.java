package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;

public class HealthySystemBean implements Serializable {
    /**
     * system_description : 无故障的系统名称
     */

    private String system_description;

    public String getSystem_description() {
        return system_description;
    }

    public void setSystem_description(String system_description) {
        this.system_description = system_description.replace("\n","");
    }
}