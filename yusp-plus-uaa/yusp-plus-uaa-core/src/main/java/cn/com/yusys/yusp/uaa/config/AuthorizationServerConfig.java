package cn.com.yusys.yusp.uaa.config;


import cn.com.yusys.yusp.uaa.config.token.CustomRedisTokenStore;
import cn.com.yusys.yusp.uaa.config.token.TokenStore;
import cn.com.yusys.yusp.uaa.config.token.UaaOAuth2AccessAndRefreshTokenGenerator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Oauth2 配置类
 * @author zhangyt12
 * @date 2021/9/10 9:41
 */
@Configuration
public class AuthorizationServerConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ApplicationContext applicationContext;

    @Value("${uaa.keyStore.name:keystore.jks}")
    private String keyStoreName;

    @Value("${uaa.keyStore.password:password}")
    private String keyStorePassword;

    @Value("${uaa.keyStore.keyPair:selfsigned}")
    private String keyPair;


    /**
     * 用于签署访问令牌
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String keyId = UUID.nameUUIDFromBytes(publicKey.getEncoded()).toString();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 启动时生成的密钥，用于创建上面的JWKSource
     */
    private KeyPair generateRsaKey() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStoreName), keyStorePassword.toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyPair);
    }

    /**
     * 用于解码签名访问令牌
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置 jwt 解析器
     */
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator() {
        return new DelegatingOAuth2TokenGenerator(
                new UaaOAuth2AccessAndRefreshTokenGenerator(new NimbusJwtEncoder(jwkSource()))
        );
    }

    /**
     * 用于配置 Spring Authorization Server
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 将 token 存放在 Redis 中
     */
    @Bean
    public TokenStore tokenStore() {
        return new CustomRedisTokenStore(stringRedisTemplate);
    }

}
