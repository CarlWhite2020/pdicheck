package com.eucleia.tabscanap.jni.diagnostic;

import static java.lang.Thread.sleep;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispFrameBeanEvent;
import com.eucleia.tabscanap.bean.diag.child.DTC;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispFrame
 * 作者 :   <sen>
 * 时间 :  上午10:34 2017/3/6.
 * 描述 :  jni 中间层 - 诊断框架
 */
public class CDispFrame {

    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispFrameBeanEvent> FRAME_MAP_EVENT = new ConcurrentHashMap<>();

    /**
     * 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## setData: objKey= %d", objKey));
        FRAME_MAP_EVENT.put(objKey, new CDispFrameBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## resetData: objKey= %d", objKey));
        FRAME_MAP_EVENT.remove(objKey);
    }

    /**
     * 初始化车辆信息选择框
     *
     * @param strCaption
     */
    public static void InitData(int objKey, String strCaption) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData: objKey= %d, strCaption= %s", objKey, strCaption));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        // 调InitData初始化所有数据
        beanEvent.reSetInitData();
        // 初始化标题
        beanEvent.setStrCaption(strCaption);
        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_Frame);
    }

    /**
     * 设置帮助按钮是否显示
     *
     * @param bState true 显示;false 隐藏
     */
    public static void SetHelpDisplay(int objKey, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetHelpDisplay: objKey= %d, bState= %s", objKey, bState));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }
        beanEvent.setHelpEnabled(bState);
    }

    /**
     * 添加系统组
     *
     * @param strItem       系统组名称
     * @param vecStrSubItem 系统组的系统集合 String[]
     */
    public static void AddItems(int objKey, String strItem, Object[] vecStrSubItem) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## AddItems: objKey= %d, strItem= %s, vecStrSubItem= %s",
                objKey, strItem,JSON.toJSONString(vecStrSubItem)));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        // 组装数据
        CDispFrameBeanEvent.SysItem sysItem = new CDispFrameBeanEvent.SysItem().setSysName(strItem);

        int iUseNo = beanEvent.getTotalSystem();

        beanEvent.addSysItem(sysItem);
        for (Object o : vecStrSubItem) {
            iUseNo += 1;
            CDispFrameBeanEvent.SysSubItem item = new CDispFrameBeanEvent.SysSubItem().setSubName(String.valueOf(o));
            item.setNo(iUseNo);
            sysItem.addSysSubItem(item);
        }

        beanEvent.setTotalSystem(vecStrSubItem.length);
    }

    /**
     * 设置系统的状态
     *
     * @param iItemIndex    系统组索引
     * @param iSubItemIndex 系统索引
     * @param iState        系统状态(见宏定义)
     */
    public static void SetItemsState(int objKey, int iItemIndex, int iSubItemIndex, int iState) throws InterruptedException {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsState: objKey= %d, iItemIndex= %d, iSubItemIndex= %d, iState= %d",
                objKey, iItemIndex, iSubItemIndex, iState));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        final CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
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

        //非扫描情况，且当前是右边显示，则刷新界面 by cailei 2017-8-23
        if (beanEvent.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_END && beanEvent.isRightVisible()) {
            sendCmd(CDispFrameBeanEvent.SHOW, beanEvent);
            sleep(100);
        }
    }

    /**
     * -----------------------------------------------------------------------------
     * 功    能：设置系统的状态
     * 参数说明：iItemIndex 系统组索引
     * iSubItemIndex 系统索引
     * vvDtcInfo 系统故障码信息集合 -- （代码 状态 描述(含小码) 帮助）
     * 返 回 值：无
     * 说    明：string[][] vvDtcInfo结构格式如下：
     * string[]中压入4个字符串（故障代码、故障状态、故障描述(含小码)、故障帮助）
     * string[][]中压入上边的string[]
     * -----------------------------------------------------------------------------
     */
    public static void SetItemsDtcInfo(int objKey, int iItemIndex, int iSubItemIndex, Object[] vvDtcInfo) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsDtcInfo: objKey= %d, iItemIndex= %d, iSubItemIndex= %d, vvDtcInfo= %s",
                objKey, iItemIndex, iSubItemIndex, vvDtcInfo.toString()));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsDtcInfo 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        final CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsDtcInfo 方法: 下标为 %{d} 的对象为空"));
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
        List<DTC> reportItemBean = new ArrayList<>();
        for (int i = 0; i < vvDtcInfo.length; i++) {
            String objStr = JSON.toJSONString(vvDtcInfo[i]);
            List<String> strs = JSON.parseArray(objStr, String.class);
            DTC bean = new DTC();
            bean.setTroubleCode(strs.size()>0 ? strs.get(0) : " - ");
            bean.setTroubleStatus(strs.size()>1 ? strs.get(1) : " - ");
            bean.setTroubleDesc(strs.size()>2 ? strs.get(2) : " - ");
            bean.setTroubleSystemNo(i + 1);
            bean.setTroubleSystemName(sysSubItem.getSubName());
            reportItemBean.add(bean);
        }
        sysSubItem.setHaveRepairTroubleBeans(reportItemBean);
    }

    /**
     * 设置系统正在扫描和提示文本
     *
     * @param iItemIndex    系统组索引
     * @param iSubItemIndex 系统索引
     * @param strPromptText 提示文本内容
     */
    public static void SetItemsScan(int objKey, int iItemIndex, int iSubItemIndex, String strPromptText) throws InterruptedException {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsScan: objKey= %d, iItemIndex= %d, iSubItemIndex= %d, strPromptText= %s",
                objKey, iItemIndex, iSubItemIndex, strPromptText));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsScan 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        final CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsScan 方法: 下标为 %{d} 的对象为空"));
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
        sysSubItem.setPromptText(strPromptText);

        // 扫描进度
        if (beanEvent.getScanState() != CDispConstant.PageDiagnosticType.DF_SCAN_END) {
            sysItem.setProgress(Float.valueOf(sysSubItem.getNo()) / Float.valueOf(beanEvent.getTotalSystem()) * 100.0f);
            beanEvent.setProgress(Float.valueOf(sysSubItem.getNo()) / Float.valueOf(beanEvent.getTotalSystem()) * 100.0f);
        }

        sendCmd(CDispFrameBeanEvent.SHOW, beanEvent);
        sleep(100);
    }

    /**
     * 设置系统名称
     *
     * @param iItemIndex    系统组索引
     * @param iSubItemIndex 系统索引
     * @param strSysName 系统名称
     */
    public static void SetItemsSysName(int objKey, int iItemIndex, int iSubItemIndex, String strSysName){
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsSysName: objKey= %d, iItemIndex= %d, iSubItemIndex= %d, strPromptText= %s",
                objKey, iItemIndex, iSubItemIndex, strSysName));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsSysName 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        final CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsSysName 方法: 下标为 %{d} 的对象为空"));
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
        sysSubItem.setSubName(strSysName);
    }

    /**
     * 设置扫描界面状态(模态(阻塞)/非模态(非阻塞))
     *
     * @param iScanState DF_SCAN_START 非模态(非阻塞)、DF_SCAN_END(模态(阻塞))
     */
    public static void SetScanState(int objKey, int iScanState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetScanState: objKey= %d, iScanState= %d",
                objKey, iScanState));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        //判断是否是清码完成,或者是扫描完成
        if ((beanEvent.isClearCoding() || !beanEvent.isPauseScan())
                && beanEvent.getScanState() == CDispConstant.PageDiagnosticType.DF_SCAN_START
                && iScanState == CDispConstant.PageDiagnosticType.DF_SCAN_END) {
            beanEvent.setProgress(100.0f);
        }

        //设置界面状态
        beanEvent.setScanState(iScanState);
    }

    /**
     * 设置顶层功能区,显示与隐藏
     *
     * @param iStateFlag 功能状态表示、值为以下的组合
     *                   DF_FUN_RVER|DF_FUN_RDTC|DF_FUN_CDTC|DF_FUN_RDS|DF_FUN_ACTTEST|DF_FUN_SPECFUN
     *                   固定5个模块:电脑信息、读故障码、清故障码、数据流、动作测试、特殊功能
     */
    public static void SetTopFunState(int objKey, int iStateFlag) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetTopFunState: objKey= %d, iStateFlag= %d",
                objKey, iStateFlag));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        beanEvent.setFunState(iStateFlag);
    }

    /**
     * 设置左边系统桌布与顶层功能区,显示与隐藏
     *
     * @param bState true 显示;false 隐藏
     */
    public static void SetSysFunDisplay(int objKey, boolean bState) throws InterruptedException {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetSysFunDisplay: objKey= %d, bState= %s",
                objKey, bState));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }
        EDiagUtils.get().getJniModel().isFunSystemDisp = bState;
    }

    /**
     * 设置右侧桌布界面显示
     *
     * @param bVisible true 显示;false 隐藏
     */
    public static void SetRightVisible(int objKey, boolean bVisible) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetRightVisible: objKey= %d, bVisible= %s",
                objKey, bVisible));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        beanEvent.setRightVisible(bVisible);
    }

    /**
     * 设置按钮栏是否隐藏
     * cailei by 2017-7-4
     *
     * @param bVisible true 显示;false 隐藏
     */
    public static void SetButtonBarVisible(int objKey, boolean bVisible) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetButtonBarVisible: objKey= %d, bVisible= %s",
                objKey, bVisible));
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 不存在下标为 %{d} 的对象"));
            return;
        }
        CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData 方法: 下标为 %{d} 的对象为空"));
            return;
        }

        beanEvent.setButtonBarVisible(bVisible);
    }

    /**
     * 显示
     *
     * @return 返回用户按的键(包含定义的返回值宏)
     */
    public static int Show(int objKey) throws InterruptedException {
        if (!FRAME_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"## Show 方法:" + String.format("不存在下标为 %{d} 的对象"));
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## Show: objKey= %d",
                    objKey));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        final CDispFrameBeanEvent beanEvent = FRAME_MAP_EVENT.get(objKey);

        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"## Show 方法:" + String.format("下标为 %{d} 的对象为空"));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        beanEvent.setObjKey(objKey);

        // 记录路径
        IsExcuteShow = true;

        if (EDiagUtils.get().isThreadEnd()) {
            beanEvent.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            beanEvent.lockAndSignalAll();
            IsExcuteShow = false;
            return beanEvent.getBackFlag();
        }

        // 扫描结束
        if (beanEvent.getScanState() != CDispConstant.PageDiagnosticType.DF_SCAN_END) {
            sendCmd(CDispFrameBeanEvent.SHOW, beanEvent);
            sleep(100);
        } else {
            sendCmd(CDispFrameBeanEvent.SHOW, beanEvent);
            beanEvent.lockAndWait();
        }

        return beanEvent.getBackFlag();
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispFrameBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispFrameBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

    

}
