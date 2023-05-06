package com.eucleia.tabscanap.util;

import android.text.TextUtils;

import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.jni.diagnostic.so.Communication;
import com.eucleia.tabscanap.jni.diagnostic.so.StdDisp;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public final class JNIUtils {

    private JNIUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static boolean bBigEndian = true;

    /**
     * 保存下位机信息
     */
    public static void saveDeviceConfig() {
        Observable.create(subscriber -> {
            String snCode = Communication.GetDeviceConfig(0);
            String checkCode = Communication.GetDeviceConfig(1);
            DSUtils.get().putString(SPKEY.SN_CODE, HexStr2Str(snCode, false));
            DSUtils.get().putString(SPKEY.CHECK_CODE, HexStr2Str(checkCode, true));

            String version = Communication.Version();
            if (!TextUtils.isEmpty(version)) {
                String[] splits = version.split(CharVar.MINUS);
                // 固件版本
                String vciVersion = "---";
                String comVersion = splits[0];
                if (splits.length >= 2) {
                    vciVersion = splits[1];
                }
                // 固件版本
                DSUtils.get().putString(SPKEY.VCI_VERSION, vciVersion);
                // 通讯程序库版本
                DSUtils.get().putString(SPKEY.COMMUNICATION_VERSION, comVersion);
            }
            subscriber.onComplete();

        }).subscribeOn(Schedulers.io()).subscribe();

    }

    /**
     * 存储显示库版本
     */
    public static void saveCommConfig() {
        Observable.create(subscriber -> {
            String version = Communication.Version();
            if (!TextUtils.isEmpty(version)) {
                String[] splits = version.split(CharVar.MINUS);
                String comVersion = splits[0];
                // 通讯程序库版本
                DSUtils.get().putString(SPKEY.COMMUNICATION_VERSION, comVersion);
            }
            DSUtils.get().putString(SPKEY.DYNAMIC_VERSION, StdDisp.version());
            subscriber.onComplete();

        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * 16进制式字符串还原
     *
     * @param hexStr
     * @param isCut
     * @return
     */
    public static String HexStr2Str(String hexStr, boolean isCut) {
        boolean flag = false;
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        if (isCut) {
            //截取前12个有效字符做机器码
            hexStr = hexStr.substring(0, 12);
        }
        for (int i = 0; i < hexStr.length() - 1; i += 2) {
            String output = hexStr.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            if (decimal == 0) {
                sb.append("0");
            } else {
                char ch = (char) decimal;
                if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z') {
                } else {
                    // 序列号不符合规则
                    flag = true;
                }
                sb.append(ch);
            }
            temp.append(decimal);
        }
        if (flag) {
            return CharVar.EMPTY;
        } else {
            return sb.toString();
        }
    }

    /*
     * JAVA int 转 Byte
     */
    public static byte[] int2Byte(long iNum) {
        byte[] oByte = new byte[4];
        if (bBigEndian) { // 大端存储方式
            for (int i = 0; i < 4; ++i) {
                oByte[i] = (byte) ((iNum >> (i * 8)) & 0xff);
            }
        } else { // 小端存储方式
            for (int i = 4 - 1; i >= 0; --i) {
                oByte[i] = (byte) ((iNum >> (i * 8)) & 0xff);
            }
        }
        return oByte;
    }


    /*
     * JAVA byte Byte，高字节在后
     */
    public static int byte2Int(byte[] iByte, int iStart) {
        int lNum = 0;

        lNum |= ((iByte[iStart + 0] & 0xff) << 0)
                | ((iByte[iStart + 1] & 0xff) << 8)
                | ((iByte[iStart + 2] & 0xff) << 16)
                | ((iByte[iStart + 3] & 0xff) << 24);

        return (lNum & 0xffffffff);
    }

    /*
     * JAVA byte Byte，高字节在头
     */
    public static int byte2Int2(byte[] iByte, int iStart) {
        int lNum = 0;

        lNum |= ((iByte[iStart + 0] & 0xff) << 24)
                | ((iByte[iStart + 1] & 0xff) << 16)
                | ((iByte[iStart + 2] & 0xff) << 8)
                | ((iByte[iStart + 3] & 0xff) << 0);

        return (lNum & 0xffffffff);
    }


}
