package cn.com.yusys.yusp.oca.job;


import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.domain.constants.CacheEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;
import cn.com.yusys.yusp.oca.service.AdminSmMessageService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 信息导入
 *
 * @author: zhangsong
 * @date: 2021/3/31
 */
@Component("messageLoadTask")
public class MessageLoadTask implements ITask {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageLoadTask.class);
    @Autowired
    private AdminSmMessageService adminSmMessageService;
    @Autowired
    private CustomCacheService cacheService;

    @Override
    public void run(String params) {
        log.info("messageLoadTask定时任务正在执行，参数为：{}", params);
        //删除系统提示信息
        Locale defLanguage = LocaleContextHolder.getLocale();
        if (!Locale.SIMPLIFIED_CHINESE.equals(defLanguage)) {
            LocaleContextHolder.setLocale(new Locale("zh", "CN"));
        }
        List<AdminSmMessageEntity> list = adminSmMessageService.list();
        cache(list, "zh_cn");
        try {
            LocaleContextHolder.setLocale(new Locale("en", "us"));
            list = adminSmMessageService.list();
            cache(list, "en_us");
        } catch (Exception e) {
            log.warn("未找到系统提示消息国际化表，请检查");
        } finally {
            LocaleContextHolder.setLocale(defLanguage);
        }
    }

    /**
     * 缓存清除
     *
     * @param list     缓存list
     * @param language 语言环境
     */
    private void cache(List<AdminSmMessageEntity> list, String language) {
        cacheService.clear(CacheEnum.MSG.getCacheName(),CacheEnum.MSG.getKey()+"_"+language);
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmMessageEntity::getCode, AdminSmMessageEntity::getMessage));
        cacheService.hashPut(CacheEnum.MSG.getCacheName(),CacheEnum.MSG.getKey()+"_"+language,collect,24*60*60);
    }

}
