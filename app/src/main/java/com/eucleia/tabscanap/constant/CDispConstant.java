package com.eucleia.tabscanap.constant;

/**
 * 包  名: com.eucleia.tabscanap.jni.diagnostic.constant
 * 时  间: 2017-08-14 13:56.
 * 作  者: cuifu
 * 描  述:  jni 诊断层 - 宏定义、Handler、Event常量标识
 */

public interface CDispConstant {


    /**
     * --------------------------------------JNI 宏定义 ---------------------------------------
     **/
    // 线程状态
    int THREAD_BEGIN = 1;
    int THREAD_END = 2;
    int THREAD_END_OK = 0;


    //************************************宏开关 界面方向*********************************

    /**
     * 控制界面属于 左 or 右
     */
    interface PageDisp {
        //界面显示左右
        int DISP_LEFT = 0;// -- DF_MB_NOBUTTON无按钮的非模态(非阻塞)消息框
        int DISP_RIGHT = 1;    //自由按钮的模态(阻塞)消息框

    }


    //*********************************** 消息框界面宏定义 *********************************

    /**
     * 文本对其方式
     */
    interface PageFormat {
        //文本排版格式
        int DT_LEFT = 0;
        int DT_CENTER = 1;
        int DT_RIGHT = 2;

    }

    /**
     * 消息框模态(阻塞)与非模态(阻塞)
     */
    interface PageButtonType {

        int DF_MB_TYPE_ORIGINAL = 0x0000;// -- DF_MB_NOBUTTON无按钮的非模态(非阻塞)消息框
        int DF_MB_TYPE_TIP_OK = 0x1000; //消息界面展示效果,界面提示OK图标
        int DF_MB_TYPE_TIP_WAR = 0x2000; //消息界面展示效果,界面提示Warning图标
        int DF_MB_TYPE_TIP_ERR = 0x3000;//消息界面展示效果,界面提示Error图标
        int DF_MB_TYPE_GREAT_CIRCLE = 0x4000;  //消息界面展示效果,界面为一个大圆旋转
        int DF_MB_TYPE_A1_HOME = 0x5000;  //A1主界面展示效果与DF_MB_BUTTON配合阻塞，用来避免反复的退出系统

        int DF_MB_BUTTON = 0x0100;    //自由按钮的模态(阻塞)消息框

        int DF_MB_YES = 0x0101;    //  Yes(是)按钮的模态(阻塞)消息框
        int DF_MB_NO = 0x0102;     // No(否)按钮的模态(阻塞)消息框
        int DF_MB_YESNO = 0x0103;    //Yes/No按钮的模态(阻塞)消息框
        int DF_MB_OK = 0x0104;       // OK 确定
        int DF_MB_CANCEL = 0x0108;    // cancel 取消
        int DF_MB_OKCANCEL = 0x010C;  //

        int DF_MB_FREE = 0x0200; //自由按钮的非模态(非阻塞)消息框

        int DF_ID_OK = 0x03000000; //固定按钮ok 按键值
        int DF_ID_YES = 0x03000000; //固定按钮yes 按键值
        int DF_ID_CANCEL = 0x030000FF; //固定按钮cancel 按键值
        int DF_ID_NO = 0x030000FF; //固定按钮no 按键值
        int DF_ID_BACK = 0x030000FF; //固定按钮back 按键值
        int DF_ID_RESET = 0x03000006; //重置按钮 按键值
        //int DF_ID_CLEARDTC = 0x03000003; //故障码 清除故障
        int DF_ID_NEXT = 0x03000010; //下一步按钮 按键值


        int DF_ID_FREEBTN_0 = 0x03000100; //自由按钮(动态添加)0 按键值 动态+1;
        int DF_ID_FREEBTN_1 = 0x03000101; //自由按钮(动态添加)1 按键值
        int DF_ID_FREEBTN_2 = 0x03000102; //自由按钮(动态添加)2 按键值
        int DF_ID_FREEBTN_3 = 0x03000103; //自由按钮(动态添加)3 按键值
        // ...
        int DF_ID_FREEBTN_X = 0x03000100; //自由按钮(动态添加)X 按键值

