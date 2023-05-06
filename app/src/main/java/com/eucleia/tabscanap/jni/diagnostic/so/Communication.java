package com.eucleia.tabscanap.jni.diagnostic.so;

import static java.lang.System.loadLibrary;

import com.eucleia.tabscanap.constant.CharVar;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic.so
 * 文件名:  Communication
 * 作者 :   sen
 * 时间 :  上午9:29 2017/3/7.
 * 描述 :  通信层jni - so 接口
 */

public class Communication {

    static {
        loadLibrary(CharVar.Pub_Functional);
        loadLibrary(CharVar.Std_Comm);
    }

    /**
     * 在启动函数调用前，传递通讯库的可存储文件路径
     * 由于安卓11调整了文件存储规则，导致中间库无法存储文件，调整为由APP下发可存储路径
     *
     * @param strStorablePath 可存储路径
     * @return
     */
    public static native void SetStorablePath(String strStorablePath);

    /**
     * 启动
     *
     * @return
     */
    public static native void Entry();

    /**
     * 版本号
     *
     * @return
     */
    public static native String Version();

    /**
     * 获取VIN状态
     *
     * @return 返回值：0为成功，1为正在获取，2为获取失败
     */
    public static native int GetVinStatus();

    /**
     * 获取VIN码
     *
     * @return
     */
    public static native String GetVinCode();

    /**
     * 通知VCI要开始升级 status 0为开始，1为结束
     *
     * @return
     */
    public static native boolean SetUpdataMode(int status);

    /**
     * 发送升级数据给VCI 其中，格式如下： byte[4] Length; 总长度 byte[4] Type; 默认为0 byte[4]
     * TotalSize; 文件总大小，从bin文件的第32字节开始取，取4个节字，字节从0开始算 byte[4] PackSize;
     * 这一包发送的“升级数据”的大小 byte[4] PackNo; 第几包 byte[1024*N] Data; 一次读取的升级数据
     *
     * @return 0为成功
     */
    public static native int Download(byte[] dataArray);

    /**
     * 发送较验和和总文件大小
     *
     * @param checkSum  较验合，从bin文件的第36字节开始取，取4个节字，字节从0开始算起
     * @param TotalSize 文件总大小，从bin文件的第32字节开始取，取4个节字，字节从0开始算
     * @return 0为成功
     */
    public static native int SetUpdateCheckSum(int checkSum, int TotalSize);

    /**
     * 开始 日志记录
     */
    public static native void LogStart();

    /**
     * 结束 日志记录
     */
    public static native String LogStop();

    /**********
     * 获取设备信息，例如序列号，MCU ID, 激活KEY等
     ****************/
    /** Parament */
    /** DEVICE_CONFIG_SN = 0, 序列号 */
    /** DEVICE_CONFIG_SECRET_KEY = 1, 校验码 */
    /** DEVICE_CONFIG_RANDOM_KEY = 2, 随机码 */
    /** DEVICE_CONFIG_FACTORY_DATE = 3, 生产日期 */
    /**
     * DEVICE_CONFIG_MCU_UNIQUE_ID = 4, MCU唯一ID
     */
    public static native String GetDeviceConfig(int parament);

    /**
     * 在VCI升级前调用，防止VCI跑飞后无法正常升级。
     */
    public static native void ForceUpdataMode();


    /**
     * APK是否在前台运行
     *
     * @param flag 0:APK在后台运行 1:APK在前台运行
     */
    public static native void SetApkActive(int flag);

    /**
     * 用户APK退出时，调用此方法，通知通迅层
     */
    public static native void ApkExit();

    /**
     * 获取产品型号
     *
     * @return 返回 0:S7 ，1: S7W，2:T5, 3:S8, 4:S7C,  5:S7D
     */
    public static native int GetProductModel();

    /**
     * 通知通迅层，蓝牙设备已更改
     *
     * @return
     */
    public static native int BtDeviceChange();

