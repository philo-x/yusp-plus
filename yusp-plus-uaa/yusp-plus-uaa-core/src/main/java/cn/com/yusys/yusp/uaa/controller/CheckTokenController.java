package cn.com.yusys.yusp.uaa.controller;

import cn.com.yusys.yusp.commons.module.constant.Constants;
import cn.com.yusys.yusp.commons.session.context.UserContext;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.uaa.controller.vo.CheckTokenParams;
import cn.com.yusys.yusp.uaa.controller.vo.UserInfoVo;
import cn.com.yusys.yusp.uaa.exception.UaaOauth2AuthenticationException;
import cn.com.yusys.yusp.uaa.util.JwtParseUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author yinjun
 * @version 1.2
 * @description: 检查token，返回UserContext需要的信息
 * @date 2022/3/11
 **/

@RestController
@RequestMapping(value = "/api")
public class CheckTokenController {

    private static final Logger log = LoggerFactory.getLogger(CheckTokenController.class);

    @Autowired
    private TokenStore tokenStore;

    @Resource
    private JwtDecoder jwtDecoder;

    @PostMapping("/check/token")
    public ResponseEntity<?> checkToken(@RequestBody CheckTokenParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");

        log.debug("[plus-uaa] check token params: {}", params.toString());
        String code = String.valueOf(HttpStatus.UNAUTHORIZED.value());
        if (null == params.getToken()) {
            return new ResponseEntity<>(new UaaOauth2AuthenticationException(code, "[plus-uaa] Token does not empty"), headers, HttpStatus.UNAUTHORIZED);
        }
        String token;
        try {
            token = resolve(params.getToken());
        } catch (Exception e) {
            log.error("[plus-uaa] Token parse error,", e);
            return new ResponseEntity<>(new UaaOauth2AuthenticationException(code, "[plus-uaa] Token parse error"), headers, HttpStatus.UNAUTHORIZED);
        }
        if (StringUtils.isEmpty(token)) {
            return new ResponseEntity<>(new UaaOauth2AuthenticationException(code, "[plus-uaa] Token does not empty"), headers, HttpStatus.UNAUTHORIZED);
        }
        Jwt jwt = jwtDecoder.decode(token);
        Map<String, Object> claims = jwt.getClaims();
        UserContext userContext = JwtParseUtil.getUserContext(claims);
        boolean contains = tokenStore.containsAccessToken(TokenStore.getAuthenticationKey(userContext.getClientId(), userContext.getLoginCode()), token, claims);
        if (!contains) {
            return new ResponseEntity<>(new UaaOauth2AuthenticationException(code, "[plus-uaa] Token has expired"), headers, HttpStatus.UNAUTHORIZED);
        } else {
            UserInfoVo userInfoVo = new UserInfoVo((String) claims.get(Constants.CLIENT_ID), (String) claims.get(Constants.USER_ID),
                    (String) claims.get(Constants.LOGIN_CODE), (String) claims.get(Constants.ORGANIZATION_ID), (String) claims.get("role_id"));
            return new ResponseEntity<>(userInfoVo, headers, HttpStatus.OK);
        }
    }


    private String resolve(String bearerToken) {
        if (StringUtils.nonEmpty(bearerToken)) {
            String[] split = bearerToken.split(" ");
            return split[1];
        }
        return null;
    }
}
