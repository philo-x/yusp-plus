package cn.com.yusys.yusp.uaa.config.token;

import cn.com.yusys.yusp.uaa.pojo.LoginUserInfo;
import org.springframework.lang.Nullable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * RefreshToken 和 AccessToken 生成二合一
 *
 * @author dlf
 * @version 1.0
 * @since 2025/8/20 11:09
 */
public class UaaOAuth2AccessAndRefreshTokenGenerator implements OAuth2TokenGenerator<OAuth2Token> {

    private final JwtEncoder jwtEncoder;

    public UaaOAuth2AccessAndRefreshTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Nullable
    @Override
    public OAuth2Token generate(OAuth2TokenContext context) {
        if (context.getTokenType() == null ||
                (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) &&
                        !OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType()))) {
            return null;
        }
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) &&
                !OAuth2TokenFormat.SELF_CONTAINED.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
            return null;
        }

        String issuer = null;
        if (context.getAuthorizationServerContext() != null) {
            issuer = context.getAuthorizationServerContext().getIssuer();
        }
        RegisteredClient registeredClient = context.getRegisteredClient();

        Instant issuedAt = Instant.now();
        Instant expiresAt;
        JwsAlgorithm jwsAlgorithm = getJwsAlgorithm(context, registeredClient);
        // jwt 自身的过期时间比 配置的过期时间 要多一天
        expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive()).plus(1, ChronoUnit.DAYS);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }
        claimsBuilder
                .subject(context.getPrincipal().getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString());
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            claimsBuilder.notBefore(issuedAt);
            if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
                claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
            }
        } else if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            fillClaimToClaimsBuilder(context, claimsBuilder, registeredClient);
        }
        claimsBuilder.claim("type", "JWT");
        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(jwsAlgorithm);
        // 自定义 jwt
        JwtEncodingContext jwtContext = getJwtEncodingContext(context, jwsHeaderBuilder, claimsBuilder);
        JwsHeader jwsHeader = jwsHeaderBuilder.build();
        customizeJwt(jwtContext);
        return getAbstractOAuth2Token(context, claimsBuilder, jwsHeader);
    }

    private AbstractOAuth2Token getAbstractOAuth2Token(OAuth2TokenContext context, JwtClaimsSet.Builder claimsBuilder, JwsHeader jwsHeader) {
        JwtClaimsSet claims = claimsBuilder.build();

        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return new OAuth2AccessTokenClaims(OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(),
                    claims.getIssuedAt(), claims.getExpiresAt(), context.getAuthorizedScopes(),
                    claims.getClaims());
        } else {
            return new OAuth2RefreshToken(jwt.getTokenValue(), claims.getIssuedAt(), claims.getExpiresAt());
        }
    }

    private static JwsAlgorithm getJwsAlgorithm(OAuth2TokenContext context, RegisteredClient registeredClient) {
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            if (registeredClient.getTokenSettings().getIdTokenSignatureAlgorithm() != null) {
                jwsAlgorithm = registeredClient.getTokenSettings().getIdTokenSignatureAlgorithm();
            }
        }
        return jwsAlgorithm;
    }

    private static void fillClaimToClaimsBuilder(OAuth2TokenContext context, JwtClaimsSet.Builder claimsBuilder, RegisteredClient registeredClient) {
        claimsBuilder.claim(IdTokenClaimNames.AZP, registeredClient.getClientId());
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())) {
            OAuth2AuthorizationRequest authorizationRequest = context.getAuthorization().getAttribute(
                    OAuth2AuthorizationRequest.class.getName());
            String nonce = (String) authorizationRequest.getAdditionalParameters().get(OidcParameterNames.NONCE);
            if (StringUtils.hasText(nonce)) {
                claimsBuilder.claim(IdTokenClaimNames.NONCE, nonce);
            }
            SessionInformation sessionInformation = context.get(SessionInformation.class);
            if (sessionInformation != null) {
                claimsBuilder.claim("sid", sessionInformation.getSessionId());
                claimsBuilder.claim(IdTokenClaimNames.AUTH_TIME, sessionInformation.getLastRequest());
            }
        } else if (AuthorizationGrantType.REFRESH_TOKEN.equals(context.getAuthorizationGrantType())) {
            OidcIdToken currentIdToken = context.getAuthorization().getToken(OidcIdToken.class).getToken();
            if (currentIdToken.hasClaim("sid")) {
                claimsBuilder.claim("sid", currentIdToken.getClaim("sid"));
            }
            if (currentIdToken.hasClaim(IdTokenClaimNames.AUTH_TIME)) {
                claimsBuilder.claim(IdTokenClaimNames.AUTH_TIME, currentIdToken.<Date>getClaim(IdTokenClaimNames.AUTH_TIME));
            }
        }
    }

    private static JwtEncodingContext getJwtEncodingContext(OAuth2TokenContext context, JwsHeader.Builder jwsHeaderBuilder, JwtClaimsSet.Builder claimsBuilder) {
        JwtEncodingContext.Builder jwtContextBuilder = JwtEncodingContext.with(jwsHeaderBuilder, claimsBuilder)
                .registeredClient(context.getRegisteredClient())
                .principal(context.getPrincipal())
                .authorizationServerContext(context.getAuthorizationServerContext())
                .authorizedScopes(context.getAuthorizedScopes())
                .tokenType(context.getTokenType())
                .authorizationGrantType(context.getAuthorizationGrantType());
        if (context.getAuthorization() != null) {
            jwtContextBuilder.authorization(context.getAuthorization());
        }
        if (context.getAuthorizationGrant() != null) {
            jwtContextBuilder.authorizationGrant(context.getAuthorizationGrant());
        }
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            SessionInformation sessionInformation = context.get(SessionInformation.class);
            if (sessionInformation != null) {
                jwtContextBuilder.put(SessionInformation.class, sessionInformation);
            }
        }

        return jwtContextBuilder.build();
    }

    private void customizeJwt(JwtEncodingContext jwtContext) {
        Object principal = jwtContext.getPrincipal().getPrincipal();
        JwtClaimsSet.Builder claims = jwtContext.getClaims();
        if (principal instanceof LoginUserInfo loggedInUser) {
            if (cn.com.yusys.yusp.commons.util.StringUtils.nonEmpty(loggedInUser.getBusinessCode())) {
                claims.claim("code", loggedInUser.getBusinessCode());
            }
            if (cn.com.yusys.yusp.commons.util.StringUtils.nonEmpty(loggedInUser.getUserId())) {
                claims.claim("user_id", loggedInUser.getUserId());
            }
            if (cn.com.yusys.yusp.commons.util.StringUtils.nonEmpty(loggedInUser.getOrgId())) {
                claims.claim("org_id", loggedInUser.getOrgId());
            }
            if (cn.com.yusys.yusp.commons.util.StringUtils.nonEmpty(loggedInUser.getLoginCode())) {
                claims.claim("login_code", loggedInUser.getLoginCode());
            }
            if (cn.com.yusys.yusp.commons.util.StringUtils.nonEmpty(loggedInUser.getUsername())) {
                claims.claim("user_name", loggedInUser.getUsername());
            }
        }
        Duration accessTokenTimeToLive = jwtContext.getRegisteredClient().getTokenSettings().getAccessTokenTimeToLive();
        Duration refreshTokenTimeToLive = jwtContext.getRegisteredClient().getTokenSettings().getRefreshTokenTimeToLive();
        if (accessTokenTimeToLive == null || accessTokenTimeToLive.getSeconds() == 300) {
            // 如果未设置，默认为 7 天
            accessTokenTimeToLive = Duration.ofDays(7);
        }
        if (refreshTokenTimeToLive == null || refreshTokenTimeToLive.getSeconds() == 3600) {
            // 如果未设置，默认为 7 天
            refreshTokenTimeToLive = Duration.ofDays(7);
        }
        claims.claim("refresh_time_to_live", refreshTokenTimeToLive.getSeconds());
        claims.claim("access_time_to_live", accessTokenTimeToLive.getSeconds());
        claims.claim("client_id", jwtContext.getRegisteredClient().getClientId());
    }

    public static final class OAuth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

        private final Map<String, Object> claims;

        private OAuth2AccessTokenClaims(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
                                        Set<String> scopes, Map<String, Object> claims) {
            super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
            this.claims = claims;
        }

        @Override
        public Map<String, Object> getClaims() {
            return this.claims;
        }

    }
}
