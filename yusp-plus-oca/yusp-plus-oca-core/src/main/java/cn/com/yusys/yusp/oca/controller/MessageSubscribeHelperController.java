package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDutyEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserSubscribeVo;
import cn.com.yusys.yusp.oca.service.AdminSmDutyService;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author danyu
 */

@RestController
@RequestMapping("/api")
public class MessageSubscribeHelperController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageSubscribeHelperController.class);
    @Autowired
    AdminSmDutyService adminSmDutyService;

    @Autowired
    AdminSmOrgService adminSmOrgService;

    @Autowired
    AdminSmUserService adminSmUserService;

    @Autowired
    AdminSmRoleService adminSmRoleService;

    @PostMapping("/selectAllOrg")
    public JClientRspEntity<List<AdminSmOrgEntity>> selectAllOrg() {
        List<AdminSmOrgEntity> list = adminSmOrgService.list();

        return JClientRspEntity.buildSuccess(list);
    }

    @PostMapping("/selectAllUser")
    public JClientRspEntity<List<UserSubscribeVo>> selectUserSubscribeVoList() {
        List<UserSubscribeVo> list = adminSmUserService.selectUserSubscribeVoList();

        return JClientRspEntity.buildSuccess(list);
    }

    @PostMapping("/selectAllRole")
    public JClientRspEntity<List<AdminSmRoleEntity>> selectAllRole() {
        List<AdminSmRoleEntity> list = adminSmRoleService.list();

        return JClientRspEntity.buildSuccess(list);
    }

    @PostMapping("/selectAllDuty")
    public JClientRspEntity<List<AdminSmDutyEntity>> selectAllDuty() {
        List<AdminSmDutyEntity> list = adminSmDutyService.list();

        return JClientRspEntity.buildSuccess(list);
    }
}