    /**
     * Protocol 参数：
     * CAN_11BIT_500K       = 0,
     * CAN_11BIT_250K       = 1,
     * CAN_29BIT_500K       = 2,
     * CAN_29BIT_250K       = 3,
     * VPW_10400            = 5,
     * PWM_41600            = 4,
     * KWP_ISO14230_10400   = 6,
     * ISO_ISO14230_10400   = 7,
     * ISO9141_10400        = 8,
     * VW_SYS_01_ENGINE_KWP2000_TP20       = 9,   大众-发动机电控系统
     * VW_SYS_01_ENGINE_UDS                = 10,  大众-发动机电控系统
     * VW_SYS_02_TRANS_KWP2000_TP20        = 11,  大众-变速箱电控系统
     * VW_SYS_02_TRANS_UDS                 = 12,  大众-变速箱电控系统
     * VW_SYS_03_ABS_KWP2000_TP20          = 13,  大众-制动电子装置
     * VW_SYS_03_ABS_UDS                   = 14,  大众-制动电子装置
     * VW_SYS_05_ACC_AUTH_KWP2000_TP20     = 15,  大众-进入及其启动许可
     * VW_SYS_05_ACC_AUTH_UDS              = 16,  大众-进入及其启动许可
     * VW_SYS_09_CENT_ELECT_KWP2000_TP20   = 17,  大众-电子中央电气系统
     * VW_SYS_09_CENT_ELECT_UDS    = 18,  大众-电子中央电气系统
     * VW_SYS_17_INSTRUMENTS_KWP2000_TP20  = 19,  大众-仪表板
     * VW_SYS_17_INSTRUMENTS_UDS           = 20,  大众-仪表板
     * VW_SYS_25_IMMOBILIZER_KWP2000_TP20  = 21,  大众-防盗系统
     * VW_SYS_25_IMMOBILIZER_UDS           = 22,  大众-防盗系统
     * VW_SYS_46_CENT_CONV_KWP2000_TP20    = 23,  大众-舒适系统中央模块
     * VW_SYS_46_CENT_CONV_UDS             = 24,  大众-舒适系统中央模块
     * <p>
     * KWP2000_ISO15765_2_OBD_ENGINE       = 25,   KWP2000_ISO15765_2 发动机系统
     * NISSAN_RENAULT_KWP2000_ISO15765_2   = 26,  日产雷诺 KWP2000_on_ISO15765_2 发动机
     * TimeOutMs 参数：
     * 如果指定TimeOutMs为0，则立即返回，无论通信层已经获取到OBD VIN还是没有获取到，都立即返回
     * 获取到VIN以string返回，没有获取到则返回空串string
     * 如果指定TimeOutMs为非零，则在指定TimeOutMs内获取VIN码，直到成功获取后返回；
     * 次函数在TimeOutMs返回前为阻塞函数
     * 返回：jstring,为空字串，获取失败；非空且长度为17，获取成功；
     */
    public static native String GetObdVinCode(int Protocol, int TimeOut);

    /**********
     * 开始"日志自动上传"的日志记录
     ****************/
    /**
     * 函数名为: void Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_AutoLogStart(void)
     */
    public static native void AutoLogStart();

    /**********
     * 停止"日志自动上传"的日志记录
     ****************/
    /**
     * 功能:  停止"日志自动上传"的日志记录，并返回日志文件的绝对路径名
     * <p>
     * 参数: 无
     * <p>
     * 返回:
     * jstring,  如果为空字串，日志记录失败。
     * 非空子串，日志记录成功，格式为: A+B
     * A为日志文件A(绝对路径并包含文件名)，B为日志文件B(绝对路径并包含文件名)
     * 当只有一个文件时，格式为: A
     * <p>
     * 返回举例:
     * 场景1: 返回两个日志文件的jstring: /storage/sdcard0/TabScan/DataLog/LogFile20170919_214423__427952979.txt+/storage/sdcard0/TabScan/DataLog/Passthru_20170919_214423__427952979.txt
     * 场景2: 返回一个日志文件的jstring: /storage/sdcard0/TabScan/DataLog/Passthru_20170919_220521__982852838.txt
     * <p>
     * 函数名为: jstring Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_AutoLogStop(void)
     */
    public static native String AutoLogStop();

