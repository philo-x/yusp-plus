package cn.com.yusys.yusp.oca.utils;


import java.util.ArrayList;
import java.util.List;


/**
 * @author yusys
 */
public class ListUtils {
    public static List<List<String>> splitList(List<String> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        List<List<String>> result = new ArrayList<List<String>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<String> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    public static List<List<?>> splitList1(List<?> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        List<List<?>> result = new ArrayList<List<?>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}
