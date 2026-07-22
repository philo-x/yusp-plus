package cn.com.yusys.yusp.uaa.config.token;

import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.Map;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/4 15:23
 */
public interface TokenStore {

    public static final String ACCESS = "access:";
    public static final String AUTH_TO_ACCESS = "auth_to_access:";
    public static final String REFRESH_AUTH = "refresh_auth:";
    public static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    public static final String REFRESH = "refresh:";
    public static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    public static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    public static final String UNAME_TO_ACCESS = "uname_to_access:";



    /**
     * 将 oauth2AccessToken 和 refresh Token 进行存储
     * @param oauth2AccessToken accessToken
     * @param oauth2RefreshToken oauth2RefreshToken
     * @param loginSingleAgent loginSingleAgent
     * @param claims claims
     *
     * @param key 唯一标识，clientId + loginCode
     */
    void storeAccessAndRefreshToken(String key, String oauth2AccessToken, String oauth2RefreshToken, boolean loginSingleAgent, Map<String, Object> claims);

    /**
     * 判断 accessToken 是否存在
     * @param key 唯一标识，clientId + loginCode
     * @param oauth2AccessToken oauth2AccessToken
     * @param claims claims
     * @return accessToken
     */
    boolean containsAccessToken(String key, String oauth2AccessToken, Map<String, Object> claims);

    /**
     * 删除 accessToken
     * @param key 唯一标识，clientId + loginCode
     * @param oauth2AccessToken accessToken
     *
     */
    void removeAccessToken(String key, String oauth2AccessToken);

    /**
     * 判断 refreshToken 是否存在
     * @param key 唯一标识，clientId + loginCode
     * @param refreshToken refreshToken
     * @param claims claims
     * @return boolean
     */
    boolean containsRefreshToken(String key, String refreshToken, Map<String, Object> claims);

    /**
     * 删除 refreshToken
     * @param oAuth2RefreshToken refreshToken
     */
    void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken);

    /**
     * 将 clientId 和 loginCode 进行拼接
     * @param clientId 客户端id
     * @param loginCode 登录码，唯一
     * @return key
     */
    static String getAuthenticationKey(String clientId, String loginCode) {
        return clientId + ":" + loginCode;
    }

}
