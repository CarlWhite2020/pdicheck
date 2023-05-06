package com.eucleia.tabscanap.bean.diag;

import android.text.TextUtils;

import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispTroubleBeanEvent extends BaseBeanEvent {

    public final static int SHOW = 10999;

    // 是否插入到数据库
    private  boolean isInsertToDB=false;

    // code 列显示标识
    private boolean isVisibleCodeColumn  = false;
    // state 列显示标识
    private boolean isVisibleStateColumn  = false;
    // desc 列显示标识
    private boolean isVisibleDescColumn  = false;


    // 返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;

    // 自定义按钮点击返回值 基于0x02000000开始每增加一个自增1
    private int df_free_index;

    private String Caption = "";//标题文本


    // 清码按钮状态
    private boolean clearDTCStatus  = true;

    //item集合
    private List<TroubleItem> troubleItems;


    public List<TroubleItemTitle> getTroubleItemTitles() {
        return troubleItemTitles;
    }

    public void setTroubleItemTitles(List<TroubleItemTitle> troubleItemTitles) {
        this.troubleItemTitles = troubleItemTitles;
    }

    //item集合
    private List<TroubleItemTitle> troubleItemTitles;

    // 构造函数
    public CDispTroubleBeanEvent() {
        df_free_index = CDispConstant.PageTroubleType.DF_ID_DTCIDX_0;//0x02000000;
    }

    public List<TroubleItem> getTroubleItems() {
        return troubleItems;
    }

    public void setTroubleItems(List<TroubleItem> troubleItems) {
        this.troubleItems = troubleItems;
    }

    public boolean isInsertToDB() {
        return isInsertToDB;
    }

    public boolean isVisibleCodeColumn() {
        return isVisibleCodeColumn;
    }

    // 判断该列是否显示
    public CDispTroubleBeanEvent setVisibleCodeColumn(String codeColumn) {
        if (!TextUtils.isEmpty(codeColumn)) {
            isVisibleCodeColumn = true;
        }
        return this;
    }

    public boolean isVisibleStateColumn() {
        return isVisibleStateColumn;
    }

    // 判断该列
    public CDispTroubleBeanEvent setVisibleStateColumn(String stateColumn) {
        if (!TextUtils.isEmpty(stateColumn)) {
            isVisibleStateColumn=true;
        }
        return this;
    }

    public boolean isVisibleDescColumn() {
        return isVisibleDescColumn;
    }
    // 判断该列
    public CDispTroubleBeanEvent setVisibleDescColumn(String descColumn) {
        if (!TextUtils.isEmpty(descColumn)) {
            isVisibleDescColumn = true;
        }
        return this;
    }

    public CDispTroubleBeanEvent setInsertToDB(boolean insertToDB) {
        isInsertToDB = insertToDB;
        return this;
    }

    public String getCaption() {

        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }


    public boolean isClearDTCStatus() {
        return clearDTCStatus;
    }

    public CDispTroubleBeanEvent setClearDTCStatus(boolean clearDTCStatus) {
        this.clearDTCStatus = clearDTCStatus;
        return this;
    }

    /**
     * 添加一个Item
     */
    public void addTroubleItem(TroubleItem troubleItem){
        if (troubleItems == null) {
            troubleItems = new ArrayList<>();
        }
        troubleItem.DF_FreeBtn_ID = df_free_index++;
        // 添加一个对象
        troubleItems.add(troubleItem);
    }

    public static class TroubleItem implements Serializable{
        // 自定义按钮的返回值 基于0x02000000开始每增加一个自增1
        public int DF_FreeBtn_ID;
        // 码号
        public String code_text="";
        // 状态
        public String states_text="";
        //描述
        public String desc_text="";
        //帮助
        public String help_text="";
        //冻结帧是否可以
        public boolean isDjzTrue;
        //iItemType这行的故障类型标识，0表示历史码，1表示当前码，2表示判定码；默认值是1
        public int iItemType =1;

        public  String systemName;

        public TroubleItem(String code_text, String states_text, String desc_text) {
            this.code_text = code_text;
            this.states_text = states_text;
            this.desc_text = desc_text;
        }
    }

    /**
     * 添加一个recycle标题栏
     */
    public void addTroubleItemTitle(TroubleItemTitle troubleItemTitle){
        if (troubleItemTitles == null) {
            troubleItemTitles = new ArrayList<>();
        }
        // 添加一个标题对象
        troubleItemTitles.add(troubleItemTitle);
    }

    public static class TroubleItemTitle implements Serializable{
        // 码号
        public String code_text_title;
        // 状态
        public String states_text_title;
        //描述
        public String desc_text_desc;

        public TroubleItemTitle(String code_text_title, String states_text_title, String desc_text_desc) {
            this.code_text_title = code_text_title;
            this.states_text_title = states_text_title;
            this.desc_text_desc = desc_text_desc;
        }
    }


    /**
     * init初始化所有数据
     */
    public void resetConstruct(){
        //TODO 还原所有属性
        isInsertToDB=false;
        isVisibleCodeColumn=false;
        isVisibleStateColumn = false;
        isVisibleDescColumn = false;
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        df_free_index = CDispConstant.PageTroubleType.DF_ID_DTCIDX_0;//0x02000000;
        Caption = "";
        clearDTCStatus  = true;
        if (troubleItems!=null){
            troubleItems.clear();
        }
        if (troubleItemTitles!=null){
            troubleItemTitles.clear();
        }

    }


}
