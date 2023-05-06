package com.eucleia.tabscanap.bean.diag;


import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.eucleia.tabscanap.jni.diagnostic.CDispMenu;
import com.eucleia.tabscanap.constant.CDispConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CDispMenuBeanEvent extends BaseBeanEvent {


    public final static int SHOW = 1010;

    // 自定义按钮点击返回值 基于0x02000000开始每增加一个自增1
    private int df_free_index = CDispConstant.PageMenuType.DF_ID_MENU0;//0x02000000;
    // 按键返回值标识
    private int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;



    // 记录用户点击 item 坐标,用于做历史记录颜色改变
    private int selectItemIndex=-1;
    // 界面标题
    private String mTitle = "";
    // 是否排序
    private boolean isSort = false;
    // 帮助文本
    private String mHelpStr = "";

    // MenuItem 集合
    private List<MenuItem> mMenuItems = new ArrayList<>();

    private boolean isShow = false;//是否show过

    public int mLastSelectPage = 0;//上次选中的页码

    private boolean isHasImage = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isHasImage() {
        return isHasImage;
    }

    public void setHasImage(boolean hasImage) {
        isHasImage = hasImage;
    }

    public int getmLastSelectPage() {
        return mLastSelectPage;
    }

    public void setmLastSelectPage(int mLastSelectPage) {
        this.mLastSelectPage = mLastSelectPage;
    }

    // 构造函数
    public CDispMenuBeanEvent() {
        df_free_index = CDispConstant.PageMenuType.DF_ID_MENU0;//0x02000000;
        selectItemIndex = -1;
        mTitle = "";
        isSort = false;
        mHelpStr = "";
        if (mMenuItems!=null){
            mMenuItems.clear();
        }else {
            mMenuItems = new ArrayList<>();
        }
        isShow = false;
        mLastSelectPage = 0;
    }

    public void reSetInitData(){
        df_free_index = CDispConstant.PageMenuType.DF_ID_MENU0;//0x02000000;
        selectItemIndex = -1;
        mTitle = "";
        isSort = false;
        mHelpStr = "";
        if (mMenuItems!=null){
            mMenuItems.clear();
        }
        isShow = false;
        mLastSelectPage = 0;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSort() {
        return isSort;
    }

    public void setSort(boolean sort) {
        isSort = sort;
    }

    public String getHelpStr() {
        return mHelpStr;
    }

    public void setHelpStr(String helpStr) {
        mHelpStr = helpStr;
    }

    public List<MenuItem> getMenuItems() {
        return mMenuItems;
    }

    public void setmMenuItems(List<MenuItem> mMenuItems) {
        this.mMenuItems = mMenuItems;
    }


    public int getSelectItemIndex() {
        return selectItemIndex;
    }

    public void setSelectItemIndex(int selectItemIndex) {
        this.selectItemIndex = selectItemIndex;
    }

    /**
     * 添加一个Item
     */
    public void addMenuItem(MenuItem menuItem){
        if (mMenuItems == null) {
            mMenuItems = new ArrayList<>();
        }
        if (copyList == null){
            copyList = new ArrayList<>();
        }
        menuItem.DF_FreeBtn_ID = df_free_index++;

        // 添加一个对象
        mMenuItems.add(menuItem);
        copyList.add(menuItem);

    }


    /**
     * 动态添加按钮类
     */
    public static class MenuItem implements Serializable {

        // 自定义按钮的返回值 基于0x02000000开始每增加一个自增1
        public int DF_FreeBtn_ID;
        // menu文本
        public String menu_text;
        // menu图片
        public String menu_url;
        public Drawable menu_Image;
        //是否被点击
        public boolean clicked=false;

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }

        public void setMenu_url(String menu_url){
            this.menu_url = menu_url;
            if (!TextUtils.isEmpty(menu_url)) {
                this.menu_Image = Drawable.createFromPath(menu_url);
            }
        }

        public MenuItem(String menu_text, String menu_url) {
            this.menu_text = menu_text;
            this.menu_url = menu_url;
            if (!TextUtils.isEmpty(menu_url)) {
                this.menu_Image = Drawable.createFromPath(menu_url);
            }
        }

        public MenuItem(String menu_text) {
            this.menu_text = menu_text;
        }
    }

    @Override
    public void lockAndSignalAll() {
        if (this.backFlag == CDispConstant.PageButtonType.DF_ID_BACK) {
            CDispMenu.clearClickMap(getTitle()+getMenuItems().size(), getDispType());
        }
        super.lockAndSignalAll();
    }
}
