package cn.com.yusys.yusp.oca.controller;


import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleExcelVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserExcelVo;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.component.RoleExcelInitListener;
import cn.com.yusys.yusp.oca.service.component.UserExcelInitListener;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author yusys
 * @version 1.0
 * @date 2020/7/15 10:07
 */
@RequestMapping("api/authinit")
@RestController
public class AdminAuthExcelInitController {
    private static final Logger log = LoggerFactory.getLogger(AdminAuthExcelInitController.class);

    @Autowired
    private AdminSmRoleService adminSmRoleService;

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;


    @Autowired
    private AdminSmUserService adminSmUserService;


    @PostMapping("/excel")
    public JClientRspEntity<String> authInitByExcel(MultipartFile file,int flag){
//d
        try (ExcelReader reader = EasyExcel.read(file.getInputStream()).build();) {
            ReadSheet roleSheet = EasyExcel.readSheet(1).head(AdminSmRoleExcelVo.class).registerReadListener(new RoleExcelInitListener(adminSmRoleService,flag)).build();
            ReadSheet userSheet = EasyExcel.readSheet(2).head(AdminSmUserExcelVo.class).registerReadListener(new UserExcelInitListener(adminSmUserService, adminSmUserRoleRelService,flag)).build();
            reader.read(roleSheet,userSheet);
        } catch (IOException e) {
            log.error("read excel error,", e);
        }
        return JClientRspEntity.buildSuccess("成功");
    }
}
