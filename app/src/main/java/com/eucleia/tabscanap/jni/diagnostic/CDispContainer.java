package com.eucleia.tabscanap.jni.diagnostic;


import com.eucleia.tabscanap.constant.CDispConstant;

/**
 * 项目名: Eucleia_
 * 包名 :  com.eucleia.tabscan.jni.diagnostic
 * 文件名:  CDispContainer
 * 作者 :   cailei
 * 时间 :  下午5:01 2021/3/25.
 * 描述 :  jni 中间层 - 组合界面
 */

public class CDispContainer {

    /*****************************************************
     * 功能介绍:【掩码字符串】,Android 根据输入的字符串跟掩码对应的规则 判断输入的字符是否合法
     *
     * 掩码规则:
     * A: 可输入A-Z之间的字符
     * a: 表示可输入小写字母a-z
     * F: 可输入0-9，A-F之间的字符
     * 9: 表示可输入0- 9之间的字符，以及小数点符号
     * X: 表示可以输入任意可见字符
     * +: 表示可输入0-9 ，’+’ ，‘-’ ，以及小数点符号
     * M: 表示0-9，A-Z
     *
     *****************************************************/

    /**组件类型解析*/
    /****************************************************
     * 输入组件的JS数据：
     {
     "data_type":"input",
     "prompt":"TEST INPUT ",
     "mask":"MMMMMMMMMM",
     "defvalue":"CAILEILEI0",
     "maxvalue":"1234567890",
     "minvalue":"9876543210",
     "value":"CAILEILEI0"
     }
     * 输入框的返回数据：2345678901
     *****************************************************/

    /****************************************************
     * 消息框组件的JS数据：
     {
     "data_type":"msgbox",
     "prompt":"TEST MSGBOX"
     }
     * 消息框组件的返回:空串
     *****************************************************/

    /****************************************************
     * Check组件的JS数据：
     {
     "data_type":"checkbox",
     "prompt":"TEST CHECKBOX",
     "value":0
     }
     * Check组件的返回数据：1
     *****************************************************/

    /****************************************************
     * 单选组件的JS数据：
     {
     "data_type":"select",
     "prompt":"TEST SELECT",
     "vector_select":["1111","2222","3333","4444","5555","6666","7777","8888","9999"],
     "default_postion":0,
     "postion":0
     }
     * 单选组件的返回数据：1
     *****************************************************/

    /****************************************************
     * 多选组件的JS数据：
     {
     "data_type":"mutiselect",
     "prompt":"TEST MUTI SELECT",
     "vector_select":["1111","2222","3333","4444","5555","6666","7777","8888","9999"],
     "default_postion":[1,3],
     "postion":[1,3]
     }
     * 多选组件的返回数据：2,4
     *****************************************************/


    /**
     * 判断是否调用过show()一次,为SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
    }

    /**
     * 初始化组合界面
     *
     * @param strCaption 标题文本
     * @param nDispType  在界面显示位置
     */
    public static void InitData(int objKey, String strCaption, int nDispType) {
    }

    /**
     * 添加组件
     *
     * @param vecStr 界面组件内容JS格式，见文件头前的格式类型
     */
    public static void AddItems(int objKey, Object[] vecStr) {

    }

    /**
     * 添加按钮
     *
     * @param strButtonText 按钮文本
     * @return true 添加成功;false 添加失败
     */
    public static boolean AddButtonFree(int objKey, String strButtonText) {
        return true;
    }

    /**
     * 获取组合组件的各个组件操作后的返回值
     *
     * @return 组合组件的操作值的集合(统一返回字符串由诊断码来处理)，见文件头前的格式类型
     */
    public static Object[] GetValue(int objKey) {
        return new String[1];
    }

    /**
     * 显示
     *
     * @return 返回用户按得键
     */
    public static int Show(int objKey) {
        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }


}
