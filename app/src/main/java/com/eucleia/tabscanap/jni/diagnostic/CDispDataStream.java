package com.eucleia.tabscanap.jni.diagnostic;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.eucleia.tabscanap.bean.diag.CDispDataStreamBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispDataStreamBeanEvent
 * 作者 :   sen
 * 时间 :  下午2:45 2017/3/6.
 * 描述 :  jni 中间层 - 数据流
 */

public class CDispDataStream {

    /**
     * 全局对象引用管理者
     */
    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispDataStreamBeanEvent> DATASTREAM_MAP_EVENT = new ConcurrentHashMap<>();
    private static boolean isDataStreamOpen;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        DATASTREAM_MAP_EVENT.put(objKey, new CDispDataStreamBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        DATASTREAM_MAP_EVENT.remove(objKey);
    }

    /**
     * 初始化 数据流界面,非模态(非阻塞),4列比例55:15:15:15,当某一列全是空,隐藏该列
     * 并且将该列的宽度加给首列,(4列依次是:名称、值、范围、单位)
     *
     * @param strCaption 标题文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            streamBeanEvent.reSetAllData();
            streamBeanEvent.setStrCaption(strCaption);
            streamBeanEvent.setDispType(nDispType);
        }

        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_DateStream);
    }

    /**
     * 设置进度条比值
     *
     * @param iPercent    当前进度值
     * @param iAllPercent 进度总值
     */
    public static void SetProgressPercent(int objKey, int iPercent, int iAllPercent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            streamBeanEvent.setiPercent(iPercent);
            streamBeanEvent.setiAllPercent(iAllPercent);
        }
    }

    /**
     * 添加表头
     *
     * @param strName  名称
     * @param strValue 值
     * @param strRange 范围
     * @param strUnit  单位
     */
    public static void SetHead(int objKey, String strName, String strValue, String strRange, String strUnit) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            CDispDataStreamBeanEvent.HeadItem headItem = new CDispDataStreamBeanEvent.HeadItem()
                    .setStrName(strName)
                    .setStrValue(strValue)
                    .setStrRange(strRange)
                    .setStrUnit(strUnit);

            streamBeanEvent.addHeadItems(headItem);
        }
    }

    /**
     * 添加一行数据流
     *
     * @param strName     名称
     * @param strUnit     单位
     * @param strValue    值
     * @param strMinValue 最小值
     * @param strMaxValue 最大值
     */
    public static void AddItems(int objKey, String strName, String strUnit, String strValue, String strMinValue, String strMaxValue) {
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (!StringUtils.isEmpty(strMinValue) || !StringUtils.isEmpty(strMaxValue)) {
                streamBeanEvent.setRangeEnable(true);
            }
            boolean isFirst = streamBeanEvent.addTitle(EDiagUtils.get().getJniModel().systemName);
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),strName + " extend " + EDiagUtils.get().getJniModel().systemName + " && " + isFirst);
            // 添加数据行
            CDispDataStreamBeanEvent.RowItem rowItem = new CDispDataStreamBeanEvent.RowItem()
                    .setStrName(strName)
                    .setStrUnit(strUnit)
                    .setStrValue(strValue)
                    .setStrMinValue(strMinValue)
                    .setStrMaxValue(strMaxValue)
                    .setSystemName(EDiagUtils.get().getJniModel().systemName)
                    .setFirst(isFirst);
            streamBeanEvent.addRowItems(rowItem);
        }
    }

    /**
     * 设置某一行的单位
     *
     * @param iItemIndex 行号
     * @param strUnit    单位
     */
    public static void SetUnit(int objKey, int iItemIndex, String strUnit) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                streamBeanEvent.getRowItems().get(iItemIndex).setStrUnit(strUnit);
            }
        }
    }

    /**
     * 设置某一行的范围
     *
     * @param iItemIndex
     * @param strMin
     * @param strMax
     */
    public static void SetRange(int objKey, int iItemIndex, String strMin, String strMax) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),EDiagUtils.get().getJniModel().systemName + " --> " + objKey);
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (!StringUtils.isEmpty(strMin) || !StringUtils.isEmpty(strMax)) {
                streamBeanEvent.setRangeEnable(true);
            }
            // 设置范围
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                streamBeanEvent.getRowItems().get(iItemIndex).setStrMinValue(strMin).setStrMaxValue(strMax);
            }
        }
    }

    /**
     * 设置某一行的帮助文档
     *
     * @param iItemIndex 行号
     * @param strHelp    帮助文档
     */
    public static void SetHelp(int objKey, int iItemIndex, String strHelp) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                streamBeanEvent.getRowItems().get(iItemIndex).setStrHelp(strHelp);
            }
        }
    }

    /**
     * 设置某一行的ID，用来做数据流记录标识用
     *
     * @param iItemIndex 行号
     * @param strItemId  数据流的ID
     */
    public static void SetItemId(int objKey, int iItemIndex, String strItemId) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),strItemId);
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                //以后添加使用
                CDispDataStreamBeanEvent.RowItem rowItem = streamBeanEvent.getRowItems().get(iItemIndex);
                rowItem.setSystemId(strItemId);
            }
        }
    }

    /**
     * 刷新某一行的值
     *
     * @param iItemIndex 行号
     * @param strValue   值
     */
    public static void FlashValue(int objKey, int iItemIndex, String strValue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            // 刷新值
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                streamBeanEvent.getRowItems().get(iItemIndex).setStrValue(strValue);
                streamBeanEvent.getRowItems().get(iItemIndex);
            }
        }
    }

    /**
     * 获取当前需要刷新行号的集合
     *
     * @return
     */
    public static int[] GetUpdataItems(int objKey) {
        int[] ints = new int[0];
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return new int[0];
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            List<Integer> integerList = streamBeanEvent.getRefreshRowNumber();
            ints = ArraysUtils.i2i(integerList);
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),JSON.toJSONString(ints));
        return ints;
    }

    /**
     * 取得某个选项是否要更新
     *
     * @param iItemIndex
     * @return
     */
    public static boolean GetItemsUpdata(int objKey, int iItemIndex) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return false;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            if (iItemIndex < streamBeanEvent.getRowItems().size() && iItemIndex > -1) {
                return streamBeanEvent.getRowItems().get(iItemIndex).isUpdate();
            }
        }
        return false;
    }

    /**
     * 设置下一步按钮是否显示
     *
     * @param bVisiable
     * @return
     */
    public static void SetNextButtonVisiable(int objKey, boolean bVisiable) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat());
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey)) return;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (streamBeanEvent != null) {
            streamBeanEvent.setbNextButtonVisiable(bVisiable);
        }
    }

    /**
     * 显示
     *
     * @return 返回用户按的键(包含定义的返回值宏), (其他功能均是界面层与中间层单独完成)
     */
    public static int Show(int objKey) throws InterruptedException {
        // [首先判断]判断线程状态 结束
        if (!DATASTREAM_MAP_EVENT.containsKey(objKey))
            return CDispConstant.PageButtonType.DF_ID_BACK;
        CDispDataStreamBeanEvent streamBeanEvent = DATASTREAM_MAP_EVENT.get(objKey);
        if (EDiagUtils.get().isThreadEnd()) {
            streamBeanEvent.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            streamBeanEvent.lockAndSignalAll();
            return streamBeanEvent.getBackFlag();
        }
        streamBeanEvent.setObjKey(objKey);
        sendCmd(CDispDataStreamBeanEvent.SHOW, streamBeanEvent);
        if (streamBeanEvent.isLockData()) {
            streamBeanEvent.lockAndWait();
        }
        return streamBeanEvent.getBackFlag();
    }

    private static Lock s_lock = new ReentrantLock();

    public static void dataStreamLock() {
        if (null != s_lock) {
            s_lock.lock();
        }
    }

    public static void dataStreamUnlock() {
        if (null != s_lock) {
            s_lock.unlock();
        }
    }


    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispDataStreamBeanEvent beanEvent) {
        dataStreamLock();
        if (!isDataStreamOpen) {
            beanEvent.event_what = what;
            DiagnoseEvent.mCDispDataStreamBeanEvent = beanEvent;
            EventBus.getDefault().post(beanEvent);
        }
        dataStreamUnlock();
    }

}
