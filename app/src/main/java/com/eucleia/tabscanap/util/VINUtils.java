package com.eucleia.tabscanap.util;


/**
 * VIN码校验工具类
 */
public class VINUtils {


    /**
     * VIN码是否符合规则
     *
     * @param matchStr 规则
     * @param vinStr   VIN码
     * @return
     */
    public static boolean ifMatch(String matchStr, String vinStr) {
        int matchMaxLength = matchStr.length();
        boolean result1 = vinStr.matches("[0-9]+");//false
        boolean result2 = vinStr.matches("[a-z]+");//false
        boolean result3 = vinStr.matches("[A-Z]+");//true
        boolean result4 = vinStr.matches("[\\S]+");// 所有字符可见 //true
        boolean result5 = vinStr.matches("[0-9,+,-]+");// 所有0~9,+,- //false
        boolean result6 = vinStr.matches("[0-9,A-Z]+");// 所有0~9,A~Z //true
        boolean lenthJudge = vinStr.length() <= matchMaxLength;// 输入VIN码的长度是否符合要求
        if (matchStr.contains("9")) {
            if (lenthJudge && result1) {
                return true;
            }
        } else if (matchStr.contains("icon_progress_for")) {
            if (lenthJudge && result2) {
                return true;
            }
        } else if (matchStr.contains("A")) {
            if (lenthJudge && result3) {
                return true;
            }
        } else if (matchStr.contains("F")) {// 可输入0-9， A-F， icon_progress_for-f 之间的字符
            if (lenthJudge && (result1 || result2 || result3)) {
                return true;
            }
        } else if (matchStr.contains("X")) {
            if (lenthJudge && result4) {
                return true;
            }
        } else if (matchStr.contains("+")) {
            if (lenthJudge && result5) {
                return true;
            } else if (matchStr.contains("M")) {
                if (lenthJudge && result6) {
                    return true;
                }
            }
        }

        if (matchStr.contains("M")) {
            if (lenthJudge && result6) {
                return true;
            }
        }

        return false;
    }

    public static boolean calcVin(String vin) {
        if (DigitUtils.isNumber(vin)) {
            return false;
        }
        //每一位的权重*当前字母或者数字对应的值
        //然后除开中间值相加的值与11求余与中间值比较是否相等（想加的等于10则为X）
        char[] vinChars = vin.toCharArray();

        char[] vinWeight = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
        char[][] vinKey2Value = {{'A', 1}, {'B', 2}, {'C', 3}, {'D', 4}, {'E', 5}, {'F', 6},
                {'G', 7}, {'H', 8}, {'I', 0}, {'J', 1}, {'K', 2}, {'L', 3}, {'M', 4},
                {'N', 5}, {'O', 0}, {'P', 7}, {'Q', 8}, {'R', 9}, {'S', 2}, {'T', 3},
                {'U', 4}, {'V', 5}, {'W', 6}, {'X', 7}, {'Y', 8}, {'Z', 9}};

        int temp = 0;
        int check = 0;
        for (int i = 0; i < 17; i++) {
            if (vinChars[i] >= 'a' && vinChars[i] <= 'z') {
                temp = vinChars[i] - 0x20;
            } else if ((vinChars[i] >= 'A') && (vinChars[i] <= 'Z')) {
                temp = vinChars[i];
            } else if ((vinChars[i] >= '0') && (vinChars[i] <= '9')) temp = vinChars[i] - '0';
            else return false;
            if ((temp >= 'A') && (temp <= 'Z')) {
                for (int j = 0; j < 26; j++) {
                    if (temp == vinKey2Value[j][0])
                        temp = vinKey2Value[j][1];
                }
            }
            check += temp * vinWeight[i];
        }
        int result = check % 11;
        if (result == 10) {
            return 'X' == vinChars[8];
        }
        return result == Character.digit(vinChars[8], 10);
    }



}