        int DF_ID_NOKEY = 0x04000000; //消息框非模态(非阻塞)无操作返回值
    }


    /**
     * 菜单界面宏定义
     */
    interface PageMenuType {
        int DF_ID_MENU0 = 0x02000000; //菜单按键返回值 0
        int DF_ID_MENU1 = 0x02000001; //菜单按键返回值 1
        int DF_ID_MENU2 = 0x02000002; //菜单按键返回值 2
        //....
    }

    /**
     * 车辆信息选择框宏定义
     */
    interface PageVehicleType {
        int DF_ID_SLT_0 = 0x0200FFFF; //item 0 选择返回值
        int DF_ID_SLT_1 = 0x0201FFFF; //item 1 选择返回值

        int DF_ID_SLT_X = 0x0201FFFF; //0x02xxffff item 1 选择返回值
        int DF_ID_SLT_0_0 = 0x02000000; // item 0 子item0 选择返回值
        int DF_ID_SLT_0_1 = 0x02000001; // item 0 子item1 选择返回值

        int DF_ID_SLT_0_X = 0x02000001; //0x0200xxxx  item 0 子itemX 选择返回值

        int DF_ID_SLT_X_X = 0x02000001; //0x02xxxxxx  item X 子itemX 选择返回值
    }

    /**
     * 车辆信息选择框宏定义
     */
    interface PageSelectType {
        int DF_SL_TYPE_ORIGINAL = 0x00;  //原Select风格，默认是这个风格
        int DF_SL_TYPE_CODE_A1PRO = 0x01;  //A1PRO 刷隐藏界面风格

        int DF_ID_QUERY = 0x03000011; //A1PRO查询按钮，暂时仅A1PRO风格有
        int DF_ID_SETCODE = 0x03000012; //A1PRO设置按钮，暂时仅A1PRO风格有
        int DF_ID_RECOVER = 0x03000013; //A1PRO恢复按钮，暂时仅A1PRO风格有
    }

    /**
     * 诊断框架宏定义
     */
    interface PageDiagnosticType {
        // 诊断框架区域标识宏
        int DF_LEFT = 0x00000000; //左边区域
        int DF_TOP = 0x01000000; //顶部区域
        int DF_MID = 0x02000000; // 中间区域
        int DF_BUTTON = 0x03000000; // 按钮类型
        int DF_NOKEY = 0x04000000; // 无操作

        // 诊断框架按钮返回值宏
        int DF_ID_OK = 0x03000000;
        int DF_ID_START = 0x03000001;
        int DF_ID_STOP = 0x03000002;
        int DF_ID_ERASE = 0x03000005;
        int DF_ID_REPORT = 0x03000008;
        int DF_ID_HELP = 0x03000009;
        int DF_ID_BACK = 0x030000FF;
        int DF_ID_NOKEY = 0x04000000;

        int DF_ID_SYS_0_0 = 0x00000000;
        int DF_ID_SYS_0_1 = 0x00000001;
        int DF_ID_SYS_0_Y = 0x00000000;// 0x0000000Y
        // ....
        int DF_ID_SYS_X_Y = 0x00000000;// 0x00XXYYYY

        int DF_ID_FUN_RVER = 0x01000000;
        int DF_ID_FUN_RDTC = 0x01000001;
        int DF_ID_FUN_CDTC = 0x01000002;
        int DF_ID_FUN_RDS = 0x01000003;
        int DF_ID_FUN_ACTTEST = 0x01000004;
        int DF_ID_FUN_SPECFUN = 0x01000005;

        //诊断框架系统状态宏
        int DF_ENUM_UNKNOWN = 1;// 未知
        int DF_ENUM_NOTEXIST = 2;// 不存在
        int DF_ENUM_NODTC = 3;// 无码
        int DF_ENUM_DTCNUM = 4;// 有码
        // DF_ENUM_DTCNUM+1 为有一个故障码

