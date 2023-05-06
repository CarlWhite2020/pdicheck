package com.eucleia.tabscanap.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Yoyun
 * @name ReUtils
 * @time 2017/3/16 15:08
 */

public class ReUtils {

    public static Map<String, String> reMap = new HashMap<>();

    static {
        reMap.put("A", "[A-Z]");
        reMap.put("icon_progress_for", "[icon_progress_for-z]");
        reMap.put("F", "[0-9A-F]");
        reMap.put("9", "[0-9.]");
        reMap.put("X", ".");
        reMap.put("+", "[0-9+-.]");
        reMap.put("M", "[0-9A-Z]");
    }

    /**
     * A: [A-Z]
     * icon_progress_for: [icon_progress_for-z]
     * F: [0-9A-F]
     * 9: [0-9.]
     * X: .
     * +: [0-9+-.]
     * M: [0-9A-Z]
     *
     * @param mask
     * @return
     */
    public static String mask2Restr(String mask) {
        if (TextUtils.isEmpty(mask)) {
            //throw new IllegalArgumentException("传入的掩码不能为空");
            ELogUtils.d("注意：掩码不能为空!");
            mask = "MMMMMMMMMMMMMMMMMMMM";
        }
        if (!Pattern.matches("[AaF9X+M]+", mask)) {
            throw new IllegalArgumentException("掩码格式不正确：" + mask);
        }
        int count = 0;
        StringBuilder sb = new StringBuilder();
        int lastIndex = mask.length() - 1;
        for (int i = 0; i <= lastIndex; i++) {
            String v1 = mask.substring(i, i + 1);
            String v2 = i == lastIndex ? "" : mask.substring(i + 1, i + 2);
            count++;
            if (!v1.equals(v2)) {
                sb.append(String.format("%s{%d}", reMap.get(v1), count));
                count = 0;
            }
        }
        return sb.toString();
    }
}
