package com.eucleia.tabscanap.jni.diagnostic;

import static java.lang.Thread.sleep;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispListBeanEvent;
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
 * 文件名:  CDispList
 * 作者 :   sen
 * 时间 :  下午2:04 2017/3/6.
 * 描述 :  jni 中间层 - 列表框
 */

public class CDispList {


    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispListBeanEvent> LIST_MAP_EVENT = new ConcurrentHashMap<>();

    // 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList setData 方法:" + objKey);
        LIST_MAP_EVENT.put(objKey, new CDispListBeanEvent(objKey));
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList resetData 方法:" + objKey);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            LIST_MAP_EVENT.remove(objKey);
            CDispListBeanEvent.getButtonItemListOld().clear();
        }
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispListBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispListBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }


    /**
     * 初始化列表框界面,默认模态(阻塞)界面
     *
     * @param strCaption 标题文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList InitData 方法:" + objKey + "," + strCaption + "," + nDispType);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                // 重置对象内容
                bean.reSetData();
                // 设置标题
                bean.setStrCaption(strCaption);
                // 设置显示在界面左/右
                bean.setDispType(nDispType);
            }
        }
    }

    /**
     * 功能：设置界面模态(阻塞)与非模态(非阻塞)
     * 参数说明：nTyte 默认是DF_LS_TYPE_ORIGINAL，标识列表框的显示类型
     * 返回值：无
     * 说明：无
     */
    public static void SetUIType(int objKey, int nTyte) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetUIType 方法:" + objKey + "," + nTyte);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setUIType(nTyte);
            }
        }
    }

    /**
     * 设置界面模态(阻塞)与非模态(非阻塞)
     *
     * @param bStatic true 为模态(阻塞)界面,即需要等待用户操作
     *                false 为非模态(非阻塞)界面,即不需要等待用户操作
     */
    public static void SetUIStatic(int objKey, boolean bStatic) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetUIStatic 方法:" + objKey + "," + bStatic);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                // 设置界面阻塞类型
                bean.setUIStatic(bStatic);
            }
        }

    }

    /**
     * 设置列数与列宽
     *
     * @param vecColWidthPercent 列数与列宽,总和为100,如40,60为2列 宽度为2:3
     */
    public static void SetColWidthPercent(int objKey, int[] vecColWidthPercent) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetColWidthPercent 方法:" + objKey + ",");
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setNeedRefreshHead(true);
                List<Integer> columnList = new ArrayList<>();
                List<String> columnListTitle = new ArrayList<>();
                for (int i = 0; i < vecColWidthPercent.length; i++) {
                    Integer num = (Integer) vecColWidthPercent[i];
                    columnList.add(num);
                    columnListTitle.add("");
                }
                bean.setTitlePercentList(columnList);
                bean.setTitltList(columnListTitle);
            }
        }
    }

    /**
     * 添加表头
     *
     * @param vecStr 表头,数组大小同列数,顺序同列序
     */
    public static void SetHead(int objKey, Object[] vecStr) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetHead 方法:" + objKey + "," + JSON.toJSONString(vecStr));
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshHead(true);
                List<String> columnList = new ArrayList<>();
                int iSize = vecStr.length;
                for (int i = 0; i < bean.getTitlePercentList().size(); i++) {
                    String str = "";
                    if (i < iSize) {
                        str = (String) vecStr[i];
                    }
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetHead 内容:" + str + "objKey>>>" + objKey);
                    columnList.add(str);
                }
                bean.setTitltList(columnList);
            }
        }
    }

    /**
     * 添加一行
     *
     * @param vecStr 行内容,数组大小<=列数,顺序同列序
     * @param bState true 锁定行,不参与行号计算;false 正常一行,参与行号计算
     */
    public static void AddItems(int objKey, Object[] vecStr, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList AddItems 方法:" + objKey + "," + JSON.toJSONString(vecStr) + "," + bState);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshList(true);
                List<String> columnList = new ArrayList<>();
                int iSize = vecStr.length;
                for (int i = 0; i < bean.getTitlePercentList().size(); i++) {
                    String str = "";
                    if (i < iSize) {
                        str = (String) vecStr[i];
                    }
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList AddItems 内容:" + str + "objKey>>>" + objKey);
                    columnList.add(str);
                }
                CDispListBeanEvent.Item item = bean.new Item();
                item.setRowLockFlag(bState);//设置当前行是否锁定
                item.setSingleRowContentList(columnList);
                if (bean.getUIType() == CDispConstant.PageListType.DF_LS_TYPE_IM) {
                    bean.addTitle(EDiagUtils.get().getJniModel().systemName);
                    item.setSystemName(EDiagUtils.get().getJniModel().systemName);
                }
                bean.getContentList().add(item);
            }
        }
    }

    /**
     * 添加一行
     *
     * @param strItem 行的首列有数据,其他列为空
     * @param bState  true 为锁定行,不参与行号计算;false 为正常行,参与行号计算
     */
    public static void AddItems(int objKey, String strItem, boolean bState) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList AddItems 2方法:" + objKey + "," + strItem.toString() + "," + bState);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshList(true);
                List<String> columnList = new ArrayList<>();
                for (int i = 0; i < bean.getTitlePercentList().size(); i++) {
                    if (i == 0) {
                        columnList.add(strItem);
                    } else {
                        columnList.add("");
                    }
                }
                CDispListBeanEvent.Item item = bean.new Item();
                item.setRowLockFlag(bState);//设置当前行是否锁定
                item.setSingleRowContentList(columnList);
                if (bean.getUIType() == CDispConstant.PageListType.DF_LS_TYPE_IM) {
                    bean.addTitle(EDiagUtils.get().getJniModel().systemName);
                    item.setSystemName(EDiagUtils.get().getJniModel().systemName);
                }
                bean.getContentList().add(item);
            }
        }
    }

    /**
     * 修改某行的某列,同时刷新界面数据
     *
     * @param iItemIndex    行号
     * @param iSubItemIndex 列号
     * @param strCont       文本
     */
    public static void SetItems(int objKey, int iItemIndex, int iSubItemIndex, String strCont) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetItems 方法:" + objKey + "," + iItemIndex + "," + iSubItemIndex + "," + strCont);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshList(true);
                //行数
                int rows = bean.getContentList().size();
                //列数
                int columns = bean.getTitltList().size();
                ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"cailei --lines rows:>>>" + rows + "   columns:>>>" + columns);
                if (iItemIndex < rows && iSubItemIndex < columns && iItemIndex > -1 && iSubItemIndex > -1) {
                    //找到要修改的对象
                    CDispListBeanEvent.Item item = bean.getContentList().get(iItemIndex);
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"cailei --lines item:>>>" + item.getSingleRowContentList().toString());
                    if (item.getSingleRowContentList().size() > iSubItemIndex) {
                        item.getSingleRowContentList().set(iSubItemIndex, strCont);
                    }
                } else {
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--行号或列号不符合要求:行iItemIndex>>>" + iItemIndex + "列iSubItemIndex>>>" + iSubItemIndex);
                }
            }
        }
    }

    /**
     * 获取选中一行行号
     *
     * @return 选中行号
     */
    public static int GetOneSelectIndex(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList GetOneSelectIndex 方法:" + objKey);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                return bean.getSelectedPosi();
            }
        }
        return 0;
    }

    /**
     * 获取当前屏的数据
     *
     * @param objKey
     * @return
     */
    public static int[] GetUpdataItems(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"GetUpdataItems");
        if (!LIST_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## GetUpdataItems 方法: 不存在下标为 %{d} 的对象"));
            return null;
        }
        CDispListBeanEvent beanEvent = LIST_MAP_EVENT.get(objKey);
        if (beanEvent == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("## GetUpdataItems 方法: 下标为 %{d} 的对象为空"));
            return null;
        } else {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"动作测试可见项数组:" + beanEvent.getVisibleItemIndexs());
            if (beanEvent.getVisibleItemIndexs() == null) {
                return new int[]{0, 1, 2, 3, 4, 5};
            }
            return beanEvent.getVisibleItemIndexs();
        }
    }

    /**
     * 添加按钮
     *
     * @param strButtonText 按钮文本
     * @return true 添加成功;false 添加失败
     */
    public static boolean AddButtonFree(int objKey, String strButtonText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList AddButtonFree 方法:" + objKey + "," + strButtonText);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshButton(true);
                if (bean.getButtonItemList() == null) {
                    bean.setButtonItemList(new ArrayList<CDispListBeanEvent.ButtonItem>());
                    CDispListBeanEvent.ButtonItem item = bean.new ButtonItem();
                    item.setText(strButtonText);
                    bean.getButtonItemList().add(item);
                } else {
                    List<CDispListBeanEvent.ButtonItem> list = bean.getButtonItemList();
                    CDispListBeanEvent.ButtonItem item = bean.new ButtonItem();
                    item.setText(strButtonText);
                    ELogUtils.e(EDiagUtils.get().isOpenLogCat(), "添加按钮:" + strButtonText);
                    list.add(item);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 修改添加按钮的文本
     *
     * @param iBtnIndex     按钮索引
     * @param strButtonText 按钮文本
     * @return true 修改成功;false 修改失败
     */
    public static boolean SetButtonText(int objKey, int iBtnIndex, String strButtonText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetButtonText 方法:" + objKey + "," + iBtnIndex + "," + strButtonText);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshButton(true);
                List<CDispListBeanEvent.ButtonItem> list = bean.getButtonItemList();
                if (list != null && iBtnIndex < list.size() && iBtnIndex > -1) {
                    list.get(iBtnIndex).setText(strButtonText);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 修改添加按钮的状态
     *
     * @param iBtnIndex 按钮索引
     * @param bEnable   按钮状态 true 可用;false 不可用
     * @return true 成功;false 失败
     */
    public static boolean SetButtonStatus(int objKey, int iBtnIndex, boolean bEnable) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList SetButtonStatus 方法:" + objKey + "," + iBtnIndex + "," + bEnable);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshButton(true);
                List<CDispListBeanEvent.ButtonItem> list = bean.getButtonItemList();
                if (list != null && iBtnIndex < list.size() && iBtnIndex > -1) {
                    list.get(iBtnIndex).setAvailable(bEnable);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 清空添加按钮
     *
     * @return true 成功;false 失败
     */
    public static boolean ClearButton(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList ClearButton 方法:" + objKey);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshButton(true);
                if (bean.getButtonItemList() != null && bean.getButtonItemList().size() > 0) {
                    bean.getButtonItemList().clear();

                    /*if (IsExcuteShow) {
                        DebugLog.i(TAG,"IsExcuteShow=true,ClearButton");
                        sendCmd(CDispListBeanEvent.SET_BUTTON_TEXT,bean);
                    }*/

                }
            }
        }
        return false;
    }

    /**
     * 清空列表内容
     *
     * @return true 清理成功;false 失败
     */
    public static boolean ClearList(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList ClearList 方法:" + objKey);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshList(true);
                bean.setNeedRefreshHead(true);
                if (bean.getContentList() != null && bean.getContentList().size() > 0) {
                    bean.getContentList().clear();
                    return true;
                }
            }
        }
        return false;
    }


    /*-----------------------------------------------------------------------------
    功    能：删除列表中的某一行
    参数说明：iItemIndex行索引
    返 回 值：true 清理成功
              false 清理失败
    说    明：cailei by 2017-7-19
    -----------------------------------------------------------------------------*/
    public static boolean DeleteItems(int objKey, int iItemIndex) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList DeleteItems 方法:" + objKey + "line no. - " + iItemIndex);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);

            if (bean != null) {
                bean.setNeedRefreshList(true);
                if (bean.getContentList() != null && bean.getContentList().size() > iItemIndex && iItemIndex > -1) {
                    bean.getContentList().remove(iItemIndex);
                    //当删除的行之前被选中，则删除后，无选中行记录
                    if (iItemIndex == bean.getSelectedPosi()) {
                        bean.setSelectedPosi(-1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 显示
     *
     * @return 返回用户按的键
     */
    public static int Show(int objKey) throws InterruptedException {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispList Show 方法:" + objKey);
        if (LIST_MAP_EVENT.containsKey(objKey)) {
            final CDispListBeanEvent bean = LIST_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setObjKey(objKey);
                // 记录路径
                EDiagUtils.get().addPath(objKey, PageType.Type_List);

                // 记录执行过show()方法,
                IsExcuteShow = true;
                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }
                bean.setCopyList(bean.getContentList());
                sendCmd(CDispListBeanEvent.SHOW, bean);

                if (bean.isUIStatic()) {
                    bean.lockAndWait();
                } else {
                    sleep(50);
                }
                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }

}
