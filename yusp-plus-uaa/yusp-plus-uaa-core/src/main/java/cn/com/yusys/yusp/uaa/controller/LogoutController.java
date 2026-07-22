package cn.com.yusys.yusp.uaa.controller;

import cn.com.yusys.yusp.commons.session.context.UserContext;
import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.uaa.util.JwtParseUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Objects;

/**
 * @author lty
 * @auhtor yinjun
 * @description 登出接口
 * @date 2020/12/29
 */
@RestController
@RequestMapping(value = "/api")
public class LogoutController {

    @Autowired
    TokenStore tokenStore;

    @Resource
    private JwtDecoder jwtDecoder;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @description 登出接口，清除token对应的redis
     * @author lty
     * @date 2020/12/29
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<?> postLogoutPage(HttpServletResponse response) {

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String accessToken = null;
        try {
            accessToken = httpServletRequest.getHeader("Authorization").substring(7);
        } catch (Exception e) {
            log.error("请求的token不合法");
            return new ResponseEntity<>(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
        if (StringUtils.isBlank(accessToken)) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = jwtDecoder.decode(accessToken);
        Map<String, Object> claims = jwt.getClaims();
        UserContext userContext = JwtParseUtil.getUserContext(claims);
        String key = TokenStore.getAuthenticationKey(userContext.getClientId(), userContext.getLoginCode());
        if (!tokenStore.containsAccessToken(key, accessToken, claims)) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        // 清除redis
        tokenStore.removeAccessToken(key, accessToken);

        // 清除上下文
        if (null != auth) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, response, auth);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
