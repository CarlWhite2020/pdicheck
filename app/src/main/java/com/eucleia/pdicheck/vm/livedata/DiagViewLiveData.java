package com.eucleia.pdicheck.vm.livedata;

import androidx.lifecycle.MutableLiveData;

import com.eucleia.pdicheck.bean.normal.DiagView;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;

public class DiagViewLiveData extends MutableLiveData<DiagView> {

    public void postValue(int objKey, PageType type) {
        DiagView value = getValue();
        value.setObjKey(objKey);
        value.setPageType(type);
        value.setCurrTime(System.currentTimeMillis());
        postValue(value);
    }


}
