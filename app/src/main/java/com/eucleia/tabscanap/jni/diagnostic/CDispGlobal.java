package com.eucleia.tabscanap.jni.diagnostic;

import static java.lang.Thread.sleep;

import android.text.TextUtils;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.pdicheck.bean.normal.VciStatus;
import com.eucleia.pdicheck.room.database.PDIDatabase;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.diag.CDispFrameBeanEvent;
import com.eucleia.tabscanap.bean.diag.CDispMsgBoxBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.constant.PathVar;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.SDUtils;
import com.eucleia.tabscanap.util.Util;
import com.eucleia.tabscanap.util.VciProductUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispGlobal
 * 作者 :   sen
 * 时间 :  上午9:53 2017/3/6.
 * 描述 :  jni 中间层 - 全局函数
 */

public class CDispGlobal {

    // JNI传递过来的参数;
    private volatile static int objKey = 0;
    public static Map<Integer, CDispMsgBoxBeanEvent> MSG_BOX_MAP_EVENT = new ConcurrentHashMap<>();


    /**
     * * 功能介绍：设置按钮类型 参数说明：iType 按钮类型
     * OK_BUTTON== (0X0101)
     * CANCEL_BUTTON== (0x0102)
     * ...
     * 它们分别 & iType,若不为零或者等于本身，则表示有这个按扭,反之没有这个按扭
     *
     * @return 返回值： 1. 成功 0. 失败 注意该函数必须首先调用
     */
    private static void checkBtnType(int type, CDispMsgBoxBeanEvent msgBoxBeanEvent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "-- 判断Btn 类型:" + String.format("%x", type));
        // 判断是否模态
        int isWait = type & CDispConstant.PageButtonType.DF_MB_BUTTON;
        int isFreeBtn = type & CDispConstant.PageButtonType.DF_MB_FREE;
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "--- isWait:" + String.format("%x", isWait));
        if (isWait == 0 && isFreeBtn == 0) {
            // 非阻塞、非自定义按钮 noButton
            msgBoxBeanEvent.setHasWait(false);
            msgBoxBeanEvent.setHasFreeBtn(false);

        } else if (isWait == 0) {
            // 非阻塞、添加自定义按钮
            msgBoxBeanEvent.setHasWait(false);
            msgBoxBeanEvent.setHasFreeBtn(true);

        } else if (isFreeBtn == 0) {
            // 阻塞、固定按钮 & oxff 结果为 1、2、3、4、8、C
            msgBoxBeanEvent.setHasWait(true);
            msgBoxBeanEvent.setHasFreeBtn(false);
        } else {
            // 阻塞、自定义按钮
            msgBoxBeanEvent.setHasWait(true);
            msgBoxBeanEvent.setHasFreeBtn(true);
        }

        // Yes 按钮
        if ((CDispConstant.PageButtonType.DF_MB_YES & type) == CDispConstant.PageButtonType.DF_MB_YES) {
            msgBoxBeanEvent.setHasYesBtn(true);
        } else {
            msgBoxBeanEvent.setHasYesBtn(false);
        }
        // No 按钮
        if ((CDispConstant.PageButtonType.DF_MB_NO & type) == CDispConstant.PageButtonType.DF_MB_NO) {
            msgBoxBeanEvent.setHasNoBtn(true);
        } else {
            msgBoxBeanEvent.setHasNoBtn(false);
        }
        // Yes No
        if ((CDispConstant.PageButtonType.DF_MB_YESNO & type) == CDispConstant.PageButtonType.DF_MB_YESNO) {
            msgBoxBeanEvent.setHasYesNoBtn(true);
        } else {
            msgBoxBeanEvent.setHasYesNoBtn(false);
        }
        // ok
        if ((CDispConstant.PageButtonType.DF_MB_OK & type) == CDispConstant.PageButtonType.DF_MB_OK) {
            msgBoxBeanEvent.setHasOkBtn(true);
        } else {
            msgBoxBeanEvent.setHasOkBtn(false);
        }
        // cancel
        if ((CDispConstant.PageButtonType.DF_MB_CANCEL & type) == CDispConstant.PageButtonType.DF_MB_CANCEL) {
            msgBoxBeanEvent.setHasCancelBtn(true);
        } else {
            msgBoxBeanEvent.setHasCancelBtn(false);
        }
        // ok cancel
        if ((CDispConstant.PageButtonType.DF_MB_OKCANCEL & type) == CDispConstant.PageButtonType.DF_MB_OKCANCEL) {
            msgBoxBeanEvent.setHasOkCancelBtn(true);
        } else {
            msgBoxBeanEvent.setHasOkCancelBtn(false);
        }
    }


    /**--------------------------------------JNI 接口---------------------------------------**/

    /**
     * 显示消息对话框(ok按钮模态(阻塞)界面)
     *
     * @param strTitle   标题
     * @param strContain 内容
     * @param nType      对话框类型(见宏定义)
     * @param nDispType  在界面显示位置
     * @param nformat    文本排版格式(见宏定义)
     * @param nTimer     定时器,单位ms,默认为0不处理,非0到达时 自动返回
     * @return 见宏定义 用户按键
     * 说明:该全局消息框,无自由按钮模式
     * marsShowMsgBox
     */
    public static int marsShowMsgBox(String strTitle, String strContain, int nType, int nDispType, int nformat, int nTimer) throws InterruptedException {
        CDispMsgBoxBeanEvent sMarShowMsgBean;
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strTitle + "," + strContain + "," + Integer.toHexString(nType) + "," + nDispType + "," + nformat + "," + nTimer);
        sMarShowMsgBean = new CDispMsgBoxBeanEvent(objKey++);
        sMarShowMsgBean.setnType(nType);
        sMarShowMsgBean.setMsgSource(CDispGlobal.class.getSimpleName());
        sMarShowMsgBean.setStrTitle(strTitle);
        sMarShowMsgBean.setStrContext(strContain);

        // 判断按钮类型
        checkBtnType(nType, sMarShowMsgBean);
        sMarShowMsgBean.setDispType(nDispType);
        sMarShowMsgBean.setnFormat(nformat);
        sMarShowMsgBean.setTimer(nTimer);
        sMarShowMsgBean.setnType(nType);


        //开启沙漏的判断
        if (sMarShowMsgBean.isHasWait()) {
            sMarShowMsgBean.setbHaveHourglass(false);
        } else {
            sMarShowMsgBean.setbHaveHourglass(true);
        }


        if (!EDiagUtils.get().isDiagnostic()) {
            sMarShowMsgBean.setBackFlag(CDispConstant.PageButtonType.DF_ID_OK);
            sMarShowMsgBean.lockAndSignalAll();
            return sMarShowMsgBean.getBackFlag();
        }

        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            sMarShowMsgBean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            sMarShowMsgBean.lockAndSignalAll();
            return sMarShowMsgBean.getBackFlag();
        }
        //存储全局消息框
        MSG_BOX_MAP_EVENT.put(sMarShowMsgBean.getObjKey(), sMarShowMsgBean);
        AppVM.get().postDataValue(sMarShowMsgBean.getObjKey(), PageType.Type_GlobalMsgBox);
        // 模态(阻塞),非模态(非阻塞) 动态判断是否阻塞线程
        if (sMarShowMsgBean.isHasWait() && !EDiagUtils.get().isThreadEnd()) {
            sMarShowMsgBean.lockAndWait();
        } else {
            sleep(150);
        }
        return sMarShowMsgBean.getBackFlag();
    }


    /**
     * 诊断框架
     * 设置左边系统桌布与顶层功能区,显示与隐藏
     *
     * @param bState true 显示;false 隐藏
     */
    public static void SetSysFunDisplay(boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), bState);
        EDiagUtils.get().getJniModel().isFunSystemDisp = bState;

    }

    /**
     * 获取当前语言
     *
     * @return 语言表示字符en, cn等
     */
    public static String GetLanguage() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().language);
        return EDiagUtils.get().getModel().language;
    }

    /**
     * 获取当前路径
     *
     * @return 车型路径
     */
    public static String GetVehiclePath() {
        String vehPath = EDiagUtils.get().getModel().vehPath;
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), vehPath);
        if (vehPath.contains(FileVar.LIBDIAG_SO)) {
            return vehPath.replace(FileVar.LIBDIAG_SO, "");
        }
        return vehPath + File.separator;
    }

    /**
     * 获取诊断SO当前路径
     *
     * @return
     */

    public static String GetVehicleSoPath() {
        String soPath = EDiagUtils.get().getModel().vehSoPath;
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), soPath);
        if (TextUtils.isEmpty(soPath)) {
            return "";
        }
        return soPath + File.separator;
    }

    /**
     * 获取当前车型
     *
     * @return 车型文件名称
     */
    public static String GetVehicleModel() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().vehModel);
        return EDiagUtils.get().getModel().vehModel;
    }

    /**
     * 获取当前VIN码
     *
     * @return VIN码
     */
    public static String GetVehicleCode() {
        String vin = EDiagUtils.get().getModel().vin;
        if (TextUtils.isEmpty(vin)) {
            vin = EDiagUtils.get().getJniModel().vin;
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), vin);
        return vin;
    }

    /**
     * 设置当前VIN码
     *
     * @param strVehicleCode
     */
    public static void SetVehicleCode(String strVehicleCode) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strVehicleCode);
        EDiagUtils.get().getJniModel().vin = strVehicleCode;
    }

    /**
     * 获取车型历史记录
     *
     * @return 历史记录串
     */
    public static String GetRecordList() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        return EDiagUtils.get().getJniModel().recordList;
    }


    /**
     * 设置车型历史记录
     *
     * @param strRecord 历史记录串
     */
    public static void SetRecordList(String strRecord) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strRecord);
        EDiagUtils.get().getJniModel().recordList = strRecord;
    }

    /**
     * 设置车辆信息
     *
     * @param strVehicleInfo 车辆信息
     */
    public static void SetVehicleInfo(String strVehicleInfo) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strVehicleInfo);
        // 检测上一个方法生成的时间戳key值是否存在,存在则根据时间戳更新此字段到数据表、没有则暂时记录到变量,等待上一个方法的执行
        EDiagUtils.get().getJniModel().vehicleInfo = strVehicleInfo;

    }

    /**
     * 设置当前系统名称
     *
     * @param strSystemName 当前系统名称
     *                      说明: 所有界面标题栏最右边的提示信息,显示长度有限,超过水平滚动
     */
    public static void SetSystemName(String strSystemName) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strSystemName);
        // 保存起来,存储故障码数据表时使用
        EDiagUtils.get().getJniModel().systemName = strSystemName;
    }

    /**
     * 通知UI线程启动
     * 说明: 通知界面诊断开启了线程,更改状态为1
     * 状态:1
     */
    public static void NotifyThreadBegin() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        EDiagUtils.get().getJniModel().threadStatus = CDispConstant.THREAD_BEGIN;
    }

    /**
     * 通知UI线程结束
     * 说明: 通知界面诊断开启的线程需要结束,更改状态为2
     * 状态:2
     */
    public static void NotifyThreadEnd() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        EDiagUtils.get().getJniModel().threadStatus = CDispConstant.THREAD_END;
        Util.lock();
        clearRightPage(true);
        Util.unLock();
        // 操作前加线程同步锁非阻塞锁
        Util.lockAndWait();

    }

    /**
     * 通知UI线程结束完毕
     * 说明:通知界面诊断开启的线程结束完毕,更改状态为0
     * 状态:0
     */
    public static void NotifyThreadEndOk() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());

        boolean isForceThreadEnd = false;
        if (GetThreadState() == CDispConstant.THREAD_END) {
            isForceThreadEnd = true;
        }
        EDiagUtils.get().getJniModel().threadStatus = CDispConstant.THREAD_END_OK;


        // 操作前加线程同步锁非阻塞锁
        Util.lock();

        clearRightPage(isForceThreadEnd);

        // 操作结束后解锁
        Util.unLock();

        // 解除上方ThreadEnd 锁
        Util.lockAndSignalAll();
    }

    /**
     * 获取子线程状态
     *
     * @return 当前线程状态
     */
    public static int GetThreadState() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getJniModel().threadStatus);
        return EDiagUtils.get().getJniModel().threadStatus;
    }

    /**
     * 获取产品类型代号
     *
     * @return 当前产品类型代号
     * -1 - 无效产品
     * 0 - S7
     * 1 - S7W
     * 2 - S7C
     * 3 - S7D
     * 4 - S7M
     * 100 - S8
     * 101 - S8 PRO
     * 102 - S8 EV
     * 103 - S8M、T4
     * 200 - T3、T2、T1、T6
     * * 注意：目前JNI接口那边自己实现了获取产品类型，T3、T2、T1统一获取的是200。
     */
    public static int GetProductType() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        String snCode = DSUtils.get().getString(SPKEY.SN_CODE);
        if (TextUtils.isEmpty(snCode)) {
            return CDispConstant.Enum_Product_Type.PRODUCT_TYPE_NONE;
        }
        if (snCode.startsWith(VciProductUtils.PRODUCT_T4)) {
            return CDispConstant.Enum_Product_Type.PRODUCT_TYPE_S8M;
        } else if (snCode.startsWith(VciProductUtils.PRODUCT_T1) ||
                snCode.startsWith(VciProductUtils.PRODUCT_A1) ||
                snCode.startsWith(VciProductUtils.PRODUCT_T2) ||
                snCode.startsWith(VciProductUtils.PRODUCT_T3) ||
                snCode.startsWith(VciProductUtils.PRODUCT_T3GD) ||
                snCode.startsWith(VciProductUtils.PRODUCT_T6)) {
            return CDispConstant.Enum_Product_Type.PRODUCT_TYPE_T3;
        }
        return CDispConstant.Enum_Product_Type.PRODUCT_TYPE_NONE;
    }


    /**--------------------------------------JNI 接口 End---------------------------------------**/


    /**
     * 遍历所有JNI类 Map 给所有[右侧对象 disp=right]设置返回值为back值【操作前判断map非空】并释放阻塞锁;除frame类外
     */
    public static void clearRightPage(boolean isForceThreadEnd) {
        // 全局marShowMsgBox退出
        EDiagUtils.get().clearRightPage();
    }

    /**
     * 遍历所有JNI类 Map给所有对象设置返回值back,并释放锁;
     * Home
     */
    public static void clearAllPage() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "Start");
        EDiagUtils.get().setDiagnosticExiting(true);
        Util.lock();
        EDiagUtils.get().clearAllPage();
        Util.unLock();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "End");
    }

    /**
     * 遍历所有JNI类 Map给所有对象设置返回值OK,并释放锁;
     * Home
     */
    public static void clearAllPageByOk() {
        Util.lock();
        EDiagUtils.get().okAllPage();
        Util.unLock();
    }

    /**
     * -----------------------------------------------------------------------------
     * 功    能：校验licenses
     * 参数说明：无
     * 返 回 值：int
     * 0 - 失败
     * 1 - 成功
     * 说    明：校验licenses
     * -----------------------------------------------------------------------------
     */
    public static int CheckLicenses() {
        return 1;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：获取VCI状态
     * 参数说明：无
     * 返 回 值：int
     * 0 - 下位机未连上
     * 1 - 下位机连接成功
     * 说明：获取VCI状态
     * -----------------------------------------------------------------------------
     */
    public static int GetVciState() {
        VciStatus value = AppVM.get().getVciStatus();
        if (value != null && value.getVciStatus() == 1 && value.getVoltage() >= 10) {
            //下位机连接上，并且电压大于等于10V
            return 1;
        }
        return 0;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功    能：获取诊断进入的模式
     * 参数说明：无
     * 返 回 值：int
     * ENTER_MODE_DIAGNOSIS = 1;//诊断
     * ENTER_MODE_SERVICE = 2;//保养
     * ENTER_MODE_SERVICE_OIL = 201;//机油归零
     * ENTER_MODE_SERVICE_EPB = 202;//刹车片
     * ENTER_MODE_SERVICE_THROTTLE = 203;//节气门
     * ENTER_MODE_SERVICE_SAS = 204;//转向角
     * ENTER_MODE_SERVICE_DPF = 205;//DPF
     * ENTER_MODE_SERVICE_IMMO = 206;//防盗
     * ENTER_MODE_SERVICE_WIN_DOOR = 207;//门窗
     * ENTER_MODE_SERVICE_BMS = 208;//电池
     * ENTER_MODE_SERVICE_ABS = 209;//ABS
     * ENTER_MODE_SERVICE_TPMS = 210;//胎压
     * ENTER_MODE_SERVICE_CKP = 211;//齿讯
     * ENTER_MODE_SERVICE_CVT = 212;//CVT
     * ENTER_MODE_SERVICE_LAMP = 213;//大灯
     * ENTER_MODE_SERVICE_INJECTOR = 214;//喷油嘴
     * ENTER_MODE_SERVICE_AT = 215;//变速箱
     * ENTER_MODE_SERVICE_TIRE = 216;//轮胎尺寸
     * ENTER_MODE_SERVICE_RCMM = 217;//遥控钥匙
     * ENTER_MODE_SERVICE_SRS = 218;//气囊
     * ENTER_MODE_SERVICE_SEATS = 219;//座椅
     * ENTER_MODE_SERVICE_SUSPENSION=220;//悬挂
     * ENTER_MODE_AIRCONTROLDIG = 221;//空调诊断
     * ENTER_MODE_CODING = 3;//编码
     * ENTER_MODE_NEWENERGY = 4;//新能源
     * ENTER_MODE_OBDDETECT = 5;//环评检测
     * ENTER_MODE_TPMS = 6;//胎压系统
     * ENTER_MODE_TPMS_RETROFIT = 601;//胎压加装
     * ENTER_MODE_DIAGNOSIS_PREDETECT = 101;//车辆预检
     * ENTER_MODE_DIAGNOSIS_BXDETECT = 102;//保险检测
     * ENTER_MODE_DIAGNOSIS_OBD_SR_HOTKEY = 103,//OBD 扫描进入
     * ENTER_MODE_DIAGNOSIS_OBD_VI_HOTKEY = 104,//OBD 车辆信息进入
     * ENTER_MODE_DIAGNOSIS_OBD_IM_HOTKEY = 105,//OBD IM状态进入
     * ENTER_MODE_DIAGNOSIS_OBD_LD_HOTKEY = 106,//OBD 数据流进入
     * ENTER_MODE_DIAGNOSIS_OBD_FF_HOTKEY = 107,//OBD 冻结帧进入
     * ENTER_MODE_DIAGNOSIS_OBD_M6_HOTKEY = 108,//OBD MODE06进入
     * ENTER_MODE_DIAGNOSIS_OBD_CT_HOTKEY = 109,//OBD MODE08进入
     * ENTER_MODE_DIAGNOSIS_OBD_ML_HOTKEY = 110,//OBD MIL灯进入
     * ENTER_MODE_DIAGNOSIS_OBD_ED_HOTKEY = 111,//OBD 清码进入
     * ENTER_MODE_DIAGNOSIS_OBD_O2_HOTKEY = 112,//OBD O2传感器进入
     * ENTER_MODE_DIAGNOSIS_CLEARALLDTC = 120,//全系统清码进入
     * ENTER_MODE_DIAGNOSIS_AUTOVIN = 121,//自动定位进入
     * 说    明：获取诊断进入的模式
     * -----------------------------------------------------------------------------
     */
    public static int GetEntranceMode() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().entranceMode);
        return EDiagUtils.get().getModel().entranceMode;
    }


    /**
     * 设置系统的状态
     *
     * @param iItemIndex    系统组索引
     * @param iSubItemIndex 系统索引
     * @param iState        系统状态(见宏定义)
     */
    public static void SetItemsState(int objKey, int iItemIndex, int iSubItemIndex, int iState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), String.format("## SetItemsState: objKey= %d, iItemIndex= %d, iSubItemIndex= %d, iState= %d",
                objKey, iItemIndex, iSubItemIndex, iState));
        if (!CDispFrame.FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "不存在对象");
            return;
        }
        final CDispFrameBeanEvent beanEvent = CDispFrame.FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "对象为空");
            return;
        }
        if (beanEvent.getSysItems() == null || iItemIndex >= beanEvent.getSysItems().size() || iItemIndex < 0) {
            return;
        }
        CDispFrameBeanEvent.SysItem sysItem = beanEvent.getSysItems().get(iItemIndex);
        if (sysItem == null || iSubItemIndex >= sysItem.getSysSubItems().size() || iSubItemIndex < 0) {
            return;
        }
        CDispFrameBeanEvent.SysSubItem sysSubItem = sysItem.getSysSubItems().get(iSubItemIndex);
        if (sysSubItem == null) {
            return;
        }
        sysSubItem.setState(iState);
        sysSubItem.setPromptText("");
        boolean haveDTCS = iState >= CDispConstant.PageDiagnosticType.DF_ENUM_DTCNUM;
        if (beanEvent.isHaveDTCS() || haveDTCS) {
            beanEvent.setHaveDTCS(iItemIndex, iSubItemIndex, haveDTCS);
        }

        //通知界面刷新
        beanEvent.event_what = CDispFrameBeanEvent.NOTIFYNUM;
        DiagnoseEvent.mCDispFrameBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * -----------------------------------------------------------------------------
     * 功    能：通过网络获取车辆信息与ID
     * 参数说明：const string& 车辆的VIN码
     * 返 回 值：const string& 错误ID、车辆信息ID、厂家、品牌、车型、年款、排放、车辆类别、国别、发动机、
     * 排量、进气方式、燃油类型、变速器、档位数、助力类型、驱动方式
     * 说    明：返回值以特定的字符串形式返回，诊断开发用户解析字符串含义得到以上信息；
     * 错误ID含义：0 正常，1 VIN错误， 2 网络错误， 3 无信息；
     * 返回可能存在多组情况：
     * [VehicleInfo]\t\n
     * ErrId=0\t\n
     * InfoNum=2\t\n
     * \t\n
     * [VehicleInfo_0]\t\n
     * LevelId=CFV0314D0007\t\n
     * Manufacturers=一汽大众\t\n
     * Brand=大众\t\n
     * Models=高尔夫6\t\n
     * Year=2011\t\n
     * EmissionStandard=国4\t\n
     * VehicleType=轿车\t\n
     * VehicleAttributes=合资\t\n
     * EngineModel=CFB\t\n
     * Displacement=1.4\t\n
     * Induction=涡轮增压\t\n
     * FuelType=汽油\t\n
     * TransmissionType=自动\t\n
     * GearNumber=\t\n
     * PowerSteering=\t\n
     * DriveMode=前轮驱动\t\n
     * \t\n
     * [VehicleInfo_1]\t\n
     * LevelId=CFV0314D0008\t\n
     * Manufacturers=一汽大众\t\n
     * Brand=大众\t\n
     * Models=高尔夫6\t\n
     * Year=2011\t\n
     * EmissionStandard=国4\t\n
     * VehicleType=轿车\t\n
     * VehicleAttributes=合资\t\n
     * EngineModel=CFB\t\n
     * Displacement=1.4\t\n
     * Induction=涡轮增压\t\n
     * FuelType=汽油\t\n
     * TransmissionType=自动\t\n
     * GearNumber=\t\n
     * PowerSteering=\t\n
     * DriveMode=前轮驱动\t\n
     * -----------------------------------------------------------------------------
     */
    public static String GetVehicleInfoByNet(String strVin) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strVin);
        return "";
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：解压文件
     * 参数说明：strPath -- zip所在目录
     * strZipFile -- zip文件名称
     * strUnzipFile -- 需要解压的文件在zip中的相对路径
     * 返回值：int  0 -- 失败  1 -- 成功
     * 说明：无
     * -----------------------------------------------------------------------------
     **/
    public static int UnzipFile(String strPath, String strZipFile, String strUnzipFile) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strPath + " & " + strZipFile + " & " + strUnzipFile);
        int iReturn = 0;

        try {
            ZipFile inZip = new ZipFile(strPath + strZipFile);
            ZipEntry zipEntry = inZip.getEntry(strUnzipFile);
            if (zipEntry != null) {
                if (zipEntry.isDirectory() || zipEntry.getSize() == 0) {
                    Enumeration<?> entries = inZip.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry element = ((ZipEntry) entries.nextElement());
                        if (!element.getName().contains(strUnzipFile)) {
                            continue;
                        }
                        String szName = element.getName();
                        if (element.isDirectory()) {
                            szName = szName.substring(0, szName.length() - 1);
                            File folder = new File(strPath + szName);
                            folder.mkdirs();
                        } else {
                            File file = new File(strPath + szName);
                            if (!file.exists()) {
                                file.getParentFile().mkdirs();
                            } else {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream out = new FileOutputStream(file);
                            InputStream in = new BufferedInputStream(inZip.getInputStream(element));
                            byte[] buffer = new byte[8192];
                            int len = -1;
                            while ((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                            CloseUtils.closeIO(in, out);
                        }
                    }
                    iReturn = 1;
                } else {
                    File file = new File(strPath + strUnzipFile);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    } else {
                        file.delete();
                    }
                    file.createNewFile();
                    OutputStream out = new FileOutputStream(file);
                    InputStream in = new BufferedInputStream(inZip.getInputStream(zipEntry));

                    byte[] buffer = new byte[8192];
                    int len = -1;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    CloseUtils.closeIO(in, out);
                    iReturn = 1;
                }
            }
            CloseUtils.closeIO(inZip);
        } catch (IOException e) {
            e.printStackTrace();
            iReturn = 0;
        }


        return iReturn;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：删除解压文件
     * 参数说明：strUnzipFilePath -- 路径，若是文件删除文件，是目录删除目录
     * 返回值：int  0 -- 失败  1 -- 成功
     * 说明：无
     * -----------------------------------------------------------------------------
     **/
    public static int DelUnzipFile(String strUnzipFilePath) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strUnzipFilePath);
        int iReturn = 0;
        if (TextUtils.isEmpty(strUnzipFilePath)) {
            return iReturn;
        }
        String root = SDUtils.getLocalPath();
        if (TextUtils.equals(strUnzipFilePath, root) || TextUtils.equals(strUnzipFilePath, root + PathVar.rootPath)) {
            return iReturn;
        }
        if (!FileUtils.isFileExists(strUnzipFilePath)) {
            iReturn = 1;
            return iReturn;
        }
        boolean isDel;
        if (FileUtils.isDir(strUnzipFilePath)) {
            isDel = FileUtils.deleteAllInDir(strUnzipFilePath);
        } else {
            isDel = FileUtils.delete(strUnzipFilePath);
        }
        iReturn = isDel ? 1 : 0;
        return iReturn;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置行驶里程数（KM）
     * 参数说明：iMileage 行驶里程
     * iLastMileage 上次保养里程
     * 返回值：无
     * 说明：无
     * -----------------------------------------------------------------------------
     **/
    public static void SetMileage(int iMileage, int iLastMileage) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), iMileage + " & " + iLastMileage);
        EDiagUtils.get().getModel().iMileage = iMileage;
        EDiagUtils.get().getModel().iLastMileage = iLastMileage;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：字符串编码转换
     * 参数说明：strCodeType - 源字符传的编码类型
     * byteString - 源字符串
     * strTargetCodeType - 目标字符串的编码类型，默认是UTF-8
     * 返回值：转换编码后字符串
     * 说   明：该函数用于根据字符串的源编码对源字符串进行目标编码的转换
     * -----------------------------------------------------------------------------
     **/
    public static String StringChangeByCodeType(String strCodeType, byte[] byteString, String strTargetCodeType) throws UnsupportedEncodingException {
        String strResult = "N/A";
        String strOrg = new String(byteString, strCodeType);
        strResult = new String(strOrg.getBytes(strTargetCodeType), strTargetCodeType);
        return strResult;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：获取进入的编码
     * 参数说明：无
     * 返回值：string 编码 ，由品牌id、车型id、年份id、功能id共7个字节组成
     * 说明：该函数用于获取诊断进入路径与功能的编码；主要适用于A1PRO的刷隐藏；例如F06A0004170002。
     * -----------------------------------------------------------------------------
     */
    public static String GetEntranceCode() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().goodsId);
        //由品牌id、车型id、年份id、功能id共7个字节组成
        return EDiagUtils.get().getModel().goodsId;
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：下载需要的文件
     * 参数说明：strPathFile 文件在包的相对路径，如.icon/Demo.png
     * 返回值：int 接口返回的状态，0为成功，1为网络异常，2为文件缺失
     * 说明:该函数用于下载需要的文件
     * -----------------------------------------------------------------------------
     */
    public static int DownLoadFile(String strPathFile) {
        int iResult = 0;
        //实现需要补充
        String url = String.format(PathVar.DownloadPath,
                EDiagUtils.get().getModel().appType,
                EDiagUtils.get().getModel().vehModel,
                EDiagUtils.get().getModel().versionId,
                EDiagUtils.get().getModel().vehModel,
                strPathFile);
        String localPath = SDUtils.getLocalPath() + PathVar.codingPath + EDiagUtils.get().getModel().vehModel + File.separator + strPathFile;
        if (FileUtils.isFileExists(localPath) && FileUtils.getFileLength(localPath) > 0) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "-- CDispGlobal DownLoadFile in Local -> " + strPathFile);
            return iResult;
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "-- CDispGlobal DownLoadFile FileName -> " + strPathFile);
        FileUtils.createOrExistsFile(localPath);
