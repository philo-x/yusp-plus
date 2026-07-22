package cn.com.yusys.yusp.uaa.config.token;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * 将 Token 缓存在 Redis，用于实现登录相关的策略
 *
 * @author dlf
 * @version 1.0
 * @since 2025/9/4 19:24
 */
public class CustomRedisTokenStore implements TokenStore {

    private static final String ACCESS_TIME_TO_LIVE = "access_time_to_live";

    private final StringRedisTemplate stringRedisTemplate;

    public CustomRedisTokenStore(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void storeAccessAndRefreshToken(String key, String oauth2AccessToken, String oauth2RefreshToken,
                                           boolean loginSingleAgent, Map<String, Object> claims) {
        // 在存储的时候，判断是否开启了登录互斥策略，如果开启了，则删除之前的 Token
        if (loginSingleAgent) {
            Set<String> members = stringRedisTemplate.opsForSet().members(UNAME_TO_ACCESS + key);
            for (String member : members) {
                stringRedisTemplate.delete(ACCESS + member);
            }
            stringRedisTemplate.delete(UNAME_TO_ACCESS + key);
        }
        // jwt 自身的过期时间比 Redis 过期时间要多一天，实际上是否过期交由 Redis 过期时间进行控制
        stringRedisTemplate.opsForSet().add(UNAME_TO_ACCESS + key, oauth2AccessToken);
        stringRedisTemplate.opsForValue().set(ACCESS + oauth2AccessToken, key);
        stringRedisTemplate.expire(UNAME_TO_ACCESS + key, Duration.ofSeconds((Long) claims.get(ACCESS_TIME_TO_LIVE)));
        stringRedisTemplate.expire(ACCESS + oauth2AccessToken, Duration.ofSeconds((Long) claims.get(ACCESS_TIME_TO_LIVE)));
        if (oauth2RefreshToken != null) {
            stringRedisTemplate.opsForValue().set(REFRESH + oauth2RefreshToken, key);
            stringRedisTemplate.expire(REFRESH + oauth2RefreshToken, Duration.ofSeconds((Long) claims.get("refresh_time_to_live")));
        }
    }

    @Override
    public boolean containsAccessToken(String key, String oauth2AccessToken, Map<String, Object> claims) {
        boolean contains = stringRedisTemplate.hasKey(ACCESS + oauth2AccessToken);
        if (contains) {
            // 如果存在，就更新过期时间
            stringRedisTemplate.expire(UNAME_TO_ACCESS + key, Duration.ofSeconds((Long) claims.get(ACCESS_TIME_TO_LIVE)));
            stringRedisTemplate.expire(ACCESS + oauth2AccessToken, Duration.ofSeconds((Long) claims.get(ACCESS_TIME_TO_LIVE)));
        }
        return contains;
    }

    @Override
    public void removeAccessToken(String key, String oauth2AccessToken) {
        stringRedisTemplate.delete(ACCESS + oauth2AccessToken);
        stringRedisTemplate.opsForSet().remove(UNAME_TO_ACCESS + key, oauth2AccessToken);
    }

    @Override
    public boolean containsRefreshToken(String key, String refreshToken, Map<String, Object> claims) {
        boolean contains = stringRedisTemplate.hasKey(REFRESH + refreshToken);
        if (contains) {
            // 如果存在，就更新过期时间
            stringRedisTemplate.expire(REFRESH + refreshToken, Duration.ofSeconds((Long) claims.get("refresh_time_to_live")));
        }
        return contains;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        stringRedisTemplate.delete(REFRESH + oAuth2RefreshToken);
    }
}
