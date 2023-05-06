package com.eucleia.tabscanap.jni.diagnostic.so;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic.so
 * 文件名:  StdDisp
 * 作者 :   sen
 * 时间 :  上午9:31 2017/3/7.
 * 描述 :  中间层 jni -- 入口
 */

public class StdDisp {

    /**程序入口*/
    public static native void entrance();

    /**获取中间层的版本号*/
    public static native String version();

    /**释放*/
    public static native void ReleaseIso();

    /**输入日志*/
    public static native void Log(byte[] content);

}
