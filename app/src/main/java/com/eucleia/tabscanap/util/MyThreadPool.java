package com.eucleia.tabscanap.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class MyThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    public MyThreadPool get() {
        return InClass.myExecutorService;
    }

    private MyThreadPool() {
        threadPoolExecutor = new ScheduledThreadPoolExecutor(2);
    }

    private static class InClass {
        private final static MyThreadPool myExecutorService = new MyThreadPool();
    }

}
