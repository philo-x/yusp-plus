package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmLogDto;
import cn.com.yusys.yusp.oca.service.log.CommonLogInfo;
import cn.com.yusys.yusp.oca.service.log.DispatchLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * @author yusys
 * @version 1.0
 * @date 2020/7/15 10:07
 */
@Component
@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private static final String UNKNOWN_IP = "unknown";

    @Autowired
    private DispatchLogService dispatchLogService;

    @Pointcut(value = "@annotation(cn.com.yusys.yusp.oca.annotation.Log)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void saveLog(JoinPoint pjp) throws Throwable {
        //解析SpringEL获取动态参数
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Log log = signature.getMethod().getAnnotation(Log.class);

        AdminSmLogDto logDto = new AdminSmLogDto();
        logDto.setOperObjId(log.operObjId());
        logDto.setOperFlag(log.operFlag());
        logDto.setContent(log.content());
        logDto.setLoginIp(getIpAddress());
        logDto.setLogTypeId(log.type());
        // 添加前端未传数据
        logDto.setOperTime(DateUtils.formatDateTimeByDef(new Date()));
        logDto.setUserId(SessionUtils.getUserId());
        logDto.setOrgId(SessionUtils.getUserOrganizationId());
        CommonLogInfo commonLogInfo = new CommonLogInfo();
        commonLogInfo.setLogInfo(logDto);
        //保持日志
        try {
            dispatchLogService.handleLog(commonLogInfo);
        } catch (Exception e) {
            logger.error("保存日志失败：{}", e.getMessage(), e);
        }
    }

    public String getIpAddress() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return UNKNOWN_IP;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
