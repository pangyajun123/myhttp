package com.iwbfly.myhttp.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 09:44
 **/
public class NameUtils {
    /**
     * 按驼峰命名法的规则将字符串分割
     * @param name
     * @return
     */
    public static String[] splitCamelName(String name) {
        int len = name.length();
        List<String> names = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = name.charAt(i);
            if (Character.isUpperCase(ch)) {
                String item = builder.toString();
                if (StringUtils.isNotBlank(item)) {
                    names.add(item);
                }
                builder = new StringBuilder();
                ch = Character.toLowerCase(ch);
            }
            builder.append(ch);
        }
        String last = builder.toString();
        if (StringUtils.isNotBlank(last)) {
            names.add(last);
        }
        String[] result = new String[names.size()];
        return names.toArray(result);
    }

    public static String setterName(String name) {
        String[] strs = splitCamelName(name);
        String prefix = strs[0];
        if (prefix.equals("set")) {
            return name;
        } else if (prefix.equals("is")) {
            StringBuilder builder = new StringBuilder("set");
            for (int i = 1; i < strs.length; i++) {
                String str = strs[i];
                builder.append(Character.toUpperCase(str.charAt(0)) + str.substring(1));
            }
            return builder.toString();
        } else {
            StringBuilder builder = new StringBuilder("set");
            for (int i = 0; i < strs.length; i++) {
                String str = strs[i];
                builder.append(Character.toUpperCase(str.charAt(0)) + str.substring(1));
            }
            return builder.toString();
        }
    }

}
