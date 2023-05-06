package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.tabscanap.bean.diag.CDispTroubleBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispTrouble
 * 作者 :   sen
 * 时间 :  上午11:57 2017/3/6.
 * 描述 :  jni 中间层 - 故障码
 */

public class CDispTrouble {

    /*
     *   全局对象引用管理者
     */
    public static Map<Integer, CDispTroubleBeanEvent> MSG_TROUBLE_MAP_EVENT = new ConcurrentHashMap<>();

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
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble setData 方法:" + objKey);
        MSG_TROUBLE_MAP_EVENT.put(objKey, new CDispTroubleBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble resetData 方法:" + objKey);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            MSG_TROUBLE_MAP_EVENT.remove(objKey);
        }
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispTroubleBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispTroubleBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }


    /**
     * 初始化故障码界面,3列的一个列表,默认列宽20:20:60;
     * 当某一列全为空,则该列隐藏,并将该列宽度加给最后一列,(3列一次是:代码、状态、描述)
     *
     * @param strCaption 标题文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble InitData 方法:" + objKey + "," + strCaption + "," + nDispType);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.resetConstruct();
                bean.setCaption(strCaption);
                bean.setDispType(nDispType);
            }
        }
    }

    /**
     * 功能：添加某行的类型，用于故障界面显示炫酷
     * 参数说明：iItemIndex 行号
     * iItemType这行的故障类型标识，0表示历史码，1表示当前码，2表示判定码；默认值是1
     * 返回值：无
     * 说明：无
     */
    public static void SetItemsType(int objKey, int iItemIndex, int iItemType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble SetItemsType 方法:" + objKey + "," + iItemIndex + "," + iItemType);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getTroubleItems() != null
                        && iItemIndex < bean.getTroubleItems().size()
                        && iItemIndex > -1) {
                    bean.getTroubleItems().get(iItemIndex).iItemType = iItemType;
                }
            }
        }
    }

    /**
     * 添加表头
     *
     * @param strCode     码号
     * @param strState    状态
     * @param strDescribe 描述
     */
    public static void SetHead(int objKey, String strCode, String strState, String strDescribe) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble SetHead 方法:" + objKey + "," + strCode + "," + strState + "," + strDescribe);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.addTroubleItemTitle(new CDispTroubleBeanEvent.TroubleItemTitle(strCode, strState, strDescribe));
            }
        }
    }


    /**
     * 添加一行故障码
     *
     * @param strCode     码号
     * @param strState    状态
     * @param strDescribe 描述
     */
    public static void AddItems(int objKey, String strCode, String strState, String strDescribe) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble AddItems 方法:" + objKey + "," + strCode + "," + strState + "," + strDescribe);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setVisibleCodeColumn(strCode);
                bean.setVisibleStateColumn(strState);
                bean.setVisibleDescColumn(strDescribe);
                bean.setInsertToDB(true);
                CDispTroubleBeanEvent.TroubleItem item = new CDispTroubleBeanEvent.TroubleItem(strCode, strState, strDescribe);
                item.systemName = EDiagUtils.get().getJniModel().systemName;
                bean.addTroubleItem(item);
            }
        }
    }


    /**
     * 添加某行的帮助文档
     *
     * @param iItemIndex
     * @param strCont
     */
    public static void SetItemsHelp(int objKey, int iItemIndex, String strCont) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble SetItemsHelp 方法:" + objKey + "," + iItemIndex + "," + strCont);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getTroubleItems() != null
                        && iItemIndex < bean.getTroubleItems().size()
                        && iItemIndex > -1) {
                    bean.getTroubleItems().get(iItemIndex).help_text = strCont;
                }
            }
        }
    }

    /**
     * 添加某行的冻结帧按钮是否可用
     *
     * @param iItemIndex
     * @param bFZTrue
     */
    public static void SetItemsFreezeFrameState(int objKey, int iItemIndex, boolean bFZTrue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble SetItemsFreezeFrameState 方法:" + objKey + "," + iItemIndex + "," + bFZTrue);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getTroubleItems() != null
                        && iItemIndex < bean.getTroubleItems().size()
                        && iItemIndex > -1) {
                    bean.getTroubleItems().get(iItemIndex).isDjzTrue = bFZTrue;
                }
            }
        }
    }

    /**
     * 设置清码按钮状态
     *
     * @param bState
     */
    public static void SetClearDtcState(int objKey, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispTrouble SetClearDtcState 方法:" + objKey + "," + bState);
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setClearDTCStatus(bState);
            }
        }

    }

    /**
     * 显示(模态(阻塞)的界面)
     *
     * @return 用户按的键(帮助 、 冻结帧 、 搜索无需返回值)
     */
    public static int Show(int objKey) {
        if (MSG_TROUBLE_MAP_EVENT.containsKey(objKey)) {
            CDispTroubleBeanEvent bean = MSG_TROUBLE_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setObjKey(objKey);
                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_Trouble);

                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }

                // 通知逻辑层显示对话框
                sendCmd(CDispTroubleBeanEvent.SHOW, bean);

                // 阻塞线程
                bean.lockAndWait();

                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }


}
