package cn.com.yusys.yusp.job.core.utils;

/**
 * 字符串分割
 *
 * @Author: lty
 * @Date: 2021/6/29
 */
public class SubStringUtils {

    public static Long[] stringToLongArray(String s) {
        String[] temps = s.split(",");
        Long[] sToLong = new Long[temps.length];
        for (int i = 0; i < temps.length; i++) {
            sToLong[i] = Long.valueOf(temps[i]);
        }
        return sToLong;
    }
}
