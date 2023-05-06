package com.eucleia.tabscanap.bean.diag;

import com.eucleia.pdicheck.bean.normal.PdiGroup;

import java.util.Stack;

/**
 * PDI 检测计划
 */
public class CDispPdiCheckBeanEvent extends BaseBeanEvent {

    private String strCaption;
    private int scanState;
    private String strContext;
    private int format;
    private int color;
    private int percent;
    private int allPercent = 1;
    private boolean progressState;
    private int result;
    private int dtcNum;

    private String vin;
    private String model;
    private String brand;
    private String year;

    private volatile boolean pdfSwitch =true;

    private Stack<PdiGroup> groups = new Stack<>();

    public String getStrCaption() {
        return strCaption;
    }

    public void setStrCaption(String strCaption) {
        this.strCaption = strCaption;
    }

    public int getScanState() {
        return scanState;
    }

    public void setScanState(int scanState) {
        this.scanState = scanState;
    }

    public String getStrContext() {
        return strContext;
    }

    public void setStrContext(String strContext) {
        this.strContext = strContext;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getAllPercent() {
        return allPercent;
    }

    public void setAllPercent(int allPercent) {
        this.allPercent = allPercent;
    }

    public boolean isProgressState() {
        return progressState;
    }

    public void setProgressState(boolean progressState) {
        this.progressState = progressState;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Stack<PdiGroup> getGroups() {
        return groups;
    }

    public void setGroups(Stack<PdiGroup> groups) {
        this.groups = groups;
    }

    public PdiGroup getPdiGroup() {
        return groups.peek();
    }

    public void putPdiGroup(PdiGroup group) {
        groups.push(group);
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isPdfSwitch() {
        return pdfSwitch;
    }

    public void setPdfSwitch(boolean pdfSwitch) {
        this.pdfSwitch = pdfSwitch;
    }

    public int getDtcNum() {
        return dtcNum;
    }

    public void setDtcNum(int dtcNum) {
        this.dtcNum = dtcNum;
    }
}
