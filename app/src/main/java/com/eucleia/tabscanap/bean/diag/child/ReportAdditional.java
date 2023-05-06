package com.eucleia.tabscanap.bean.diag.child;

import android.text.TextUtils;

import java.io.Serializable;

public class ReportAdditional implements Serializable {
    private String plateCode;//车牌
    private String mileage;//里程
    private String displacement;//排量
    private String transmission;//变速箱
    private String brand;//品牌
    private String year;//年份
    private String carinfo;//车辆信息
    private String vin;

    public String getPlateCode() {
        return TextUtils.isEmpty(plateCode)?"":plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public String getMileage() {
        return TextUtils.isEmpty(mileage)?"":mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
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

    public String getCarinfo() {
        return carinfo;
    }

    public void setCarinfo(String carinfo) {
        this.carinfo = carinfo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
