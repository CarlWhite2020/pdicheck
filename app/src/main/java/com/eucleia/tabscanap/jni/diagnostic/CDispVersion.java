package com.eucleia.tabscanap.jni.diagnostic;


import com.eucleia.tabscanap.bean.diag.CDispVersionBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispVersion
 * 作者 :   sen
 * 时间 :  上午11:49 2017/3/6.
 * 描述 :  jni 中间层 - 版本信息(电脑信息)
 */
public class CDispVersion {

    /*
     *   全局对象引用管理者
     */
    public static Map<Integer, CDispVersionBeanEvent> VERSION_MAP_EVENT = new ConcurrentHashMap<>();

    /**
     * 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispVersionBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispVersionBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispVersion setData 方法:" + objKey);
        VERSION_MAP_EVENT.put(objKey, new CDispVersionBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispVersion resetData 方法:" + objKey);

        if (VERSION_MAP_EVENT.containsKey(objKey)) {
            VERSION_MAP_EVENT.remove(objKey);
        }

    }

    /**
     * 初始化版本信息界面,2列比例50:50
     *
     * @param strCaption 标题文本
     * @param nDispType  界面显示左右
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        if (VERSION_MAP_EVENT.containsKey(objKey)) {
            CDispVersionBeanEvent bean = VERSION_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setTextTitle(strCaption);
                bean.setDispType(nDispType);
            }

        }

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispVersion InitData方法:strCaption>>>" + strCaption);
    }

    /**
     * 设置分组名
     *
     * @param strGroupName 分组名文本
     */
    public static void AddGroupName(int objKey, String strGroupName) {
        if (VERSION_MAP_EVENT.containsKey(objKey)) {
            CDispVersionBeanEvent bean = VERSION_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.addTitle(EDiagUtils.get().getJniModel().systemName);
                CDispVersionBeanEvent.GroupItem item = bean.new GroupItem(new ArrayList<CDispVersionBeanEvent.Item>(), strGroupName);
                item.setSystemName(EDiagUtils.get().getJniModel().systemName);
                bean.addList(item);
            }
        }
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispVersion AddGroupName方法:strGroupName>>>" + strGroupName);
    }

    /**
     * 添加内容
     *
     * @param strName  名称
     * @param strValue 值
     */
    public static void AddItems(int objKey, String strName, String strValue) {
        if (VERSION_MAP_EVENT.containsKey(objKey)) {
            CDispVersionBeanEvent bean = VERSION_MAP_EVENT.get(objKey);
            if (bean != null) {
                int listSize = bean.getItemList().size();
                if (listSize == 0) {
                    CDispVersionBeanEvent.GroupItem item = bean.new GroupItem(new ArrayList<CDispVersionBeanEvent.Item>(), "");
                    listSize = 1;
                    bean.addList(item);
                }
                CDispVersionBeanEvent.Item item = bean.new Item(strName, strValue);
                bean.getItemList().get(listSize - 1).addItem(item);
            }

        }

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispVersion AddGroupName方法:AddItems>>>strName:" + strName + "strValue>>>" + strValue);
    }

    /**
     * 显示(模态(阻塞)的界面)
     *
     * @return 返回用户按的键(包含定义的返回值宏)
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"Show");
        if (VERSION_MAP_EVENT.containsKey(objKey)) {
            CDispVersionBeanEvent bean = VERSION_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setObjKey(objKey);
                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_Version);

                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }

                sendCmd(CDispVersionBeanEvent.SHOW, bean);
                bean.lockAndWait();

                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }

}
