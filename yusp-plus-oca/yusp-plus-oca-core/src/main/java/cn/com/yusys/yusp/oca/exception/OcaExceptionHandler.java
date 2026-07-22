package cn.com.yusys.yusp.oca.exception;

import cn.com.yusys.yusp.common.utils.TableConstant;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.CacheEnum;
import cn.com.yusys.yusp.oca.job.MessageLoadTask;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.cscb.uadp.tc.cachedependency.constant.CacheLevelEnum;
import cn.cscb.uadp.tc.cachedependency.service.CacheDependencyService;
import org.slf4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author yusys
 */
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class OcaExceptionHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OcaExceptionHandler.class);
    private final CustomCacheService cacheService;
    private final MessageLoadTask messageLoadTask;

    private final CacheDependencyService cacheDependencyService;

    public OcaExceptionHandler(CustomCacheService cacheService, MessageLoadTask messageLoadTask, CacheDependencyService cacheDependencyService) {
        this.cacheService = cacheService;
        this.messageLoadTask = messageLoadTask;
        this.cacheDependencyService = cacheDependencyService;
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public JClientRspEntity<String> handleBizException(BizException e) {
        log.error("统一异常处理：BizException, 异常原因：", e);
        String errorCode = e.getErrorCode();
        String language= LocaleContextHolder.getLocale().toString().toLowerCase();
        String key = CacheEnum.MSG.getKey() + "_" + language;
        String fullKey = CacheEnum.MSG.getCacheName() + ":" + key;
        String message = cacheService.hashGet(CacheEnum.MSG.getCacheName(), key, errorCode);
        if (StringUtils.isEmpty(message)) {
            //重新加载缓存
            cacheDependencyService.recordDependencies(fullKey, CacheLevelEnum.SECONDS.getLevel(), TableConstant.ADMIN_SM_MESSAGE);
            messageLoadTask.run(null);
            message  = cacheService.hashGet(CacheEnum.MSG.getCacheName(), key, errorCode);
        }
        if (StringUtils.isEmpty(message)) {
            message = e.getMessage();
        }
        return StringUtils.isEmpty(e.getErrorCode()) ? JClientRspEntity.buildFail(message) : JClientRspEntity.buildFail(e.getErrorCode(), message);
    }

}
