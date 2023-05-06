package com.eucleia.tabscanap.jni.diagnostic;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispSelectBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispSelect
 * 作者 :   sen
 * 时间 :  上午10:03 2017/3/6.
 * 描述 :  jni 中间层 - 选择框界面
 */

public class CDispSelect {

    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispSelectBeanEvent> SELECT_MAP_EVENT = new ConcurrentHashMap<>();
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
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect setData 方法:" + objKey);
        SELECT_MAP_EVENT.put(objKey, new CDispSelectBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect resetData 方法:" + objKey);
        if (SELECT_MAP_EVENT.containsKey(objKey)) {
            SELECT_MAP_EVENT.remove(objKey);
        }
    }

    /**
     * 初始化一个选择框
     *
     * @param strCaption       标题文本
     * @param strPrompt        提示文本
     * @param vecStrSelect     备选项内容（一维数组）
     * @param vIntDefSelectPos 默认选中文职
     * @param nDispType        在界面显示位置
     */
    public static void InitDataOneSelect(int objKey, String strCaption, String strPrompt, Object[] vecStrSelect, int[] vIntDefSelectPos, int nDispType) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect InitDataManySelect 方法: %s, %s, %s, %s, %s, %s",
                objKey, strCaption,strPrompt, JSON.toJSONString(vecStrSelect),
                JSON.toJSONString(vIntDefSelectPos), nDispType));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect InitDataOneSelect 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect InitDataOneSelect 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        bean.reSetInitData();
        CDispSelectBeanEvent.SelectItem item = new CDispSelectBeanEvent.SelectItem();
        item.setStrPrompt(strPrompt);
        item.setVecStrSelect(Arrays.asList(ArraysUtils.o2s(vecStrSelect)));
        item.setIntDefSelectPos(ArraysUtils.i2i(vIntDefSelectPos));
        item.setIntSelectPos(ArraysUtils.i2i(vIntDefSelectPos));

        bean.setStrCaption(strCaption).addSelectItem(item);
        bean.setDispType(nDispType);

    }

    /**
     * 初始化多个选择框
     *
     * @param strCaption        标题文本
     * @param vecStrPrompt      提示文本
     * @param vvStrSelect       备选项内容（二维数组）
     * @param vvIntDefSelectPos 默认选中位置
     * @param nDispType         在界面显示位置
     *                          <p>
     *                          Tip: 此方法会调用多次,每次初始化一个大项-N个子项过来(这样避免使用二维数组,所有二维数组改为一维,一维改为字符串)
     *                          建议使用对象数组存储,供界面调用
     */
    public static void InitDataManySelect(int objKey, String strCaption, Object[] vecStrPrompt, Object[] vvStrSelect, Object[] vvIntDefSelectPos, int nDispType) {

        /**
         * 0,
         * "",
         * ["Turn signal blinks times","Lock car sound"],
         * [["3 times","4 times","5 times","6 times","7 times","8 times","9 times","10 times"],["Lock car ring once","Lock car ring twice","Lock car no sound","Unlock car ring once","Unlock car ring twice","Unlock car no sound"]],
         * [],
         * 0
         */
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect InitDataManySelect 方法: %s, %s, %s, %s, %s, %s",
                objKey, strCaption, JSON.toJSONString(vecStrPrompt), JSON.toJSONString(vvStrSelect),
                JSON.toJSONString(vvIntDefSelectPos), nDispType));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect InitDataOneSelect 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect InitDataOneSelect 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        bean.reSetInitData();
        bean.setStrCaption(strCaption)
                .setDispType(nDispType);

        for (int i = 0; i < vecStrPrompt.length; i++) {
            CDispSelectBeanEvent.SelectItem item = new CDispSelectBeanEvent.SelectItem();
            item.setStrPrompt(String.valueOf(vecStrPrompt[i]));
            if (vvStrSelect != null && vvStrSelect.length > i) {
                item.setVecStrSelect(ArraysUtils.o2s(vvStrSelect[i]));
            }
            if (vvIntDefSelectPos != null && vvIntDefSelectPos.length > i) {
                item.setIntDefSelectPos(ArraysUtils.o2i(vvIntDefSelectPos[i]));
                item.setIntSelectPos(ArraysUtils.o2i(vvIntDefSelectPos[i]));
            }
            bean.addSelectItem(item);
        }

    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置界面显示风格，默认原风格，
     * 参数说明：nType 默认是CDispConstant.PageSelectType.DF_SL_TYPE_ORIGINAL，标识选择框的显示类型
     * #define CDispConstant.PageSelectType.DF_SL_TYPE_ORIGINAL	0x00  //原Select风格，默认是这个风格
     * #define CDispConstant.PageSelectType.DF_SL_TYPE_CODE_A1PRO	0x01  //A1PRO的刷隐藏界面风格
     * 返回值：无
     * 说明：无
     * -----------------------------------------------------------------------------
     **/
    public static void SetUIType(int objKey, int nType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetUIType 方法:" + objKey + "," + nType);
        if (SELECT_MAP_EVENT.containsKey(objKey)) {
            CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setUiType(nType);
            }
        }
    }

    /**
     * 设置多选
     *
     * @param bMutiSelect true 多选;false 单选
     */
    public static void SetMutiSelect(int objKey, boolean bMutiSelect) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect SetMutiSelect 方法: %s, %s", objKey, bMutiSelect));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetMutiSelect 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetMutiSelect 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }

        bean.setMutiSelect(bMutiSelect);
    }

    /**
     * 设置多选
     *
     * @param bMutiSelect true 多选;false 单选
     */
    public static void SetMutiSelect(int objKey, int iPos, boolean bMutiSelect) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect SetMutiSelect 方法: %s, %s, %s", objKey, iPos, bMutiSelect));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetMutiSelect 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetMutiSelect 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        List<CDispSelectBeanEvent.SelectItem> selectItems = bean.getSelectItems();
        if (selectItems != null && selectItems.size() > iPos) {
            CDispSelectBeanEvent.SelectItem selectItem = selectItems.get(iPos);
            if (selectItem != null) {
                selectItem.setMutiSelect(bMutiSelect);
            }
        }

    }

    /**
     * 获取单个索引 0-x
     *
     * @return 索引值
     */
    public static int[] GetOneSelectIndex(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetOneSelectIndex 方法:" + objKey);
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetOneSelectIndex 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return null;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetOneSelectIndex 方法:" + String.format("下标为 %{d} 的对象为空"));
            return null;
        }
        int[] ret = bean.getOneSelectIndex();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetOneSelectIndex 方法 \nreturn :" + JSON.toJSONString(ret));
        return ret;
    }

    /**
     * 获取索引集合
     *
     * @return 索引集合
     */
    public static Object[] GetManySelectIndex(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法:" + objKey);
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return null;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法:" + String.format("下标为 %{d} 的对象为空"));
            return null;
        }
        int[][] ret = bean.getManySelectIndex();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法 \nreturn :" + JSON.toJSONString(ret));
        return ret;
    }

    /**
     * 取得存储的暗码
     *
     * @return 存储的暗码
     */
    public static String GetRecordSecretCode(int objKey) {
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetRecordSecretCode 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return null;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetRecordSecretCode 方法:" + String.format("下标为 %{d} 的对象为空"));
            return null;
        }

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetRecordSecretCode 方法:" + bean.getRecordSecretCode());
        //实现需要补充，目前A1 PRO使用
        return bean.getRecordSecretCode();
    }

    /**
     * -----------------------------------------------------------------------------
     * 功能：设置选择的结果，并进行记录存储
     * 参数说明：vvIntSelectPos选择结果的索引集
     * strSecretCode 需要存储的暗码
     * iSetType 设置的类型：0 为原始查询、1为设置、2为恢复、10自动查询
     * iResult 设置的结果：0为失败、1为成功
     * 返回值：无
     * 说明：通过这个接口进行设置选择的结果，并进行结果数据存储，便于恢复使用
     * -----------------------------------------------------------------------------
     **/
    public static void SetManySelectIndex(int objKey, Object[] vvIntSelectPos, String strSecretCode, int iSetType, int iResult) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetManySelectIndex 方法 ");

        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法:" + String.format("不存在下标为 %d 的对象", objKey));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect GetManySelectIndex 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        //实现需要补充，目前A1 PRO使用
        for (int i = 0; i < vvIntSelectPos.length; i++) {
            List<CDispSelectBeanEvent.SelectItem> selectItems = bean.getSelectItems();
            if (selectItems != null && selectItems.size() > i) {
                CDispSelectBeanEvent.SelectItem item = selectItems.get(i);
                if (iResult == 1) {
                    item.setIntDefSelectPos(ArraysUtils.o2i(vvIntSelectPos[i]));
                }
                if (iSetType != 10) {
                    item.setIntSelectPos(ArraysUtils.o2i(vvIntSelectPos[i]));
                }
            }
        }
        if (iSetType == 10) {
            iSetType = 0;
        }
