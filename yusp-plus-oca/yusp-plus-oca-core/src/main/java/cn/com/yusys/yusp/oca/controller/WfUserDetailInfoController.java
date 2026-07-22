/**
 * Copyright (C), 2014-2021
 * FileName: WfUserDetailInfoController
 * Author: Administrator
 * Date: 2021/3/21 18:03
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Administrator 18:03 1.0.0 新建类
 */

package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.query.QueryModel;
import cn.com.yusys.yusp.oca.domain.query.*;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 〈〉
 * @author zhui
 * @create 2021/3/21
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/wf")
public class WfUserDetailInfoController {
    @Autowired
    private AdminSmOrgService orgService;
    @Autowired
    private AdminSmUserService userService;
    @Autowired
    private AdminSmDptService dptService;
    @Autowired
    private AdminSmDutyService dutyService;
    @Autowired
    private AdminSmRoleService roleService;

    @PostMapping("/getusers")
    public Page<AdminSmUserVo> getUsers(QueryModel param){
        AdminSmUserQuery query = new AdminSmUserQuery();
        query.setUserName((String) param.getCondition().get("userName"));
        query.setLoginCode((String) param.getCondition().get("userId"));
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setSort(param.getSort());
        return userService.getUsersForWf(query);
    }

    @PostMapping("/getdepts")
    public Page<AdminSmDptVo> getDepts(QueryModel param) {
        AdminSmDptQuery query = new AdminSmDptQuery();
        query.setOrgId((String) param.getCondition().get("orgId"));
        query.setDptCode((String) param.getCondition().get("deptId"));
        query.setDptName((String) param.getCondition().get("deptName"));
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setSort(param.getSort());
        return dptService.getDeptsForWf(query);
    }

    @PostMapping("/getdutys")
    public Page<AdminSmDutyVo> getDutys(QueryModel param) {
        AdminSmDutyQuery query = new AdminSmDutyQuery();
        query.setDutyCode((String) param.getCondition().get("dutyId"));
        query.setDutyName((String) param.getCondition().get("dutyName"));
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setSort(param.getSort());
        return dutyService.getDutysForWf(query);
    }

    @PostMapping("/getorgs")
    public Page<AdminSmOrgVo> getOrgs(QueryModel param) {
        AdminSmOrgExtQuery query = new AdminSmOrgExtQuery();
        query.setOrgName((String) param.getCondition().get("orgName"));
        query.setOrgCode((String) param.getCondition().get("orgCode"));
        query.setUpOrgId((String) param.getCondition().get("orgId"));
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setSort(param.getSort());
        return orgService.getOrgsForWf(query);
    }

    @PostMapping("/getroles")
    public Page<AdminSmRoleVo> getRoles(QueryModel param) {
        AdminSmRoleQuery query = new AdminSmRoleQuery();
        query.setRoleCode((String) param.getCondition().get("roleId"));
        query.setRoleName((String) param.getCondition().get("roleName"));
        query.setPage(param.getPage());
        query.setSize(param.getSize());
        query.setSort(param.getSort());
        return roleService.getRolesForWf(query);
    }

    @PostMapping("/getorgusers")
    public List<AdminSmUserVo> getUsersByOrg(@RequestParam String systemId,@RequestParam String orgId) {
        return userService.getUsersByOrgForWf(orgId);
    }

    @PostMapping("/getdeptusers")
    public List<AdminSmUserVo> getUsersByDept(@RequestParam String systemId,@RequestParam String deptId) {
        return userService.getUsersByDeptForWf(deptId);
    }

    @PostMapping("/getdutyusers")
    public List<AdminSmUserVo> getUsersByDuty(@RequestParam String systemId,@RequestParam String dutyId) {
        return userService.getUsersByDutyForWf(dutyId);
    }

    @PostMapping("/getroleusers")
    public List<AdminSmUserVo> getUsersByRole(@RequestParam String systemId,@RequestParam String roleId) {
        return userService.getUsersByRoleForWf(roleId);
    }

    @PostMapping("/getuserinfo")
    public AdminSmUserVo getUserInfo(@RequestParam String systemId, @RequestParam String userId) {
        return userService.getUserInfoForWf(userId);
    }

    @PostMapping("/getlowerorg")
    public List<String> getLowerOrgId(@RequestParam String orgCode) {
        return orgService.getLowerOrgId(orgCode);
    }

    @PostMapping("/getuserdutys")
    public List<String> getDutysByUserId(@RequestParam String userId){
        return dutyService.getDutysByUserIdForWf(userId);
    }

}
