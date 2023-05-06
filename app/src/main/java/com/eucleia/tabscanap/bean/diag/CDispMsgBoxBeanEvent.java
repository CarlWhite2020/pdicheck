package com.eucleia.tabscanap.bean.diag;


import android.text.TextUtils;

import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispMsgBoxBeanEvent extends BaseBeanEvent {

    /**
     * 对应Msg映射类 每一个方法
     */
    public final static int SHOW = 1010;

    private String msgSource = "";

    private String strTitle = "";
    private String strContext = "";
    private List<MsgBoxSpanPng> msgBoxSpanPngList = new ArrayList<>();
    private int nType;
    private int nFormat = CDispConstant.PageFormat.DT_LEFT;

    private boolean bHaveHourglass;
    private String strButtonText = "";
    private String colorText = "";
    // 按钮索引(第几个按钮)
    private int iBtnIndex;
    private boolean bEnable;
    private boolean bState;
    private int iPercent;
    private int iAllPercent = 1;

    // 判断是否模态、是否是固定按钮、是否是自有按钮
    private boolean hasWait; //是否模态

    private boolean hasYesBtn; // 是否是固定按钮 yes
    private boolean hasNoBtn;  // ...
    private boolean hasYesNoBtn; // ...
    private boolean hasOkBtn;   // ...
    private boolean hasCancelBtn; // ...
    private boolean hasOkCancelBtn; // ...

    private boolean hasFreeBtn; // 是否是动态添加按钮类型

    // 全局 CDispGlobal倒计时、圆圈:进度条
    private int timer;

    // 自定义按钮点击返回值 基于0x03000100开始每增加一个自增1
    private int df_free_index;

    // 动态按钮的集合
    private List<MsgBoxFreeBtn> mMsgBoxFreeBtnList = new ArrayList<>();

    public static List<MsgBoxFreeBtn> getmMsgBoxFreeBtnListOld() {
        return mMsgBoxFreeBtnListOld;
    }

    // 动态按钮的集合，记录按钮区域上一次的情况
    private static List<MsgBoxFreeBtn> mMsgBoxFreeBtnListOld = new ArrayList<>();

    // 返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;

    // 动态创建、刷新标识
    private boolean isRefreshButtonLayout = true;

    // 对象ID
    private int iObjectKey = -1;

    // 构造函数
    public CDispMsgBoxBeanEvent(int iObjectKey) {
        df_free_index = CDispConstant.PageButtonType.DF_ID_FREEBTN_0;
        this.iObjectKey = iObjectKey;
    }

    public int getiObjectKey() {
        return iObjectKey;
    }

    public void reSetData() {
        msgSource = "";
        strTitle = "";
        strContext = "";
        if (msgBoxSpanPngList != null) {
            msgBoxSpanPngList.clear();
        }
        nType = -1;
        nFormat = -1;
        bHaveHourglass = false;
        strButtonText = "";
        colorText = "";

        iBtnIndex = -1;
        bEnable = false;
        bState = false;
        iPercent = 0;
        iAllPercent = 0;

        hasWait = false;
        hasYesBtn = false; // 是否是固定按钮 yes
        hasNoBtn = false;  // ...
        hasYesNoBtn = false; // ...
        hasOkBtn = false;   // ...
        hasCancelBtn = false; // ...
        hasOkCancelBtn = false; // ...
        hasFreeBtn = false;

        timer = 0;

        df_free_index = CDispConstant.PageButtonType.DF_ID_FREEBTN_0;//0x03000100;

        if (mMsgBoxFreeBtnList != null) {
            mMsgBoxFreeBtnList.clear();
        }

        isRefreshButtonLayout = true;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrContext() {
        return strContext;
    }

    public void setStrContext(String strContext) {
        this.strContext = strContext;
        setMsgBoxSpanPngList(strContext);
    }

    public void setMsgBoxSpanPngList(String strContext) {

        if (msgBoxSpanPngList != null) {
            msgBoxSpanPngList.clear();
        }
        if (TextUtils.isEmpty(strContext)) {
            return;
        }
        int iPosStart = 0;
        while (true) {
            iPosStart = strContext.indexOf("<SPAN-PNG path=", iPosStart);
            if (iPosStart < 0) {
                break;
            }
            int iPosEnd = strContext.indexOf("/>", iPosStart);
            if (iPosStart > iPosEnd) {
                break;
            }
            String strInfor = strContext.substring(iPosStart, iPosEnd + 2);
            int iPos1 = strInfor.indexOf("=");
            int iPos2 = strInfor.indexOf("/>");
            String strPath = strInfor.substring(iPos1 + 1, iPos2);
            msgBoxSpanPngList.add(new MsgBoxSpanPng(iPosStart, iPosEnd + 2, strPath));
            iPosStart = iPosEnd + 2;
        }
    }

    public List<MsgBoxSpanPng> getMsgBoxSpanPngList() {
        return msgBoxSpanPngList;
    }

    public int getnType() {
        return nType;
    }

    public void setnType(int nType) {
        this.nType = nType;
    }

    public int getnFormat() {
        return nFormat;
    }

    public void setnFormat(int nFormat) {
        this.nFormat = CDispConstant.PageFormat.DT_LEFT;//因诊断的未注意这个接口，导致全部传递的是DT_CENTER，界面很丑
    }

    public boolean isbHaveHourglass() {
        return bHaveHourglass;
    }

    public void setbHaveHourglass(boolean bHaveHourglass) {
        this.bHaveHourglass = bHaveHourglass;
    }

    public String getStrButtonText() {
        return strButtonText;
    }

    public void setStrButtonText(String strButtonText) {
        this.strButtonText = strButtonText;
    }

    public int getiBtnIndex() {
        return iBtnIndex;
    }

    public void setiBtnIndex(int iBtnIndex) {
        this.iBtnIndex = iBtnIndex;
    }

    public boolean isbEnable() {
        return bEnable;
    }

    public void setbEnable(boolean bEnable) {
        this.bEnable = bEnable;
    }

    public boolean getbState() {
        return bState;
    }

    public void setbState(boolean bState) {
        this.bState = bState;
    }

    public int getiPercent() {
        return iPercent;
    }

    public void setiPercent(int iPercent) {
        this.iPercent = iPercent;
    }

    public int getiAllPercent() {
        return iAllPercent;
    }

    public void setiAllPercent(int iAllPercent) {
        this.iAllPercent = iAllPercent;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = "#" + colorText;
    }

    public void setColorText(byte[] colorText) {
        String col = String.format("%02x", colorText[0]);
        String col2 = String.format("%02x", colorText[1]);
        String col3 = String.format("%02x", colorText[2]);
        this.colorText = "#" + col + col2 + col3;
    }

    public boolean isHasWait() {
        return hasWait;
    }

    public void setHasWait(boolean hasWait) {
        this.hasWait = hasWait;
    }

    public boolean isHasYesBtn() {
        return hasYesBtn;
    }

    public void setHasYesBtn(boolean hasYesBtn) {
        this.hasYesBtn = hasYesBtn;
    }

    public boolean isHasNoBtn() {
        return hasNoBtn;
    }

    public void setHasNoBtn(boolean hasNoBtn) {
        this.hasNoBtn = hasNoBtn;
    }

    public boolean isHasYesNoBtn() {
        return hasYesNoBtn;
    }

    public void setHasYesNoBtn(boolean hasYesNoBtn) {
        this.hasYesNoBtn = hasYesNoBtn;
    }

    public boolean isHasOkBtn() {
        return hasOkBtn;
    }

    public void setHasOkBtn(boolean hasOkBtn) {
        this.hasOkBtn = hasOkBtn;
    }

    public boolean isHasCancelBtn() {
        return hasCancelBtn;
    }

    public void setHasCancelBtn(boolean hasCancelBtn) {
        this.hasCancelBtn = hasCancelBtn;
    }

    public boolean isHasOkCancelBtn() {
        return hasOkCancelBtn;
    }

    public void setHasOkCancelBtn(boolean hasOkCancelBtn) {
        this.hasOkCancelBtn = hasOkCancelBtn;
    }

    public boolean isHasFreeBtn() {
        return hasFreeBtn;
    }

    public void setHasFreeBtn(boolean hasFreeBtn) {
        this.hasFreeBtn = hasFreeBtn;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public boolean isbState() {
        return bState;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public List<MsgBoxFreeBtn> getMsgBoxFreeBtnList() {
        return mMsgBoxFreeBtnList;
    }

    public void setMsgBoxFreeBtnList(List<MsgBoxFreeBtn> msgBoxFreeBtnList) {
        mMsgBoxFreeBtnList = msgBoxFreeBtnList;
    }

    public void setmMsgBoxFreeBtnListOld() {
        ELogUtils.d("cailei -- setmMsgBoxFreeBtnListOld");
        if (mMsgBoxFreeBtnListOld == null) {
            mMsgBoxFreeBtnListOld = new ArrayList<>();
        }
        mMsgBoxFreeBtnListOld.clear();
        if (mMsgBoxFreeBtnList != null) {
            for (int i = 0; i < mMsgBoxFreeBtnList.size(); i++) {
                MsgBoxFreeBtn btnOld = new CDispMsgBoxBeanEvent.MsgBoxFreeBtn(mMsgBoxFreeBtnList.get(i).btn_text, mMsgBoxFreeBtnList.get(i).isEnable);
                btnOld.DF_FreeBtn_ID = mMsgBoxFreeBtnList.get(i).DF_FreeBtn_ID;
                mMsgBoxFreeBtnListOld.add(btnOld);
            }
        }
    }

    public boolean isCompareBtnFree() {
        int iSize = 0;
        int iSizeOld = 0;
        if (mMsgBoxFreeBtnList != null) {
            iSize = mMsgBoxFreeBtnList.size();
        }
        if (mMsgBoxFreeBtnListOld != null) {
            iSizeOld = mMsgBoxFreeBtnListOld.size();
        }

        if (iSize != iSizeOld) {
            ELogUtils.d("cailei -- isCompareBtnFree false");
            return false;
        } else {
            for (int i = 0; i < iSize; i++) {
                if (!mMsgBoxFreeBtnList.get(i).btn_text.equals(mMsgBoxFreeBtnListOld.get(i).btn_text)
                        || mMsgBoxFreeBtnList.get(i).isEnable != mMsgBoxFreeBtnListOld.get(i).isEnable
                        || mMsgBoxFreeBtnList.get(i).DF_FreeBtn_ID != mMsgBoxFreeBtnListOld.get(i).DF_FreeBtn_ID) {
                    ELogUtils.d("cailei -- isCompareBtnFree false");
                    return false;
                }
            }
            return true;
        }

    }


    public boolean isRefreshButtonLayout() {
        return isRefreshButtonLayout;
    }

    public void setRefreshButtonLayout(boolean refreshButtonLayout) {
        isRefreshButtonLayout = refreshButtonLayout;
    }

    /**
     * 添加一个自定义按钮
     */
    public void addButtonFree(MsgBoxFreeBtn msgBoxFreeBtn) {
        if (mMsgBoxFreeBtnList == null) {
            mMsgBoxFreeBtnList = new ArrayList<>();
        }
        msgBoxFreeBtn.DF_FreeBtn_ID = df_free_index + mMsgBoxFreeBtnList.size();
        // 添加一个对象
        mMsgBoxFreeBtnList.add(msgBoxFreeBtn);

    }


    /**
     * 动态添加按钮类
     */
    public static class MsgBoxFreeBtn implements Serializable {

        // 自定义按钮的返回值 基于0x03000100开始每增加一个自增1
        public int DF_FreeBtn_ID;
        // 按钮文本
        public String btn_text;
        // 是否禁用
        public boolean isEnable;

        public MsgBoxFreeBtn(String btn_text, boolean isEnable) {
            this.btn_text = btn_text;
            this.isEnable = isEnable;
        }
    }

    /**
     * 保存内容文本中的图片信息
     */
    public static class MsgBoxSpanPng implements Serializable {
        public int iStartPos;
        public int iEndPos;
        public String strPngPath;

        public MsgBoxSpanPng(int iStartPos, int iEndPos, String strPngPath) {
            this.iStartPos = iStartPos;
            this.iEndPos = iEndPos;
            this.strPngPath = strPngPath;
        }
    }
}
