package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.notice.constant.NoticeConstant;
import cn.com.yusys.yusp.notice.dao.AdminSmNoticeReciveDao;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReciveEntity;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeReciveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/17 21:13
 */
public class AdminSmNoticeReciveServiceImplTest {

    @InjectMocks
    @Spy
    private AdminSmNoticeReciveServiceImpl adminSmNoticeReciveService;

    @Mock
    private AdminSmNoticeReciveDao adminSmNoticeReciveDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(adminSmNoticeReciveDao).when(adminSmNoticeReciveService).getBaseMapper();
    }

    @Test
    @DisplayName("测试 findListByCondition 方法，传入空的角色ID列表和组织ID列表")
    void findListByCondition_EmptyLists_ReturnsEmptyList() {
        List<String> emptyRoleList = new ArrayList<>();
        List<String> emptyOrgList = new ArrayList<>();

        when(adminSmNoticeReciveDao.selectByCondition(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        List<AdminSmNoticeReciveEntity> result = adminSmNoticeReciveService.findListByCondition(emptyRoleList, emptyOrgList);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("测试 findListByCondition 方法，传入非空的角色ID列表和组织ID列表")
    void findListByCondition_NonEmptyLists_ReturnsPopulatedList() {
        List<String> roleList = new ArrayList<>();
        roleList.add("role1");
        List<String> orgList = new ArrayList<>();
        orgList.add("org1");
        when(adminSmNoticeReciveDao.selectByCondition(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        List<AdminSmNoticeReciveEntity> result = adminSmNoticeReciveService.findListByCondition(roleList, orgList);
        assertEquals(0, result.size());
    }


    @Test
    @DisplayName("测试 selectReciveIdAndName 方法，返回空列表")
    void selectReciveIdAndName_NoData_ReturnsEmptyList() {
        // 1. 造数
        when(adminSmNoticeReciveDao.selectReciveIdAndName(anyString())).thenReturn(Collections.emptyList());

        // 2. 执行
        List<AdminSmNoticeReciveVo> result = adminSmNoticeReciveService.selectReciveIdAndName("noticeId");

        // 3. 断言
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("测试 selectReciveIdAndName 方法，返回非空列表")
    void selectReciveIdAndName_HasData_ReturnsPopulatedList() {
        // 1. 造数
        List<AdminSmNoticeReciveVo> mockData = new ArrayList<>();
        AdminSmNoticeReciveVo vo = new AdminSmNoticeReciveVo();
        vo.setReciveId("reciveId");
        vo.setOrgName("orgName");
        mockData.add(vo);
        when(adminSmNoticeReciveDao.selectReciveIdAndName(anyString())).thenReturn(mockData);

        // 2. 执行
        List<AdminSmNoticeReciveVo> result = adminSmNoticeReciveService.selectReciveIdAndName("noticeId");

        // 3. 断言
        assertFalse(result.isEmpty());
        assertEquals("reciveId", result.get(0).getReciveId());
        assertEquals("orgName", result.get(0).getOrgName());
    }

    @Test
    @DisplayName("测试 saveByAdminSmNoticeAllEntity 方法，类型为 RECIVE_UPDATE")
    void saveByAdminSmNoticeAllEntity_TypeUpdate_DeletesAndSavesEntities() {
        // 1. 造数
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity();
        reciveEntity.setNoticeId(UUID.randomUUID().toString());
        String type = NoticeConstant.RECIVE_UPDATE;

        List<AdminSmNoticeReciveEntity> entityList = new ArrayList<>();
        AdminSmNoticeReciveEntity adminSmNoticeReciveEntity = new AdminSmNoticeReciveEntity();
        adminSmNoticeReciveEntity.setNoticeId(reciveEntity.getNoticeId());
        entityList.add(adminSmNoticeReciveEntity);

        doReturn(entityList).when(adminSmNoticeReciveService).getAnnouncementEntity(any());
        doReturn(true).when(adminSmNoticeReciveService).saveBatch(entityList);

        // 2. 执行
        adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, type);

        // 3. 断言
        verify(adminSmNoticeReciveService, times(1)).deleteRecive(anyList());
        verify(adminSmNoticeReciveService, times(1)).saveBatch(entityList);
    }

    @Test
    @DisplayName("测试 saveByAdminSmNoticeAllEntity 方法，部门和角色ID均为空")
    void saveByAdminSmNoticeAllEntity_EmptyIds_SavesNaEntities() {
        // 1. 造数
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity();
        reciveEntity.setNoticeId(UUID.randomUUID().toString());
        reciveEntity.setReciveOgjId("");
        reciveEntity.setReciveRoleId("");

        String type = "OTHER";

        List<AdminSmNoticeReciveEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ROLE, NoticeConstant.RECIVE_OGJ_ID_NA));
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ORG, NoticeConstant.RECIVE_OGJ_ID_NA));

        doReturn(expectedEntities).when(adminSmNoticeReciveService).getAnnouncementEntity(any());
        doReturn(true).when(adminSmNoticeReciveService).saveBatch(expectedEntities);

        // 2. 执行
        adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, type);

        // 3. 断言
        verify(adminSmNoticeReciveService, times(1)).saveBatch(expectedEntities);
    }

    @Test
    @DisplayName("测试 saveByAdminSmNoticeAllEntity 方法，拆分角色访问权限")
    void saveByAdminSmNoticeAllEntity_SplitRoleAccess_SavesRoleEntities() {
        // 1. 造数
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity();
        reciveEntity.setNoticeId(UUID.randomUUID().toString());
        reciveEntity.setReciveRoleId("role1,role2");

        String type = "OTHER";

        List<AdminSmNoticeReciveEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ROLE, "role1"));
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ROLE, "role2"));

        doReturn(expectedEntities).when(adminSmNoticeReciveService).getAnnouncementEntity(any());
        doReturn(true).when(adminSmNoticeReciveService).saveBatch(expectedEntities);

        // 2. 执行
        adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, type);

        // 3. 断言
        verify(adminSmNoticeReciveService, times(1)).saveBatch(expectedEntities);
    }

    @Test
    @DisplayName("测试 saveByAdminSmNoticeAllEntity 方法，拆分部门访问权限")
    void saveByAdminSmNoticeAllEntity_SplitOrgAccess_SavesOrgEntities() {
        // 1. 造数
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity();
        reciveEntity.setNoticeId(UUID.randomUUID().toString());
        reciveEntity.setReciveOgjId("org1,org2");

        String type = "OTHER";

        List<AdminSmNoticeReciveEntity> expectedEntities = new ArrayList<>();
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ORG, "org1"));
        expectedEntities.add(new AdminSmNoticeReciveEntity(StringUtils.getUUID(), reciveEntity.getNoticeId(), NoticeConstant.RECIVE_TYPE_ORG, "org2"));

        doReturn(expectedEntities).when(adminSmNoticeReciveService).getAnnouncementEntity(any());
        doReturn(true).when(adminSmNoticeReciveService).saveBatch(expectedEntities);


        // 2. 执行
        adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, type);

        // 3. 断言
        verify(adminSmNoticeReciveService, times(1)).saveBatch(expectedEntities);
    }

}