//        DownloadPresenter.get().download(url, localPath, UIUtils.getString(R.string.file_loading), true);
//        while (DownloadPresenter.get().getDownloadStatus(url) == -1) {
//            SystemClock.sleep(200);
//        }
//        if (!FileUtils.isFileExists(localPath)) {
//            SystemClock.sleep(200);
//        } else {
//            iResult = DownloadPresenter.get().getDownloadStatus(url);
//        }
        if (FileUtils.getFileLength(localPath) == 0) {
            iResult = 2;
        } else {
            ELogUtils.d(" DownLoad File Complete -> " + strPathFile + "  && isFileExists : " + FileUtils.isFileExists(localPath));
        }
        return iResult;
    }


    /**
     * -----------------------------------------------------------------------------
     * 功能：获取刷隐藏的授权码
     * 参数说明：
     * 返回值：string 授权码
     * 说明:该函数用于获取刷隐藏的授权码,主要用在A1PRO产品上
     * -----------------------------------------------------------------------------
     */
    public static String GetLicensesOfCoding() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        //来自后台的授权码
//        CodingLicensesPresenter.get().getNetLicenses(JNIConstant.GoodsId);
//        while (CodingLicensesPresenter.get().isLoading()) {
//            SystemClock.sleep(200);
//        }
//        return CodingLicensesPresenter.get().getLicenses();
        return "";
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：获取算法值
     * 参数说明：iKey 算法代号
     * vParameter 参数
     * 返回值：string 算法值
     * 说明:该函数用于获取算法值，参数定义根据不同算法不同
     * -----------------------------------------------------------------------------
     */
    public static String GetAlgorithmValueByParameter(int iKey, Object[] vParameter) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), vParameter);

        StringBuilder sb = new StringBuilder();
        int length = vParameter.length;
        for (int i = 0; i < length; i++) {
            sb.append(vParameter[i]);
            if (i < length - 1) {
                sb.append(",");
            }
        }
        return "";
