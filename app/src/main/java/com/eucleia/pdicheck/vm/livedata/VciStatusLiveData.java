package com.eucleia.pdicheck.vm.livedata;

import androidx.lifecycle.MutableLiveData;

import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.tabscanap.constant.BTConstant;

public class VciStatusLiveData extends MutableLiveData<VciStatus> {

    public void initVciStatus(int vciStatus, double voltage) {
        VciStatus value = new VciStatus();
        value.setVciStatus(vciStatus);
        value.setVoltage(voltage);
        setValue(value);
    }

    public boolean isVoltageEqual(double voltage) {
        VciStatus value = getValue();
        if (value != null) {
            return value.getVoltage() == voltage;
        }
        return false;
    }

    public boolean isStatusEqual(double status) {
        VciStatus value = getValue();
        if (value != null) {
            return value.getVciStatus() == status;
        }
        return false;
    }

    public void setVoltage(double voltage) {
        getValue().setVoltage(voltage);
    }


    public void setVciStatus(int vciStatus) {
        getValue().setVciStatus(vciStatus);
    }

    public void postVoltage(double voltage) {
        VciStatus value = getValue();
        value.setVoltage(voltage);
        value.setStatus(false);
        postValue(value);
    }


    public void postVciStatus(int vciStatus) {
        VciStatus value = getValue();
        value.setVciStatus(vciStatus);
        value.setStatus(true);
        postValue(value);
    }

    @Override
    public void postValue(VciStatus value) {
        super.postValue(value);
        BTConstant.isVciConnect = value.getVciStatus() == 1;
    }
}
