package com.eucleia.pdicheck.bean.normal;

public class VciStatus {

    private double voltage;
    private int vciStatus;
    private boolean isStatus;

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public int getVciStatus() {
        return vciStatus;
    }

    public void setVciStatus(int vciStatus) {
        this.vciStatus = vciStatus;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }
}
