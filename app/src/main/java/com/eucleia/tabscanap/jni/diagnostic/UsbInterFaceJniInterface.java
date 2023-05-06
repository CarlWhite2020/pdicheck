package com.eucleia.tabscanap.jni.diagnostic;

import android.annotation.SuppressLint;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.JNIUtils;


/**
 * 包  名: com.eucleia.tabscanap.jni.diagnostic
 * 时  间: 2017-08-15 12:13.
 * 作  者: cuifu
 * 描  述: TODO
 */

public class UsbInterFaceJniInterface {


    static byte[] dd = null;
    static int idd = 0;

    @SuppressLint("DefaultLocale")
    public static String bytes2HexString(byte[] b) {
        StringBuilder ret = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex.toUpperCase());
        }
        return ret.toString();
    }


    /**
     * status 1为连接上VCI，0为VCI断开
     */
    public static void VciStatusInform(byte status) {
        if (AppVM.get().isStatusEqual(status)) {
            return;
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), " 状态 -> " + status);
        if (status == 1) {
            EToastUtils.get().showShort(R.string.blue_connect_success, true);
            JNIUtils.saveDeviceConfig();
        } else {
            EDiagUtils.get().setNeedShowTakeDialog(false);
        }
        AppVM.get().postVciStatus(status);
    }


    /**
     * 电压
     *
     * @param voltage
     */
    public static void VciVoltageInform(int voltage) {
        double vol = ((double) (voltage / 100)) / 10.0;
        if (AppVM.get().isVoltageEqual(vol)) {
            return;
        }
        //ELogUtils.e(EDiagUtils.get().isOpenLogCat()," 电压 -> " + vol);
        AppVM.get().postVoltage(vol);
    }

    /**
     * ：1. 获取汽车型号
     *
     * @return 字符串
     */
    public static String GetVehicleModel() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().vehModel);
        return EDiagUtils.get().getModel().vehModel;
    }

    /**
     * 获取VIN码的接口
     *
     * @return 返回VIN码字符串
     */
    public static String GetVehicleCode() {
        String vin = EDiagUtils.get().getModel().vin;
        if (vin != null) {
            vin = vin.toUpperCase();
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), vin);
        return vin;
    }

    /**
     * 返回当前车型信息
     *
     * @return
     */
    public static String GetVehicleInfo() {
        StringBuilder strInfor = new StringBuilder();
        strInfor.append(CharVar.VERSION);
        strInfor.append("0.00");
        strInfor.append(EDiagUtils.get().getJniModel().vehicleInfo);
        strInfor.append(EDiagUtils.get().getJniModel().systemName);
        return strInfor.toString();
    }

    /**
     * 返回VIN码
     *
     * @return
     */
    public static String GetVinCode() {
        return EDiagUtils.get().getModel().vin;
    }


}
