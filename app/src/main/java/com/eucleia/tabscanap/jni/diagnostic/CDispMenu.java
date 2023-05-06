package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.tabscanap.bean.diag.CDispMenuBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispMenu
 * 作者 :   sen
 * 时间 :  上午9:54 2017/3/6.
 * 描述 :  jni 中间层 - 菜单界面
 */

public class CDispMenu {

    /*
     *   全局对象引用管理者
     */
    public static Map<Integer, CDispMenuBeanEvent> MENU_MAP_EVENT = new ConcurrentHashMap<>();

    private static Map<String, List<String>> clickMapLeft = new HashMap<>();
    private static Map<String, List<String>> clickMapRight = new HashMap<>();

    /**
     * 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    public static void addClickMap(String key, int nType, String value) {
        List<String> list = new ArrayList<>();
        if (nType == CDispConstant.PageDisp.DISP_RIGHT) {
            if (clickMapRight.containsKey(key)) {
                list = clickMapRight.get(key);
            }
            if (!list.contains(value)) {
                list.add(value);
            }
            clickMapRight.put(key, list);
        } else {
            if (clickMapLeft.containsKey(key)) {
                list = clickMapLeft.get(key);
            }
            if (!list.contains(value)) {
                list.add(value);
            }
            clickMapLeft.put(key, list);
        }
    }

    public static List<String> getClickMap(String key, int nType) {
        List<String> strings;
        if (nType == CDispConstant.PageDisp.DISP_RIGHT) {
            strings = clickMapRight.get(key);
        } else {
            strings = clickMapLeft.get(key);
        }
        return strings;
    }

    public static void clearClickMap(String key, int nType) {
        if (nType == CDispConstant.PageDisp.DISP_RIGHT) {
            if (clickMapRight != null) {
                clickMapRight.remove(key);
            }
        } else {
            if (clickMapLeft != null) {
                clickMapLeft.remove(key);
            }
        }
    }

    public static void clearAllClickMapRight() {
        if (clickMapRight != null) {
            clickMapRight.clear();
        }
    }

    public static void clearAllClickMap() {
        if (clickMapLeft != null) {
            clickMapLeft.clear();
        }
        if (clickMapRight != null) {
            clickMapRight.clear();
        }
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispMenuBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispMenuBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu setData 方法:" + objKey);
        MENU_MAP_EVENT.put(objKey, new CDispMenuBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu resetData 方法:" + objKey);

        if (MENU_MAP_EVENT.containsKey(objKey)) {
            MENU_MAP_EVENT.remove(objKey);
        }

//        // 释放界面
        //sendCmd(CDispMsgBoxBeanEvent.RESET,null);


    }

    /**
     * 初始化
     *
     * @param objKey       对象标志
     * @param strTitleText 标题文本
     * @param nType        界面类型:左/右
     */
    public static void InitData(int objKey, String strTitleText, int nType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu InitData 方法:" + strTitleText + " " + nType);
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.reSetInitData();
                bean.setTitle(strTitleText);
                bean.setDispType(nType);
            }
        }
    }

    /**
     * 菜单选择项增加一项
     *
     * @param str str要增加的文本
     */
    public static void AddItems(int objKey, String str) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu AddItems 方法:" + str);
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.addMenuItem(new CDispMenuBeanEvent.MenuItem(str));
            }
        }
    }

    /**
     * 菜单添加图片
     *
     * @param nItem   要添加图片菜单项的id
     * @param strIcon 图片文件
     */
    public static void SetIcon(int objKey, int nItem, String strIcon) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu SetIcon 方法:" + nItem + " " + strIcon);
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null && bean.getMenuItems() != null && nItem < bean.getMenuItems().size() && nItem > -1) {
                bean.getMenuItems().get(nItem).setMenu_url(strIcon);//获取Item,设置图片
                bean.setHasImage(true);
            }
        }
    }

    /**
     * 设置菜单帮助文档(帮助按钮一般隐藏,调用该函数且文档(即参数)非空,则按钮显示)
     *
     * @param strHelpText 帮助文档
     */
    public static void SetHelp(int objKey, String strHelpText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu SetHelp 方法:" + strHelpText);
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setHelpStr(strHelpText);//获取Item,设置图片
            }
        }
    }

    /**
     * 设置菜单是否排序
     *
     * @param bSort true 排序;false 不排序
     */
    public static void SetSort(int objKey, boolean bSort) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu SetSort 方法:" + bSort);
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setSort(bSort);//设置是否排序
            }

        }
    }

    /**
     * 显示
     *
     * @return 用户按得键
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispMenu Show 方法:");
        if (MENU_MAP_EVENT.containsKey(objKey)) {
            CDispMenuBeanEvent bean = MENU_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setObjKey(objKey);
                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_Menu);
                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }

                //菜单显示前的点击记录更正
                List<String> strings = getClickMap(bean.getTitle() + bean.getMenuItems().size(), bean.getDispType());
                for (int i = 0; i < bean.getMenuItems().size(); i++) {
                    if (strings != null
                            && strings.contains(bean.getMenuItems().get(i).menu_text + bean.getMenuItems().get(i).DF_FreeBtn_ID)) {
                        bean.getMenuItems().get(i).setClicked(true);
                    }
                }

                sendCmd(CDispMenuBeanEvent.SHOW, bean);

                bean.lockAndWait();

                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }


}
