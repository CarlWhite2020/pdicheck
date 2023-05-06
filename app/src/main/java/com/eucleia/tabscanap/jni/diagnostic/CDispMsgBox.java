package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.pdicheck.vm.AppVM;
import com.eucleia.tabscanap.constant.BTConstant;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.bean.diag.CDispMsgBoxBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static android.os.SystemClock.sleep;


/**
 * 项目名: Eucleia_
 * 包名 :  com.eucleia.plus.so
 * 文件名:  CDispMsgBox
 * 作者 :   sen
 * 时间 :  下午3:29 2017/3/3.
 * 描述 :  jni 中间层 - 对话框
 */

public class CDispMsgBox {

    /*
     *全局对象引用管理者
     */
    public static Map<Integer, CDispMsgBoxBeanEvent> MSG_BOX_MAP_EVENT = new ConcurrentHashMap<>();

    // 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), objKey);
        MSG_BOX_MAP_EVENT.put(objKey, new CDispMsgBoxBeanEvent(objKey));
    }


    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),objKey);
        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            MSG_BOX_MAP_EVENT.remove(objKey);
            CDispMsgBoxBeanEvent.getmMsgBoxFreeBtnListOld().clear();
        }
    }


    /**
     * * 功能介绍：设置按钮类型 参数说明：iType 按钮类型
     * OK_BUTTON== (0X0101)
     * CANCEL_BUTTON== (0x0102)
     * ...
     * 它们分别 & iType,若不为零或者等于本身，则表示有这个按扭,反之没有这个按扭
     *
     * @return 返回值： 1. 成功 0. 失败 注意该函数必须首先调用
     */
    private static void checkBtnType(CDispMsgBoxBeanEvent beanEvent, int type) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"判断Btn类型:" + String.format("%x", type));

        // 判断是否模态
        int isWait = type & CDispConstant.PageButtonType.DF_MB_BUTTON;

        int isFreeBtn = type & CDispConstant.PageButtonType.DF_MB_FREE;

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"--- checkBtnType isWait or Free :" + String.format("%x", isWait) + "," + String.format("%x", isFreeBtn));

        if (isWait == 0 && isFreeBtn == 0) {
            // 非阻塞、非自定义按钮 noButton
            beanEvent.setHasWait(false);
            beanEvent.setHasFreeBtn(false);

        } else if (isWait == 0) {
            // 非阻塞、添加自定义按钮
            beanEvent.setHasWait(false);
            beanEvent.setHasFreeBtn(true);

        } else if (isFreeBtn == 0) {
            // 阻塞、固定按钮 & oxff 结果为 1、2、3、4、8、C

            beanEvent.setHasWait(true);
            beanEvent.setHasFreeBtn(false);
            // 初始化按钮状态
            beanEvent.setHasYesBtn(false);
            beanEvent.setHasNoBtn(false);
            beanEvent.setHasYesNoBtn(false);
            beanEvent.setHasOkBtn(false);
            beanEvent.setHasCancelBtn(false);
            beanEvent.setHasOkCancelBtn(false);
            beanEvent.setbState(false);
            beanEvent.setbHaveHourglass(false);

            // Yes 按钮
            if ((CDispConstant.PageButtonType.DF_MB_YES & type) == CDispConstant.PageButtonType.DF_MB_YES) {
                beanEvent.setHasYesBtn(true);
            } else {
                beanEvent.setHasYesBtn(false);
            }
            // No 按钮
            if ((CDispConstant.PageButtonType.DF_MB_NO & type) == CDispConstant.PageButtonType.DF_MB_NO) {
                beanEvent.setHasNoBtn(true);
            } else {
                beanEvent.setHasNoBtn(false);
            }
            // Yes No
            if ((CDispConstant.PageButtonType.DF_MB_YESNO & type) == CDispConstant.PageButtonType.DF_MB_YESNO) {
                beanEvent.setHasYesNoBtn(true);
            } else {
                beanEvent.setHasYesNoBtn(false);
            }
            // ok
            if ((CDispConstant.PageButtonType.DF_MB_OK & type) == CDispConstant.PageButtonType.DF_MB_OK) {
                beanEvent.setHasOkBtn(true);
            } else {
                beanEvent.setHasOkBtn(false);
            }
            // cancel
            if ((CDispConstant.PageButtonType.DF_MB_CANCEL & type) == CDispConstant.PageButtonType.DF_MB_CANCEL) {
                beanEvent.setHasCancelBtn(true);
            } else {
                beanEvent.setHasCancelBtn(false);
            }
            // ok cancel
            if ((CDispConstant.PageButtonType.DF_MB_OKCANCEL & type) == CDispConstant.PageButtonType.DF_MB_OKCANCEL) {
                beanEvent.setHasOkCancelBtn(true);
            } else {
                beanEvent.setHasOkCancelBtn(false);
            }

        } else {
            // 阻塞、自定义按钮
            beanEvent.setHasWait(true);
            beanEvent.setHasFreeBtn(true);
        }


    }


    /**--------------------------------------JNI 接口---------------------------------------**/


    /**
     *
     * Tip: 除show() 函数外,所有函数只是收集JNI 传递给我们的数据, 操作界面均在Show() 函数,
     * 更新布局控件时强制会调用show(),更新文本,颜色等不会强制调用show()
     *
     */


    /**
     * 初始化
     *
     * @param pszCaptionText 标题文本
     * @param pszContainText 内容文本
     * @param nType          对话框类型(见定义)
     * @param nDispType      界面显示位置
     * @param nFormat        文本排版格式(检定仪)
     */
    public static void InitData(int objKey, String pszCaptionText, String pszContainText, int nType, int nDispType, int nFormat) {

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.reSetData();

                bean.setMsgSource(CDispMsgBox.class.getSimpleName());
                bean.setStrTitle(pszCaptionText);
                bean.setStrContext(pszContainText);
                bean.setnType(nType);
                bean.setDispType(nDispType);
                bean.setnFormat(nFormat);
                bean.setbHaveHourglass(false);
                bean.setbState(false);
                bean.setColorText("000000");
                // 判断按钮类型
                checkBtnType(bean, nType);

                //开启沙漏的判断
                if (bean.isHasWait()) {
                    bean.setbHaveHourglass(false);
                } else {
                    bean.setbHaveHourglass(true);
                }
                if (bean.getMsgBoxFreeBtnList() != null) {
                    bean.setRefreshButtonLayout(true);
                    bean.getMsgBoxFreeBtnList().clear();
                }
            }

        }


        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),pszCaptionText + "," + pszContainText + "," + nType + "," + nFormat + "," + nDispType);
    }

    /**
     * 设置沙漏 圆圈进度条
     *
     * @param bHaveHourglass true 存在沙漏;false 不存在沙漏
     */
    public static void SetHourglass(int objKey, boolean bHaveHourglass) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), bHaveHourglass);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setbHaveHourglass(bHaveHourglass);
            }
        }
    }

    /**
     * 设置标题
     *
     * @param strTitle 标题文本
     */
    public static void SetTitle(int objKey, String strTitle) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strTitle);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setStrTitle(strTitle);
            }
        }
    }

    /**
     * 设置内容
     *
     * @param strContext 内容文本
     * @param nFormat    显示位置
     * @param clrText    文本颜色
     */
    public static void SetContext(int objKey, String strContext, int nFormat, byte[] clrText) {

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setStrContext(strContext);
                bean.setColorText(clrText);
                bean.setnFormat(nFormat);
                ELogUtils.e(EDiagUtils.get().isOpenLogCat(),strContext + ",颜色:" + bean.getColorText() + "-," + nFormat);
            }
        }
    }

    /**
     * 添加按钮 (用户设置UImessageBox)风格为DF_MB_FREE 时此函数才能添加按钮)
     *
     * @param strButtonText 按钮文本
     * @return true 添加成功;false 失败
     */
    public static boolean AddButtonFree(int objKey, String strButtonText) {

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setRefreshButtonLayout(true);
                bean.addButtonFree(new CDispMsgBoxBeanEvent.MsgBoxFreeBtn(strButtonText, true));
            }
        }

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), strButtonText);

        return true;
    }


    /**
     * 修改添加按钮的文本 (用户设置UImessageBox)风格为DF_MB_FREE 时此函数才能添加按钮)
     *
     * @param iBtnIndex     按钮索引
     * @param strButtonText 按钮文本
     * @return true 修改成功;false 失败
     */
    public static boolean SetButtonText(int objKey, int iBtnIndex, String strButtonText) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), iBtnIndex + "," + strButtonText);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getMsgBoxFreeBtnList() != null
                        && iBtnIndex < bean.getMsgBoxFreeBtnList().size()
                        && iBtnIndex > -1) {
                    bean.setRefreshButtonLayout(true);
                    bean.getMsgBoxFreeBtnList().get(iBtnIndex).btn_text = strButtonText;
                }
            }
        }
        return true;
    }

    /**
     * 修改添加按钮状态 (用户设置UImessageBox)风格为DF_MB_FREE 时此函数才能添加按钮)
     *
     * @param iBtnIndex 按钮索引
     * @param bEnable   按钮状态 true 可用;false 不可用
     * @return true 修改成功;false 失败
     */
    public static boolean SetButtonStatus(int objKey, int iBtnIndex, boolean bEnable) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),iBtnIndex + "," + bEnable);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getMsgBoxFreeBtnList() != null
                        && iBtnIndex < bean.getMsgBoxFreeBtnList().size()
                        && iBtnIndex > -1) {
                    bean.setRefreshButtonLayout(true);
                    bean.getMsgBoxFreeBtnList().get(iBtnIndex).isEnable = bEnable;
                }
            }
        }

        return true;
    }

    /**
     * 清空添加按钮 (用户设置UImessageBox)风格为DF_MB_FREE 时此函数才能添加按钮)
     *
     * @return true 清除成功;false 失败
     */
    public static boolean ClearButton(int objKey) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat());

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                if (bean.getMsgBoxFreeBtnList() != null) {
                    bean.setRefreshButtonLayout(true);
                    bean.getMsgBoxFreeBtnList().clear();
                }
            }
        }
        return true;
    }

    /**
     * 设置进度条是否可用,默认不可用不显示
     *
     * @param bState true 可用、显示;false 不可用、不显示
     */
    public static void SetProgressBarState(int objKey, boolean bState) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), bState);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setbState(bState);
            }
        }
    }

    /**
     * 设置进度条比值
     *
     * @param iPercent    当前进度值
     * @param iAllPercent 进度总值
     */
    public static void SetProgressPercent(int objKey, int iPercent, int iAllPercent) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), iPercent + "," + iAllPercent);


        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setiPercent(iPercent);
                bean.setiAllPercent(iAllPercent);
            }
        }
    }

    /**
     * 设置界面定时器，界面显示后进入倒计时，时间到则界面返回，阻塞界面返回DF_ID_OK，非阻塞界面返回DF_ID_NOKEY
     *
     * @param nTimer 定时器，单位ms，默认为零不处理，非零到达时自动返回
     */
    public static void SetTimer(int objKey, int nTimer) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat(), nTimer);

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                //实现需要补充
            }
        }
    }

    /**
     * 显示
     *
     * @return 返回用户按的键
     * 说明: 1.当用户定义为非模态(非阻塞)消息框,无操作返回DF_ID_NOKEY;有按键操作返回用户选择按钮DF_ID_FREEBTN_X
     * 2.当用户定义为模态(阻塞)消息框,等待用户操作返回选择按钮;固定按钮返回固定按钮返回值,自定义按钮 返回自定义按钮返回值;
     * Tip:等待用户操作时 需加锁停止return的执行,等用户操作完成在解锁,放行return;
     */
    public static int Show(int objKey) {

        ELogUtils.e(EDiagUtils.get().isOpenLogCat());

        if (MSG_BOX_MAP_EVENT.containsKey(objKey)) {
            final CDispMsgBoxBeanEvent bean = MSG_BOX_MAP_EVENT.get(objKey);
            if (bean != null) {
                bean.setObjKey(objKey);
                // 记录执行过show()方法,
                IsExcuteShow = true;

                // [首先判断]判断线程状态 结束
                if (EDiagUtils.get().isThreadEnd()) {
                    bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    bean.lockAndSignalAll();
                    IsExcuteShow = false;
                    return bean.getBackFlag();
                }

                if ((bean.getnType() & 0xF000) == CDispConstant.PageButtonType.DF_MB_TYPE_A1_HOME) {
                    if (BTConstant.isVciConnect) {
                        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"当前进入A1选择诊断功能 : " + bean.isHasWait());
                        EDiagUtils.get().setSuspending(true);
                    } else {
                        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"当前未连接退出A1诊断功能 : " + bean.isHasWait());
                        EDiagUtils.get().setSuspending(false);
                        bean.setHasWait(false);
                        bean.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
                    }
                }

                AppVM.get().postDataValue(objKey, PageType.Type_MsgBox);
                // 模态(阻塞),非模态(非阻塞) 动态判断是否阻塞线程
                if (bean.isHasWait() ||
                        (EDiagUtils.get().isSuspending() && BTConstant.isVciConnect)) {
                    bean.lockAndWait();
                } else {
                    EDiagUtils.get().setSuspending(false);
                    sleep(150);
                }
                return bean.getBackFlag();
            }
        }

        return CDispConstant.PageButtonType.DF_ID_NOKEY;
    }


}
