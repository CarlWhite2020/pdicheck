package com.eucleia.tabscanap.bean.diag;

import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CDispListBeanEvent extends BaseBeanEvent {

    private String TAG = "CDispListBeanEvent";
    public final static int SHOW = 1099;

    // 按键返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;

    public static int DF_SLT_INDEX_ADDED = 0x03000100;//0x03000100

    public final static int SET_BUTTON_TEXT = 1005;
    public final static int SET_BUTTON_STATUS = 1006;
    public final static int CLEAR_BUTTON = 1007;

    // 界面是否阻塞
    private boolean isUIStatic = true;
    // 界面标题
    private String strCaption = "";

    //列表元素比例,即行宽的比例
    List<Integer> titlePercentList;

    //表头的所有数据 如:标题一,标题二,标题三,标题四
    List<String> titltList;

    List<Item> contentList;//表的每一行数据的集合

    List<ButtonItem> buttonItemList = new ArrayList<>();//按钮集合

    //nTyte 默认是DF_LS_TYPE_ORIGINAL，标识列表框的显示类型
    int UIType;

    public static List<ButtonItem> getButtonItemListOld() {
        return buttonItemListOld;
    }

    static List<ButtonItem> buttonItemListOld = new ArrayList<>();//旧按钮集合

    public int[] visibleItemIndexs;//当前List的可见项

    public int selectedPosi = -1;//选中行的行号

    private Set<String> titleSet = new HashSet<>();

    public boolean isNeedRefreshHead() {
        return isNeedRefreshHead;
    }

    public void setNeedRefreshHead(boolean needRefreshHead) {
        isNeedRefreshHead = needRefreshHead;
    }

    private boolean isNeedRefreshHead = true;//是否需要刷新数据

    public boolean isNeedRefreshList() {
        return isNeedRefreshList;
    }

    public void setNeedRefreshList(boolean needRefreshList) {
        isNeedRefreshList = needRefreshList;
    }

    private boolean isNeedRefreshList = true;//是否需要刷新数据

    private boolean isNeedRefreshButton = true;//是否需要刷新数据

    public boolean isNeedRefreshButton() {
        return isNeedRefreshButton;
    }

    public void setNeedRefreshButton(boolean needRefreshButton) {
        isNeedRefreshButton = needRefreshButton;
    }

    public int getUIType() {
        return UIType;
    }

    public void setUIType(int UIType) {
        this.UIType = UIType;
    }

    public int[] getVisibleItemIndexs() {
        return visibleItemIndexs;
    }

    public void setVisibleItemIndexs(int[] visibleItemIndexs) {
        this.visibleItemIndexs = visibleItemIndexs;
    }


    public void addTitle(final String title) {
        titleSet.add(title);
    }

    public Set<String> getTitleSet() {
        return titleSet;
    }

    public void setTitleSet(Set<String> titleSet) {
        this.titleSet = titleSet;
    }

    public int getiObjectKey() {
        return iObjectKey;
    }

    private int iObjectKey = -1;

    public void reSetData() {

        isUIStatic = true;

        strCaption = "";

        if (titlePercentList != null) {
            titlePercentList.clear();
        }
        if (titltList != null) {
            titltList.clear();
        }
        if (contentList != null) {
            contentList.clear();
        }
        if (buttonItemList != null) {
            buttonItemList.clear();
        }

        visibleItemIndexs = null;

        selectedPosi = -1;

        isNeedRefreshHead = true;
        isNeedRefreshButton = true;
        isNeedRefreshList = true;
    }

    // 构造函数
    public CDispListBeanEvent(int iObjectKey) {
        DF_SLT_INDEX_ADDED = 0x03000100;
        isUIStatic = false;
        strCaption = "";
        titlePercentList = new ArrayList<>();
        titltList = new ArrayList<>();
        contentList = new ArrayList<>();
        buttonItemList = new ArrayList<>();
        this.iObjectKey = iObjectKey;
    }

    public CDispListBeanEvent() {

    }

    public List<ButtonItem> getButtonItemList() {
        return buttonItemList;
    }

    public void setButtonItemList(List<ButtonItem> buttonItemList) {
        this.buttonItemList = buttonItemList;
    }

    public void setButtonItemListOld() {
        ELogUtils.d("cailei -- setButtonItemListOld");
        if (buttonItemListOld == null) {
            buttonItemListOld = new ArrayList<>();
        }
        buttonItemListOld.clear();
        if (buttonItemList != null) {
            for (int i = 0; i < buttonItemList.size(); i++) {
                CDispListBeanEvent.ButtonItem btnOld = new CDispListBeanEvent.ButtonItem();
                btnOld.setText(buttonItemList.get(i).getText());
                btnOld.setReturnValue(buttonItemList.get(i).getReturnValue());
                btnOld.setAvailable(buttonItemList.get(i).isAvailable());
                buttonItemListOld.add(btnOld);
            }
        }
    }

    public boolean isCompareBtnFree() {
        int iSize = 0;
        int iSizeOld = 0;
        if (buttonItemList != null) {
            iSize = buttonItemList.size();
        }
        if (buttonItemListOld != null) {
            iSizeOld = buttonItemListOld.size();
        }
        ELogUtils.d("cailei -- buttonItemList size: " + iSize + " -- buttonItemListOld size: " + iSizeOld);

        if (iSize != iSizeOld) {
            ELogUtils.d("cailei -- isCompareBtnFree false");
            return false;
        } else {
            for (int i = 0; i < iSize; i++) {
                if (!buttonItemList.get(i).getText().equals(buttonItemListOld.get(i).getText())
                        || buttonItemList.get(i).isAvailable() != buttonItemListOld.get(i).isAvailable()
                        || buttonItemList.get(i).getReturnValue() != buttonItemListOld.get(i).getReturnValue()) {
                    ELogUtils.d("cailei -- isCompareBtnFree false");
                    return false;
                }
            }
            ELogUtils.d("cailei -- isCompareBtnFree true");
            return true;
        }

    }

    //对比新旧list
    public boolean isCompareBtn() {
        int iSize = 0;
        int iSizeOld = 0;
        if (buttonItemList != null) {
            iSize = buttonItemList.size();
        }
        if (buttonItemListOld != null) {
            iSizeOld = buttonItemListOld.size();
        }

        if (iSize != iSizeOld) {
            return false;
        }

        for (int i = 0; i < iSize; i++) {
            String str1 = buttonItemList.get(i).getText();
            String str2 = buttonItemListOld.get(i).getText();
            if (str1.equals(str2)) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }



    public boolean isUIStatic() {
        return isUIStatic;
    }

    public void setUIStatic(boolean UIStatic) {
        isUIStatic = UIStatic;
    }

    public String getStrCaption() {
        return strCaption;
    }

    public int getSelectedPosi() {
        return selectedPosi;
    }

    public void setSelectedPosi(int selectedPosi) {
        this.selectedPosi = selectedPosi;
    }

    public CDispListBeanEvent setStrCaption(String strCaption) {
        this.strCaption = strCaption;
        return this;
    }

    public List<Integer> getTitlePercentList() {
        return titlePercentList;
    }

    public void setTitlePercentList(List<Integer> titlePercentList) {
        if (this.titlePercentList != null) {
            this.titlePercentList.clear();
        }
        this.titlePercentList = titlePercentList;
    }

    public List<String> getTitltList() {
        return titltList;
    }

    public void setTitltList(List<String> titltList) {
        if (this.titltList != null) {
            this.titltList.clear();
        }
        this.titltList = titltList;
    }

    public List<Item> getContentList() {
        return contentList;
    }

    public void setContentList(List<Item> contentList) {
        if (this.contentList != null) {
            this.contentList.clear();
        }
        this.contentList = contentList;
    }

    public class Item implements Serializable {
        List<String> singleRowContentList;//单行的所有数据
        boolean rowLockFlag;//当前一行是否不计入行号
        String systemName;


        public List<String> getSingleRowContentList() {
            return singleRowContentList;
        }

        public void setSingleRowContentList(List<String> singleRowContentList) {
            this.singleRowContentList = singleRowContentList;
        }

        public boolean isRowLockFlag() {
            return rowLockFlag;
        }

        public void setRowLockFlag(boolean rowLockFlag) {
            this.rowLockFlag = rowLockFlag;
        }

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }
    }

    //List>>>按钮
    public class ButtonItem implements Serializable {
        String text;//按钮内容
        boolean isAvailable = true;//是否可用
        // 按钮点击时的返回值
        private int returnValue;


        public int getReturnValue() {
            return returnValue;
        }

        public void setReturnValue(int returnValue) {
            this.returnValue = returnValue;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }
    }

}
