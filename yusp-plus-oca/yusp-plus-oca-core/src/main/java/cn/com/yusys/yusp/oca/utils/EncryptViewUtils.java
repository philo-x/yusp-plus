package cn.com.yusys.yusp.oca.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yinjun
 * @version 1.2
 * @description: 数据加密替换字符工具类
 * @date 2022/3/7
 **/
public class EncryptViewUtils {

    /**
     * 字符串加密
     *
     * @param strVal    字符串
     * @param spanChar  加密字符
     * @param splitChar 分隔符
     * @param beginIdx  开始下标
     * @param endIdx    结束下标
     * @return 加密后的字符串
     */
    public static String toEncryptView(String strVal, String spanChar, String splitChar, int beginIdx, int endIdx) {
        if (StringUtils.isNotEmpty(strVal)) {
            return getString(strVal, spanChar, splitChar, beginIdx, endIdx);
        }
        return strVal;
    }

    private static String getString(String strVal, String spanChar, String splitChar, int beginIdx, int endIdx) {
        // 修正开始下标位移问题
        if (beginIdx < 0) {
            beginIdx = 0;
        }

        // 匹配字符下标位置
        int matchIdx = -1;
        if (StringUtils.isNotBlank(splitChar)) {
            matchIdx = strVal.indexOf(splitChar);
        }

        if (matchIdx > -1) {
            endIdx = matchIdx == 0 ? 0 : matchIdx - 1;
            if (beginIdx > endIdx) {
                beginIdx = endIdx;
            }
        } else {
            endIdx = getEndIdx(strVal, beginIdx, endIdx);
        }

        return doEncryptView(strVal, spanChar, beginIdx, endIdx);
    }

    private static int getEndIdx(String strVal, int beginIdx, int endIdx) {
        // 修正结束下标位移问题
        if (endIdx > strVal.length()) {
            endIdx = strVal.length() - 1;
        }
        // 处理结束下标小于开始下标的问题
        if (beginIdx > endIdx) {
            endIdx = beginIdx;
        }
        return endIdx;
    }

    private static String doEncryptView(String strVal, String spanChar, int beginIdx, int endIdx) {
        StringBuilder builder = new StringBuilder();
        // 循环处理加密字符
        char[] chars = strVal.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i >= beginIdx && i <= endIdx) {
                builder.append(spanChar);
            } else {
                builder.append(chars[i]);
            }
        }

        return builder.toString();
    }


}
