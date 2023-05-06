package com.eucleia.tabscanap.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.bean.model.BlueModel;
import com.eucleia.tabscanap.util.BTUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import java.nio.charset.StandardCharsets;

/**
 * 蓝牙广播监听
 */
public class BluetoothReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            ELogUtils.v("ACTION_DISCOVERY_STARTED");
            BTConstant.devices.clear();
            BTUtils.get().btActive(BluetoothAdapter.ACTION_DISCOVERY_STARTED, null);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            ELogUtils.v("ACTION_DISCOVERY_FINISHED");
            BTUtils.get().btActive(BluetoothAdapter.ACTION_DISCOVERY_FINISHED, null);
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            BTUtils.get().statusChange(blueState);
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && device.getName() != null) {
                BlueModel model = new BlueModel();
                model.setBtMac(device.getAddress());
                model.setBtName(device.getName());
                if (!BTConstant.devices.contains(model) &&
                        BTUtils.get().isFilterBt(model.getBtName())) {
                    ELogUtils.v("ACTION_FOUND & ADD Name -> " + device.getName()
                            + "  && MacAddress ->" + device.getAddress());
                    BTConstant.devices.add(model);
                    BTUtils.get().btActive(BluetoothDevice.ACTION_FOUND, model);
                }
            }
        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && device.getName() != null) {
                ELogUtils.v("ACTION_BOND_STATE_CHANGED Name -> " + device.getName() + "  && MacAddress -> " + device.getAddress());
            }

        } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null && device.getName() != null) {
                ELogUtils.v("ACTION_PAIRING_REQUEST Name -> " + device.getName() + "  && MacAddress ->" + device.getAddress());
                try {
                    String pwd = "1234";
                    byte[] pinBytes = pwd.getBytes("UTF-8");
                    device.setPin(pinBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
