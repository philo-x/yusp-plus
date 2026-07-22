package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.oca.service.AdminSmUserDutyRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 用户角色关联表
 *
 * @author terry
 * @email tanrui1@yusys.com.cn
 * @date 2020-12-01 21:55:19
 */
@RestController
@RequestMapping("/api/adminsmuserdutyrel")
public class AdminSmUserDutyRelController {
    @Autowired
    private AdminSmUserDutyRelService adminSmUserDutyRelService;
}
