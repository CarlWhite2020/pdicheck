package com.eucleia.tabscanap.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5转换工具.
 */
public final class MDUtlis {

    private MDUtlis() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将字符串用MD5加密.
     *
     * @param str 字符串
     * @return 加密串
     */
    public static String getMd5(final String str) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            messageDigest.update(bytes);
            byte[] digest = messageDigest.digest();
            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
