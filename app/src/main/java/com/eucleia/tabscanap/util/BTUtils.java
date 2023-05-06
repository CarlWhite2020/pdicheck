package com.eucleia.tabscanap.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.listener.BluetoothActiveListener;
import com.eucleia.tabscanap.listener.BluetoothStatusListener;
import com.eucleia.tabscanap.bean.model.BlueModel;
import com.eucleia.tabscanap.bean.model.BtFilter;
import com.eucleia.tabscanap.receiver.BluetoothReceiver;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * 蓝牙工具类.
 */
public final class BTUtils {

    private BTUtils() {
    }

    private static class InClass {
        /**
         * 单例.
         */
        private static final BTUtils INSTANCE = new BTUtils();
    }

    /**
     * 外部获取蓝牙的入口.
     *
     * @return 蓝牙工具类
     */
    public static BTUtils get() {
        return InClass.INSTANCE;
    }

    /**
     * 蓝牙广播.
     */
    private final BluetoothReceiver searchDevices = new BluetoothReceiver();

    /**
     * 蓝牙状态监听回调集合.
     */
    private final ArrayList<BluetoothStatusListener> statusListeners = new ArrayList<>();

    /**
     * 蓝牙动作监听回调集合.
     */
    private final ArrayList<BluetoothActiveListener> activeListeners = new ArrayList<>();

    /**
     * 添加蓝牙状态监听回调.
     *
     * @param listener 蓝牙状态监听回调
     */
    public void addBluetoothStatusListener(
            final BluetoothStatusListener listener) {
        if (!statusListeners.contains(listener)) {
            statusListeners.add(listener);
        }
    }

    /**
     * 移除蓝牙状态监听回调.
     *
     * @param listener 蓝牙状态监听回调
     */
    public void removeBluetoothStatusListener(
            final BluetoothStatusListener listener) {
        statusListeners.remove(listener);
    }

    /**
     * 添加蓝牙动作监听回调.
     *
     * @param listener 蓝牙动作监听回调
     */
    public void addBluetoothActiveListener(
            final BluetoothActiveListener listener) {
        if (!activeListeners.contains(listener)) {
            activeListeners.add(listener);
        }
    }

    /**
     * 移除蓝牙动作监听回调.
     *
     * @param listener 蓝牙动作监听回调
     */
    public void removeBluetoothActiveListener(
            final BluetoothActiveListener listener) {
        activeListeners.remove(listener);
    }

    public void setConnectInsecure(final boolean isInsecure) {
        BTConstant.isConnectInsecure = isInsecure;
    }