    /**
     * 获取VCI产品类型
     *
     * @参数： 无
     * <p>
     * 返回:
     * jstring,  如果为空字串，VCI没有连接
     * 如果非空字串，返回字串分别如下:
     * <p>
     * T6                 --- 下位机为T6,软件编码:MarsTool500FirmWare
     * T6 Plus            --- 下位机为T6 Plus,软件编码:MarsTool500FirmWare
     * T6 IC              --- 下位机为带IC的T6,软件编码:MarsTool500FirmWare
     * T6 S               --- 下位机为T6S,软件编码:MarsTool500FirmWare
     * T3                 --- 下位机为T3,软件编码:wiScanT3FirmWare
     * S7                 --- 下位机为S7,软件编码:MarsTool700FirmWare
     * S7 IC              --- 下位机为带IC的S7,软件编码:MarsTool700FirmWare
     * T6 Pro             --- 下位机为T6 Pro,软件编码:wiScanT6ProFirmWare
     * S7-C11             --- 下位机为S7-C11,wiScanT3FirmWare
     * T6 N               --- 下位机为T6N,软件编码:MarsTool500FirmWare
     * T6 NS              --- 下位机为T6NS,软件编码:MarsTool500FirmWare
     * T6 SS                --- 下位机为T6S New,软件编码:wiScanT6SFirmWare
     * T6 ST                --- 下位机为T6 ST,软件编码:JAD500FirmWare
     * T6 Pro3                --- 下位机为T6 Pro3,软件编码:wiScanT6PRO3FirmWare
     * <p>
     * 返回举例:
     * 场景1: 当S7W蓝牙连接上了T6+盒子时，返回的jstring: T6 Plus
     * 场景2: 当S7平板为IC板的S7时，返回的jstring: S7 IC
     * 场景3: 当手机版APK蓝牙连接上了T6盒子时，返回的jstring: T6
     * @TabScan函数名为: jstring Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_GetVciProductType(void)
     */
    public static native String GetVciProductType();

    /**********  判断VCI是否处于boot模式  ****************/
    /**
     * 功能:  判断VCI是否处于boot模式
     * 参数: 无
     * 返回:
     * jboolean    false， VCI处在Application正常模式
     * true，  VCI处在boot升级模式
     * 注意: 如果VCI没有连接上，此接口返回false，判断VCI是否处在Boot模式中没有意义
     * 函数名为: jboolean IsVciInBootMode(void);
     */
    public static native boolean IsVciInBootMode();


    /**
     * S8新加接口
     */

    /**
     * S8蓝牙模组初始化
     *
     * @参数： 无
     * @返回： 无，对蓝牙模组进行初始化
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtInit(void)
     */
    public static native void S8BtInit();


    /**
     * 指定S8蓝牙模组连接远端设备
     *
     * @参数： jstring jStringMac:   远端蓝牙设备的MAC地址: XX:XX:XX:XX:XX:XX
     * 格式必须是"XX:XX:XX:XX:XX:XX"的字符串
     * @返回： 无
     * @注意: 蓝牙是否连接成功，将在函数S8BtInform中通知
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtConnect(jstring jStringMac)
     */

    public static native void S8BtConnect(String Mac);

    /**
     * 指定S8蓝牙模组与远端设备断开连接
     *
     * @参数： 无
     * @返回： 无，断开蓝牙模组与远端设备的连接，返回结果将以函数S8BtInform中通知
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtDisConnect(void)
     */

    public static native void S8BtDisConnect();

    /**
     * 指定S8蓝牙模组进行蓝牙扫描
     *
     * @参数： jint ScanTimeOutSeconds:   指定扫描蓝牙的扫描时间，单位为秒
     * 此函数为非阻塞函数，立即返回。函数调用后，蓝牙模组将会开始扫描指定的时间ScanTimeOutSeconds
     * @返回： 扫描结果将以函数S8BtInform中通知
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtScan(jint ScanTimeOutSeconds)
     */

    public static native void S8BtScan(int scanTimeOutSeconds);

