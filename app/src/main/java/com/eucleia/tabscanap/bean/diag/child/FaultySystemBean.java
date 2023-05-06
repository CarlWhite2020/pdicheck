package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;
import java.util.List;

public class FaultySystemBean implements Serializable {
    /**
     * faults : [{"fault_code":"P0401","fault_description":"EGR Valve A Flow Insufficient Detected","fault_status":""},{"fault_code":"P1291","fault_description":"Injector High Side Short To GND Or VBATT (Bank1)","fault_status":""}]
     * system_description : ECM(Engine Control Module)
     */

    private String system_description;
    private List<FaultsBean> faults;

    public String getSystem_description() {
        return system_description;
    }

    public void setSystem_description(String system_description) {
        this.system_description = system_description;
    }

    public List<FaultsBean> getFaults() {
        return faults;
    }

    public void setFaults(List<FaultsBean> faults) {
        this.faults = faults;
    }


}
