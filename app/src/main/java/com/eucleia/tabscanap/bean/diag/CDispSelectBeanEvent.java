package com.eucleia.tabscanap.bean.diag;

import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispSelectBeanEvent extends BaseBeanEvent {

    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#InitDataOneSelect(int, String, String, Object[], int[], int)}
     */
    public final static int INIT_DATA_ONE_SELECT = 1040;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#InitDataManySelect(int, String, Object[], Object[], Object[], int)}
     */
    public final static int INIT_DATA_MANY_SELECT = 1041;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#SetMutiSelect(int, boolean)}
     */
    public final static int SET_MUTI_SELECT = 1042;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#GetOneSelectIndex(int)}
     */
    public final static int GET_ONE_SELECT_INDEX = 1043;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#GetManySelectIndex(int)}
     */
    public final static int GET_MANY_SELECT_INDEX = 1044;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#SetHelp(int, String)}
     */
    public final static int SET_HELP = 1045;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispSelect#Show(int)}
     */
    public final static int SHOW = 1046;


    // 动态创建、刷新标识
    private boolean isRefreshButtonLayout;
    // 标题文本
    private String strCaption = "";
    // 单选或多选
    private Boolean mutiSelect = false;
    private String help = "";
    //选择结果展示
    private Boolean selectResultVisible = true;
    //
    private List<SelectItem> selectItems;

    private int uiType = CDispConstant.PageSelectType.DF_SL_TYPE_ORIGINAL;

    private String HelpTitle;
    private String RecordSecretCode;

    // 构造函数
    public CDispSelectBeanEvent() {
    }

    public void reSetInitData(){
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        strCaption = "";
        if (selectItems!=null){
            selectItems.clear();
        }
    }

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int UIType) {
        this.uiType = UIType;
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

    public CDispSelectBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public CDispSelectBeanEvent setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
        return this;
    }

    public void addSelectItem(SelectItem item) {
        if (selectItems == null) {
            selectItems = new ArrayList<>();
        }

        selectItems.add(item);
    }

    public int[] getOneSelectIndex() {
        SelectItem item = selectItems.get(0);
        int[] selectIndexs = ArraysUtils.i2i(item.getIntSelectPos());
        ELogUtils.d(selectIndexs);
        return selectIndexs;
    }

    public int[][] getManySelectIndex() {
        int[][] selects = new int[selectItems.size()][];
        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem item = selectItems.get(i);
            int[] selectIndexs = ArraysUtils.i2i(item.getIntSelectPos());
            selects[i] = selectIndexs;
        }
        ELogUtils.d(selects);
        return selects;
    }

    public Boolean getMutiSelect() {
        return mutiSelect;
    }

    public CDispSelectBeanEvent setMutiSelect(Boolean mutiSelect) {
        this.mutiSelect = mutiSelect;
        return this;
    }

    public String getHelp() {
        return help;
    }

    public CDispSelectBeanEvent setHelp(String help) {
        this.help = help;
        return this;
    }

    public String getHelpTitle() {
        return HelpTitle;
    }

    public void setHelpTitle(String helpTitle) {
        HelpTitle = helpTitle;
    }

    public Boolean getSelectResultVisible() {
        return selectResultVisible;
    }

    public CDispSelectBeanEvent setSelectResultVisible(Boolean bVisible) {
        this.selectResultVisible = bVisible;
        return this;
    }

    public String getRecordSecretCode() {
        return RecordSecretCode;
    }

    public void setRecordSecretCode(String recordSecretCode) {
        RecordSecretCode = recordSecretCode;
    }

    public static class SelectItem implements Serializable {
        private boolean mutiSelect = false;
        private String strPrompt;
        private List<String> vecStrSelect;
        private List<Integer> vIntDefSelectPos;
        private List<Integer> vIntSelectPos;
        private boolean isHide;

        public boolean isMutiSelect() {
            return mutiSelect;
        }

        public void setMutiSelect(boolean mutiSelect) {
            this.mutiSelect = mutiSelect;
        }

        public String getStrPrompt() {
            return strPrompt;
        }

        public void setStrPrompt(String strPrompt) {
            this.strPrompt = strPrompt;
        }

        public List<String> getVecStrSelect() {
            return vecStrSelect;
        }

        public void setVecStrSelect(List<String> vecStrSelect) {
            this.vecStrSelect = vecStrSelect;
        }

        public List<Integer> getIntDefSelectPos() {
            return vIntDefSelectPos;
        }

        public void setIntDefSelectPos(List<Integer> vIntDefSelectPos) {
            this.vIntDefSelectPos = vIntDefSelectPos;
        }

        public List<Integer> getIntSelectPos() {
            return vIntSelectPos;
        }

        public void setIntSelectPos(List<Integer> vIntSelectPos) {
            this.vIntSelectPos = vIntSelectPos;
        }

        public boolean isHide() {
            return isHide;
        }

        public void setHide(boolean hide) {
            isHide = hide;
        }
    }
}