package com.eucleia.tabscanap.bean.diag;


import com.eucleia.tabscanap.bean.diag.child.DTC;
import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CDispFrameBeanEvent extends BaseBeanEvent {

    public final static int INIT_DATA = 1060;
    public final static int SET_HELP_DISPLAY = 1061;
    public final static int ADD_ITEMS = 1062;
    public final static int SET_ITEMS_STATE = 1063;
    public final static int SET_ITEMS_SCAN = 1064;
    public final static int SET_SCAN_STATE = 1065;
    public final static int SET_TOP_FUN_STATE = 1066;
    public final static int SET_SYS_FUN_DISPLAY = 1067;
    public final static int SET_RIGHT_VISIBLE = 1068;
    public final static int SHOW = 1069;
    public final static int NOTIFYNUM = 1070;

    // 返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
    // 动态创建、刷新标识
    private boolean isRefreshButtonLayout = false;
    // 标题文本
    private String strCaption = "";
    // 帮助按钮是否显示,显示实际,显示全部,一键扫描,暂停扫描,一键清码
    private boolean helpEnabled = false;
    private boolean helpDisplayEnabled = false;
    private boolean reportEnabled = false;
    private boolean showAllDisplay = false;
    private boolean showActDisplay = true;
    private boolean showAllEnabled = false;
    private boolean showActEnabled = false;
    private boolean scanDisplay = true;
    private boolean pauseScanDisplay = false;
    private boolean scanEnabled = true;
    private boolean pauseScanEnabled = false;
    private boolean clearEnabled = false;
    private boolean cancelEnabled = true;

    // 顶层功能区，显示与隐藏
    private int funState = 0;
    // 顶层功能区，是否显示
    private boolean funRver = false;
    private boolean funRdtc = false;
    private boolean funCdtc = false;
    private boolean funRds = false;
    private boolean funActtest = false;
    private boolean funSpecfun = false;
    // 右边界面显示
    private boolean rightVisible = false;
    private boolean buttonBarVisible = true;
    private float progress = 0;
    private boolean clearCoding = false;
    private boolean pauseScan = false;

    private int iTotalSystem = 0;
    private Map<Integer, Boolean> mapHaveDTCS = new ConcurrentHashMap<>();

    /**
     * {@link CDispConstant.PageDiagnosticType#DF_SCAN_START}
     * {@link CDispConstant.PageDiagnosticType#DF_SCAN_END}
     */
    private int scanState = CDispConstant.PageDiagnosticType.DF_SCAN_END;

    private List<SysItem> sysItems;

    public boolean isbResetInitdata() {
        return bResetInitdata;
    }

    public void setbResetInitdata(boolean bResetInitdata) {
        this.bResetInitdata = bResetInitdata;
    }

    private boolean bResetInitdata = true;

    // 构造函数
    public CDispFrameBeanEvent() {
        this.setDispType(CDispConstant.PageDisp.DISP_LEFT);
    }

    // 清空存储的数据信息
    public void reSetInitData() {
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        isRefreshButtonLayout = false;
        strCaption = "";
        helpEnabled = false;
        helpDisplayEnabled = false;
        reportEnabled = false;
        showAllDisplay = false;
        showActDisplay = true;
        showAllEnabled = false;
        showActEnabled = false;
        scanDisplay = true;
        pauseScanDisplay = false;
        scanEnabled = true;
        pauseScanEnabled = false;
        clearEnabled = false;
        cancelEnabled = true;
        funState = 0;
        funRver = false;
        funRdtc = false;
        funCdtc = false;
        funRds = false;
        funActtest = false;
        funSpecfun = false;
        rightVisible = false;
        buttonBarVisible = true;
        progress = 0;
        clearCoding = false;
        pauseScan = false;
        iTotalSystem = 0;
        mapHaveDTCS.clear();
        scanState = CDispConstant.PageDiagnosticType.DF_SCAN_END;
        if (sysItems != null) {
            sysItems.clear();
        }

        bResetInitdata = true;
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

    public CDispFrameBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public boolean getHelpEnabled() {
        return helpEnabled;
    }

    public CDispFrameBeanEvent setHelpEnabled(boolean helpEnabled) {
        this.helpEnabled = helpEnabled;
        this.helpDisplayEnabled = helpEnabled;
        return this;
    }

    public boolean getHelpDisplayEnabled() {
        return helpDisplayEnabled;
    }

    public CDispFrameBeanEvent setHelpDisplayEnabled(boolean helpDisplayEnabled) {
        this.helpDisplayEnabled = helpDisplayEnabled;
        return this;
    }

    public boolean getReportEnabled() {
        return reportEnabled;
    }

    public void setReportEnabled(boolean reportEnabled) {
        this.reportEnabled = reportEnabled;
    }

    public boolean getShowAllDisplay() {
        return showAllDisplay;
    }

    public void setShowAllDisplay(boolean showAllDisplay) {
        this.showAllDisplay = showAllDisplay;
    }

    public boolean getShowAllEnabled() {
        return showAllEnabled;
    }

    public void setShowAllEnabled(boolean showAllEnabled) {
        this.showAllEnabled = showAllEnabled;
    }

    public boolean getShowActDisplay() {
        return showActDisplay;
    }

    public void setShowActDisplay(boolean showActDisplay) {
        this.showActDisplay = showActDisplay;
    }

    public boolean getShowActEnabled() {
        return showActEnabled;
    }

    public void setShowActEnabled(boolean showActEnabled) {
        this.showActEnabled = showActEnabled;
    }

    public boolean getScanDisplay() {
        return scanDisplay;
    }

    public void setScanDisplay(boolean scanDisplay) {
        this.scanDisplay = scanDisplay;
    }

    public boolean getScanEnabled() {
        return scanEnabled;
    }

    public void setScanEnabled(boolean scanEnabled) {
        this.scanEnabled = scanEnabled;
    }

    public boolean getPauseScanDisplay() {
        return pauseScanDisplay;
    }

    public void setPauseScanDisplay(boolean pauseScanDisplay) {
        this.pauseScanDisplay = pauseScanDisplay;
    }

    public boolean getPauseScanEnabled() {
        return pauseScanEnabled;
    }

    public void setPauseScanEnabled(boolean pauseScanEnabled) {
        this.pauseScanEnabled = pauseScanEnabled;
    }

    public boolean getClearEnabled() {
        return clearEnabled;
    }

    public void setClearEnabled(boolean clearEnabled) {
        this.clearEnabled = clearEnabled;
    }

    public boolean getCancelEnabled() {
        return cancelEnabled;
    }

    public void setCancelEnabled(boolean cancelEnabled) {
        this.cancelEnabled = cancelEnabled;
    }

    public int getTotalSystem() {
        return iTotalSystem;
    }

    public CDispFrameBeanEvent setTotalSystem(int iItems) {
        this.iTotalSystem += iItems;
        return this;
    }

    public float getProgress() {
        return progress;
    }

    public CDispFrameBeanEvent setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public void clearProgress() {
        for (SysItem sysItem : this.sysItems) {
            sysItem.setProgress(0.0f);
        }
        this.progress = 0.0f;
    }

    public boolean isClearCoding() {
        return clearCoding;
    }

    public void setClearCoding(boolean clearCoding) {
        this.clearCoding = clearCoding;
    }

    public boolean isPauseScan() {
        return pauseScan;
    }

    public void setPauseScan(boolean pauseScan) {
        this.pauseScan = pauseScan;
    }

    // 扫描状态
    public int getScanState() {
        return scanState;
    }

    public CDispFrameBeanEvent setScanState(int scanState) {
        this.scanState = scanState;
        return this;
    }

    public List<SysItem> getSysItems() {
        return sysItems;
    }

    public int nextId = 0x00000000;
    public static int sysAdded = 0x00010000;
    public static int sysSubAdded = 0x00000001;

    public CDispFrameBeanEvent addSysItem(SysItem sysItem) {
        if (this.sysItems == null) {
            this.sysItems = new ArrayList<>();
        }
        sysItem.setDfId(nextId);
        nextId += sysAdded;
        this.sysItems.add(sysItem);
        return this;
    }

    public int getFunState() {
        return funState;
    }

    public CDispFrameBeanEvent setFunState(int funState) {
        this.funState = funState;
        // 通过运算符 确认需要显示的功能区按钮
        this.funRver = (CDispConstant.PageDiagnosticType.DF_FUN_RVER & this.funState) != 0;
        this.funRdtc = (CDispConstant.PageDiagnosticType.DF_FUN_RDTC & this.funState) != 0;
        this.funCdtc = (CDispConstant.PageDiagnosticType.DF_FUN_CDTC & this.funState) != 0;
        this.funRds = (CDispConstant.PageDiagnosticType.DF_FUN_RDS & this.funState) != 0;
        this.funActtest = (CDispConstant.PageDiagnosticType.DF_FUN_ACTTEST & this.funState) != 0;
        this.funSpecfun = (CDispConstant.PageDiagnosticType.DF_FUN_SPECFUN & this.funState) != 0;
        return this;
    }

    public boolean isRightVisible() {
        return rightVisible;
    }

    public CDispFrameBeanEvent setRightVisible(boolean rightVisible) {
        this.rightVisible = rightVisible;
        return this;
    }

    public boolean isButtonBarVisible() {
        return buttonBarVisible;
    }

    public CDispFrameBeanEvent setButtonBarVisible(boolean buttonBarVisible) {
        this.buttonBarVisible = buttonBarVisible;
        return this;
    }

    public boolean isFunRver() {
        return funRver;
    }

    public boolean isFunRdtc() {
        return funRdtc;
    }

    public boolean isFunCdtc() {
        return funCdtc;
    }

    public boolean isFunRds() {
        return funRds;
    }

    public boolean isFunActtest() {
        return funActtest;
    }

    public boolean isFunSpecfun() {
        return funSpecfun;
    }

    public boolean isHaveDTCS() {
        return mapHaveDTCS.size() > 0;
    }

    public void setHaveDTCS(int i, int j, boolean haveDTCS) {
        if (haveDTCS) {
            mapHaveDTCS.put(i * 1000 + j, haveDTCS);
        } else {
            mapHaveDTCS.remove(i * 1000 + j);
        }
    }

    public void clearHaveDTCS() {
        mapHaveDTCS.clear();
    }

    public static class Item implements Serializable {
        private boolean isBlue;

        public boolean isBlue() {
            return isBlue;
        }

        public void setBlue(boolean blue) {
            isBlue = blue;
        }

    }

    // 系统组
    public static class SysItem extends Item {
        private String sysName;
        private List<SysSubItem> sysSubItems;
        private float progress = 0.0f;
        private boolean extend = true;
        private int dfId;
        private int nextId;

        public String getSysName() {
            return sysName;
        }

        public SysItem setSysName(String sysName) {
            this.sysName = sysName;
            return this;
        }

        public int getDfId() {
            return dfId;
        }

        public SysItem setDfId(int dfId) {
            this.dfId = dfId;
            this.nextId = this.dfId;
            return this;
        }

        public List<SysSubItem> getSysSubItems() {
            return sysSubItems;
        }

        public SysItem addSysSubItem(SysSubItem sysSubItem) {
            if (this.sysSubItems == null) {
                this.sysSubItems = new ArrayList<>();
            }
            sysSubItem.setDfId(nextId);
            nextId += sysSubAdded;
            this.sysSubItems.add(sysSubItem);
            return this;
        }

        public float getProgress() {
            return progress;
        }

        public SysItem setProgress(float progress) {
            this.progress = progress;
            return this;
        }

        public boolean isExtend() {
            return extend;
        }

        public SysItem setExtend(boolean extend) {
            this.extend = extend;
            return this;
        }
    }

    // 系统子项
    public static class SysSubItem extends Item {
        private String subName;
        private String promptText;
        private int state = 0;
        private int codeCount = 0;
        private int dfId;
        private int iNo = 0;
        private int showNO = 0;
        private SysItem sysItem;
        private List<DTC> haveRepairTroubleBeans;

        public String getSubName() {
            return subName;
        }

        public SysSubItem setSubName(String subName) {
            this.subName = subName;
            return this;
        }

        public int getNo() {
            return iNo;
        }

        public SysSubItem setNo(int iUseNo) {
            this.iNo = iUseNo;
            return this;
        }

        public int getShowNO() {
            return showNO;
        }

        public SysSubItem setShowNO(int showNO) {
            this.showNO = showNO;
            return this;
        }

        public int getDfId() {
            return dfId;
        }

        public SysSubItem setDfId(int dfId) {
            this.dfId = dfId;
            return this;
        }

        public String getPromptText() {
            return promptText;
        }

        public SysSubItem setPromptText(String promptText) {
            this.promptText = promptText;
            return this;
        }

        public SysItem getSysItem() {
            return sysItem;
        }

        public SysSubItem setSysItem(SysItem sysItem) {
            this.sysItem = sysItem;
            return this;
        }

        public int getState() {
            return state;
        }

        public SysSubItem setState(int state) {
            this.state = state;
            if (this.state >= 4) {
                this.codeCount = this.state - 4;
            }
            return this;
        }

        public int getCodeCount() {
            return codeCount;
        }

        public SysSubItem setCodeCount(int codeCount) {
            this.codeCount = codeCount;
            return this;
        }

        public List<DTC> getHaveRepairTroubleBeans() {
            return haveRepairTroubleBeans;
        }

        public SysSubItem setHaveRepairTroubleBeans(List<DTC> haveRepairTroubleBeans) {
            this.haveRepairTroubleBeans = haveRepairTroubleBeans;
            return this;
        }
    }

}
