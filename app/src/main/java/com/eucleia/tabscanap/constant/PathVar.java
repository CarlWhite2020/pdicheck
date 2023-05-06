package com.eucleia.tabscanap.constant;


import com.eucleia.pdicheck.bean.constant.Constant;
import com.eucleia.tabscanap.util.SDUtils;

/**
 * 文件路径相关的存储位置
 */
public interface PathVar {

    String tabScan = "/TabScan/";

    /**
     * 车系安装的文件目录
     */
    String rootPath = "VehDiag/";

    /**
     * 保养路径
     */
    String servicePath = "VehService/";

    /**
     * 保养路径
     */
    String codingPath = "VehCoding/";

    /**
     * 相机拍摄存储路径
     */
    String cameraPath = "Camera/";

    /**
     * 下载存储路径
     */
    String downloadPath = "Download/";

    /**
     * 下载存储路径
     */
    String Image = "Image/";

    /**
     * 下位机更新存储路径
     */
    String VciPath = "UpdatePack/";

    /**
     * pdf文件存储路径
     */
    String PdfPath = "PDF/";


    /**
     * 崩溃存储
     */
    String CrashPath = "Crash/";

    /**
     * 日志存储
     */
    String LogPath = "log/";

    /**
     * 无限升级模式
     */
    String noLimitVci = "UpdatePack/VCI888/";

    /**
     * 本地升级模式
     */
    String localVci = "UpdatePack/local/";

    /**
     * 日志以LOG文件形式存储
     */
    String collectLog = "CollectData/Logs/";

    /**
     * 采集日志ZIP
     */
    String collectZip = "CollectData/Zips/";

    /**
     * 采集日志临时文件
     */
    String collectTemp = "CollectData/Temps/";

    /**
     * 自动采集 临时文件
     */
    String autoCollectTemp = "CollectData/AutoTemps/";

    /**
     * 自动采集ZIP
     */
    String autoCollectZip = "CollectData/AutoZips/";

    /**
     * 自动采集 崩溃日志
     */
    String autoCrashLog = "CollectData/AutoCrashLog/";


    String VCI_LOCAL = SDUtils.getLocalPath() + localVci;

    String VCI_NO_LIMIT = SDUtils.getLocalPath() + noLimitVci;

    String DCIM_PATH = SDUtils.getLocalPath() + cameraPath;

    String OBD_PATH = SDUtils.getLocalPath() + rootPath + "Eobd";

    String OBDData_PATH = SDUtils.getLocalPath() + rootPath + "Eobd/Data/";

    String OBDHelp_PATH = SDUtils.getLocalPath() + rootPath + "Eobd/Help";

    String OBDIcon_PATH = SDUtils.getLocalPath() + rootPath + "Eobd/.icon";

    String ExternalOBDReportPath = SDUtils.getLocalPath() + "/External/obdreport.json";

    String ExternalVinPath = SDUtils.getLocalPath() + "/External/vin.json";


    String DB_PATH = "/data/data/" + Constant.PackageName + "/databases/";

    /**
     * https://oss.eucleia.net/a1pro/demo/v3_00/Demo/libDiag32.so
     */
    String DownloadPath = "https://oss.eucleia.net/%s/%s/%s/%s/%s";

}
