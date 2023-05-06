package com.eucleia.pdicheck.vm;

import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.bean.normal.DiagView;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.vm.livedata.DiagViewLiveData;
import com.eucleia.pdicheck.vm.livedata.VciStatusLiveData;
import com.eucleia.pdicheck.vm.viewmodel.DiagViewViewModel;
import com.eucleia.pdicheck.vm.viewmodel.VciStatusViewModel;
import com.eucleia.tabscanap.constant.PageType;

/**
 * 全局LiveData.
 */
public final class AppVM {

    /**
     * 电压.
     */
    private VciStatusLiveData vciStatusLiveData;

    /**
     * 诊断.
     */
    private DiagViewLiveData diagViewLiveData;

    /**
     * 唯一入口.
     *
     * @return AppVM
     */
    public static AppVM get() {
        return INNERCLASS.INSTANCE;
    }

    private static class INNERCLASS {
        /**
         * 内部单例.
         */
        private static final AppVM INSTANCE = new AppVM();
    }

    private AppVM() {
        init();
    }

    /**
     * 首次初始化.
     */
    public void init() {

        vciStatusLiveData = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Utils.getApp())
                .create(VciStatusViewModel.class)
                .getVciData();

        diagViewLiveData = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Utils.getApp())
                .create(DiagViewViewModel.class)
                .getDiagLiveData();
    }

    /**
     * 刷新诊断.
     *
     * @param objKey 位置
     * @param type   类型
     */
    public void postDataValue(final int objKey, final PageType type) {
        diagViewLiveData.postValue(objKey, type);
    }

    /**
     * 获取vci状态.
     *
     * @return VciStatus
     */
    public VciStatus getVciStatus() {
        return vciStatusLiveData.getValue();
    }


    /**
     * 获取诊断.
     *
     * @return DiagView
     */
    public DiagView getDiagView() {
        return diagViewLiveData.getValue();
    }

    /**
     * 电压是否和之前一样.
     *
     * @param voltage 电压
     * @return true 相同、false 不相同
     */
    public boolean isVoltageEqual(final double voltage) {
        return vciStatusLiveData.isVoltageEqual(voltage);
    }

    /**
     * 连接状态是否和之前一样.
     *
     * @param status 连接状态
     * @return true 相同、false 不相同
     */
    public boolean isStatusEqual(final double status) {
        return vciStatusLiveData.isStatusEqual(status);
    }

    /**
     * 刷新电压.
     *
     * @param voltage 电压
     */
    public void postVoltage(final double voltage) {
        vciStatusLiveData.postVoltage(voltage);
    }


    /**
     * 刷新连接状态.
     *
     * @param vciStatus 连接状态
     */
    public void postVciStatus(final int vciStatus) {
        vciStatusLiveData.postVciStatus(vciStatus);
    }

    /**
     * 电压.
     *
     * @return 电压Livedata
     */

    public VciStatusLiveData getVciStatusLiveData() {
        return vciStatusLiveData;
    }

    /**
     * 诊断.
     *
     * @return 诊断Livedata
     */
    public DiagViewLiveData getDiagViewLiveData() {
        return diagViewLiveData;
    }
}

