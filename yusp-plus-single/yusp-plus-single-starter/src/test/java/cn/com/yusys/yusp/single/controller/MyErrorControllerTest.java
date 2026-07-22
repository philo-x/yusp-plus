package cn.com.yusys.yusp.single.controller;

import java.util.*;
import java.math.*;

import cn.com.yusys.yusp.single.controller.MyErrorController;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @Mock
    private ErrorAttributes errorAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        myErrorController = new MyErrorController(errorAttributes, new ServerProperties());
    }

    @Test
    @DisplayName("测试内部服务器错误且消息以 'http' 开头时返回支付需要状态")
    void error_InternalServerErrorWithHttpMessage_ReturnsPaymentRequired() {
        // 1. 准备请求参数和模拟返回值
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        Map<String, Object> errorAttributesMap = new HashMap<>(8);
        errorAttributesMap.put("message", "http://example.com");
        when(errorAttributes.getErrorAttributes(any(), any())).thenReturn(errorAttributesMap);

        // 2. 调用 Controller 方法
        ResponseEntity<Map<String, Object>> response = myErrorController.error(request);

        // 3. 验证结果
        assertEquals(HttpStatus.PAYMENT_REQUIRED, response.getStatusCode());
        assertEquals("http://example.com", response.getBody().get("message"));
    }

    @Test
    @DisplayName("测试内部服务器错误且消息不以 'http' 开头时返回内部服务器错误状态")
    void error_InternalServerErrorWithoutHttpMessage_ReturnsInternalServerError() {
        // 1. 准备请求参数和模拟返回值
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        Map<String, Object> errorAttributesMap = new HashMap<>(8);
        errorAttributesMap.put("message", "Internal Server Error");
        when(errorAttributes.getErrorAttributes(any(), any())).thenReturn(errorAttributesMap);

        // 2. 调用 Controller 方法
        ResponseEntity<Map<String, Object>> response = myErrorController.error(request);

        // 3. 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().get("message"));
    }

    @Test
    @DisplayName("测试非内部服务器错误时返回对应状态码")
    void error_NotInternalServerError_ReturnsCorrectStatus() {
        // 1. 准备请求参数和模拟返回值
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);

        Map<String, Object> errorAttributesMap = new HashMap<>(8);
        errorAttributesMap.put("message", "Not Found");
        when(errorAttributes.getErrorAttributes(any(), any())).thenReturn(errorAttributesMap);

        // 2. 调用 Controller 方法
        ResponseEntity<Map<String, Object>> response = myErrorController.error(request);

        // 3. 验证结果
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("message"));
    }
}
