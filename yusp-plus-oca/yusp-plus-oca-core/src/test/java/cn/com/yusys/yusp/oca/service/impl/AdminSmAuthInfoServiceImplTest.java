package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmAuthInfoDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthInfoEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AdminSmAuthInfoServiceImplTest {

    @InjectMocks
    @Spy
    private AdminSmAuthInfoServiceImpl adminSmAuthInfoService;

    @Mock
    private AdminSmAuthInfoDao adminSmAuthInfoDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(adminSmAuthInfoDao).when(adminSmAuthInfoService).getBaseMapper();
    }

    @Test
    @DisplayName("测试 queryPage 方法，传入有效参数")
    void queryPage_ValidParams_ReturnsCorrectPageData() {
        // Arrange
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");

        IPage<AdminSmAuthInfoEntity> page = new Page<>(1, 10);
        // 假设返回空记录集
        page.setRecords(new ArrayList<>());
        when(adminSmAuthInfoDao.selectPage(any(IPage.class), any(QueryWrapper.class))).thenReturn(page);

        // Act
        PageUtils result = adminSmAuthInfoService.queryPage(params);

        // Assert
        assertEquals(10, result.getPageSize());
        assertEquals(0, result.getTotalCount());
        assertTrue(result.getList().isEmpty());
    }

    @Test
    @DisplayName("测试 getAuthKeyValue 方法，数据库中有数据")
    void getAuthKeyValue_DataExists_ReturnsConvertedVoList() {
        // Arrange
        List<AdminSmAuthInfoEntity> entities = new ArrayList<>();
        AdminSmAuthInfoEntity entity = new AdminSmAuthInfoEntity();
        entity.setAuthId("1");
        entity.setAuthName("AuthName");
        entities.add(entity);

        when(adminSmAuthInfoDao.selectList(any())).thenReturn(entities);

        // Act
        List<AdminSmAuthInfoVo> result = adminSmAuthInfoService.getAuthKeyValue();

        // Assert
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getKey());
        assertEquals("AuthName", result.get(0).getValue());
    }

    @Test
    @DisplayName("测试 getAuthKeyValue 方法，数据库中无数据")
    void getAuthKeyValue_NoData_ReturnsEmptyList() {
        // Arrange
        when(adminSmAuthInfoDao.selectList(any())).thenReturn(new ArrayList<>());

        // Act
        List<AdminSmAuthInfoVo> result = adminSmAuthInfoService.getAuthKeyValue();

        // Assert
        assertTrue(result.isEmpty());
    }
}
