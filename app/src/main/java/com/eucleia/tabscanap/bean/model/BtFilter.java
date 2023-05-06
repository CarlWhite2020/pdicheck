package com.eucleia.tabscanap.bean.model;

public class BtFilter {

    private String startFilter;

    private String endFilter;

    public BtFilter() {
    }

    public BtFilter(String startFilter, String endFilter) {
        this.startFilter = startFilter;
        this.endFilter = endFilter;
    }

    public String getStartFilter() {
        return startFilter;
    }

    public void setStartFilter(String startFilter) {
        this.startFilter = startFilter;
    }

    public String getEndFilter() {
        return endFilter;
    }

    public void setEndFilter(String endFilter) {
        this.endFilter = endFilter;
    }
}
