package com.eucleia.tabscanap.jni.diagnostic;

import android.graphics.Color;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.bean.normal.PdiGroup;
import com.eucleia.pdicheck.bean.normal.PdiItem;
import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.bean.diag.CDispPdiCheckBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.ResUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CDispPdiCheck {

    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispPdiCheckBeanEvent> PDICHECK_MAP_EVENT = new ConcurrentHashMap<>();

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey);
        CDispPdiCheckBeanEvent event = new CDispPdiCheckBeanEvent();
        event.setBrand(ResUtils.getString(R.string.wuling));
        event.setModel(EDiagUtils.get().getModel().model);
        event.setYear(EDiagUtils.get().getModel().year);
        event.setVin(EDiagUtils.get().getModel().vin);
        PDICHECK_MAP_EVENT.put(objKey, event);
        AppVM.get().postDataValue(objKey, PageType.Type_PdiCheck);
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            PDICHECK_MAP_EVENT.remove(objKey);
        }
    }

    /**
     * 功能：初始化PDI-Check界面
     * 参数说明：strCaption标题文本
     * 返回值：无 说明：无
     */
    public static void InitData(int objKey, String strCaption) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strCaption);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setStrCaption(strCaption);
            }
        }

    }

    /**
     * 功能：设置界面状态（同步与异步、报告是否生成与上传）
     * 参数说明：iScanState
     * DF_SCAN_START 异步,开始
     * DF_SCAN_END 同步，结束
     * DF_SCAN_END_OF_CANCEL 同步，结束不生成报告
     * DF_SCAN_END_OF_PAUSE 同步，暂停不生成报告
     * DF_SCAN_END_OF_RESULT 同步，结束生成报告与上传报告
     * 返回值：无
     * 说明：无
     */
    public static void SetScanState(int objKey, int iScanState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iScanState);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setScanState(iScanState);
            }
        }
    }

    /**
     * 功 能：设置进度条是否可用，默认不可用不显示
     * 参数说明：无
     * 返回值：bState true 进度条可用、显示 false 进度条不可用、不显示
     * 说明：无
     */
    public static void SetProgressBarState(int objKey, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + bState);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setProgressState(bState);
            }
        }
    }

    /**
     * 功 能：设置进度条比值
     * 参数说明：无
     * 返回值：iPercent
     * 当前进度值 iAllPercent
     * 进度总值 说明：无
     */
    public static void SetProgressPercent(int objKey, int iPercent, int iAllPercent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iPercent + " & " + iAllPercent);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setPercent(iPercent);
                bean.setAllPercent(iAllPercent);
            }
        }
    }

    /**
     * 功能：设置内容
     * 参数说明：strContext内容文本
     * 返回值：无
     * 说明：无
     */
    public static void SetContext(int objKey, String strContext, int uFormat, byte[] clrText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strContext + " & " + uFormat + " & " + JSON.toJSONString(clrText));
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setStrContext(strContext);
                bean.setFormat(uFormat);

                String col = String.format("%02x", clrText[0]);
                String col2 = String.format("%02x", clrText[1]);
                String col3 = String.format("%02x", clrText[2]);
                int color = Color.parseColor("#" + col + col2 + col3);
                bean.setColor(color);
            }
        }
    }

    /**
     * 功 能：设置分组名
     * 参数说明：strGroupName 分组名文本
     * 返回值：无
     * 说 明：无
     */
    public static void AddGroup(int objKey, String strGroupName, boolean isShow) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strGroupName + " & " + isShow);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup group = new PdiGroup();
                group.setGroupName(strGroupName);
                group.setShowGroup(isShow && !TextUtils.isEmpty(strGroupName));
                group.setShowChild(true);
                bean.putPdiGroup(group);
            }
        }
    }

    /**
     * 功能：添加内容
     * 参数说明：strName 名称
     * 返回值：无
     * 说明：无
     */
    public static void AddItemName(int objKey, String strName) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strName);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = new PdiItem();
                pdiItem.setItemName(strName);
                pdiItem.setGroupBold(!pdiGroup.isShowGroup());
                pdiGroup.putPdiItem(pdiItem);
            }
        }
    }

    /**
     * 功能：添加值
     * 参数说明：strValue 值
     * 返回值：无
     * 说明：无
     */
    public static void AddItemValue(int objKey, String strValue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strValue);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setItemValue(strValue);
                pdiItem.setShowGroup(false);
            }
        }
    }

    /**
     * 功能：添加值
     * 参数说明：strPartNumber 值
     * 返回值：无
     * 说明：无
     */
    public static void AddItemPartNumber(int objKey, String strPartNumber) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strPartNumber);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setItemPartNumber(strPartNumber);
                pdiItem.setShowGroup(true);
            }
        }
    }

    /**
     * 功能：添加值
     * 参数说明：strVersionNumber 值
     * 返回值：无
     * 说明：无
     */
    public static void AddItemVersionNumber(int objKey, String strVersionNumber) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + strVersionNumber);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setItemVersionNumber(strVersionNumber);
                pdiItem.setShowGroup(true);
            }
        }
    }

    /**
     * 功能：添加当前码
     * 参数说明：vecStrCurDtc 值
     * 返回值：无
     * 说明：无
     */
    public static void AddItemCurDtc(int objKey, Object[] vecStrCurDtc) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + JSON.toJSONString(vecStrCurDtc));
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setItemCurDtc(ArraysUtils.o2s(vecStrCurDtc));
                pdiItem.setShowGroup(true);
            }
        }
    }


    /**
     * 功能：添加历史码
     * 参数说明：vecStrHisDtc 值
     * 返回值：无
     * 说明：无
     */
    public static void AddItemHisDtc(int objKey, Object[] vecStrHisDtc) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + vecStrHisDtc);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setItemHisDtc(ArraysUtils.o2s(vecStrHisDtc));
                pdiItem.setShowGroup(true);
            }
        }
    }

    /**
     * 功能：添加子项结果
     * 参数说明：iResult 值（见检测项状态宏）
     * 返回值：无
     * 说明：无
     */
    public static void AddItemResult(int objKey, int iResult) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iResult);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                PdiItem pdiItem = pdiGroup.getPdiItem();
                pdiItem.setResult(iResult);
            }
        }
    }

    /**
     * 功能：添加分组结果
     * 参数说明：iResult 值（见检测项状态宏）
     * 返回值：无
     * 说明：无
     */
    public static void AddGroupResult(int objKey, int iResult) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iResult);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                PdiGroup pdiGroup = bean.getPdiGroup();
                pdiGroup.setNeedUpdate(true);
                pdiGroup.setResult(iResult);
                if (pdiGroup.isShowGroup()){
                    pdiGroup.setShowChild(false);
                }
                AppVM.get().postDataValue(objKey, PageType.Type_PdiCheck);
            }
        }
    }

    /**
     * 功能：设置总故障数
     * 参数说明：iDtcNum
     * 返回值：无
     * 说明：无
     */
    public static void SetAllDtcNum(int objKey, int iDtcNum) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iDtcNum);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setDtcNum(iDtcNum);
            }
        }
    }

    /**
     * 功能：设置检测结果
     * 参数说明：iResult 0表示不合格、1表示合格
     * 返回值：无
     * 说明：无
     */
    public static void SetResult(int objKey, int iResult) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey + " & " + iResult);
        if (PDICHECK_MAP_EVENT.containsKey(objKey)) {
            CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setResult(iResult);
            }
        }
    }

    /**
     * 功能：显示
     * 参数说明：无
     * 返回值：W_U32 返回用户按的键（包含上边定义的返回值宏）
     * 说 明：无
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey);
        if (!PDICHECK_MAP_EVENT.containsKey(objKey)) {
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        CDispPdiCheckBeanEvent bean = PDICHECK_MAP_EVENT.get(objKey);
        if (bean == null) {
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            bean.lockAndSignalAll();
            return bean.getBackFlag();
        }
        AppVM.get().postDataValue(objKey, PageType.Type_PdiCheck);
        if (bean.getScanState() != CDispConstant.PageDiagnosticType.DF_SCAN_START) {
            bean.lockAndWait();
        }
        return bean.getBackFlag();
    }
}
