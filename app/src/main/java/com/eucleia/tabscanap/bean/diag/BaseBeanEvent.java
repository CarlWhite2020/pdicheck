package com.eucleia.tabscanap.bean.diag;

import com.alibaba.fastjson.JSON;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @param <T>
 */
public class BaseBeanEvent<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 界面显示左右
     */
    private int dispType = CDispConstant.PageDisp.DISP_LEFT;

    /**
     * 线程锁
     */
    protected Lock lock;
    protected Condition condition;
    public boolean isLocked;

    private boolean isSearchModel;//是否处于搜索模式中
    private String searchTxt = "";    // 返回值标识
    protected List<T> copyList = new ArrayList<>();

    /**
     * 事件码
     */
    public int event_what;
    protected int backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
    protected int objKey;
    private volatile boolean isShowModel = true;

    public String getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public List<T> getCopyList() {
        return copyList;
    }


    public void setCopyList(List<T> copyList) {
        this.copyList.clear();
        this.copyList.addAll(copyList);
    }

    public boolean isSearchModel() {
        return isSearchModel;
    }

    public void setSearchModel(boolean searchModel) {
        isSearchModel = searchModel;
    }

    public BaseBeanEvent() {
        lock = new ReentrantLock();
        condition = lock.newCondition();

    }

    public int getDispType() {
        return dispType;
    }

    public BaseBeanEvent setDispType(int nDispType) {
        this.dispType = nDispType;
        return this;
    }

    public boolean isShowModel() {
        return isShowModel;
    }

    public void setShowModel(boolean showModel) {
        isShowModel = showModel;
    }

    public void lockAndWait() {
        if (lock != null) {
            lock.lock();
            isLocked = true;
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();

            }
        }
    }

    public void lockAndSignal() {
        if (lock != null) {
            lock.lock();
            try {
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void lockAndSignalAll() {
        if (lock != null) {
            lock.lock();
            isLocked = false;
            try {
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();

            }
        }
    }

    public int getBackFlag() {
        int tempBackFlag = backFlag;
        backFlag = CDispConstant.PageButtonType.DF_ID_NOKEY;
        return tempBackFlag;
    }

    public void setBackFlag(int backFlag) {
        this.backFlag = backFlag;
    }

    public int getObjKey() {
        return objKey;
    }

    public void setObjKey(int objKey) {
        this.objKey = objKey;
    }
}
