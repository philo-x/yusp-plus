package cn.com.yusys.yusp.uaa.config;


import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.uaa.exception.handler.ClientAuthExceptionEntryPoint;
import cn.com.yusys.yusp.uaa.exception.handler.UaaAuthenticationFailureHandler;
import cn.com.yusys.yusp.uaa.grant.oca.OcaGrantAuthenticationConvert;
import cn.com.yusys.yusp.uaa.grant.oca.OcaGrantAuthenticationProvider;
import cn.com.yusys.yusp.uaa.provider.OcaRefreshAuthenticationProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ErrorAuthenticationFailureHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author lty
 * @auhtor yinjun
 * @description 安全配置，配置白名单等
 * @date 2020/12/28
 */
@Configuration
@AutoConfigureAfter(AuthorizationServerConfig.class)
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Autowired
    private UserDetailsService ocaUserDetailsServiceImpl;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OAuth2TokenGenerator<?> tokenGenerator;

    @Resource
    private RegisteredClientRepository registeredClientRepository;

    /**
     * @description 配置静态资源白名单
     * @author lty
     * @date 2020/12/28
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .requestMatchers("/assets/**", "/css/**", "/images/**")
                .requestMatchers("/swagger-resources/**")
                .requestMatchers("/swagger-resources")
                .requestMatchers("/favicon.ico")
                .requestMatchers("/content/**")
                // 由于在管控平台中查看每个服务的SwaggerAPI接口文档时，会先通过网关到UAA获取一个OAuth Token，然后使用这个Token再访问各个服务的/v2/api-docs接口,现在UAA只管Toke生成，不做Token校验，因此，访问UAA的/v2/api-docs接口携带的OAuth Token将无法校验。
                .requestMatchers("/v2/**")
                .requestMatchers("/doc.html")
                .requestMatchers("/h2-console/**")
                .requestMatchers("/error")
                // 不走 security 的任何过滤器
                .requestMatchers("/api/**")
                .requestMatchers("/webjars/**")
                .requestMatchers("/actuator/health")
                ;
    }


    /**
     * @description 配置认证链路白名单
     * @author lty
     * @date 2020/12/28
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.tokenEndpoint(tokenEndpoint ->
                                tokenEndpoint.accessTokenRequestConverter(ocaGrantAuthenticationConvert())
                                        .authenticationProvider(ocaGrantAuthenticationProvider())
                                        .errorResponseHandler(customAuthenticationFailureHandler())
                        )
                        .getEndpointsMatcher())
                .with(authorizationServerConfigurer, withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/logout", "/api/index", "/api/check/token").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 未从授权端点进行身份验证时重定向到登录页面
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        log.error("未从授权端点进行身份验证时重定向到登录页面", accessDeniedException);
                    }
                }))
                // 接收用户信息或客户端注册的访问令牌
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(withDefaults()));
        // 客户端登录异常
        http.exceptionHandling(handling -> handling.authenticationEntryPoint(new ClientAuthExceptionEntryPoint()));
        return http.build();
    }

    /**
     * 传入自定义的认证失败处理器，用于处理 OAuth2Error，并将其封装为研发平台需要的格式进行返回
     *
     * @see OAuth2ErrorAuthenticationFailureHandler
     * @see OAuth2ErrorHttpMessageConverter
     */
    private AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new UaaAuthenticationFailureHandler();
    }

    private OcaGrantAuthenticationProvider ocaGrantAuthenticationProvider() {
        return new OcaGrantAuthenticationProvider(ocaUserDetailsServiceImpl, tokenStore, stringRedisTemplate, tokenGenerator);
    }

    private OcaGrantAuthenticationConvert ocaGrantAuthenticationConvert() {
        return new OcaGrantAuthenticationConvert(registeredClientRepository);
    }

    private OcaRefreshAuthenticationProvider ocaRefreshAuthenticationProvider() {
        return new OcaRefreshAuthenticationProvider();
    }


}


