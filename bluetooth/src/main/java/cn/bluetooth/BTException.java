package cn.bluetooth;

/**
 * date: 2019/8/3 12:08.
 * author: zengfansheng
 */
public class BTException extends RuntimeException {
    private static final long serialVersionUID = 1164256970604106282L;

    /**
     * 异常.
     * @param message 信息
     */
    public BTException(final String message) {
        super(message);
    }

    /**
     * 异常.
     * @param message 信息
     * @param cause 异常
     */
    public BTException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 异常.
     * @param cause 异常
     */
    public BTException(final Throwable cause) {
        super(cause);
    }
}