    /**
     * Native 通过JNI调用JAVA的函数名:
     * void S8BtInform(String Inform)
     *
     * @参数： String BtResponse:   蓝牙模组的事件通知，字串型
     * @说明: //////////////////////////////////////////////////////////////////////////
     * 扫描结果事件返回格式:
     * INQUIRY_PARTIAL XX:XX:XX:XX:XX:XX YYYYYY
     * 或者
     * INQUIRY XX:XX:XX:XX:XX:XX YYYYYY
     * <p>
     * XX:XX:XX:XX:XX:XX    扫描到的蓝牙的MAC地址，例如: 05:00:20:40:04:16
     * <p>
     * YYYYYY               扫描到的蓝牙的相应设备类型，十六进制，
     * 例如: 001e00  T6的类型统一为0x001e00，十六进制
     * <p>
     * 举例: 蓝牙模组扫描到6个设备:
     * INQUIRY 05:00:20:40:04:16 001e00
     * INQUIRY 00:0c:bf:13:81:a0 001e00
     * INQUIRY 00:0c:bf:09:06:48 001e00
     * INQUIRY 16:10:10:10:02:04 001e00
     * INQUIRY 16:10:10:10:01:95 001e00
     * INQUIRY 00:11:67:50:00:00 001e00
     * <p>
     * S8BtInform会调用6次，分别将以上6个结果返回给JAVA
     * <p>
     * //////////////////////////////////////////////////////////////////////////
     * 扫描到的蓝牙名称返回格式:
     * NAME XX:XX:XX:XX:XX:XX "BTNAME"
     * <p>
     * XX:XX:XX:XX:XX:XX           扫描到的蓝牙的MAC地址，例如: 05:00:20:40:04:16
     * <p>
     * "BTNAME"                    扫描到的蓝牙的相应设备，字符串型，
     * 例如: "T610170801123469", 此处带有双引号"
     * <p>
     * 举例: 查询到6个蓝牙的蓝牙名称
     * NAME 16:10:10:10:02:04 "T600161010100204"
     * NAME 00:0c:bf:1c:80:66 "T610170801123464"
     * NAME 16:10:10:10:01:95 "T600161010100195"
     * NAME 00:0c:bf:0a:6e:0f "T500160440200018"
     * NAME 16:10:10:10:03:28 "T600161010100328"
     * NAME 00:0c:bf:1c:7b:74 "T610170801123469"
     * <p>
     * S8BtInform会调用6次，分别将以上6个结果返回给JAVA
     * <p>
     * <p>
     * //////////////////////////////////////////////////////////////////////////
     * 连接上指定的蓝牙返回格式:
     * CONNECT N RFCOMM 1
     * <p>
     * 举例: 成功连接到指定的蓝牙  16:10:10:10:02:04 "T600161010100204"
     * <p>
     * S8BtInform会调用1次, 字符串为 "CONNECT 0 RFCOMM 1"，此处没有双引号"
     */

    public static native void S8BtInform(String inform);


    /**
     * 蓝牙连接成功返回状态
     *
     * @param MacAddress
     */
    public static native void S8BtConnectEvent(String MacAddress);


    /**
     * 蓝牙扫描完成
     */
    public static native void S8BtScanComplete();

    /**
     * 指定S8蓝牙模组获取远端设备的名称
     *
     * @参数： jstring jStringMac:   远端蓝牙设备的MAC地址: XX:XX:XX:XX:XX:XX
     * <p>
     * 格式必须是"XX:XX:XX:XX:XX:XX"的字符串
     * @返回： 无
     * @注意:
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtGetName(jstring jStringMac)
     */
    public static native void S8BtGetName(String jStringMac);

    /**
     * 指定S8蓝牙模组的名称
     *
     * @参数： jstring jStringName:   S8蓝牙名称
     * @返回： 无
     * @函数名为: Java_com_eucleia_marstest_mainModule_diagnostic_so_Communication_S8BtSetName(jstring jStringName)
     */
    public static native void S8BtSetName(String jStringName);

    /**
     * 打开S8蓝牙模组
     *
     * @参数： 无
     * @返回： 无，将会开通S8蓝牙模组的电源，对蓝牙模组进行上电
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtPowerOn(void)
     */
    public static native void S8BtPowerOn();

    /**
     * 关闭S8蓝牙模组
     *
     * @参数： 无
     * @返回： 无，将会切断S8蓝牙模组的电源，
     * @函数名为: Java_com_eucleia_tabscan_jni_diagnostic_so_Communication_S8BtPowerOff(void)
     */
    public static native void S8BtPowerOff();

    public static native void S8SelectBtType(int BtType);

}
