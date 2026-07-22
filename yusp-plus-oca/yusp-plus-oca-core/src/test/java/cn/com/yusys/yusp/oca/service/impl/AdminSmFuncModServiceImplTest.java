package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.oca.dao.AdminSmFuncModDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmFuncModBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmFuncModEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmFuncModQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 系统模块管理服务测试类
 *
 * @author yourname
 */
public class AdminSmFuncModServiceImplTest {

    @InjectMocks
    @org.mockito.Spy
    private AdminSmFuncModServiceImpl adminSmFuncModService;

    @Mock
    private AdminSmFuncModDao adminSmFuncModDao;

    @Mock
    private AdminSmBusiFuncService adminSmBusiFuncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        // Mock getBaseMapper() 方法返回 dao 实例
        doReturn(adminSmFuncModDao).when(adminSmFuncModService).getBaseMapper();
    }

    /**
     * 测试 removeByModId 正常场景 - 删除无关联业务功能的模块
     */
    @Test
    @DisplayName("测试removeByModId_正常场景_删除成功")
    public void testRemoveByModId_NormalCase_Success() {
        // Given - 准备测试数据
        String[] modIds = {"mod1", "mod2"};
        
        // Mock 业务功能服务返回空列表，表示没有关联业务功能
        List<AdminSmBusiFuncEntity> busiFuncList = new ArrayList<>();
        when(adminSmBusiFuncService.list(any(QueryWrapper.class))).thenReturn(busiFuncList);
        
        // Mock DAO 删除操作返回成功删除的数量
        when(adminSmFuncModDao.deleteBatchIds(anyList())).thenReturn(2);
        
        // When - 执行被测试方法
        int result = adminSmFuncModService.removeByModId(modIds);
        
        // Then - 验证结果
        assertEquals(2, result, "应该成功删除2个模块");
        
        // 验证业务功能服务被正确调用
        verify(adminSmBusiFuncService, times(1)).list(any(QueryWrapper.class));
        
        // 验证DAO的删除方法被正确调用
        verify(adminSmFuncModDao, times(1)).deleteBatchIds(anyList());
    }

    /**
     * 测试 removeByModId 异常场景 - 存在关联业务功能的模块不能删除
     */
    @Test
    @DisplayName("测试removeByModId_异常场景_存在关联业务功能时返回-1")
    public void testRemoveByModId_WithAssociatedBusinessFunction_ReturnsMinusOne() {
        // Given - 准备测试数据
        String[] modIds = {"mod1", "mod2"};
        
        // Mock 业务功能服务返回非空列表，表示存在关联业务功能
        List<AdminSmBusiFuncEntity> busiFuncList = new ArrayList<>();
        busiFuncList.add(new AdminSmBusiFuncEntity()); // 添加一个关联的业务功能
        when(adminSmBusiFuncService.list(any(QueryWrapper.class))).thenReturn(busiFuncList);
        
        // When - 执行被测试方法
        int result = adminSmFuncModService.removeByModId(modIds);
        
        // Then - 验证结果
        assertEquals(-1, result, "存在关联业务功能时应该返回-1");
        
        // 验证业务功能服务被正确调用
        verify(adminSmBusiFuncService, times(1)).list(any(QueryWrapper.class));
        
        // 验证DAO的删除方法不应该被调用
        verify(adminSmFuncModDao, never()).deleteBatchIds(anyList());
    }

    /**
     * 测试 removeByModId 边界场景 - 传入空数组
     */
    @Test
    @DisplayName("测试removeByModId_边界场景_传入空数组")
    public void testRemoveByModId_EmptyArray() {
        // Given - 准备测试数据
        String[] modIds = {};
        
        // Mock 业务功能服务返回空列表
        List<AdminSmBusiFuncEntity> busiFuncList = new ArrayList<>();
        when(adminSmBusiFuncService.list(any(QueryWrapper.class))).thenReturn(busiFuncList);
        
        // Mock DAO 删除操作返回0
        when(adminSmFuncModDao.deleteBatchIds(anyList())).thenReturn(0);
        
        // When - 执行被测试方法
        int result = adminSmFuncModService.removeByModId(modIds);
        
        // Then - 验证结果
        assertEquals(0, result, "空数组应该返回0");
        
        // 验证业务功能服务被正确调用
        verify(adminSmBusiFuncService, times(1)).list(any(QueryWrapper.class));
        
        // 验证DAO的删除方法被正确调用
        verify(adminSmFuncModDao, times(1)).deleteBatchIds(anyList());
    }
    
    /**
     * 测试 removeByModId 异常场景 - 传入null时应该抛出异常
     */
    @Test
    @DisplayName("测试removeByModId_异常场景_传入null时抛出异常")
    public void testRemoveByModId_NullArray_ThrowsException() {
        // Given - 准备测试数据
        String[] modIds = null;
        
        // When & Then - 验证方法在null输入时抛出异常
        assertThrows(NullPointerException.class, () -> {
            adminSmFuncModService.removeByModId(modIds);
        });
    }

    /**
     * 测试 queryPageWithCondition 正常场景 - 查询条件不为空
     */
    @Test
    @DisplayName("测试queryPageWithCondition_正常场景_查询条件不为空")
    public void testQueryPageWithCondition_NormalCase() {
        // Given - 准备测试数据
        AdminSmFuncModQuery query = new AdminSmFuncModQuery();
        query.setModelName("测试模块");
        query.setPage(1);
        query.setSize(10);

        // Mock DAO 返回分页数据
        Page<AdminSmFuncModVo> page = new Page<>(1, 10);
        List<AdminSmFuncModVo> resultList = new ArrayList<>();
        AdminSmFuncModVo vo = new AdminSmFuncModVo();
        vo.setModName("测试模块");
        resultList.add(vo);
        // 设置返回的分页数据
        page.setRecords(resultList);
        when(adminSmFuncModDao.queryPageWithCondition(any(Page.class), any(QueryWrapper.class))).thenReturn(page);

        // When - 执行被测试方法
        Page<AdminSmFuncModVo> result = adminSmFuncModService.queryPageWithCondition(query);

        // Then - 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals("测试模块", result.getRecords().get(0).getModName());

        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).queryPageWithCondition(any(Page.class), any(QueryWrapper.class));
    }

    /**
     * 测试 queryPageWithCondition 边界场景 - 查询条件为空
     */
    @Test
    @DisplayName("测试queryPageWithCondition_边界场景_查询条件为空")
    public void testQueryPageWithCondition_EmptyCondition() {
        // Given - 准备测试数据
        AdminSmFuncModQuery query = new AdminSmFuncModQuery();
        query.setModelName(null); // 空查询条件
        query.setPage(1);
        query.setSize(10);

        // Mock DAO 返回分页数据
        Page<AdminSmFuncModVo> page = new Page<>(1, 10);
        when(adminSmFuncModDao.queryPageWithCondition(any(Page.class), any(QueryWrapper.class))).thenReturn(page);

        // When - 执行被测试方法
        Page<AdminSmFuncModVo> result = adminSmFuncModService.queryPageWithCondition(query);

        // Then - 验证结果
        assertNotNull(result);

        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).queryPageWithCondition(any(Page.class), any(QueryWrapper.class));
    }

    /**
     * 测试 checkName 正常场景 - 模块名称已存在
     */
    @Test
    @DisplayName("测试checkName_正常场景_模块名称已存在")
    public void testCheckName_NameExists() {
        // Given - 准备测试数据
        String modName = "测试模块";
        String modId = "mod1";

        // Mock DAO 返回已存在的模块ID
        List<Object> resultObjects = Arrays.asList("mod2", "mod3"); // 模拟已存在的模块ID
        when(adminSmFuncModDao.selectObjs(any(QueryWrapper.class))).thenReturn(resultObjects);

        // When - 执行被测试方法
        List<String> result = adminSmFuncModService.checkName(modName, modId);

        // Then - 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("mod2"));
        assertTrue(result.contains("mod3"));

        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).selectObjs(any(QueryWrapper.class));
    }

    /**
     * 测试 checkName 边界场景 - 模块名称不存在
     */
    @Test
    @DisplayName("测试checkName_边界场景_模块名称不存在")
    public void testCheckName_NameNotExists() {
        // Given - 准备测试数据
        String modName = "新模块";
        String modId = "mod1";

        // Mock DAO 返回空列表
        when(adminSmFuncModDao.selectObjs(any(QueryWrapper.class))).thenReturn(new ArrayList<>());

        // When - 执行被测试方法
        List<String> result = adminSmFuncModService.checkName(modName, modId);

        // Then - 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).selectObjs(any(QueryWrapper.class));
    }

    /**
     * 测试 checkName 异常场景 - 传入空参数
     */
    @Test
    @DisplayName("测试checkName_异常场景_传入空参数")
    public void testCheckName_EmptyParams() {
        // Given - 准备测试数据
        String modName = "";
        String modId = "";

        // Mock DAO 返回空列表
        when(adminSmFuncModDao.selectObjs(any(QueryWrapper.class))).thenReturn(new ArrayList<>());

        // When - 执行被测试方法
        List<String> result = adminSmFuncModService.checkName(modName, modId);

        // Then - 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).selectObjs(any(QueryWrapper.class));
    }

    /**
     * 测试 saveFuncMod 正常场景 - 模块名称不存在，可以保存
     */
    @Test
    @DisplayName("测试saveFuncMod_正常场景_模块名称不存在可以保存")
    public void testSaveFuncMod_Success() {
        // Given - 准备测试数据
        AdminSmFuncModBo bo = new AdminSmFuncModBo();
        bo.setModName("新模块");
        bo.setModId("mod_new");

        // Mock checkName 返回空列表，表示名称不存在
        doReturn(new ArrayList<>()).when(adminSmFuncModService).checkName(anyString(), anyString());
        
        // Mock DAO 插入操作返回1（成功插入一条记录）
        when(adminSmFuncModDao.insert(any(AdminSmFuncModEntity.class))).thenReturn(1);

        // When - 执行被测试方法
        int result = adminSmFuncModService.saveFuncMod(bo);

        // Then - 验证结果
        assertEquals(1, result, "应该成功保存模块");

        // 验证checkName方法被正确调用
        verify(adminSmFuncModService, times(1)).checkName(anyString(), anyString());
        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).insert(any(AdminSmFuncModEntity.class));
    }

    /**
     * 测试 saveFuncMod 异常场景 - 模块名称已存在，不能保存
     */
    @Test
    @DisplayName("测试saveFuncMod_异常场景_模块名称已存在不能保存")
    public void testSaveFuncMod_NameExists() {
        // Given - 准备测试数据
        AdminSmFuncModBo bo = new AdminSmFuncModBo();
        bo.setModName("已存在模块");
        bo.setModId("mod1");

        // Mock checkName 返回非空列表，表示名称已存在
        doReturn(Arrays.asList("mod2")).when(adminSmFuncModService).checkName(anyString(), anyString());

        // When - 执行被测试方法
        int result = adminSmFuncModService.saveFuncMod(bo);

        // Then - 验证结果
        assertEquals(-1, result, "模块名称已存在时应该返回-1");

        // 验证checkName方法被正确调用
        verify(adminSmFuncModService, times(1)).checkName(anyString(), anyString());
        // 验证DAO方法不应该被调用
        verify(adminSmFuncModDao, never()).insert(any(AdminSmFuncModEntity.class));
    }

    /**
     * 测试 updateFuncMod 正常场景 - 模块名称不存在，可以更新
     */
    @Test
    @DisplayName("测试updateFuncMod_正常场景_模块名称不存在可以更新")
    public void testUpdateFuncMod_Success() {
        // Given - 准备测试数据
        AdminSmFuncModBo bo = new AdminSmFuncModBo();
        bo.setModName("更新模块");
        bo.setModId("mod1");

        // Mock checkName 返回空列表，表示名称不存在（或只包含自己）
        doReturn(new ArrayList<>()).when(adminSmFuncModService).checkName(anyString(), anyString());
        
        // Mock DAO 更新操作返回1（成功更新一条记录）
        when(adminSmFuncModDao.updateById(any(AdminSmFuncModEntity.class))).thenReturn(1);

        // When - 执行被测试方法
        int result = adminSmFuncModService.updateFuncMod(bo);

        // Then - 验证结果
        assertEquals(1, result, "应该成功更新模块");

        // 验证checkName方法被正确调用
        verify(adminSmFuncModService, times(1)).checkName(anyString(), anyString());
        // 验证DAO方法被正确调用
        verify(adminSmFuncModDao, times(1)).updateById(any(AdminSmFuncModEntity.class));
    }

    /**
     * 测试 updateFuncMod 异常场景 - 模块名称已存在，不能更新
     */
    @Test
    @DisplayName("测试updateFuncMod_异常场景_模块名称已存在不能更新")
    public void testUpdateFuncMod_NameExists() {
        // Given - 准备测试数据
        AdminSmFuncModBo bo = new AdminSmFuncModBo();
        bo.setModName("已存在模块");
        bo.setModId("mod1");

        // Mock checkName 返回非空列表，表示名称已存在
        doReturn(Arrays.asList("mod2")).when(adminSmFuncModService).checkName(anyString(), anyString());

        // When - 执行被测试方法
        int result = adminSmFuncModService.updateFuncMod(bo);

        // Then - 验证结果
        assertEquals(-1, result, "模块名称已存在时应该返回-1");

        // 验证checkName方法被正确调用
        verify(adminSmFuncModService, times(1)).checkName(anyString(), anyString());
        // 验证DAO方法不应该被调用
        verify(adminSmFuncModDao, never()).updateById(any(AdminSmFuncModEntity.class));
    }
}
