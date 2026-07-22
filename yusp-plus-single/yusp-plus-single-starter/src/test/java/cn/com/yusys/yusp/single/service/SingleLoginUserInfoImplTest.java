package cn.com.yusys.yusp.single.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.LoginCheckSecretService;
import cn.com.yusys.yusp.oca.service.ThirdPartyLoginService;
import cn.com.yusys.yusp.uaa.exception.UserVerificationException;
import cn.com.yusys.yusp.uaa.pojo.TokenParamDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SingleLoginUserInfoImplTest {

    @Mock
    private LoginCheckSecretService loginCheckSecretService;

    @InjectMocks
    private SingleLoginUserInfoImpl singleLoginUserInfoImpl;

    @Mock
    private ThirdPartyLoginService thirdPartyLoginService;

    @Mock
    private AdminSmCrelStraService adminSmCrelStraService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoginUserInfo_ValidResponse_ReturnsTokenParamDto() {
        // 准备模拟数据
        UserInfoForPasswordDto userInfoForPasswordDto = new UserInfoForPasswordDto();
        userInfoForPasswordDto.setLoginCode("testLoginCode");
        userInfoForPasswordDto.setOrgId("testOrgId");
        userInfoForPasswordDto.setUserId("testUserId");
        userInfoForPasswordDto.setUserPassword("testPassword");
        userInfoForPasswordDto.setLoginSingleAgent(true);

        JClientRspEntity<UserInfoForPasswordDto> response = JClientRspEntity.buildSuccess(userInfoForPasswordDto);
        response.getHead().setRetCode("0000");

        when(loginCheckSecretService.getUserInfoWithForPassword(anyString())).thenReturn(response);

        // 执行测试方法
        TokenParamDto result = singleLoginUserInfoImpl.getLoginUserInfo("testUsername");

        // 验证结果
        assertNotNull(result);
        assertEquals("0000", result.getBusinessCode());
        assertEquals("testLoginCode", result.getLoginCode());
        assertEquals("testOrgId", result.getOrgId());
        assertEquals("testUserId", result.getUserId());
        assertEquals("testPassword", result.getPassword());
        assertTrue(result.getLoginSingleAgent());
    }

    @Test
    void getLoginUserInfo_NullResponse_ThrowsException() {
        when(loginCheckSecretService.getUserInfoWithForPassword(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            singleLoginUserInfoImpl.getLoginUserInfo("testUsername");
        });
    }

    @Test
    void getLoginUserInfo_ResponseWithoutBody_ThrowsException() {
        JClientRspEntity<UserInfoForPasswordDto> response = JClientRspEntity.buildSuccess(null);
        response.getHead().setRetCode("0000");

        when(loginCheckSecretService.getUserInfoWithForPassword(anyString())).thenReturn(response);

        assertThrows(NullPointerException.class, () -> {
            singleLoginUserInfoImpl.getLoginUserInfo("testUsername");
        });
    }

    @Test
    @DisplayName("SSO 启用且返回成功")
    void getLoginUserInfo_SsoEnabled_Success() {
        // 设置 SSO 启用
        ReflectionTestUtils.setField(singleLoginUserInfoImpl, "ssoEnable", true);

        // 准备模拟数据
        UserEntityVo userEntityVo = new UserEntityVo();
        userEntityVo.setLoginCode("testLoginCode");
        userEntityVo.setOrgId("testOrgId");
        userEntityVo.setUserId("testUserId");

        JClientRspEntity response = JClientRspEntity.buildSuccess(userEntityVo);
        response.getHead().setRetCode("0000");

        when(thirdPartyLoginService.getUserInfoWithThirdParty(anyString())).thenReturn(response);

        // 执行测试方法
        TokenParamDto result = singleLoginUserInfoImpl.getLoginUserInfo("testUsername", "testPassword");

        // 验证结果
        assertNotNull(result);
        assertEquals("0000", result.getBusinessCode());
        assertEquals("testLoginCode", result.getLoginCode());
        assertEquals("testOrgId", result.getOrgId());
        assertEquals("testUserId", result.getUserId());
    }

    @Test
    @DisplayName("SSO 启用但返回错误")
    void getLoginUserInfo_SsoEnabled_Failure() {
        // 设置 SSO 启用
        ReflectionTestUtils.setField(singleLoginUserInfoImpl, "ssoEnable", true);

        // 准备模拟数据
        JClientRspEntity response = JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), ResponseAndMessageEnum.BAD_CREDENTIALS.getMessage());

        when(thirdPartyLoginService.getUserInfoWithThirdParty(anyString())).thenReturn(response);

        // 断言抛出异常
        assertThrows(UserVerificationException.class, () -> {
            singleLoginUserInfoImpl.getLoginUserInfo("testUsername", "testPassword");
        });
    }

    @Test
    @DisplayName("SSO 未启用且返回成功")
    void getLoginUserInfo_SsoDisabled_Success() {
        // 设置 SSO 未启用
        ReflectionTestUtils.setField(singleLoginUserInfoImpl, "ssoEnable", false);

        // 准备模拟数据
        UserInfoForPasswordDto userInfoForPasswordDto = new UserInfoForPasswordDto();
        userInfoForPasswordDto.setLoginCode("testLoginCode");
        userInfoForPasswordDto.setOrgId("testOrgId");
        userInfoForPasswordDto.setUserId("testUserId");
        userInfoForPasswordDto.setUserPassword("testPassword");
        userInfoForPasswordDto.setLoginSingleAgent(true);

        JClientRspEntity response = JClientRspEntity.buildSuccess(userInfoForPasswordDto);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("password")).thenReturn("testPassword");
        when(request.getParameter("seq")).thenReturn("1");
        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(new ServletRequestAttributes(request));
            when(loginCheckSecretService.queryUserAndCheckSecret(anyString(), anyString(), anyString())).thenReturn(response);

            // 执行测试方法
            TokenParamDto result = singleLoginUserInfoImpl.getLoginUserInfo("testUsername", "testPassword");

            // 验证结果
            assertNotNull(result);
            assertEquals("0000", result.getBusinessCode());
            assertEquals("testLoginCode", result.getLoginCode());
            assertEquals("testOrgId", result.getOrgId());
            assertEquals("testUserId", result.getUserId());
            assertEquals("testPassword", result.getPassword());
            assertTrue(result.getLoginSingleAgent());
        }

    }

    @Test
    @DisplayName("SSO 未启用且返回错误并调用 passwordErrorLimit")
    void getLoginUserInfo_SsoDisabled_FailureWithPasswordErrorLimit() {
        // 设置 SSO 未启用
        ReflectionTestUtils.setField(singleLoginUserInfoImpl, "ssoEnable", false);

        // 准备模拟数据
        JClientRspEntity response = JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), ResponseAndMessageEnum.BAD_CREDENTIALS.getMessage());

        JClientRspEntity passwordErrorResponse = JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), "用户名或密码错误！剩余次数：1");


        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("password")).thenReturn("testPassword");
        when(request.getParameter("seq")).thenReturn("1");
        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(new ServletRequestAttributes(request));
            when(loginCheckSecretService.queryUserAndCheckSecret(anyString(), anyString(), anyString())).thenReturn(response);
            when(adminSmCrelStraService.passwordErrorLimit(anyString(), anyString())).thenReturn(passwordErrorResponse);
            // 执行测试方法
            assertThrows(UserVerificationException.class, () -> {
                singleLoginUserInfoImpl.getLoginUserInfo("testUsername", "testPassword");
            });
        }



    }

    @Test
    @DisplayName("封装 TokenParamDto 时 Body 为 UserEntityVo")
    void getLoginUserInfo_UserEntityVoBody() {
        // 设置 SSO 启用
        ReflectionTestUtils.setField(singleLoginUserInfoImpl, "ssoEnable", true);

        // 准备模拟数据
        UserEntityVo userEntityVo = new UserEntityVo();
        userEntityVo.setLoginCode("testLoginCode");
        userEntityVo.setOrgId("testOrgId");
        userEntityVo.setUserId("testUserId");

        JClientRspEntity response = JClientRspEntity.buildSuccess(userEntityVo);
        response.getHead().setRetCode("0000");

        when(thirdPartyLoginService.getUserInfoWithThirdParty(anyString())).thenReturn(response);

        // 执行测试方法
        TokenParamDto result = singleLoginUserInfoImpl.getLoginUserInfo("testUsername", "testPassword");

        // 验证结果
        assertNotNull(result);
        assertEquals("0000", result.getBusinessCode());
        assertEquals("testLoginCode", result.getLoginCode());
        assertEquals("testOrgId", result.getOrgId());
        assertEquals("testUserId", result.getUserId());
    }

}