        //诊断框架界面状态宏
        int DF_SCAN_START = 0;
        int DF_SCAN_END = 1;
        int DF_SCAN_END_OF_CANCEL = 2;
        int DF_SCAN_END_OF_PAUSE = 3;
        int DF_SCAN_END_OF_RESULT = 4;

        //诊断框架 功能区域的功能开关宏
        int DF_FUN_RVER = 0x01;
        int DF_FUN_RDTC = 0x02;
        int DF_FUN_CDTC = 0x04;
        int DF_FUN_RDS = 0x08;
        int DF_FUN_ACTTEST = 0x10;
        int DF_FUN_SPECFUN = 0x20;
    }

    /**
     * 故障码界面定义宏
     */
    interface PageTroubleType {

        int DF_ID_CLEARDTC = 0x03000003;

        int DF_ID_DTCIDX_0 = 0x02000000; //故障码冻结帧点击返回值
        int DF_ID_DTCIDX_1 = 0x02000001;
        int DF_ID_DTCIDX_2 = 0x02000002;
        int DF_ID_DTCIDX_3 = 0x02000003;
        int DF_ID_DTCIDX_X = 0x02000003;//0x0200xxxx
    }


    /**--------------------------------------JNI 宏定义 End---------------------------------------**/


    /**--------------------------------------Handler、EventBus 常量---------------------------------------**/

    /**
     * USBInterface 数据传递标识 EventBus
     */
    interface USBInterface_CONSTANS {
        // 电压、VCI连接状态 EventBus 标识
        int EVENT_VCI_STATE_FLAG = 1012;
        int EVENT_VCI_VOLTAGE_FLAG = 1013;
    }


    /**
     * 消息框界面显示样式的宏
     */
    interface PageMsgBoxType {
        int DF_MB_TYPE_ORIGINAL = 0x0000; //以前的消息界面展示效果
        int DF_MB_TYPE_TIP_OK = 0x1000; //消息界面展示效果,界面提示OK图标
        int DF_MB_TYPE_TIP_WAR = 0x2000; //消息界面展示效果,界面提示Warning图标
        int DF_MB_TYPE_TIP_ERR = 0x3000; //消息界面展示效果,界面提示Error图标
        int DF_MB_TYPE_GREAT_CIRCLE = 0x4000; //消息界面展示效果,界面为一个大圆旋转
    }

    /**
     * 列表框界面显示类型宏
     */
    interface PageListType {
        int DF_LS_TYPE_ORIGINAL = 0x00;  //原list风格，默认是这个风格
        int DF_LS_TYPE_IM = 0x01;       //I/M就绪界面风格
        int DF_LS_TYPE_FREEZE = 0x02;  //冻结帧风格
        int DF_LS_TYPE_MODE06 = 0x03;  //MODE06风格
        int DF_LS_TYPE_MIL = 0x04;       //MIL灯界面风格

    }

    /**--------------------------------------Handler、EventBus 常量 End---------------------------------------**/


