package com.eucleia.tabscanap.jni.diagnostic;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispVehicleBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispVehicle
 * 作者 :   sen
 * 时间 :  上午10:18 2017/3/6.
 * 描述 :  jni 中间层 - 车辆选择框
 */

public class CDispVehicle {
    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispVehicleBeanEvent> VEHICLE_MAP_EVENT = new ConcurrentHashMap<>();

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
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"## setData 方法:" + objKey);
        VEHICLE_MAP_EVENT.put(objKey, new CDispVehicleBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"## resetData 方法:" + objKey);
        if (VEHICLE_MAP_EVENT.containsKey(objKey)) {
            VEHICLE_MAP_EVENT.remove(objKey);
        }

    }


    /**
     * 初始化车辆信息选择框
     *
     * @param strCaption 标题文本
     * @param strPrompt  提示文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, String strPrompt, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData\n%d,\n%s,\n%s,\n%d",
                objKey, strCaption, strCaption, nDispType));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## InitData\n下标为%d的对象为空", objKey));
            return;
        }

        // 填充数据
        beanEvent.setStrCaption(strCaption)
                .setStrPrompt(strPrompt)
                .setDispType(nDispType);
    }

    /**
     * 设置帮助文档(帮助按钮一般隐藏,调用该函数且文档(即参数)非空,则按钮显示)
     *
     * @param strHelpText 帮助文档
     */
    public static void SetHelp(int objKey, String strHelpText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetHelp\n%d,\n%s",
                objKey, strHelpText));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetHelp\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetHelp\n下标为%d的对象为空", objKey));
            return;
        }

        // 填充数据
        beanEvent.setStrHelpText(strHelpText);
    }

    /**
     * 设置菜单是否排序
     *
     * @param bSort true 排序;false 不排序
     */
    public static void SetSort(int objKey, boolean bSort) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetSort\n%d,\n%s",
                objKey, bSort));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetSort\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetSort\n下标为%d的对象为空", objKey));
            return;
        }

        // 填充数据
        beanEvent.setbSort(bSort);
    }

    /**
     * 添加一个选择项
     *
     * @param strItem    选择项文本
     * @param vecStrCont 备选项内容(即子项)
     */
    public static void AddItems(int objKey, String strItem, Object[] vecStrCont) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## AddItems\n%d,\n%s,\n%s",
                objKey, strItem, JSON.toJSONString(vecStrCont)));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## AddItems\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## AddItems\n下标为%d的对象为空", objKey));
            return;
        }

        // 创建车辆信息选择框
        CDispVehicleBeanEvent.VehicleItem item = new CDispVehicleBeanEvent.VehicleItem()
                .setStrItem(strItem)
                .setListStrCont(Arrays.asList(ArraysUtils.o2s(vecStrCont)));

        // 填充数据
        beanEvent.addVehicleItem(item);
    }

    /**
     * 设置指定选择项的备选项
     *
     * @param iItemIndex 选择项索引
     * @param vecStrCont 备选项内容
     */
    public static void SetItems(int objKey, int iItemIndex, Object[] vecStrCont) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItems\n%d,\n%d,\n%s",
                objKey, iItemIndex, JSON.toJSONString(vecStrCont)));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItems\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItems\n下标为%d的对象为空", objKey));
            return;
        }
        if (beanEvent.getVehicleItems() != null
                && iItemIndex < beanEvent.getVehicleItems().size()
                && iItemIndex > -1) {
            CDispVehicleBeanEvent.VehicleItem item = beanEvent.getVehicleItems().get(iItemIndex);
            item.setListStrCont(Arrays.asList(ArraysUtils.o2s(vecStrCont)));
        }
    }

    /**
     * 设置指定选择项的选择文本
     *
     * @param iItemIndex
     * @param strText
     */
    public static void SetItemsText(int objKey, int iItemIndex, String strText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsText\n%d,\n%d,\n%s",
                objKey, iItemIndex, strText));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsText\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsText\n下标为%d的对象为空", objKey));
            return;
        }
        if (beanEvent.getVehicleItems() != null
                && iItemIndex < beanEvent.getVehicleItems().size()
                && iItemIndex > -1) {
            CDispVehicleBeanEvent.VehicleItem item = beanEvent.getVehicleItems().get(iItemIndex);
            item.setStrSelectedText(strText);
        }
    }

    /**
     * 设置指定选择项的状态(可用与不可用)
     *
     * @param iItemIndex 选择项索引
     * @param bState     true 可用;false 不可用
     */
    public static void SetItemsState(int objKey, int iItemIndex, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsState\n%d,\n%d,\n%s",
                objKey, iItemIndex, bState));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsState\n不存在下标为%d的对象", objKey));
            return;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## SetItemsState\n下标为%d的对象为空", objKey));
            return;
        }
        if (beanEvent.getVehicleItems() != null
                && iItemIndex < beanEvent.getVehicleItems().size()
                && iItemIndex > -1) {
            CDispVehicleBeanEvent.VehicleItem item = beanEvent.getVehicleItems().get(iItemIndex);
            item.setbState(bState);
        }
    }

    /**
     * 显示
     *
     * @return 用户按得键(包含按钮 、 选择项 、 备选项 ; 除帮助文档按钮)
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## Show\n%d", objKey));
        if (!VEHICLE_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## Show\n不存在下标为%d的对象", objKey));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        CDispVehicleBeanEvent beanEvent = VEHICLE_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## Show\n下标为%d的对象为空", objKey));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }

        beanEvent.setObjKey(objKey);
        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_Vehicle);

        // 记录执行过show()方法,
        IsExcuteShow = true;

        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            beanEvent.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            beanEvent.lockAndSignalAll();
            IsExcuteShow = false;
            return beanEvent.getBackFlag();
        }

        sendCmd(CDispVehicleBeanEvent.SHOW, beanEvent);
        beanEvent.lockAndWait();

        return beanEvent.getBackFlag();
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispVehicleBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispVehicleBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

}
