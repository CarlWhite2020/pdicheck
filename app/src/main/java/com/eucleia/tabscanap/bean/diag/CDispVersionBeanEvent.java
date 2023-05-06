package com.eucleia.tabscanap.bean.diag;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class CDispVersionBeanEvent extends BaseBeanEvent {
    //电脑信息开头有三个文本值,左边:显示VIN码
    //中间的文本
    private String textTitle = "";
    //右边的文本
    private String textRight = "";
    //单个电脑信息表单的数据
    List<GroupItem> itemList = new ArrayList<>();

    private Set<String> titleSet = new HashSet<>();

    public List<GroupItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<GroupItem> itemList) {
        this.itemList = itemList;
    }

    public final static int SHOW = 1010;

    public void addList(GroupItem groupItem) {
        itemList.add(groupItem);
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public String getTextRight() {
        return textRight;
    }

    public void setTextRight(String textRight) {
        this.textRight = textRight;
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


    public class Item implements Serializable {
        String name;
        String value;

        public Item(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class GroupItem implements Serializable {
        List<Item> items;
        String title;
        String systemName;

        public GroupItem(List<Item> items, String title) {
            this.items = items;
            this.title = title;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void addItem(Item item) {
            items.add(item);
        }

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }
    }
}
