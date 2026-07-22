package cn.com.yusys.yusp.uaa.util;

import cn.com.yusys.yusp.commons.session.context.UserContext;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/5 10:52
 */
public class JwtParseUtil {

    public static UserContext getUserContext(Map<String, Object> claims) {
        return UserContext.of((String) claims.get("client_id"), (String) claims.get("user_id"), (String) claims.get("login_code"), (String) claims.get("org_id"));
    }

    public static UserContext getUserContext(Jwt jwt) {
        return getUserContext(jwt.getClaims());
    }



}
