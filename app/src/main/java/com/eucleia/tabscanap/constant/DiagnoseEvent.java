package com.eucleia.tabscanap.constant;


import com.eucleia.tabscanap.bean.diag.BaseBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispDataStreamBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispFrameBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispInputBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispListBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispMenuBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispMsgBoxBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispOBDReportBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispReportBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispSelectBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispTroubleBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispVehicleBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispVersionBeanEvent;

public class DiagnoseEvent {

    public static int Type = 0;
    public static int iObjectKey = 0;

    public BaseBeanEvent getEvent() {
        switch (Type) {
            case 0:
                break;
        }
        return null;
    }

    public static CDispMsgBoxBeanEvent mCDispMsgBoxBeanEvent;
    public static CDispInputBeanEvent mCDispInputBeanEvent;
    public static CDispSelectBeanEvent mCDispSelectBeanEvent;
    public static CDispMenuBeanEvent mCDispMenuBeanEvent;
    public static CDispVehicleBeanEvent mCDispVehicleBeanEvent;
    public static CDispFrameBeanEvent mCDispFrameBeanEvent;
    public static CDispVersionBeanEvent mCDispVersionBeanEvent;
    public static CDispTroubleBeanEvent mCDispTroubleBeanEvent;
    public static CDispReportBeanEvent mCDispReportBeanEvent;
    public static CDispListBeanEvent mCDispListBeanEvent;
    public static CDispDataStreamBeanEvent mCDispDataStreamBeanEvent;
    public static CDispOBDReportBeanEvent mCDispOBDReportBeanEvent;

    public static boolean isAllEmpty() {

        if (mCDispMsgBoxBeanEvent == null && mCDispInputBeanEvent == null &&
                mCDispSelectBeanEvent == null && mCDispMenuBeanEvent == null &&
                mCDispVehicleBeanEvent == null && mCDispFrameBeanEvent == null &&
                mCDispVersionBeanEvent == null && mCDispTroubleBeanEvent == null &&
                mCDispReportBeanEvent == null && mCDispListBeanEvent == null &&
                mCDispDataStreamBeanEvent == null && mCDispOBDReportBeanEvent == null) {
            return true;
        } else {
            return false;
        }
    }
}