    /**
     * 诊断进入的模式：
     * 汽车诊断
     * 汽车保养
     * 刷隐藏
     */
    interface Enum_Entrance_Mode {
        int ENTER_MODE_DIAGNOSIS = 1;//诊断
        int ENTER_MODE_SERVICE = 2;//保养
        int ENTER_MODE_SERVICE_OIL = 201;//机油归零
        int ENTER_MODE_SERVICE_EPB = 202;//刹车片
        int ENTER_MODE_SERVICE_THROTTLE = 203;//节气门
        int ENTER_MODE_SERVICE_SAS = 204;//转向角
        int ENTER_MODE_SERVICE_DPF = 205;//DPF
        int ENTER_MODE_SERVICE_IMMO = 206;//防盗
        int ENTER_MODE_SERVICE_WIN_DOOR = 207;//门窗
        int ENTER_MODE_SERVICE_BMS = 208;//电池
        int ENTER_MODE_SERVICE_ABS = 209;//ABS
        int ENTER_MODE_SERVICE_TPMS = 210;//胎压
        int ENTER_MODE_SERVICE_CKP = 211;//齿讯
        int ENTER_MODE_SERVICE_CVT = 212;//CVT
        int ENTER_MODE_SERVICE_LAMP = 213;//大灯
        int ENTER_MODE_SERVICE_INJECTOR = 214;//喷油嘴
        int ENTER_MODE_SERVICE_AT = 215;//变速箱
        int ENTER_MODE_SERVICE_TIRE = 216;//轮胎尺寸
        int ENTER_MODE_SERVICE_RCMM = 217;//遥控钥匙
        int ENTER_MODE_SERVICE_SRS = 218;//气囊
        int ENTER_MODE_SERVICE_SEATS = 219;//座椅
        int ENTER_MODE_SERVICE_SUSPENSION = 220;//悬挂
        int ENTER_MODE_AIRCONTROLDIG = 221;//空调诊断
        int ENTER_MODE_CODING = 3;//编码
        int ENTER_MODE_NEWENERGY = 4;//新能源
        int ENTER_MODE_OBDDETECT = 5;//环评检测
        int ENTER_MODE_DIAGNOSIS_BXDETECT = 102;//保险检测
        int ENTER_MODE_DIAGNOSIS_OBD_SR_HOTKEY = 103;//OBD 扫描进入
        int ENTER_MODE_DIAGNOSIS_OBD_VI_HOTKEY = 104;//OBD 车辆信息进入
        int ENTER_MODE_DIAGNOSIS_OBD_IM_HOTKEY = 105;//OBD IM状态进入
        int ENTER_MODE_DIAGNOSIS_OBD_LD_HOTKEY = 106;//OBD 数据流进入
        int ENTER_MODE_DIAGNOSIS_OBD_FF_HOTKEY = 107;//OBD 冻结帧进入
        int ENTER_MODE_DIAGNOSIS_OBD_M6_HOTKEY = 108;//OBD MODE06进入
        int ENTER_MODE_DIAGNOSIS_OBD_CT_HOTKEY = 109;//OBD MODE08进入
        int ENTER_MODE_DIAGNOSIS_OBD_ML_HOTKEY = 110;//OBD MIL灯进入
        int ENTER_MODE_DIAGNOSIS_OBD_ED_HOTKEY = 111;//OBD 清码进入
        int ENTER_MODE_DIAGNOSIS_OBD_O2_HOTKEY = 112;//OBD O2传感器进入
        int ENTER_MODE_DIAGNOSIS_CLEARALLDTC = 120;//全系统清码进入
        int ENTER_MODE_DIAGNOSIS_AUTOVIN = 121;//自动定位进入

        int ENTER_MODE_PDICHECK_AUTOVIN = 701;//五菱智能定位进入
        int ENTER_MODE_PDICHECK_ENTERVIN = 702;//五菱输入VIN进入
        int ENTER_MODE_PDICHECK_MANUAL = 703;//五菱手动选择进入
    }


    /**
     * 产品类型：
     * -1 - 无效产品
     * 0 - S7
     * 1 - S7W
     * 2 - S7C
     * 3 - S7D
     * 4 - S7M
     * 100 - S8
     * 101 - S8 PRO
     * 102 - S8 EV
     * 103 - S8M
     * 200 - T3
     * 201 - T2
     * 202 - T1
     */
    interface Enum_Product_Type {

        int PRODUCT_TYPE_NONE = -1;
        int PRODUCT_TYPE_S7 = 0;
        int PRODUCT_TYPE_S7W = 1;
        int PRODUCT_TYPE_S7C = 2;
        int PRODUCT_TYPE_S7D = 3;
        int PRODUCT_TYPE_S7M = 4;
        int PRODUCT_TYPE_S8 = 100;
        int PRODUCT_TYPE_S8PRO = 101;
        int PRODUCT_TYPE_S8EV = 102;
        int PRODUCT_TYPE_S8M = 103;
        int PRODUCT_TYPE_S8S = 105;
        int PRODUCT_TYPE_T3 = 200;
        int PRODUCT_TYPE_T2 = 201;
        int PRODUCT_TYPE_T1 = 202;
    }

}
