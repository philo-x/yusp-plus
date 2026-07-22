package cn.com.yusys.yusp.uaa.filter;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.context.UserContext;
import cn.com.yusys.yusp.commons.util.ObjectMapperUtils;
import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.uaa.exception.UaaException;
import cn.com.yusys.yusp.uaa.util.JwtParseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author lty
 * @auhtor yinjun
 * @description 业务url的过滤器
 * @date 2022/3/14
 */
public class SingleSessionFilter implements Filter {

    private static final String ERR_CODE_90000001 = "90000001";

    public static final String IGNORE_URLS_KEY = "ignoreUrls";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SingleSessionFilter.class);
    final ObjectMapper objectMapper;

    private List<String> ignoreUrls;

    private final JwtDecoder jwtDecoder;

    public SingleSessionFilter(ObjectMapper objectMapper, JwtDecoder jwtDecoder) {
        this.objectMapper = objectMapper;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        String ignoreUrlStr = filterConfig.getInitParameter(IGNORE_URLS_KEY);
        if (StringUtils.nonEmpty(ignoreUrlStr)) {
            ignoreUrls = Arrays.stream(ignoreUrlStr.replace("*", "").split(",")).map(String::trim).collect(Collectors.toList());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String method = ((HttpServletRequest) servletRequest).getMethod();
        if (HttpMethod.OPTIONS.toString().equals(method)) {
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            try {
                this.checkAuth(servletRequest);
            } catch (UaaException e) {
                ServletOutputStream out = servletResponse.getOutputStream();
                JClientRspEntity rsp = JClientRspEntity.buildFail(e.getCode(), e.getMessage());
                out.write(objectMapper.writeValueAsBytes(rsp));
                ((HttpServletResponse) servletResponse).setStatus(401);
                out.flush();
                out.close();
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void checkAuth(ServletRequest servletRequest) throws UaaException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();


        for (String whiteUri : ignoreUrls) {
            if (uri.startsWith(whiteUri)) {
                log.info("SingleSessionFilter.checkAuth - 现在访问的 uri 地址：" + uri);
                return;
            }
        }
        String accessToken = resolve(request);
        if (StringUtils.isBlank(accessToken)) {
            log.warn("无法校验 token：token 不存在");
            throw new UaaException(ERR_CODE_90000001, "token未设置");
        }
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(accessToken);
        } catch (BadJwtException e) {
            log.warn("由于系统已经进行过重启，jwt生成的密钥发生改变，原先的jwt已经失效，需要重新登录");
            throw new UaaException(ERR_CODE_90000001, "登录失效，请重新登录");
        }
        Map<String, Object> claims = jwt.getClaims();
        UserContext userContext = JwtParseUtil.getUserContext(claims);
        UserContext.addUserContext(userContext);

        TokenStore tokenStore = SpringContextUtils.getBean(TokenStore.class);
        if (!tokenStore.containsAccessToken(TokenStore.getAuthenticationKey(userContext.getClientId(), userContext.getLoginCode()), accessToken, claims)) {
            throw new UaaException(ERR_CODE_90000001, "token 已过期");
        }
    }

    public String resolve(ServletRequest request) {

        String authorization = ((HttpServletRequest) request).getHeader("Authorization");
        if (StringUtils.nonEmpty(authorization)) {
            String[] split = ((HttpServletRequest) request).getHeader("Authorization").split(" ");
            return split[1];
        } else {
            return request.getParameter("access_token");
        }
    }
}
