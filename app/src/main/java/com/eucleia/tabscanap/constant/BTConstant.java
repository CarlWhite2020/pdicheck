package com.eucleia.tabscanap.constant;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.eucleia.tabscanap.bean.model.BlueModel;
import com.eucleia.tabscanap.bean.model.BtFilter;

import java.util.ArrayList;
import java.util.List;

public class BTConstant {


    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    public static boolean isJniConnecting = false;

    public static boolean isConnectInsecure = true;

    public static volatile boolean isVciConnect = false;

    public volatile static boolean isShowList = false;

    public static String btAddress;

    public static String btName;

    public static boolean isRegister;

    public static volatile List<BlueModel> devices = new ArrayList<>();

    public static List<BtFilter> filters = new ArrayList<>();

    /**
     * 蓝牙接收数据的缓存区
     */
    public static byte[] btCache = new byte[4 * SizeVar.BYTE_ARR_SIZE];

    /**
     * 蓝牙socket连接对象
     */
    public static BluetoothSocket socket = null;
    /**
     * 远程蓝牙实例对象
     */
    public static BluetoothDevice device = null;

}
