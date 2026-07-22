package cn.com.yusys.yusp.uaa.exception.handler;

import cn.com.yusys.yusp.uaa.enumerate.BusinessCodeForExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 客户端异常返回
 * @author chenjing11
 * @date 2022/03/07 11:48
 */
public class ClientAuthExceptionEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthExceptionEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        HashMap<String, String> map = new HashMap<>(2);

        if ( e  instanceof  InsufficientAuthenticationException) {
            log.error("client auth failed,", e);
            map.put("code", BusinessCodeForExceptionEnum.AUTHENTICATION_CLIENT_FAILURE.getCode());
            map.put("message", "客户端认证失败，请确认客户端名称和密码");
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }
}
