package com.eucleia.tabscanap.bean.diag;

import android.text.TextUtils;


import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CDispDataStreamBeanEvent extends BaseBeanEvent {

    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#InitData(int, String, int)}
     */
    public final static int INIT_DATA = 1080;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#SetProgressPercent(int, int, int)}
     */
    public final static int SET_PROGRESS_PERCENT = 1081;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#SetHead(int, String, String, String, String)}
     */
    public final static int SET_HEAD = 1082;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#AddItems(int, String, String, String, String, String)}
     */
    public final static int ADD_ITEMS = 1083;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#SetUnit(int, int, String)}
     */
    public final static int SET_UNIT = 1084;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#SetRange(int, int, String, String)}
     */
    public final static int SET_RANGE = 1085;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#SetHelp(int, int, String)}
     */
    public final static int SET_HELP = 1086;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#FlashValue(int, int, String)}
     */
    public final static int FLASH_VALUE = 1087;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#GetUpdataItems(int)}
     */
    public final static int GET_UPDATA_ITEMS = 1088;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#GetItemsUpdata(int, int)}
     */
    public final static int GET_ITEMS_UPDATA = 1089;
    /**
     * {@link com.eucleia.tabscanap.jni.diagnostic.CDispDataStream#Show(int)}
     */
    public final static int SHOW = 1090;

    // 返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
    // 动态创建、刷新标识
    private boolean isRefreshButtonLayout = true;
    // 标题文本
    private String strCaption = "";
    // 当前进度
    private int iPercent = 0;
    // 进度总值
    private int iAllPercent = 0;
    // 表头
    private List<HeadItem> headItems;
    // 行数据
    private List<RowItem> rowItems;
    private boolean rangeEnable = false;
    private Set<String> titleSet = new HashSet<>();

    // 待刷新的行号集合
    private List<Integer> refreshRowNumber = new ArrayList<>();
    private boolean visiable = true;
    private boolean bNextButtonVisiable = false;
    private boolean bFullScreen = false;
    private long firstTime = 0;
    private boolean isLockData;

    public boolean isLockData() {
        return isLockData;
    }

    public void setLockData(boolean lockData) {
        isLockData = lockData;
        if (!isLockData) {
            lockAndSignalAll();
        }
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public void reSetAllData() {
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        isRefreshButtonLayout = true;
        strCaption = "";
        iPercent = 0;
        iAllPercent = 0;
        if (headItems != null) {
            headItems.clear();
        }
        if (rowItems != null) {
            rowItems.clear();
        }
        rangeEnable = false;
        if (refreshRowNumber != null) {
            refreshRowNumber.clear();
        }
        visiable = true;
        bFullScreen = false;
        titleSet.clear();
    }


    public boolean addTitle(String title) {
        if (titleSet.contains(title)) return false;
        titleSet.add(title);
        return true;
    }

    public Set<String> getTitleSet() {
        return titleSet;
    }

    public void setTitleSet(Set<String> titleSet) {
        this.titleSet = titleSet;
    }


    public boolean isVisiable() {
        return visiable;
    }

    public boolean isbFullScreen() {
        return bFullScreen;
    }

    public void setbFullScreen(boolean bFullScreen) {
        this.bFullScreen = bFullScreen;
    }

    public CDispDataStreamBeanEvent setVisiable(boolean visiable) {
        this.visiable = visiable;
        return this;
    }

    public CDispDataStreamBeanEvent setRangeEnable(boolean rangeEnable) {
        this.rangeEnable = rangeEnable;
        return this;
    }

    public boolean isRangeEnable() {
        return rangeEnable;
    }



    public boolean isRefreshButtonLayout() {
        return isRefreshButtonLayout;
    }

    public CDispDataStreamBeanEvent setRefreshButtonLayout(boolean refreshButtonLayout) {
        isRefreshButtonLayout = refreshButtonLayout;
        return this;
    }

    public String getStrCaption() {
        return strCaption;
    }

    public CDispDataStreamBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public int getiPercent() {
        return iPercent;
    }

    public CDispDataStreamBeanEvent setiPercent(int iPercent) {
        this.iPercent = iPercent;
        return this;
    }

    public int getiAllPercent() {
        return iAllPercent;
    }

    public CDispDataStreamBeanEvent setiAllPercent(int iAllPercent) {
        this.iAllPercent = iAllPercent;
        return this;
    }

    public CDispDataStreamBeanEvent addHeadItems(HeadItem headItem) {
        if (this.headItems == null) {
            this.headItems = new ArrayList<>();
        }
        this.headItems.add(headItem);
        return this;
    }

    public CDispDataStreamBeanEvent addRowItems(RowItem rowItem) {
        if (this.rowItems == null) {
            this.rowItems = new ArrayList<>();
        }
        rowItem.setIndex(rowItems.size());
        this.rowItems.add(rowItem);
        return this;
    }

    public List<HeadItem> getHeadItems() {
        return headItems;
    }

    public List<RowItem> getRowItems() {
        return rowItems;
    }

    public List<Integer> getRefreshRowNumber() {
        return refreshRowNumber;
    }

    public CDispDataStreamBeanEvent setRefreshRowNumber(List<Integer> refreshRowNumber) {
        this.refreshRowNumber = refreshRowNumber;
        return this;
    }

    public boolean isbNextButtonVisiable() {
        return bNextButtonVisiable;
    }

    public CDispDataStreamBeanEvent setbNextButtonVisiable(boolean bNextButtonVisiable) {
        this.bNextButtonVisiable = bNextButtonVisiable;
        return this;
    }

    /**
     * 表头
     */
    public static class HeadItem implements Serializable {
        private String strName;
        private String strValue;
        private String strRange;
        private String strUnit;

        public String getStrName() {
            return strName;
        }

        public HeadItem setStrName(String strName) {
            this.strName = strName;
            return this;
        }

        public String getStrValue() {
            return strValue;
        }

        public HeadItem setStrValue(String strValue) {
            this.strValue = strValue;
            return this;
        }

        public String getStrRange() {
            return strRange;
        }

        public HeadItem setStrRange(String strRange) {
            this.strRange = strRange;
            return this;
        }

        public String getStrUnit() {
            return strUnit;
        }

        public HeadItem setStrUnit(String strUnit) {
            this.strUnit = strUnit;
            return this;
        }

    }

    /**
     * 数据行
     */
    public static class RowItem implements Serializable {
        private String systemId;
        private int index = 0;
        private String strName;
        private String strUnit;
        private String strValue;
        private String strMinValue;
        private String strMaxValue;
        private String strHelp;
        // 是否需要刷新
        private boolean update = false;
        private String systemName;
        private boolean isFirst;

        private String recordList;

        private float value = 0;
        private boolean bInRange;
        private boolean hasMax = false;
        private boolean hasMin = false;

        private float maxValue = 0;
        private float minValue = 0;
        private boolean valueNumber = false;


        private boolean isEditMaxMin = false;
        private float tempMax;
        private float tempMin;

        public int getIndex() {
            return index;
        }

        public RowItem setIndex(int index) {
            this.index = index;
            return this;
        }

        public String getStrName() {
            return strName;
        }

        public RowItem setStrName(String strName) {
            this.strName = strName;
            return this;
        }

        public String getStrUnit() {
            return strUnit;
        }

        public RowItem setStrUnit(String strUnit) {
            this.strUnit = strUnit;
            return this;
        }

        public String getStrValue() {
            return strValue;
        }

        public RowItem setStrValue(String strValue) {
            this.strValue = strValue;
            return this;
        }

        public String getStrMinValue() {
            return strMinValue;
        }

        public RowItem setStrMinValue(String strMinValue) {
            this.strMinValue = strMinValue;
            return this;
        }

        public String getStrMaxValue() {
            return strMaxValue;
        }

        public RowItem setStrMaxValue(String strMaxValue) {
            this.strMaxValue = strMaxValue;
            return this;
        }

        public String getStrHelp() {
            return strHelp;
        }

        public RowItem setStrHelp(String strHelp) {
            this.strHelp = strHelp;
            return this;
        }

        public boolean isUpdate() {
            return update;
        }

        public RowItem setUpdate(boolean update) {
            this.update = update;
            return this;
        }

        public String getSystemName() {
            return systemName;
        }

        public RowItem setSystemName(String systemName) {
            this.systemName = systemName;
            return this;
        }

        public boolean isEditMaxMin() {
            return isEditMaxMin;
        }

        public void setEditMaxMin(boolean editMaxMin) {
            isEditMaxMin = editMaxMin;
        }

        public float getTempMax() {
            return tempMax;
        }

        public void setTempMax(float tempMax) {
            this.tempMax = tempMax;
        }

        public float getTempMin() {
            return tempMin;
        }

        public void setTempMin(float tempMin) {
            this.tempMin = tempMin;
        }

        public String getRecordList() {
            return recordList;
        }

        public void setRecordList(String recordList) {
            this.recordList = recordList;
        }

        public boolean isFirst() {
            return isFirst;
        }

        public RowItem setFirst(boolean first) {
            isFirst = first;
            return this;
        }


        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public boolean isbInRange() {
            return bInRange;
        }

        public void setbInRange(boolean bInRange) {
            this.bInRange = bInRange;
        }

        public float getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(float maxValue) {
            this.maxValue = maxValue;
        }

        public float getMinValue() {
            return minValue;
        }

        public void setMinValue(float minValue) {
            this.minValue = minValue;
        }

        public boolean isValueNumber() {
            return valueNumber;
        }

        public void setValueNumber(boolean valueNumber) {
            this.valueNumber = valueNumber;
        }




        public String getSystemId() {
            return systemId;
        }

        public String getShortSystemId() {
            if (!TextUtils.isEmpty(systemId) && systemId.length() > 2) {
                return systemId.substring(0, 2);
            }
            return "";
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
    }


}
