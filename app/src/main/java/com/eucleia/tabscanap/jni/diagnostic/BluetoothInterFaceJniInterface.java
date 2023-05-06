package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.util.BTUtils;

/**
 * JNI 蓝牙操作回调
 */
public class BluetoothInterFaceJniInterface {

    /**
     * 打开蓝牙
     *
     * @return 成功返回true, 失败false
     */
    public static boolean OpenDevice() {
        return BTUtils.get().connect();
    }


    /**
     * 关闭蓝牙
     *
     * @return 成功返回true, 失败false
     */
    public static boolean CloseDevice() {
        if (BTConstant.isJniConnecting) {
            return false;
        }
        boolean bool = false;
        if (BTConstant.isVciConnect) {
            bool = BTUtils.get().disConnect();
        }
        return bool;
    }

    /**
     * 发送数据
     *
     * @return 失败返回-1，否则都是成功
     */
    public static int SendToBlueTooth(byte[] group) {
        if (BTConstant.isJniConnecting) {
            return -1;
        }
        return BTUtils.get().setData(group);
    }

    /**
     * 接收数据，总接到多少，全部返回多少
     *
     * @return
     */
    public static byte[] ReceiveFromBlueTooth() {
        if (BTConstant.isJniConnecting) {
            return null;
        }
        return BTUtils.get().getData();
    }

}
