package com.eucleia.pdicheck.bean.normal;

public class PdiItem {

    private String itemName;
    private String itemValue;
    private String itemPartNumber;
    private String itemVersionNumber;
    private String[] itemCurDtc;
    private String[] itemHisDtc;
    private int result;
    private boolean isShowGroup =false;
    private boolean isShowDetail =false;
    private boolean isGroupBold =false;
    private volatile boolean needUpdate = true;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemPartNumber() {
        return itemPartNumber;
    }

    public void setItemPartNumber(String itemPartNumber) {
        this.itemPartNumber = itemPartNumber;
    }

    public String getItemVersionNumber() {
        return itemVersionNumber;
    }

    public void setItemVersionNumber(String itemVersionNumber) {
        this.itemVersionNumber = itemVersionNumber;
    }

    public String[] getItemCurDtc() {
        return itemCurDtc;
    }

    public void setItemCurDtc(String[] itemCurDtc) {
        this.itemCurDtc = itemCurDtc;
    }

    public String[] getItemHisDtc() {
        return itemHisDtc;
    }

    public void setItemHisDtc(String[] itemHisDtc) {
        this.itemHisDtc = itemHisDtc;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean isShowGroup() {
        return isShowGroup;
    }

    public void setShowGroup(boolean showGroup) {
        this.isShowGroup = showGroup;
    }

    public boolean isShowDetail() {
        return isShowDetail;
    }

    public void setShowDetail(boolean showDetail) {
        isShowDetail = showDetail;
    }

    public boolean isGroupBold() {
        return isGroupBold;
    }

    public void setGroupBold(boolean groupBold) {
        isGroupBold = groupBold;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
