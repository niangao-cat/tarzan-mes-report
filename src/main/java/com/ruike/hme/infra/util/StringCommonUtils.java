package com.ruike.hme.infra.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 字符串 工具类
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 17:57
 */
public class StringCommonUtils {

    public final static String BLANK = "";

    public static boolean equalsIgnoreBlank(String str, String cmpStr) {
        return Optional.ofNullable(str).orElse(BLANK).equals(Optional.ofNullable(cmpStr).orElse(BLANK));
    }

    public static boolean contains(String str, String... items) {
        if (StringUtils.isBlank(str)) {
            return false;
        }

        if (items.length != 0) {
            for (String item : items) {
                if (str.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> splitIntoList(String str) {
        return splitIntoList(str, ",");
    }

    public static List<String> splitIntoList(String str, String separator) {
        String[] strs = StringUtils.split(str, separator);
        List<String> list = new ArrayList<>();
        if (strs != null) {
            list = new ArrayList<>(Arrays.asList(strs));
        }
        return list;
    }

    public static String numberToUpperLetter(int num) {
        if (num <= 0) {
            return null;
        }
        StringBuilder letter = new StringBuilder();
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter.insert(0, ((char) (num % 26 + (int) 'A')));
            num = (num - num % 26) / 26;
        } while (num > 0);

        return letter.toString();

    }
}
