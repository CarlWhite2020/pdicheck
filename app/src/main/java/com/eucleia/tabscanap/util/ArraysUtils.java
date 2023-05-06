package com.eucleia.tabscanap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数组工具类.
 */
public final class ArraysUtils {

    private ArraysUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断数组是否为空.
     *
     * @param objects 数组
     * @return true 空
     */
    public static boolean isEmpty(final Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 判断列表是否为空.
     *
     * @param list 集合
     * @return true 空
     */
    public static boolean isEmpty(final List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断map是否为空.
     *
     * @param map map对象
     * @return true 空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * 数组对象转换成数组字符串.
     *
     * @param objArr 对象数组
     * @return 数组字符串
     */
    public static String[] o2s(final Object[] objArr) {
        return Arrays.asList(objArr).toArray(new String[objArr.length]);
    }

    /**
     * 数组对象转换成Integer列表.
     *
     * @param objects 数组对象
     * @return Integer列表.
     */
    public static List<Integer> o2i(final Object objects) {
        List<Integer> list = new ArrayList<>();
        if (objects instanceof int[]) {
            for (int o : (int[]) objects) {
                list.add(o);
            }
        } else if (objects instanceof Integer[]) {
            list.addAll(Arrays.asList((Integer[]) objects));
        } else {
            ELogUtils.d(objects);
        }
        return list;
    }


    /**
     * 对象转换成字符串列表.
     *
     * @param objects 数组对象
     * @return 字符串列表.
     */
    public static List<String> o2s(final Object objects) {
        List<String> list = new ArrayList<>();
        if (objects instanceof Object[]) {
            for (Object o : (Object[]) objects) {
                list.add(String.valueOf(o));
            }
        } else {
            ELogUtils.d(objects);
        }
        return list;
    }

    /**
     * int数组转成int列表.
     *
     * @param intArr int数组
     * @return int列表
     */
    public static List<Integer> i2i(final int[] intArr) {
        List<Integer> list = new ArrayList<>();
        for (int j : intArr) {
            list.add(j);
        }
        return list;
    }


    /**
     * @param intList int列表
     * @return int数组
     */
    public static int[] i2i(final List<Integer> intList) {
        if (intList == null || intList.size() == 0) {
            return new int[0];
        }
        int[] arr = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            arr[i] = intList.get(i);
        }
        return arr;
    }

}