    /**
     * 注册蓝牙广播.
     */
    public void register() {
        if (!BTConstant.isRegister) {
            // 注册Receiver来获取蓝牙设备相关的结果
            IntentFilter intent = new IntentFilter();
            //搜索发现设备
            intent.addAction(BluetoothDevice.ACTION_FOUND);
            //状态改变
            intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            //动作状态发生了变化
            intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            //开始搜索
            intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            //结束搜索
            intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
            }
            Utils.getApp().registerReceiver(searchDevices, intent);
            BTConstant.isRegister = true;
        }
    }

    /**
     * 打开蓝牙扫描.
     */
    public void startSearch() {
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    private boolean isSocketConnect() {
        return BTConstant.socket != null
                && BTConstant.socket.isConnected();
    }

    /**
     * 进行蓝牙连接.
     *
     * @return
     */
    public boolean connect() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            return false;
        }
        if (BTConstant.isJniConnecting) {
            return false;
        }
        if (isSocketConnect()) {
            return true;
        }
        if (TextUtils.isEmpty(BTConstant.btAddress)) {
            return false;
        }
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        boolean hasBond = false;

        if (bondedDevices != null) {
            for (BluetoothDevice bt : bondedDevices) {
                if (bt.getAddress().equalsIgnoreCase(BTConstant.btAddress)) {
                    hasBond = true;
                }
            }
        }
        if (!hasBond) {
            createBond();
            SystemClock.sleep(3000);
            return false;
        }


        if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        }
        try {
            BTConstant.device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(BTConstant.btAddress);
            BTConstant.isJniConnecting = true;
            if (BTConstant.device.getName() != null && BTConstant.isConnectInsecure) {
                BTConstant.socket = BTConstant.device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(BTConstant.UUID));
            } else {
                BTConstant.socket = BTConstant.device.createRfcommSocketToServiceRecord(UUID.fromString(BTConstant.UUID));
            }
            BTConstant.socket.connect();
            SystemClock.sleep(100);
        } catch (IOException e) {
            e.printStackTrace();
            disConnect();
        }
        BTConstant.isJniConnecting = false;
        return isSocketConnect();
    }

    public void createBond() {
        BTConstant.device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(BTConstant.btAddress);
        BTConstant.device.createBond();
    }

    /***
     * 关闭蓝牙连接，停止接收线程.
     */
    public boolean disConnect() {
        try {
            if (isSocketConnect()) {
                if (BTConstant.socket != null) {
                    CloseUtils.closeIO(BTConstant.socket.getInputStream(), BTConstant.socket.getOutputStream());
                }
                CloseUtils.closeIO(BTConstant.socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BTConstant.device = null;
            BTConstant.socket = null;
        }
        return true;
    }

    /**
     * 设置蓝牙拦截.
     *
     * @param filters 拦截器
     */
    public void setFilter(BtFilter... filters) {
        BTConstant.filters = Arrays.asList(filters);
    }

    /**
     * 判断当前蓝牙名称是否拦截显示.
     *
     * @param name 名称
     * @return true 显示 / false 不显示
     */
    public boolean isFilterBt(final String name) {
        boolean isFilter = false;
        if (!TextUtils.isEmpty(name) && name.length() == 16) {
            for (BtFilter btFilter : BTConstant.filters) {
                if (name.startsWith(btFilter.getStartFilter())) {
                    if (!TextUtils.isEmpty(btFilter.getEndFilter())) {
                        String customStr = name.substring(8, 11);
                        if (customStr.equals(btFilter.getEndFilter())) {
                            isFilter = true;
                            break;
                        }
                    } else {
                        isFilter = true;
                        break;
                    }
                }
            }
        }
        return isFilter;
    }


    /**
     * 发送so传来的数据.
     *
     * @param msg 数据
     * @return int
     */
    public int setData(final byte[] msg) {
        int a = 0;
        try {
            if (BTConstant.socket != null) {
                if (BTConstant.socket.isConnected()) {
                    BTConstant.socket.getOutputStream().write(msg);
                    a = 1;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            BTUtils.get().disConnect();
            a = -1;
        }
        return a;
    }

    /**
     * 返回接收的数据
     *
     * @return byte[]
     */
    public byte[] getData() {
        if (BTConstant.isJniConnecting) {
            //正在连接
            return null;
        }
        if (BTConstant.socket != null && BTConstant.socket.isConnected()) {
            //准备接收数据
            try {
                if (BTConstant.socket.getInputStream().available() > 0) {
                    int origin_posi = BTConstant.socket.getInputStream().read(BTConstant.btCache);
                    if (origin_posi > 0) {
                        byte[] byteNum = new byte[origin_posi];
                        System.arraycopy(BTConstant.btCache, 0, byteNum, 0, origin_posi);
                        return byteNum;
                    }
                } else {
                    return new byte[0];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void statusChange(int blueState) {
        ArrayList<BluetoothStatusListener> listeners = (ArrayList<BluetoothStatusListener>) statusListeners.clone();
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            listeners.get(i).btStatus(blueState);
        }
    }


    public void btActive(String blueState, BlueModel blueModel) {
        ArrayList<BluetoothActiveListener> listeners = (ArrayList<BluetoothActiveListener>) activeListeners.clone();
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            listeners.get(i).btActive(blueState, blueModel);
        }
    }

    public boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public boolean cancelBondProcess(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{str.getBytes()});
            Log.e("returnValue", "" + returnValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        // cancelBondProcess()
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    public boolean setPairingConfirmation(Class<?> btClass, BluetoothDevice device, boolean isConfirm) throws Exception {
        Method setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", boolean.class);
        Boolean returnValue = (Boolean) setPairingConfirmation.invoke(device, isConfirm);
        return returnValue;
    }

}
