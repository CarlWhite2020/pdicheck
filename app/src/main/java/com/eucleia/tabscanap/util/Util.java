package com.eucleia.tabscanap.util;

import android.annotation.SuppressLint;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 项目名: TabScan
 * 包名 :  com.eucleia.tabscan.jni.diagnostic.utils
 * 文件名:  Util
 * 作者 :   sen
 * 时间 :  上午9:41 2017/3/9.
 * 描述 :  工具类
 */
public class Util {


    // 阻塞锁
    private static Lock s_lock=new ReentrantLock();
    private static Condition condition=condition=s_lock.newCondition();
    // 流程锁
    private static Lock m_lock=new ReentrantLock();

    public static void lock() {
        if (m_lock != null) {
            m_lock.lock();
        }
    }

    public static void unLock() {
        if (m_lock != null) {
            m_lock.unlock();
        }
    }

    public static void lockAndWait() {
        if (s_lock != null) {
            s_lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                s_lock.unlock();
            }
        }
    }

    public static void lockAndSignal() {
        if (s_lock != null) {
            s_lock.lock();
            try {
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                s_lock.unlock();
            }
        }
    }

    public static void lockAndSignalAll() {
        if (s_lock != null) {
            s_lock.lock();
            try {
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                s_lock.unlock();
            }
        }
    }





    /**
     * 把存在的字母都转为大写
     */

    @SuppressLint("DefaultLocale")
    public static String convertString(String str) {
        String upStr=str.toUpperCase();

        StringBuffer buf=new StringBuffer(str.length());

        for (int i=0; i < str.length(); i++) {
            if (str.charAt(i) == upStr.charAt(i)) {
                buf.append(str.charAt(i));
            } else {
                buf.append(upStr.charAt(i));
            }
        }
        return buf.toString();
    }





}
