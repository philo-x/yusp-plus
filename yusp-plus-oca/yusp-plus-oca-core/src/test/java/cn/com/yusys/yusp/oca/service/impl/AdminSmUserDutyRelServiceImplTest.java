package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.oca.dao.AdminSmUserDutyRelDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmUserDutyRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 测试 AdminSmUserDutyRelServiceImpl.findUserDutyRelsByUser 方法
 */
public class AdminSmUserDutyRelServiceImplTest {

    @InjectMocks
    @Spy 
    private AdminSmUserDutyRelServiceImpl adminSmUserDutyRelService;  // 被测试的Service

    @Mock
    private AdminSmUserDutyRelDao adminSmUserDutyRelDao;  // 用于数据验证的Dao

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(adminSmUserDutyRelDao).when(adminSmUserDutyRelService).getBaseMapper();
    }

    /**
     * 测试 findUserDutyRelsByUser 正常场景
     * 当用户存在且 userId 不为 null 时，返回有效的岗位ID列表
     */
    @Test
    @DisplayName("测试 findUserDutyRelsByUser_正常场景_返回岗位ID列表")
    public void testFindUserDutyRelsByUser_NormalCase_ReturnDutyIds() {
        // Given - 准备测试数据
        AdminSmUserEntity user = new AdminSmUserEntity();
        user.setUserId("1001");
        
        List<String> expectedDutyIds = new ArrayList<>();
        expectedDutyIds.add("DUTY001");
        expectedDutyIds.add("DUTY002");
        
        // When - 执行被测试方法
        when(adminSmUserDutyRelDao.findUserDutyRelsByUser("1001")).thenReturn(expectedDutyIds);
        List<String> result = adminSmUserDutyRelService.findUserDutyRelsByUser(user);
        
        // Then - 验证结果
        assertNotNull( result);
        assertEquals(expectedDutyIds.size(), result.size());
        assertTrue(result.containsAll(expectedDutyIds));
    }

    /**
     * 测试 findUserDutyRelsByUser_用户为null_抛出NullPointerException
     */
    @Test
    @DisplayName("测试 findUserDutyRelsByUser_用户为null_抛出NullPointerException")
    public void testFindUserDutyRelsByUser_UserIsNull_ThrowNullPointerException() {
        // Given - 准备测试数据
        AdminSmUserEntity user = null;
        
        // When & Then - 执行被测试方法并验证异常
        assertThrows(NullPointerException.class, () -> {
            adminSmUserDutyRelService.findUserDutyRelsByUser(user);
        }, "当用户为null时，应该抛出NullPointerException");
    }

    /**
     * 测试 findUserDutyRelsByUser_用户userId为null_抛出NullPointerException
     */
    @Test
    @DisplayName("测试 findUserDutyRelsByUser_用户userId为null_抛出NullPointerException")
    public void testFindUserDutyRelsByUser_UserIdIsNull_ThrowNullPointerException() {
        // Given - 准备测试数据
        AdminSmUserEntity user = new AdminSmUserEntity();
        user.setUserId(null);
        // When & Then - 执行被测试方法并验证异常
        assertThrows(NullPointerException.class, () -> {
            adminSmUserDutyRelService.findUserDutyRelsByUser(user);
        }, "当用户userId为null时，应该抛出NullPointerException");
    }

    /**
     * 测试 findUserDutyRelsByUser_用户无岗位关联_返回空列表
     */
    @Test
    @DisplayName("测试 findUserDutyRelsByUser_用户无岗位关联_返回空列表")
    public void testFindUserDutyRelsByUser_NoDutyRelation_ReturnEmptyList() {
        // Given - 准备测试数据
        AdminSmUserEntity user = new AdminSmUserEntity();
        user.setUserId("1002");
        
        List<String> expectedDutyIds = new ArrayList<>();
        
        // When - 执行被测试方法
        when(adminSmUserDutyRelDao.findUserDutyRelsByUser("1002")).thenReturn(expectedDutyIds);
        List<String> result = adminSmUserDutyRelService.findUserDutyRelsByUser(user);
        
        // Then - 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
