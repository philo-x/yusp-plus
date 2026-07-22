package cn.com.yusys.yusp.uaa.grant.oca;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.uaa.config.token.UaaOAuth2AccessAndRefreshTokenGenerator;
import cn.com.yusys.yusp.uaa.enumerate.BusinessCodeForExceptionEnum;
import cn.com.yusys.yusp.uaa.exception.UaaImageCodeException;
import cn.com.yusys.yusp.uaa.pojo.LoginUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

/**
 * oca模式的 provider 实现类：认证逻辑的执行者
 * <p>
 * 1. 通过 supports 方法声明可处理的 Authentication 类型
 * <p>
 * 2. 在 authenticate 方法实现业务逻辑，比如验证码校验、数据库查询用户等
 * <p>
 * 3. 认证成功后返回包含用户权限的 Authentication 对象
 *
 * @author zhangyt12
 * @date 2021/9/7 11:21
 */
public class OcaGrantAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OcaGrantAuthenticationProvider.class);

    private static final String CODE = "code";
    private static final String USER_ID = "user_id";
    private static final String ORG_ID = "org_id";
    private static final String LOGIN_CODE = "login_code";
    private static final String ROLE_ID = "role_id";
    private static final String PARAM_ORG_ID = "orgId";
    private static final String PARAM_ROLE_ID = "roleId";

    private final UserDetailsService userDetailsService;

    private final TokenStore tokenStore;

    private final StringRedisTemplate stringRedisTemplate;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public OcaGrantAuthenticationProvider(UserDetailsService userDetailsService, TokenStore tokenStore, StringRedisTemplate stringRedisTemplate, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.stringRedisTemplate = stringRedisTemplate;
        this.tokenGenerator = tokenGenerator;
    }

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 请求中的登录信息
        OcaGrantAuthenticationToken authenticationToken = (OcaGrantAuthenticationToken) authentication;

        Map<String, String> params = new HashMap<>(8);
        for (Map.Entry<String, Object> entry : authenticationToken.getAdditionalParameters().entrySet()) {
            params.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        // 验证码 校验判断
        verifyCodeImage(params);

        String username = params.get("username");

        // 获取用户信息，里面会校验密码是否正确，等等
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // 校验用户是否可用
        preAuthenticationChecks(user);

        // 校验通过，构建一个已认证的对象
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(authentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.authenticated(user, clientPrincipal, new ArrayList<>());
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(OcaGrantAuthenticationToken.OCA)
                .authorizedScopes(registeredClient.getScopes())
                .authorizationGrant(authentication);

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizedScopes(registeredClient.getScopes())
                .attribute(Principal.class.getName(), username)
                .authorizationGrantType(OcaGrantAuthenticationToken.OCA);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            Map<String, Object> map = new HashMap<>(8);
            map.put("message", "无法生成访问token，请联系管理员。");
            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "errorToken", Instant.now(), Instant.now().plusNanos(1)), null, map);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) -> {
                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims());
            });
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // 不向公共客户端颁发刷新令牌
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                Map<String, Object> map = new HashMap<>(8);
                map.put("message", "无法生成访问token，请联系管理员。");
                return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "fdsafas", Instant.now(), Instant.now().plusNanos(1)), null, map);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }


        // 保存认证信息至redis
        Map<String, Object> claims = new HashMap<>(8);
        boolean loginSingleAgent = false;
        if (generatedAccessToken instanceof UaaOAuth2AccessAndRefreshTokenGenerator.OAuth2AccessTokenClaims oauth2AccessTokenClaims) {
            claims = oauth2AccessTokenClaims.getClaims();
        }
        if (user instanceof LoginUserInfo loginUserInfo) {
            loginSingleAgent = loginUserInfo.getLoginSingleAgent();
        }
        tokenStore.storeAccessAndRefreshToken(
                TokenStore.getAuthenticationKey(registeredClient.getClientId(), username),
                accessToken.getTokenValue(),
                refreshToken == null ? null : refreshToken.getTokenValue(),
                loginSingleAgent,
                claims);

        // 返回access_token、refresh_token以及其它信息给到前端
        Map<String, Object> map = returnAdditionalParameters(user, params);
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, map);
    }

    /**
     * 登录成功的时候，额外返回的响应参数
     */
    private Map<String, Object> returnAdditionalParameters(UserDetails user, Map<String, String> requestParams) {
        HashMap<String, Object> additionalParams =  new HashMap<>(8);
        if (user instanceof LoginUserInfo loggedInUser) {
            if (StringUtils.nonEmpty(loggedInUser.getBusinessCode())) {
                additionalParams.put(CODE, loggedInUser.getBusinessCode());
            }
            if (StringUtils.nonEmpty(loggedInUser.getUserId())) {
                additionalParams.put(USER_ID, loggedInUser.getUserId());
            }
            if (StringUtils.nonEmpty(loggedInUser.getOrgId())) {
                additionalParams.put(ORG_ID, loggedInUser.getOrgId());
            }
            if (StringUtils.nonEmpty(loggedInUser.getLoginCode())) {
                additionalParams.put(LOGIN_CODE, loggedInUser.getLoginCode());
            }
        }
        //设置当前登录的机构
        String orgId = requestParams.get(PARAM_ORG_ID);
        if (!StringUtils.isEmpty(orgId)) {
            additionalParams.put(ORG_ID, orgId);
        }
        //设置当前登录的角色
        String roleId = requestParams.get(PARAM_ROLE_ID);
        if (!StringUtils.isEmpty(roleId)) {
            additionalParams.put(ROLE_ID, roleId);
        }
        return additionalParams;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 通过类型进行匹配
        return OcaGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void preAuthenticationChecks(UserDetails user) {

        if (!user.isAccountNonLocked()) {
            throw new LockedException(messages.getMessage(
                    "AccountStatusUserDetailsChecker.locked", "User account is locked"));
        }

        if (!user.isEnabled()) {
            throw new DisabledException(messages.getMessage(
                    "AccountStatusUserDetailsChecker.disabled", "User is disabled"));
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(
                    messages.getMessage("AccountStatusUserDetailsChecker.expired",
                            "User account has expired"));
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(messages.getMessage(
                    "AccountStatusUserDetailsChecker.credentialsExpired",
                    "User credentials have expired"));
        }
    }

    /**
     * 验证验证码
     */
    private void verifyCodeImage(Map<String, String> params) {
        String isImageStatus = params.get("isImageStatus");

        if (!Boolean.parseBoolean(isImageStatus)) {
            return;
        }

        String imageUuid = params.get("imageUUID");
        String imageCode = params.get("imageCode");

        if (StringUtils.isEmpty(imageUuid)) {
            LOG.error(BusinessCodeForExceptionEnum.SYSTEM_ERROR.getMessage() + " imageUUID parameter is missing");
            throw new UaaImageCodeException(BusinessCodeForExceptionEnum.SYSTEM_ERROR.getMessage() + " imageUUID parameter is missing");
        }

        if (StringUtils.isEmpty(imageCode)) {
            LOG.error(BusinessCodeForExceptionEnum.IMAGE_CODE_ERROR.getMessage() + " imageCode parameter is missing");
            throw new UaaImageCodeException("验证码不能为空！");
        }

        String redisImageCode = stringRedisTemplate.opsForValue().get("IMAGE_CODE_REDIS_KEY-" + imageUuid);

        if (StringUtils.isBlank(redisImageCode)) {
            throw new UaaImageCodeException("验证码已过期！");
        }
        if (!redisImageCode.equals(imageCode)) {
            throw new UaaImageCodeException("验证码错误！");
        }
        //验证后删除 防止重复使用20220907
        stringRedisTemplate.delete("IMAGE_CODE_REDIS_KEY-" + imageUuid);
    }


    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
