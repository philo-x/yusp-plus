package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.oca.service.AdminSmUserMgrOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 用户授权管理机构表
 *
 * @author terry
 * @email tanrui1@yusys.com.cn
 * @date 2020-12-02 16:29:50
 */
@RestController
@RequestMapping("/api/adminsmusermgrorg")
public class AdminSmUserMgrOrgController {
    @Autowired
    private AdminSmUserMgrOrgService adminSmUserMgrOrgService;




}
