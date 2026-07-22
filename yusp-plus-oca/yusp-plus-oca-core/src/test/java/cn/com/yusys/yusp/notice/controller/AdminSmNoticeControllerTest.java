package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeEntity;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeCondition;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeForm;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeService;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeVo;
import cn.com.yusys.yusp.notice.vo.NoticeHomePageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminSmNoticeControllerTest {

    @Mock
    private AdminSmNoticeService adminSmNoticeService;

    @InjectMocks
    private AdminSmNoticeController adminSmNoticeController;

    @Test
    void getViewList_ValidInput_ReturnsExpectedOutput() throws Exception {
        // 1. 准备请求参数和模拟返回值
        AdminSmNoticeCondition condition = new AdminSmNoticeCondition();
        condition.setPage(1);
        condition.setSize(10);

        IPage<NoticeHomePageVo> mockPage = new Page<>(1, 10);
        NoticeHomePageVo noticeHomePageVo = new NoticeHomePageVo();
        noticeHomePageVo.setNoticeId("1");
        noticeHomePageVo.setNoticeTitle("Test Notice");

        // 2. 定义 Mock 行为
        mockPage.setRecords(Collections.singletonList(noticeHomePageVo));
        when(adminSmNoticeService.getViewList(any(AdminSmNoticeCondition.class))).thenReturn(mockPage);

        // 3. 构造请求体
        JClientReqEntity<AdminSmNoticeCondition> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(condition);

        // 4. 调用 Controller 方法
        JClientRspEntity<IPage<NoticeHomePageVo>> rspEntity = adminSmNoticeController.getViewList(reqEntity);

        // 5. 验证结果
        assertEquals(rspEntity.getBody(), mockPage);
        assertEquals(rspEntity.getHead().getRetCode(), "0000");
        assertEquals(rspEntity.getBody().getRecords().size(), 1);
        assertEquals(rspEntity.getBody().getRecords().get(0).getNoticeId(), "1");
        assertEquals(rspEntity.getBody().getRecords().get(0).getNoticeTitle(), "Test Notice");

        verify(adminSmNoticeService, times(1)).getViewList(condition);
    }

    @Test
    void getUnreadList_ValidOutput_ReturnsExpectedResponse() {
        // 1. 准备模拟返回值
        AdminSmNoticeEntity notice1 = new AdminSmNoticeEntity();
        notice1.setNoticeId("1");
        notice1.setNoticeTitle("Test Notice 1");

        AdminSmNoticeEntity notice2 = new AdminSmNoticeEntity();
        notice2.setNoticeId("2");
        notice2.setNoticeTitle("Test Notice 2");

        List<AdminSmNoticeEntity> mockList = Arrays.asList(notice1, notice2);

        // 2. 定义 Mock 行为
        when(adminSmNoticeService.getUnreadList()).thenReturn(mockList);

        // 3. 调用 Controller 方法
        JClientRspEntity<List<AdminSmNoticeEntity>> response = adminSmNoticeController.getUnreadList();

        // 4. 验证结果
        assertEquals("0000", response.getHead().getRetCode());
        assertEquals(mockList, response.getBody());

        verify(adminSmNoticeService, times(1)).getUnreadList();
    }

    @Test
    void getControlList_ValidInput_ReturnsExpectedOutput() {
        // 1. 准备请求参数和模拟返回值
        AdminSmNoticeCondition condition = new AdminSmNoticeCondition();
        condition.setPage(1);
        condition.setSize(10);

        IPage<AdminSmNoticeEntity> mockPage = new Page<>(1, 10);
        AdminSmNoticeEntity notice = new AdminSmNoticeEntity();
        notice.setNoticeId("1");
        notice.setNoticeTitle("Test Notice");

        // 2. 定义 Mock 行为
        mockPage.setRecords(Collections.singletonList(notice));
        when(adminSmNoticeService.getControlList(any(AdminSmNoticeCondition.class))).thenReturn(mockPage);

        // 3. 构造请求体
        JClientReqEntity<AdminSmNoticeCondition> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(condition);

        // 4. 调用 Controller 方法
        JClientRspEntity<IPage<AdminSmNoticeEntity>> rspEntity = adminSmNoticeController.getControlList(reqEntity);

        // 5. 验证结果
        assertEquals(rspEntity.getBody(), mockPage);
        assertEquals(rspEntity.getHead().getRetCode(), "0000");
        assertEquals(rspEntity.getBody().getRecords().size(), 1);
        assertEquals(rspEntity.getBody().getRecords().get(0).getNoticeId(), "1");
        assertEquals(rspEntity.getBody().getRecords().get(0).getNoticeTitle(), "Test Notice");

        verify(adminSmNoticeService, times(1)).getControlList(condition);
    }

    @Test
    void createNotice_ValidInput_ReturnsSuccess() {
        // 1. 准备请求参数
        AdminSmNoticeForm form = new AdminSmNoticeForm();
        form.setNoticeTitle("Test Notice");
        form.setNoticeLevel("Normal");

        JClientReqEntity<AdminSmNoticeForm> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(form);

        // 2. 定义 Mock 行为
        doNothing().when(adminSmNoticeService).createNotice(any(AdminSmNoticeForm.class));

        // 3. 调用 Controller 方法
        JClientRspEntity<Object> rspEntity = adminSmNoticeController.createNotice(reqEntity);

        // 4. 验证结果
        assertEquals("0000", rspEntity.getHead().getRetCode());
        assertEquals("成功", rspEntity.getBody());

        verify(adminSmNoticeService, times(1)).createNotice(form);
    }

    @Test
    void deleteNotice_ValidInput_ReturnsSuccess() {
        // 1. 准备请求参数
        JClientReqEntity<List<String>> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(Arrays.asList("1", "2"));

        // 2. 定义 Mock 行为
        when(adminSmNoticeService.deleteNotice(anyList())).thenReturn(null);

        // 3. 调用 Controller 方法
        JClientRspEntity<Object> rspEntity = adminSmNoticeController.deleteNotice(reqEntity);

        // 4. 验证结果
        assertEquals("0000", rspEntity.getHead().getRetCode());
        assertEquals("成功", rspEntity.getBody());

        verify(adminSmNoticeService, times(1)).deleteNotice(Arrays.asList("1", "2"));
    }

    @Test
    void deleteNotice_ValidInput_ReturnsFailure() {
        // 1. 准备请求参数
        JClientReqEntity<List<String>> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(Arrays.asList("1", "2"));

        // 2. 定义 Mock 行为
        when(adminSmNoticeService.deleteNotice(anyList())).thenReturn("删除失败");

        // 3. 调用 Controller 方法
        JClientRspEntity<Object> rspEntity = adminSmNoticeController.deleteNotice(reqEntity);

        // 4. 验证结果
        assertEquals("删除失败", rspEntity.getBody());

        verify(adminSmNoticeService, times(1)).deleteNotice(Arrays.asList("1", "2"));
    }

    @Test
    void updateNotice_Success_ReturnsSuccessResponse() {
        // 1. 准备请求参数和模拟返回值
        AdminSmNoticeForm form = new AdminSmNoticeForm();
        form.setNoticeId("1");
        form.setNoticeTitle("Updated Notice");

        JClientReqEntity<AdminSmNoticeForm> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(form);

        when(adminSmNoticeService.updateNotice(form)).thenReturn(null);

        // 2. 调用 Controller 方法
        JClientRspEntity<Object> response = adminSmNoticeController.updateNotice(reqEntity);

        // 3. 验证结果
        assertEquals("0000", response.getHead().getRetCode());
        assertEquals("成功", response.getBody());

        verify(adminSmNoticeService, times(1)).updateNotice(form);
    }

    @Test
    void updateNotice_Failure_ReturnsFailureResponse() {
        // 1. 准备请求参数和模拟返回值
        AdminSmNoticeForm form = new AdminSmNoticeForm();
        form.setNoticeId("1");
        form.setNoticeTitle("Updated Notice");

        JClientReqEntity<AdminSmNoticeForm> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(form);

        when(adminSmNoticeService.updateNotice(form)).thenReturn("更新失败");

        // 2. 调用 Controller 方法
        JClientRspEntity<Object> response = adminSmNoticeController.updateNotice(reqEntity);

        // 3. 验证结果
        assertEquals("更新失败", response.getBody());
        assertEquals("500", response.getHead().getRetCode());

        verify(adminSmNoticeService, times(1)).updateNotice(form);
    }

    @Test
    void pubNotices_ValidInput_ReturnsSuccessResponse() {
        // 1. 准备请求参数
        JClientReqEntity<List<String>> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(Arrays.asList("1", "2", "3"));

        doNothing().when(adminSmNoticeService).pubNotices(reqEntity.getBody());

        // 2. 调用 Controller 方法
        JClientRspEntity<Object> response = adminSmNoticeController.pubNotices(reqEntity);

        // 3. 验证结果
        assertEquals("0000", response.getHead().getRetCode());
        assertEquals("成功", response.getBody());

        verify(adminSmNoticeService, times(1)).pubNotices(reqEntity.getBody());
    }

    @Test
    void getInfo_ValidNoticeId_ReturnsExpectedOutput() {
        // 1. 准备请求参数和模拟返回值
        String noticeId = "1";
        AdminSmNoticeVo mockVo = new AdminSmNoticeVo();
        mockVo.setNoticeId(noticeId);
        mockVo.setNoticeTitle("Test Notice");

        // 2. 定义 Mock 行为
        when(adminSmNoticeService.getInfo(noticeId)).thenReturn(mockVo);

        // 3. 调用 Controller 方法
        JClientRspEntity<AdminSmNoticeVo> response = adminSmNoticeController.getInfo(noticeId);

        // 4. 验证结果
        assertEquals("0000", response.getHead().getRetCode());
        assertEquals(mockVo, response.getBody());

        verify(adminSmNoticeService, times(1)).getInfo(noticeId);
    }

    @Test
    void getInfo_InvalidNoticeId_ReturnsEmptyOutput() {
        // 1. 准备请求参数和模拟返回值
        String noticeId = "invalid";

        // 2. 定义 Mock 行为
        when(adminSmNoticeService.getInfo(noticeId)).thenReturn(null);

        // 3. 调用 Controller 方法
        JClientRspEntity<AdminSmNoticeVo> response = adminSmNoticeController.getInfo(noticeId);

        // 4. 验证结果
        assertEquals("0000", response.getHead().getRetCode());
        assertNull(response.getBody());

        verify(adminSmNoticeService, times(1)).getInfo(noticeId);
    }
}
