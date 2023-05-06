package com.eucleia.tabscanap.bean.diag;


import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispReportBeanEvent extends BaseBeanEvent {

    String title = "";//标题
    int errorCounts = 0;//故障总数
    //列数为集合元素个数,列的比例为各元素的比值,总数为0,如40/60 表示两列 2:3
    List<Integer> columnList;
    //表头的所有数据
    List<String> titltList;
    List<Item> contentList;//表的每一行数据的集合
    // 按键返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;

    public final static int SHOW = 1010;

    public CDispReportBeanEvent(){
        title = "";
        errorCounts = 0;
        columnList = new ArrayList<>();
        titltList = new ArrayList<>();
        contentList = new ArrayList<>();
    }

    public void reSetData(){
        title = "";
        errorCounts = 0;
        if (columnList!=null){
            columnList.clear();
        }
        if (titltList!=null){
            titltList.clear();
        }
        if (contentList!=null){
            contentList.clear();
        }
    }

    public List<String> getTitltList() {
        return titltList;
    }

    public void setTitltList(List<String> titltList) {
        this.titltList = titltList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getErrorCounts() {
        return errorCounts;
    }

    public void setErrorCounts(int errorCounts) {
        this.errorCounts = errorCounts;
    }

    public List<Integer> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Integer> columnList) {
        this.columnList = columnList;
    }

    public List<Item> getContentList() {
        return contentList;
    }

    public void setContentList(List<Item> contentList) {
        this.contentList = contentList;
    }


    public class Item implements Serializable{
        List<String> content;
        String helpStr;//帮助信息

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        public String getHelpStr() {
            return helpStr;
        }

        public void setHelpStr(String helpStr) {
            this.helpStr = helpStr;
        }
    }

}
