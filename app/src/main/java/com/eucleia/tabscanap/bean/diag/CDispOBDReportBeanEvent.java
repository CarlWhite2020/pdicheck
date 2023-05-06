package com.eucleia.tabscanap.bean.diag;


import com.eucleia.tabscanap.bean.diag.child.InspectionObdresultsBean;

public class CDispOBDReportBeanEvent extends BaseBeanEvent {

    private InspectionObdresultsBean inspectionObdresultsBean;

    public InspectionObdresultsBean getInspectionObdresultsBean() {
        return inspectionObdresultsBean;
    }

    public void setInspectionObdresultsBean(InspectionObdresultsBean inspectionObdresultsBean) {
        this.inspectionObdresultsBean = inspectionObdresultsBean;
    }

}
