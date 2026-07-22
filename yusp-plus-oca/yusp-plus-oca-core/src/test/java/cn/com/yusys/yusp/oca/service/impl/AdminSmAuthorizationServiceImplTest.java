package cn.com.yusys.yusp.oca.service.impl;

import java.util.*;
import java.math.*;

import cn.com.yusys.yusp.oca.dao.AdminSmMenuDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantUserRelEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthTreeForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrAuthVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminTmplAndRecoVo;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.service.cache.AuthTreeService;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AdminSmAuthorizationServiceImplTest {

    @Mock
    @Qualifier("authTreeNoCacheService")
    private AuthTreeService authTreeService;

    @Mock
    private AdminSmAuthRecoService adminSmAuthRecoService;

    @Mock
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;

    @Mock
    private AdminSmResContrService adminSmResContrService;

    @Mock
    private AdminSmMenuDao adminSmMenuDao;

    @Mock
    private AdminSmTenantUserRelService adminSmTenantUserRelService;

    @Mock
    private AdminSmTenantService adminSmTenantService;

    @InjectMocks
    private AdminSmAuthorizationServiceImpl adminSmAuthorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("测试 getAuthTmplList 方法，查询结果为空")
    void getAuthTmplList_NoRecords_ReturnsEmptyPage() {
        // 1. 造数
        AdminSmAuthTreeForm form = new AdminSmAuthTreeForm();
        form.setAuthObjId("1");
        form.setKeyWord("");
        form.setDataTenantId("DT001");
        form.setPage(1);
        form.setSize(10);

        IPage<AdminSmAuthTreeVo> emptyPage = new Page<>();
        emptyPage.setRecords(new ArrayList<>());

        when(adminSmResContrService.selectAuthTreePage(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);

        // 2. 执行
        IPage<AdminSmResContrAuthVo> result = adminSmAuthorizationService.getAuthTmplList(form);

        // 3. 断言
        assertTrue(result.getRecords().isEmpty());
    }
}