//        CodingDataPresenter.get().saveData(JNIConstant.GoodsId, vvIntSelectPos, strSecretCode, iSetType, iResult);
    }

    /**
     * 设置帮助文档(帮助按钮一般隐藏,调用该函数且文档(即参数)非空,则按钮显示)
     *
     * @param strHelpText 帮助文档
     */
    public static void SetHelp(int objKey, String strHelpText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect SetHelp 方法: %s, %s", objKey, strHelpText));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetHelp 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetHelp 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        bean.setHelp(strHelpText);
    }

    /**
     * 设置帮助文档标题
     *
     * @param strHelpTitle 帮助文档标题
     */
    public static void SetHelpTitle(int objKey, String strHelpTitle) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect SetHelpTitle 方法: %s, %s", objKey, strHelpTitle));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetHelpTitle 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetHelpTitle 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        bean.setHelpTitle(strHelpTitle);
    }

    /**
     * 设置选择结果是否展示
     *
     * @param bVisible true 展示;false 不展示
     */
    public static void SetSelectResultVisible(int objKey, boolean bVisible) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),String.format("-- CDispSelect SetSelectResultVisible 方法: %s, %s", objKey, bVisible));
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetSelectResultVisible 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect SetSelectResultVisible 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }

        bean.setSelectResultVisible(bVisible);
    }

    /**
     * 显示
     *
     * @return 用户按得键
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect Show 方法:");
        if (!SELECT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect Show 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        CDispSelectBeanEvent bean = SELECT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispSelect Show 方法:" + String.format("下标为 %{d} 的对象为空"));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }

        bean.setObjKey(objKey);

        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_Select);

        // 记录执行过show()方法,
        IsExcuteShow = true;

        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            bean.lockAndSignalAll();
            IsExcuteShow = false;
            return bean.getBackFlag();
        }

        sendCmd(CDispSelectBeanEvent.SHOW, bean);
        bean.lockAndWait();

        return bean.getBackFlag();
    }

    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispSelectBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispSelectBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }


}
