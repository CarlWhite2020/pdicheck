package com.eucleia.tabscanap.bean.diag.child;

import java.io.Serializable;
import java.util.List;

public class InspectionResultsBean implements Serializable {
    private List<FaultySystemBean> faulty_system;
    private List<HealthySystemBean> healthy_system;

    public List<FaultySystemBean> getFaulty_system() {
        return faulty_system;
    }

    public void setFaulty_system(List<FaultySystemBean> faulty_system) {
        this.faulty_system = faulty_system;
    }

    public List<HealthySystemBean> getHealthy_system() {
        return healthy_system;
    }

    public void setHealthy_system(List<HealthySystemBean> healthy_system) {
        this.healthy_system = healthy_system;
    }


}