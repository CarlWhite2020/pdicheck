package com.eucleia.tabscanap.bean.event;

public class VciStateChange {

    private boolean isShow;

    public VciStateChange(boolean bool) {
        isShow = bool;
    }

    public boolean isShow() {
        return isShow;
    }
}
