package cn.bluetooth;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

/**
 * 清空已配对设备时的过滤器.
 * date: 2019/8/3 21:11
 * author: zengfansheng
 */
public interface RemoveBondFilter {

    /**
     * 显示过滤后的设备.
     *
     * @param device 设备
     * @return 是否过滤
     */
    boolean accept(@NonNull BluetoothDevice device);
}
