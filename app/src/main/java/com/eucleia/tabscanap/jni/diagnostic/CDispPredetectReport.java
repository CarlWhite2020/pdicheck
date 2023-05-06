package com.eucleia.tabscanap.jni.diagnostic;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispPredetectBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
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
 * 文件名:  CDispPredetectReport
 * 作者 :   cailei
 * 时间 :  下午13:40 2020/6/11.
 * 描述 :  jni 中间层 - 预检报告
 */

public class CDispPredetectReport {

    private static CDispPredetectBeanEvent.GroupItem item;
    private static final String TAG = "CDispPredetectReport";

    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispPredetectBeanEvent> PREDETECT_REPORT_MAP_EVENT = new ConcurrentHashMap<>();

    // 判断是否调用过show()一次;
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport setData 方法:" + objKey);
        PREDETECT_REPORT_MAP_EVENT.put(objKey, new CDispPredetectBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport resetData 方法:" + objKey);
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
            PREDETECT_REPORT_MAP_EVENT.remove(objKey);
        }
    }

    /**
     * 初始化列表框界面,默认模态(阻塞)界面
     *
     * @param strCaption 标题文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport InitData 方法:" + objKey + "," + strCaption + "," + nDispType);
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispPredetectBeanEvent bean = PREDETECT_REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {

                    // 重置对象内容
                    bean.reSetData();
                    // 设置标题
                    bean.setItems(new ArrayList<CDispPredetectBeanEvent.GroupItem>());
                bean.setStrCaption(strCaption);
                // 设置显示在界面左/右
                bean.setDispType(nDispType);
            }
        }
    }

    /**
     * 设置分组名
     *
     * @param strGroupName 分组名文本
     */
    public static void AddGroupName(int objKey, String strGroupName) {
        Log.d(TAG, "AddGroupName: " + strGroupName);
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
                List<CDispPredetectBeanEvent.GroupItem> items = PREDETECT_REPORT_MAP_EVENT.get(objKey).getItems();
                for (CDispPredetectBeanEvent.GroupItem i : items) {
                    if (i.getStrGroupName().equals(strGroupName)) {
                        item = i;
                        break;
                    }
                }

        }
    }

    /**
     * 设置列数与列宽
     *
     * @param vecColWidthPercent 列数与列宽,总和为100,如40,60为2列 宽度为2:3
     */
    public static void SetColWidthPercent(int objKey, int[] vecColWidthPercent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport SetColWidthPercent 方法:" + objKey + "," + JSON.toJSONString(vecColWidthPercent));
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
                item.setVecColWidthPercent(vecColWidthPercent);
        }
    }

    /**
     * 添加表头
     *
     * @param vecStr 表头,数组大小同列数,顺序同列序
     */
    public static void SetHead(int objKey, Object[] vecStr) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport SetHead 方法:" + objKey + "," +JSON.toJSONString(vecStr));
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
                List<String> strList = new ArrayList<>();
                for (Object obj : vecStr) {
                    strList.add((String) obj);
                }
                item.setHeadList(strList);
        }
    }

    /**
     * 添加一行
     *
     * @param vecStr   行内容,数组大小<=列数,顺序同列序
     * @param iQualify 是否合格，0 - 异常，1 - 警示， 2 - 正常
     */
    public static void AddItems(int objKey, Object[] vecStr, int iQualify) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport AddItems 方法:" + objKey + "," + JSON.toJSONString(vecStr) + "," + iQualify);
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {



                List<CDispPredetectBeanEvent.ChildItem> itemList = item.getItemList();
                CDispPredetectBeanEvent.ChildItem item = new CDispPredetectBeanEvent.ChildItem();
                List<String> strList = new ArrayList<>();
                for (Object obj : vecStr) {
                    strList.add((String) obj);
                }
                item.setVecStr(strList);
                item.setiQualify(iQualify);
                itemList.add(item);


        }
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：添加当前分组的总结与建议
     * 参数说明：strDescription 列序总结建议内容
     * 返回值：无
     * 说明：无
     * -----------------------------------------------------------------------------
     */
    public static void AddSummary(int objKey, String strDescription) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispPredetectReport AddSummary 方法:" + objKey + "," + strDescription);
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispPredetectBeanEvent bean = PREDETECT_REPORT_MAP_EVENT.get(objKey);
            item.setStrDescription(strDescription);
            bean.getItems().add(item);

        }
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：显示
     * 参数说明：无
     * 返回值：W_U32 返回用户按的键（包含上边定义的返回值宏）
     * 说   明：每次调用显示，刷新界面
     * -----------------------------------------------------------------------------
     */
    public static int Show(int objKey) {
        if (PREDETECT_REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispPredetectBeanEvent beanEvent = PREDETECT_REPORT_MAP_EVENT.get(objKey);
            if (beanEvent != null) {

                beanEvent.setObjKey(objKey);

                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_Predetect);
                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    beanEvent.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    beanEvent.lockAndSignalAll();
                    IsExcuteShow = false;
                    return beanEvent.getBackFlag();
                }

                sendCmd(CDispPredetectBeanEvent.SHOW, beanEvent);
                beanEvent.lockAndWait();
                return beanEvent.getBackFlag();
            }

        }
        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }

    private static void sendCmd(int show, CDispPredetectBeanEvent beanEvent) {
        beanEvent.event_what = show;
        EventBus.getDefault().post(beanEvent);
    }

}
