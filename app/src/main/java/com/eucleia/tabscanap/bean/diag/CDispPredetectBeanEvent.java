package com.eucleia.tabscanap.bean.diag;

import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class CDispPredetectBeanEvent extends BaseBeanEvent {

    public static final String PREDETECT_BEAN_EVENT_FLAG="PREDETECT_BEAN_EVENT_FLAG";

    // 按键返回值标识
    private int backFlag= CDispConstant.PageButtonType.DF_ID_NOKEY;
    private String StrCaption;
    public final static int SHOW=1010;


    List<GroupItem> items;

    public void reSetData() {
    }


    public String getStrCaption() {
        return StrCaption;
    }

    public void setStrCaption(String strCaption) {
        StrCaption=strCaption;
    }

    public List<GroupItem> getItems() {
        return items;
    }

    public void setItems(List<GroupItem> items) {
        this.items=items;
    }


    public static class ChildItem implements Serializable {
        private List<String> vecStr;
        private int iQualify;

        public List<String> getVecStr() {
            return vecStr;
        }

        public void setVecStr(List<String> vecStr) {
            this.vecStr=vecStr;
        }

        public int getiQualify() {
            return iQualify;
        }

        public void setiQualify(int iQualify) {
            this.iQualify=iQualify;
        }
    }


    public static class GroupItem implements Serializable {
        private int[] vecColWidthPercent;
        private String strDescription;
        private String strGroupName;
        private List<ChildItem> itemList;
        private List<String> headList;

        public String getStrGroupName() {
            return strGroupName;
        }

        public void setStrGroupName(String strGroupName) {
            this.strGroupName=strGroupName;
        }

        public int[] getVecColWidthPercent() {
            return vecColWidthPercent;
        }

        public void setVecColWidthPercent(int[] vecColWidthPercent) {
            this.vecColWidthPercent=vecColWidthPercent;
        }

        public String getStrDescription() {
            return strDescription;
        }

        public void setStrDescription(String strDescription) {
            this.strDescription=strDescription;
        }

        public List<ChildItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<ChildItem> itemList) {
            this.itemList=itemList;
        }

        public List<String> getHeadList() {
            return headList;
        }

        public void setHeadList(List<String> headList) {
            this.headList=headList;
        }
    }
}
