package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.tabscanap.bean.diag.CDispReportBeanEvent;
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
 * 文件名:  CDispReport
 * 作者 :   sen
 * 时间 :  上午11:33 2017/3/6.
 * 描述 :  jni 中间层 - 诊断报告
 */

public class CDispReport {


    /*
     *   全局对象引用管理者
     */
    public static Map<Integer, CDispReportBeanEvent> REPORT_MAP_EVENT = new ConcurrentHashMap<>();

    /**
     * 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispReportBeanEvent beanEvent) {
        beanEvent.event_what = what;
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--CDispReport发送命令:CDispReportBeanEvent");
        DiagnoseEvent.mCDispReportBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport setData 方法:" + objKey);
        REPORT_MAP_EVENT.put(objKey, new CDispReportBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport resetData 方法:" + objKey);

        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            REPORT_MAP_EVENT.remove(objKey);
        }
    }

    /**
     * 初始化诊断报告
     *
     * @param strCaption 标题文本
     * @param nDispType  界面显示左右
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport InitData 方法:" + strCaption + " " + nDispType);
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.reSetData();
                bean.setTitle(strCaption);
                bean.setDispType(nDispType);
            }
        }
    }

    /**
     * 设置故障总数
     *
     * @param iTotal 故障总数
     */
    public static void SetDTCTotal(int objKey, int iTotal) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport SetDTCTotal 方法:" + iTotal);
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setErrorCounts(iTotal);
            }
        }
    }

    /**
     * 设置列数与列宽
     *
     * @param vecColWidthPercent 列数与列宽,总和为100,如40,60为2列宽度为2:3
     */
    public static void SetColWidthPercent(int objKey, int[] vecColWidthPercent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport SetColWidthPercent 方法:" + objKey);
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                List<Integer> columnList = new ArrayList<>();
                List<String> columnListTitle = new ArrayList<>();
                for (int i = 0; i < vecColWidthPercent.length; i++) {
                    Integer num = (Integer) vecColWidthPercent[i];
                    columnList.add(num);
                    columnListTitle.add("");
                }
                bean.setTitltList(columnListTitle);
                bean.setColumnList(columnList);
            }
        }
    }

    /**
     * 添加表头
     *
     * @param vecStr vecStr 表头,数组大小同列数,顺序同列序
     */
    public static void SetHead(int objKey, Object[] vecStr) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport SetHead 方法:" + vecStr.toString());
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                List<String> columnList = new ArrayList<>();
                int sizeVec = vecStr.length;
                int sizePercent = bean.getColumnList().size();
                for (int i = 0; i < sizePercent; i++) {
                    String str = "";
                    if (i < sizeVec) {
                        str = (String) vecStr[i];
                    }
                    columnList.add(str);
                }
                bean.setTitltList(columnList);
            }
        }
    }

    /**
     * 添加一行
     *
     * @param vecStr 行内容,数组大小<= 列数,顺序同列序
     */
    public static void AddItems(int objKey, Object[] vecStr) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport AddItems 数组 方法:" + vecStr.toString());
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                List<String> columnList = new ArrayList<>();

                for (int i = 0; i < vecStr.length; i++) {
                    String str = (String) vecStr[i];
                    columnList.add(str);
                }
                CDispReportBeanEvent.Item item = bean.new Item();
                item.setContent(columnList);
                bean.getContentList().add(item);
                ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport AddItems 添加后行数:" + bean.getContentList().size());
            }
        }
    }

    /**
     * 添加一行
     *
     * @param strItem 行的首列,其他列为空
     */
    public static void AddItems(int objKey, String strItem) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport AddItems 添加单个一行数据的方法:strItem" + strItem);
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                List<String> columnList = new ArrayList<>();
                for (int i = 0; i < bean.getColumnList().size(); i++) {
                    if (i == 0) {
                        columnList.add(strItem);
                    } else {
                        columnList.add("");
                    }
                }
                CDispReportBeanEvent.Item item = bean.new Item();
                item.setContent(columnList);
                bean.getContentList().add(item);
            }
        }
    }

    /**
     * 修改某行的某列的值
     *
     * @param iItemIndex    行号
     * @param iSubItemIndex 列号
     * @param strCont       内容文本
     */
    public static void SetItems(int objKey, int iItemIndex, int iSubItemIndex, String strCont) {
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {
                //行数
                int rows = bean.getContentList().size();
                //列数
                int columns = bean.getColumnList().size();
                if (iItemIndex < rows && iSubItemIndex < columns && iItemIndex > -1 && iSubItemIndex > -1) {
                    //找到要修改的对象
                    CDispReportBeanEvent.Item item = bean.getContentList().get(iItemIndex);
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--赋值:行iItemIndex>>>" + iItemIndex + "列iSubItemIndex>>>" + iSubItemIndex + "值:" + strCont);
                    item.getContent().set(iSubItemIndex, strCont);
                } else {
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--行号或列号不符合要求:行iItemIndex>>>" + iItemIndex + "列iSubItemIndex>>>" + iSubItemIndex);
                }
            }
        }
    }

    /**
     * 添加某行的帮助文档
     *
     * @param iItemIndex 行号
     * @param strCont    帮助内容文本
     */
    public static void SetItemsHelp(int objKey, int iItemIndex, String strCont) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport SetItemsHelp 添加帮助文档的方法:strCont" + strCont);
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null && bean.getContentList() != null
                    && iItemIndex < bean.getContentList().size()
                    && iItemIndex > -1) {
                CDispReportBeanEvent.Item item = bean.getContentList().get(iItemIndex);
                item.setHelpStr(strCont);
                ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--添加帮助文档:行iItemIndex>>>" + iItemIndex + "内容>>>" + strCont);
            }
        }
    }


    /**
     * 显示(模态(阻塞)的界面)
     *
     * @return 返回用户按的键(包含定义的返回值宏)(帮助 、 冻结帧 、 搜索 、 无需返回值)
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispReport Show");
        if (REPORT_MAP_EVENT.containsKey(objKey)) {
            CDispReportBeanEvent bean = REPORT_MAP_EVENT.get(objKey);
            if (bean != null) {

                bean.setObjKey(objKey);

                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_Report);

                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }

                sendCmd(CDispReportBeanEvent.SHOW, bean);

                bean.lockAndWait();

                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }

}