//        CodingAlgorithmPresenter.get().request(iKey, sb.toString());
//        while (CodingAlgorithmPresenter.get().isLoading()) {
//            SystemClock.sleep(200);
//        }
//        return CodingAlgorithmPresenter.get().getAlgorithm();
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置车辆名称
     * 参数说明：strCarNameId 车辆名称id（4个字符，例如：0004）
     * strCarName 车辆名称
     * 返回值：无
     * 说明:该函数用于设置车辆名称，主要针对哪些需要自定定位车型的品牌
     * -----------------------------------------------------------------------------
     */
    public static void SetCarName(String strCarNameId, String strCarName) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "-- CDispGlobal DownLoadFile 方法strCarNameId: " + strCarNameId + " strCarName: " + strCarName);
        //实现需要补充

    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置车辆年份
     * 参数说明：strCarYearId 车辆年份id（2个字符，例如：21）
     * strCarYear 车辆年份
     * 返回值：无
     * 说明:该函数用于设置车辆年份，主要针对哪些需要自定定位车型的品牌
     * -----------------------------------------------------------------------------
     */
    public static void SetCarYear(String strCarYearId, String strCarYear) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "-- CDispGlobal DownLoadFile 方法strCarYearId: " + strCarYearId + " strCarYear: " + strCarYear);
        //实现需要补充

    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置车辆支持的功能元
     * 参数说明：vMetaFunctionId 车辆支持的功能id集合（id为14个字符，例如：F06A0004170002）
     * 返回值：无
     * 说明:该函数用于设置车辆支持的功能元，应用层筛选支持的功能列表
     * -----------------------------------------------------------------------------
     */
    public static void SetMetaFunctions(Object[] vMetaFunctionId) {
        ELogUtils.d(vMetaFunctionId);
        Set<String> MetaFunctions = new HashSet<>();
        for (Object o : vMetaFunctionId) {
            if (o instanceof String) {
                MetaFunctions.add((String) o);
            }
        }

    }

    /**
     * 功能：获取车型ID 参数说明： 返回值：string 车型ID 如：0001 说明:该函数用于车型Id,主要用在PDI-Check产品上
     */
    public static String GetCarNameId() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().carNameId);
        return EDiagUtils.get().getModel().carNameId;
    }

    /**
     * 功能：获取车型年份ID 参数说明： 返回值：string 车型年份ID 如：22 说明:该函数用于车型年份Id,主要用在PDI-Check产品上
     */
    public static String GetCarYearId() {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), EDiagUtils.get().getModel().carYearId);
        return EDiagUtils.get().getModel().carYearId;
    }

    /**
     * 功能：获取车辆功能元，主要用来做PDI-Check的检测计划获取
     * 参数说明：无
     * 返回值：vector<string>容器 （id为14个字符，例如：F07A0001220101）
     * 说明:该函数用于获取车辆功能元，应用层的功能列表，主要用来做PDI-Check的检测计划获取。
     * F07A为品牌、0001为车型730S、22为年份2022、01为整车电控模块、01为故障代码检测
     */
    public static Object[] GetMetaFunctions() {
        String[] strings = PDIDatabase.get().getCheckPlanDao().loadPlanFun();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strings);
        return strings;
    }
}
