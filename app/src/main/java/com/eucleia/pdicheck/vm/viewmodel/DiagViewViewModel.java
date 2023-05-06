package com.eucleia.pdicheck.vm.viewmodel;

import androidx.lifecycle.ViewModel;

import com.eucleia.pdicheck.vm.livedata.DiagViewLiveData;
import com.eucleia.pdicheck.bean.normal.DiagView;

public class DiagViewViewModel extends ViewModel {
    private DiagViewLiveData diagViewLiveData;

    public DiagViewLiveData getDiagLiveData() {
        if (diagViewLiveData == null) {
            diagViewLiveData = new DiagViewLiveData();
            diagViewLiveData.setValue(new DiagView());
        }
        return diagViewLiveData;
    }
}
