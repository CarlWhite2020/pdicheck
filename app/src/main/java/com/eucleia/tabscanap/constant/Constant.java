package com.eucleia.tabscanap.constant;


import com.eucleia.pdicheck.BuildConfig;

/**
 *
 */
public class Constant {

    public static final String APP_TAG ="pdicheck";

    public static final boolean isAppBundle = false;//Google Play —— true ；Other - false

    public static String PackageName = BuildConfig.APPLICATION_ID;

    public static final int[] Protocols = new int[]{0, 2, 1, 3, 5, 4, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 6, 7, 8};

    public static int orientation;


    public static boolean isActive;
}
