package com.eucleia.tabscanap.bean.event;

/**
 * 包  名: com.eucleia.tabscanap.bean.bean
 * 时  间: 2017-08-15 12:07.
 * 作  者: cuifu
 * 描  述: EventBus 通信实体类
 */

public class EventBean {

    public int mWhatFlag;
    public Object mObject;

    public EventBean(int whatFlag, Object object) {
        mWhatFlag = whatFlag;
        mObject = object;
    }
}
