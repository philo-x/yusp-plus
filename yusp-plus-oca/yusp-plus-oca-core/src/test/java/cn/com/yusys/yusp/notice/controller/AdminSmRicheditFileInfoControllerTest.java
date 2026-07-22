package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.notice.form.AdminSmRicheditFileInfoForm;
import cn.com.yusys.yusp.notice.service.AdminSmRicheditFileInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminSmRicheditFileInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminSmRicheditFileInfoService adminSmRicheditFileInfoService;

    @InjectMocks
    private AdminSmRicheditFileInfoController adminSmRicheditFileInfoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminSmRicheditFileInfoController).build();
    }

    @Test
    void addFileInfo_ValidInput_ReturnsSuccess() throws Exception {
        JClientReqEntity<List<AdminSmRicheditFileInfoForm>> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(List.of(new AdminSmRicheditFileInfoForm()));

        mockMvc.perform(post("/api/adminsmricheditfileinfo/add")
                .contentType("application/json")
                .content("{\"body\":[{}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"));

        verify(adminSmRicheditFileInfoService, times(1)).addFileInfo(anyList());
    }

    @Test
    void delete_ValidInput_ReturnsSuccess() throws Exception {
        JClientReqEntity<List<AdminSmRicheditFileInfoForm>> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody(List.of(new AdminSmRicheditFileInfoForm()));

        mockMvc.perform(post("/api/adminsmricheditfileinfo/delete")
                .contentType("application/json")
                .content("{\"body\":[{}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"));

        verify(adminSmRicheditFileInfoService, times(1)).deleteFileInfo(anyList());
    }

    @Test
    void deleteWithFileServer_ValidInput_ReturnsSuccess() throws Exception {
        JClientReqEntity<String> reqEntity = new JClientReqEntity<>();
        reqEntity.setBody("testFileId");

        mockMvc.perform(post("/api/adminsmricheditfileinfo/del")
                .contentType("application/json")
                .content("{\"body\":\"testFileId\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.head.retCode").value("0000"));

        verify(adminSmRicheditFileInfoService, times(1)).deleteFileInfoWithFileServer(anyString());
    }
}
