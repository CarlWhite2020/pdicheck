package com.eucleia.tabscanap.bean.diag;


import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.ReUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * jni传过来的页面参数 -- MsgBox
 * <p>
 * Created by sen on 2017/1/18.
 */

public class CDispInputBeanEvent extends BaseBeanEvent {

    public final static int SHOW = 1026;

    // MsgBox调用来源:CDispGlobal.marsShowMsgBox/CDispMsgBox.initData();

    // 自定义按钮点击返回值 基于0x03000100开始每增加一个自增1
    private int df_free_index;
    // 返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
    // 动态创建、刷新标识
    private boolean isRefreshButtonLayout;

    private String strCaption;
    private List<InputItem> inputItems;
    private List<ButtonItem> buttonItems;
    private String strHelpDoc = "";

    // 构造函数
    public CDispInputBeanEvent() {
        df_free_index = 0x03000100;
    }

    public void ResetAllData(){
        df_free_index = 0x03000100;
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        isRefreshButtonLayout = true;
        strCaption = "";
        if (inputItems!=null){
            inputItems.clear();
        }
        if (buttonItems!=null){
            buttonItems.clear();
        }

        strHelpDoc = "";
    }


    public boolean isRefreshButtonLayout() {
        return isRefreshButtonLayout;
    }

    public void setRefreshButtonLayout(boolean refreshButtonLayout) {
        isRefreshButtonLayout = refreshButtonLayout;
    }

    public String getStrCaption() {
        return strCaption;
    }

    public CDispInputBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public void addInputItem(InputItem item) {
        if (inputItems == null) {
            inputItems = new ArrayList<>();
        }
        ELogUtils.d("addInputItem： "+this.inputItems.size());

        inputItems.add(item);
    }

    public void addButtonItem(ButtonItem item) {
        if (buttonItems == null) {
            buttonItems = new ArrayList<>();
        }
        ELogUtils.d("addButtonItem： "+this.buttonItems.size());

        item.DF_FreeBtn_ID = df_free_index++;
        buttonItems.add(item);
    }

    public String getInputOneValue() {
        InputItem item = getInputItems().get(0);
        return item.getText();
    }

    public String[] getInputManyValue() {
        String[] valueArr = new String[inputItems.size()];
        for (int i = 0; i < valueArr.length; i++) {
            valueArr[i] = inputItems.get(i).getText();
        }
        return valueArr;
    }

    public void setInputValue(int index, String strValue) {
        InputItem item = inputItems.get(index);
        item.setText(strValue);
    }

    public List<InputItem> getInputItems() {
        if (this.inputItems == null) {
            this.inputItems = new ArrayList<>();
        }
        ELogUtils.d("getInputItems： "+this.inputItems.size());
        return inputItems;
    }

    public List<ButtonItem> getButtonItems() {
        if (this.buttonItems == null) {
            this.buttonItems = new ArrayList<>();
        }
        ELogUtils.d("getButtonItems： "+this.buttonItems.size());
        return buttonItems;
    }

    public String getStrHelpDoc() {
        return strHelpDoc;
    }

    public void setStrHelpDoc(String strHelpDoc) {
        this.strHelpDoc = strHelpDoc;
    }

    public static class InputItem implements Serializable {
        String strPrompt;
        String strMask;
        String strDefault;
        String strMinValue;
        String strMaxValue;
        String reStr;
        String text;
        List<String> textInputHistory;

        public void obtionHistoryInput() {

        }

        public String getStrPrompt() {
            return strPrompt;
        }

        public InputItem setStrPrompt(String strPrompt) {
            this.strPrompt = strPrompt;
            return this;
        }

        public String getStrMask() {
            return strMask;
        }

        public InputItem setStrMask(String strMask) {
            this.strMask = strMask;
            this.reStr = ReUtils.mask2Restr(strMask);
            return this;
        }

        public String getStrDefault() {
            return strDefault;
        }

        public InputItem setStrDefault(String strDefault) {
            this.strDefault = strDefault;
            this.setText(strDefault);
            return this;
        }

        public String getStrMinValue() {
            return strMinValue;
        }

        public InputItem setStrMinValue(String strMinValue) {
            this.strMinValue = strMinValue;
            return this;
        }

        public String getStrMaxValue() {
            return strMaxValue;
        }

        public InputItem setStrMaxValue(String strMaxValue) {
            this.strMaxValue = strMaxValue;
            return this;
        }

        public String getReStr() {
            return reStr;
        }

        public InputItem setReStr(String reStr) {
            this.reStr = reStr;
            return this;
        }

        public String getText() {
            return text;
        }

        public InputItem setText(String text) {
            this.text = text;
            return this;
        }

        public List<String> getTextInputHistory() {
            return textInputHistory;
        }

        public InputItem setTextInputHistory(List<String> textInputHistory) {
            this.textInputHistory = textInputHistory;
            return this;
        }
    }

    public static class ButtonItem implements Serializable{
        // 自定义按钮的返回值 基于0x03000100开始每增加一个自增1
        public int DF_FreeBtn_ID;
        private String strButtonText;

        public ButtonItem(String strButtonText) {
            this.strButtonText = strButtonText;
        }

        public int getDF_FreeBtn_ID() {
            return DF_FreeBtn_ID;
        }

        public ButtonItem setDF_FreeBtn_ID(int DF_FreeBtn_ID) {
            this.DF_FreeBtn_ID = DF_FreeBtn_ID;
            return this;
        }

        public String getStrButtonText() {
            return strButtonText;
        }

        public ButtonItem setStrButtonText(String strButtonText) {
            this.strButtonText = strButtonText;
            return this;
        }
    }
}
