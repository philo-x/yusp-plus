package cn.com.yusys.yusp.oca.interceptor;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.annotation.LoginBeforeStrategy;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器
 *
 * @author zhangsong
 * @date 2021/3/29
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoginInterceptor.class);

    private AdminSmCrelStraService adminSmCrelStraService;
    private CustomCacheService customCacheService;
    private I18nMessageByCode i18nMessageByCode;

    /**
     * 拦截器构造函数
     */
    public LoginInterceptor(AdminSmCrelStraService adminSmCrelStraService, CustomCacheService customCacheService, I18nMessageByCode i18nMessageByCode) {
        this.adminSmCrelStraService = adminSmCrelStraService;
        this.customCacheService = customCacheService;
        this.i18nMessageByCode = i18nMessageByCode;
    }

    /**
     * 前置处理器
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录拦截（优先取方法级别）
        LoginBeforeStrategy loginStrategy = null;
        if (handler instanceof HandlerMethod method) {
            loginStrategy = method.getMethodAnnotation(LoginBeforeStrategy.class);
            if (loginStrategy == null) {
                loginStrategy = method.getMethod().getDeclaringClass().getAnnotation(LoginBeforeStrategy.class);
            }
        }
        String loginCode = getLoginCode(request);

        //检查是否因为密码输入错误次数过多，而禁止登录
        String loginCodeCountCacheKey = "loginErrorCount:loginCode_%s".formatted(loginCode);
        //判断是否存在key,次数不小于限定次数--禁止登录
        String countStr = customCacheService.stringGet(loginCodeCountCacheKey, loginCodeCountCacheKey);

        if (StringUtils.nonEmpty(countStr)) {
            if (Integer.parseInt(countStr) >= Constants.SystemUserConstance.DEFAULT_MAX_PASSWORD_ERROR) {
                sendJsonMessage(response, JClientRspEntity.buildFail(ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getCode())));
                return false;
            }
        }

        if (loginStrategy == null) {
            return true;
        }

        if (StringUtils.isEmpty(loginCode)) {
            return true;
        }

        JClientRspEntity<String> checkResult = adminSmCrelStraService.checkBeforeLogin(loginCode);
        if (checkResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            return true;
        }
        sendJsonMessage(response, checkResult);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    /**
     * 自己封装返回数据
     *
     */
    private void sendJsonMessage(HttpServletResponse response, JClientRspEntity<String> JClientRspEntity) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(JClientRspEntity));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 获取用户logincode
     *
     */
    private String getLoginCode(HttpServletRequest request) {
        String loginCodeStr = "loginCode";
        if ("GET".equals(request.getMethod())) {
            return request.getParameter(loginCodeStr);
        } else if ("POST".equals(request.getMethod())) {
            try {
                RequestWrapper requestWrapper = new RequestWrapper(request);
                String body = requestWrapper.getBody();
                Map<String, Object> paramMap = JsonUtil.parseJson(body, HashMap.class);
                return paramMap.get(loginCodeStr) == null ? null : paramMap.get(loginCodeStr).toString();
            } catch (Exception e) {
                log.error("loginCode getError : ", e);
                return null;
            }
        }

        return null;
    }
}
