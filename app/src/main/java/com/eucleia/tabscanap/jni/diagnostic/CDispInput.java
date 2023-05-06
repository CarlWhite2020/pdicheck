package com.eucleia.tabscanap.jni.diagnostic;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.bean.diag.CDispInputBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名: Eucleia_
 * 包名 :  com.eucleia.plus.so
 * 文件名:  CDispInput
 * 作者 :   sen
 * 时间 :  下午4:41 2017/3/3.
 * 描述 :  jni 中间层 - 输入框
 */

public class CDispInput {


    /**
     * 全局对象引用管理者
     */
    public static Map<Integer, CDispInputBeanEvent> INPUT_MAP_EVENT = new ConcurrentHashMap<>();
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
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput setData 方法:" + objKey);
        INPUT_MAP_EVENT.put(objKey, new CDispInputBeanEvent());
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput resetData 方法:" + objKey);
        if (INPUT_MAP_EVENT.containsKey(objKey)) {
            INPUT_MAP_EVENT.remove(objKey);
        }
    }


    /*****************************************************
     * 功能介绍:【掩码字符串】,Android 根据输入的字符串跟掩码对应的规则 判断输入的字符是否合法
     *
     * 掩码规则:
     *          'A':可输入 A-Z 之间的字符
     *          'icon_progress_for':可输入小写字母a-z
     *          'F':可输入0-9,A-F之间的字符
     *          '9':可输入0-9 之间的字符
     *          'X':可输入任意可见字符
     *          '+':可输入0-9之间,'+','-'字符
     *          'M':可输入0-9之间,A-Z之间字符
     *****************************************************/


    /**
     * 初始化1个输入框
     *
     * @param strCaption  标题文本
     * @param strPrompt   提示文本
     * @param strMask     掩码,同时决定输入框输入的最大长度
     * @param nDispType   在界面显示位置
     * @param strDefault  默认值
     * @param strMinValue 最小值
     * @param strMaxValue 最大值
     */
    public static void InitDataOneEdit(int objKey, String strCaption, String strPrompt, String strMask, int nDispType, String strDefault, String
            strMinValue, String strMaxValue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataOneEdit 方法:"
                + strCaption + ","
                + strPrompt + ","
                + strMask + ","
                + nDispType + ","
                + strDefault + ","
                + strMinValue + ","
                + strMaxValue);

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataOneEdit 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataOneEdit 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        //  清空输入框对象
        bean.ResetAllData();
        //  创建输入框对象
        CDispInputBeanEvent.InputItem inputItem = new CDispInputBeanEvent.InputItem()
                .setStrPrompt(strPrompt)
                .setStrMask(strMask)
                .setStrDefault(strDefault)
                .setStrMinValue(strMinValue)
                .setStrMaxValue(strMaxValue);

        // 装填数据
        bean.setStrCaption(strCaption)
                .addInputItem(inputItem);
        bean.setDispType(nDispType);

    }

    /**
     * 初始化多个输入框
     *
     * @param strCaption     标题文本
     * @param vecStrPrompt   提示文本
     * @param vecStrMask     掩码,同时决定输入框输入的最大长度
     * @param nDispType      在界面显示位置
     * @param vecStrDefault  默认值
     * @param vecStrMinValue 最小值
     * @param vecStrMaxValue 最大值
     */
    public static void InitDataManyEdit(int objKey, String strCaption, Object[] vecStrPrompt, Object[] vecStrMask, int nDispType, Object[]
            vecStrDefault, Object[] vecStrMinValue, Object[] vecStrMaxValue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataManyEdit 方法:"
                + strCaption + ","
                + JSON.toJSONString(vecStrPrompt) + ","
                + JSON.toJSONString(vecStrMask) + ","
                + nDispType + ","
                + JSON.toJSONString(vecStrDefault) + ","
                + JSON.toJSONString(vecStrMinValue) + ","
                + JSON.toJSONString(vecStrMaxValue));

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataManyEdit 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput InitDataManyEdit 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }
        //  清空输入框对象
        bean.ResetAllData();
        // 装填数据
        bean.setStrCaption(strCaption)
                .setDispType(nDispType);

        for (int i = 0; i < vecStrPrompt.length; i++) {
            //  创建输入框对象
            CDispInputBeanEvent.InputItem inputItem = new CDispInputBeanEvent.InputItem();

            inputItem.setStrPrompt(String.valueOf(vecStrPrompt[i]));

            if (vecStrMask.length > i) {
                inputItem.setStrMask(String.valueOf(vecStrMask[i]));
            }
            if (vecStrDefault.length > i) {
                inputItem.setStrDefault(String.valueOf(vecStrDefault[i]));
            }
            if (vecStrMinValue.length > i) {
                inputItem.setStrMinValue(String.valueOf(vecStrMinValue[i]));
            }
            if (vecStrMaxValue.length > i) {
                inputItem.setStrMaxValue(String.valueOf(vecStrMaxValue[i]));
            }
            bean.addInputItem(inputItem);
        }

    }

    /**
     * 添加按钮
     *
     * @param strButtonText 按钮文本
     * @return true 添加成功;false 添加失败
     */
    public static boolean AddButtonFree(int objKey, String strButtonText) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput AddButtonFree 方法:" + strButtonText);
        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput AddButtonFree 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return false;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput AddButtonFree 方法:" + String.format("下标为 %{d} 的对象为空"));
            return false;
        }

        // 创建按钮
        CDispInputBeanEvent.ButtonItem buttonItem = new CDispInputBeanEvent.ButtonItem(strButtonText);

        // 装填数据
        bean.addButtonItem(buttonItem);

        return true;
    }

    /**
     * 获取单个输入框的输入值
     *
     * @return string输入值(统一返回字符串由诊断码来处理)
     */
    public static String GetOneEditValue(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetOneEditValue 方法: objKey=" + objKey);

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetOneEditValue 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return "";
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetOneEditValue 方法:" + String.format("下标为 %{d} 的对象为空"));
            return "";
        }

        String value = bean.getInputOneValue();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetOneEditValue 方法:" + value);

        return value;
    }

    /**
     * 设置输入框的输入值
     *
     * @param iIndex   输入框索引值
     * @param strValue 字符串值
     */
    public static void SetIndexEditValue(int objKey, int iIndex, String strValue) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetIndexEditValue 方法:" + iIndex + "," + strValue);

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetIndexEditValue 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetIndexEditValue 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }

        bean.setInputValue(iIndex, strValue);
    }

    /**
     * 获取多个输入框的输入值
     *
     * @return 输入值得集合(统一返回字符串由诊断码来处理)
     */
    public static Object[] GetManyEditValue(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetManyEditValue 方法:");

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetManyEditValue 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return null;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetManyEditValue 方法:" + String.format("下标为 %{d} 的对象为空"));
            return null;
        }

        String[] manyValue = bean.getInputManyValue();
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput GetManyEditValue 方法:" + JSON.toJSONString(manyValue));

        return manyValue;
    }

    /**
     * 设置输入框的帮助文档
     *
     * @param objKey
     * @param strHelpDoc
     */
    public static void SetHelp(int objKey, String strHelpDoc) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetHelp 方法:");
        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetHelp 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput SetHelp 方法:" + String.format("下标为 %{d} 的对象为空"));
            return;
        }

        bean.setStrHelpDoc(strHelpDoc);

        return;
    }

    /**
     * 显示
     *
     * @return 返回用户按得键
     */
    public static int Show(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput Show 方法:");

        if (!INPUT_MAP_EVENT.containsKey(objKey)) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput Show 方法:" + String.format("不存在下标为 %{d} 的对象"));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }
        CDispInputBeanEvent bean = INPUT_MAP_EVENT.get(objKey);
        if (bean == null) {
            ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispInput Show 方法:" + String.format("下标为 %{d} 的对象为空"));
            return CDispConstant.PageButtonType.DF_ID_NOKEY;
        }

        bean.setObjKey(objKey);
        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_Input);

        // 记录执行过show()方法,
        IsExcuteShow = true;
        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            bean.lockAndSignalAll();
            IsExcuteShow = false;
            return bean.getBackFlag();
        }

        sendCmd(CDispInputBeanEvent.SHOW, bean);
        bean.lockAndWait();

        return bean.getBackFlag();
    }


    /**
     * 发送指令到指令处理类
     */
    private static void sendCmd(int what, CDispInputBeanEvent beanEvent) {
        beanEvent.event_what = what;
        DiagnoseEvent.mCDispInputBeanEvent = beanEvent;
        EventBus.getDefault().post(beanEvent);
    }

}
