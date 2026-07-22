package cn.com.yusys.yusp.oca.utils;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author yusys
 */
public class I18nMpCacheKey {

    public static final String DATA_DICT_CACHE_KEY = "datadict";

    public static String getDataDictCacheKey() {
        String language = LocaleContextHolder.getLocale().toString().toLowerCase();
        return DATA_DICT_CACHE_KEY + "_" + language;
    }
}
