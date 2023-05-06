package com.eucleia.pdicheck.listener;

public interface CheckPlanParentFunListener {

    /**
     * 选择
     * @param p
     */
    void atItemClick(int p);

    /**
     * 清除
     * @param p
     */
    void clearChild(int p);

    /**
     * 全选
     * @param p
     */
    void allChild(int p);
}
