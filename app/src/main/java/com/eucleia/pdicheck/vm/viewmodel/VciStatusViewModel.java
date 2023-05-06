package com.eucleia.pdicheck.vm.viewmodel;


import androidx.lifecycle.ViewModel;

import com.eucleia.pdicheck.vm.livedata.VciStatusLiveData;
import com.eucleia.pdicheck.bean.normal.VciStatus;

/**
 * 诊断布局
 */
public class VciStatusViewModel extends ViewModel {

    private VciStatusLiveData statusLiveData;

    public VciStatusLiveData getVciData() {
        if (null == statusLiveData) {
            statusLiveData = new VciStatusLiveData();
            statusLiveData.initVciStatus(0, 0);
        }
        return statusLiveData;
    }

    public VciStatus getVciStatus() {
        return getVciData().getValue();
    }

    public void setVciStatus(int vciStatus) {
        getVciData().setVciStatus(vciStatus);
    }

    public void setVoltage(double voltage) {
        getVciData().setVoltage(voltage);
    }
}
