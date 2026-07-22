package cn.com.yusys.yusp.oca.utils;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;
import cn.com.yusys.yusp.oca.service.AdminSmMessageService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author yusys
 */
@Component
public class I18nMessageByCode {

    @Autowired
    AdminSmMessageService adminSmMessageService;
    @Autowired
    private CustomCacheService customCacheService;


    public static String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
    public static String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_TIP_MESSAGE;



    public String getMessageByCode(String code) {
        //获取当前语言类型
        String language = LocaleContextHolder.getLocale().toString().toLowerCase();
        //根据码获取信息
        String message = customCacheService.hashGet(name, redisKey + "_" + language, code);

        //如果从缓存拿不到,重新加载缓存
        if (StringUtils.isEmpty(message)) {

            //重新加载缓存
            AdminSmMessageEntity adminSmMessageEntity = adminSmMessageService.getOne(new QueryWrapper<AdminSmMessageEntity>().eq("CODE", code));
            if (Objects.nonNull(adminSmMessageEntity)) {

                message = adminSmMessageEntity.getMessage();
            }

        }

        return message;
    }
}
