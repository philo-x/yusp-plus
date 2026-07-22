package cn.com.yusys.yusp.oca.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * 类说明
 *
 * @author zhangsong
 * @create 2020-01-07
 * @since 模块名称
 */
public class JsonUtil {
    public static JSONObject parseJson(String str) {
        return JSONObject.parseObject(str);
    }

    public static JSONArray parseJsonArray(String str) {
        return JSONArray.parseArray(str);
    }

    public static <T> T parseJson(String str, Class<T> clazz) {
         return JSONObject.parseObject(str, clazz);
    }

    public static <T> List<T> parseJsonArray(String str, Class<T> clazz) {
        return JSONArray.parseArray(str, clazz);
    }

    public static <T> T parseMap(Map map, Class<T> clazz) {
        return JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
    }

    public static String toString(JSONObject jsonObject) {
         return JSONObject.toJSONString(jsonObject);
    }

    public static String toString(JSONArray jsonArray) {
        return JSONArray.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue);
    }

    public static JSONObject deepCopy(JSONObject jsonObject) {
        return parseJson(toString(jsonObject));
    }

    public static JSONArray deepCopy(JSONArray jsonArray) {
        return parseJsonArray(toString(jsonArray));
    }

}
