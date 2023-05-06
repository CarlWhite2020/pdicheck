package com.eucleia.tabscanap.bean.diag;

import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispVehicleBeanEvent extends BaseBeanEvent {

    public final static int INIT_DATA = 1050;
    public final static int SET_HELP = 1051;
    public final static int SET_SORT = 1052;
    public final static int ADD_ITEMS = 1053;
    public final static int SET_ITEMS = 1054;
    public final static int SET_ITEMS_TEXT = 1055;
    public final static int SET_ITEMS_STATE = 1056;
    public final static int SHOW = 1057;

    // 标题文本
    private String strCaption = "";
    // 提示文本
    private String strPrompt;
    // 是否排序 true 排序 false 不排序
    private boolean bSort = true;
    // 帮助文档
    private String strHelpText;
    // 选择项集合
    private List<VehicleItem> vehicleItems;
    private int currSelectedPosition = -1;

    // 选择项下标 起始值
    private static int DF_SLT_INDEX_START = 0x0200FFFF;
    // 选项项下标 下个选择项的下标
    private int next_df_slt_index = DF_SLT_INDEX_START;
    // 选择项下标 增长值
    private static int DF_SLT_INDEX_ADDED = 0x00010000;

    // 构造函数
    public CDispVehicleBeanEvent() {
    }


    public String getStrCaption() {
        return strCaption;
    }

    public CDispVehicleBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public String getStrPrompt() {
        return strPrompt;
    }

    public CDispVehicleBeanEvent setStrPrompt(String strPrompt) {
        this.strPrompt = strPrompt;
        return this;
    }

    public int getCurrSelectedPosition() {
        return currSelectedPosition;
    }

    public CDispVehicleBeanEvent setCurrSelectedPosition(int currSelectedPosition) {
        this.currSelectedPosition = currSelectedPosition;
        return this;
    }

    public boolean isbSort() {
        return bSort;
    }

    public CDispVehicleBeanEvent setbSort(boolean bSort) {
        this.bSort = bSort;
        return this;
    }

    public String getStrHelpText() {
        return strHelpText;
    }

    public CDispVehicleBeanEvent setStrHelpText(String strHelpText) {
        this.strHelpText = strHelpText;
        return this;
    }

    public CDispVehicleBeanEvent addVehicleItem(VehicleItem item) {
        if (vehicleItems == null) {
            vehicleItems = new ArrayList<>();
        }
        if (vehicleItems.contains(item)) {
            ELogUtils.d("已存在");
            return this;
        }

        item.setDf_slt_index(next_df_slt_index);
        next_df_slt_index += DF_SLT_INDEX_ADDED;
        vehicleItems.add(item);
        return this;
    }

    public List<VehicleItem> getVehicleItems() {
        return vehicleItems;
    }


    public void reSetVehicleItemsCurrText() {
        if (vehicleItems!=null){
            for (int i=0;i<vehicleItems.size();i++){
                vehicleItems.get(i).setStrCurrText("");
                vehicleItems.get(i).setCurrPosition(-1);
            }
        }
        currSelectedPosition = -1;
    }

    public static class VehicleItem implements Serializable {

        // 备选项下标 增长值
        private static int DF_SLT_SUB_INDEX_ADDED = 0x00000001;
        // 选项的下标
        private int df_slt_index;
        // 备选项起始下标
        private int df_slt_sub_index_start;
        private String strItem;
        private String strSelectedText;
        private List<String> listStrCont;
        private boolean bState = false;
        private int currPosition = -1;

        public String getStrCurrText() {
            return strCurrText;
        }

        public void setStrCurrText(String strCurrText) {
            this.strCurrText = strCurrText;
        }

        private String strCurrText = "";

        public VehicleItem setDf_slt_index(int df_slt_index) {
            this.df_slt_index = df_slt_index;
            this.df_slt_sub_index_start = this.df_slt_index & 0xFFFF0000;
            return this;
        }

        public int getDf_slt_index() {
            return df_slt_index;
        }

        public int getCurrPosition() {
            return currPosition;
        }

        public VehicleItem setCurrPosition(int currPosition) {
            this.currPosition = currPosition;
            return this;
        }

        public String getStrItem() {
            return strItem;
        }

        public VehicleItem setStrItem(String strItem) {
            this.strItem = strItem;
            return this;
        }

        public String getStrSelectedText() {
            return strSelectedText;
        }

        public VehicleItem setStrSelectedText(String strSelectedText) {
            this.strSelectedText = strSelectedText;
            return this;
        }

        public boolean isbState() {
            return bState;
        }

        public VehicleItem setbState(boolean bState) {
            this.bState = bState;
            return this;
        }

        public VehicleItem addStrCont(String cont) {
            if (listStrCont == null) {
                listStrCont = new ArrayList<>();
            }
            listStrCont.add(cont);
            return this;
        }

        public List<String> getListStrCont() {
            return listStrCont;
        }

        public int getSubReturnIdByIndex(int index){
            return df_slt_sub_index_start + index * DF_SLT_SUB_INDEX_ADDED;
        }

        public VehicleItem setListStrCont(List<String> contList) {
            if (listStrCont == null) {
                listStrCont = new ArrayList<>();
            } else {
                listStrCont.clear();
            }
            listStrCont.addAll(contList);
            return this;
        }
    }

}
