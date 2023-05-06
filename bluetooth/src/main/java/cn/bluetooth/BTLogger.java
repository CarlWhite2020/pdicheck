package cn.bluetooth;

import cn.wandersnail.commons.util.AbstractLogger;

/**
 * date: 2020/5/20 10:54.
 * author: zengfansheng
 */
class BTLogger extends AbstractLogger {

    /**
     * 日志单例.
     */
    static final BTLogger INSTANCE = new BTLogger();

    @Override
    protected boolean accept(final int priority,
                             final String tag,
                             final String msg) {
        return BTManager.isDebugMode;
    }
}
