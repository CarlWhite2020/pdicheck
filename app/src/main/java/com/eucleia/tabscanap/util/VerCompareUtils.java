package com.eucleia.tabscanap.util;

import android.text.TextUtils;

import com.eucleia.tabscanap.constant.CharVar;

public class VerCompareUtils {

    public static boolean compareByChar(String curVer, String newVer) {
        return compareByChar(curVer, newVer, CharVar.COLON);
    }

    public static boolean compareByChar(String curVer, String newVer, String split) {
        ELogUtils.d(curVer + " & " + newVer);
        if (TextUtils.isEmpty(curVer) || TextUtils.isEmpty(newVer)) {
            return false;
        }
        curVer = curVer.replace(CharVar.VERSION, CharVar.EMPTY);
        newVer = newVer.replace(CharVar.VERSION, CharVar.EMPTY);
        if (TextUtils.isEmpty(curVer) || TextUtils.isEmpty(newVer)) {
            return false;
        }

        String[] currVerSplit = curVer.split(split);
        String[] newVerSplit = newVer.split(split);
        if (ArraysUtils.isEmpty(currVerSplit) || ArraysUtils.isEmpty(newVerSplit)
                || currVerSplit.length != newVerSplit.length) {
            return false;
        }
        for (int i = 0; i < currVerSplit.length; i++) {
            try {
                int currVerInt = Integer.parseInt(currVerSplit[i]);
                int newVerInt = Integer.parseInt(newVerSplit[i]);
                if (newVerInt > currVerInt) {
                    return true;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;

    }


}
