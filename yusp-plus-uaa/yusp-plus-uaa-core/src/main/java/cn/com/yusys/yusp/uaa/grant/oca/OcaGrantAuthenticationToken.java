package cn.com.yusys.yusp.uaa.grant.oca;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * oca 模式保存校验信息的对象:
 * <p>
 * 认证数据的中间载体，封装授权流程中的临时数据认证（比如用户名、密码、验证码），供 AuthenticationProvider 进行处理
 * <p>
 * 通过 setAuthenticated 标识当前认证状态（初始为 false，认证成功后变为 true）
 *
 * @author 19814-1
 */
public class OcaGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public static final AuthorizationGrantType OCA = new AuthorizationGrantType("oca");


    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    protected OcaGrantAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(OCA, clientPrincipal, additionalParameters);
    }
}


