package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.controller.AdminSmNoticeReadController;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AdminSmNoticeReadController 单元测试
 */
public class AdminSmNoticeReadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminSmNoticeReadService adminSmNoticeReadService;

    @InjectMocks
    private AdminSmNoticeReadController adminSmNoticeReadController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminSmNoticeReadController).build();
    }

    /**
     * 测试 recordRead 正常情况
     */
    @Test
    public void testRecordRead_Success() throws Exception {
        // 准备测试数据
        List<String> noticeIds = Arrays.asList("1", "2", "3");
        JClientReqEntity<List<String>> requestEntity = new JClientReqEntity<>();
        requestEntity.setBody(noticeIds);

        // 模拟 service 调用
        doNothing().when(adminSmNoticeReadService).recordRead(any(List.class));

        // 执行请求并验证响应
        mockMvc.perform(post("/api/notice/adminsmnoticeread/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":[\"1\",\"2\",\"3\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"))
                .andExpect(jsonPath("$.body").value("成功"));

        // 验证 service 方法被调用
        verify(adminSmNoticeReadService).recordRead(noticeIds);
    }

    /**
     * 测试 recordRead 参数为空的情况
     */
    @Test
    public void testRecordRead_EmptyList() throws Exception {
        // 准备测试数据
        List<String> noticeIds = Arrays.asList();
        JClientReqEntity<List<String>> requestEntity = new JClientReqEntity<>();
        requestEntity.setBody(noticeIds);

        // 模拟 service 调用
        doNothing().when(adminSmNoticeReadService).recordRead(any(List.class));

        // 执行请求并验证响应
        mockMvc.perform(post("/api/notice/adminsmnoticeread/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"))
                .andExpect(jsonPath("$.body").value("成功"));

        // 验证 service 方法被调用
        verify(adminSmNoticeReadService).recordRead(noticeIds);
    }

    /**
     * 测试 recordRead 参数为null的情况
     */
    @Test
    public void testRecordRead_NullList() throws Exception {
        // 执行请求并验证响应
        mockMvc.perform(post("/api/notice/adminsmnoticeread/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"))
                .andExpect(jsonPath("$.body").value("成功"));

        // 注意：由于参数为null，service方法不会被调用，因为controller中会进行null检查
    }
}
