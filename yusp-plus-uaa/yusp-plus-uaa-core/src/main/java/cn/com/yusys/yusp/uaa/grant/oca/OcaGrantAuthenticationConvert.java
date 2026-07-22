package cn.com.yusys.yusp.uaa.grant.oca;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.uaa.pojo.LoginUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 请求参数的转换器
 * <p>
 * 将 Http 请求中的参数转换为特定的 Authentication 对象，比如 OcaGrantAuthenticationToken，用于后续的认证
 * <p>
 * 1. 参数提取与验证：从 Http 中解析参数，可以判断是否符合当前转换器的处理范围
 * 2. 如果参数匹配，可以将参数封装到 Authentication 中
 *
 * @author dlf
 * @version 1.0
 * @since 2025/8/3 10:23
 */
public class OcaGrantAuthenticationConvert implements AuthenticationConverter {

    private final RegisteredClientRepository registeredClientRepository;


    public OcaGrantAuthenticationConvert(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OcaGrantAuthenticationToken.OCA.getValue().equals(grantType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = getParameters(request);

        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new OAuth2AuthenticationException("无效请求，用户名或密码不能为空");
        }

        Map<String, Object> additionalParameters = new HashMap<>(8);
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });
        additionalParameters.put("isImageStatus", isImageStatus());

        return new OcaGrantAuthenticationToken(clientPrincipal, additionalParameters);
    }


    /**
     * 获取校验码是否执行
     */
    private String isImageStatus() {
        // 获取client_id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId =  (String) authentication.getPrincipal();
        return registeredClientRepository.findByClientId(clientId).getClientSettings().getSetting("image_status");
    }

    /**
     * 从 request 中提取参数，然后存入 MultiValueMap 中
     *
     * @param request request
     * @return multiValueMap
     */
    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

}
