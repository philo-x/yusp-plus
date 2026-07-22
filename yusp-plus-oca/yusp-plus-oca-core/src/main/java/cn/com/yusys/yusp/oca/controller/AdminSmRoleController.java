package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteRoleQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleUserRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import cn.com.yusys.yusp.oca.domain.vo.UserRelationshipVo;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import cn.com.yusys.yusp.oca.validation.Insert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 系统角色表
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
@RestController
@RequestMapping("/api/adminsmrole")
public class AdminSmRoleController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmRoleController.class);
    @Autowired
    private AdminSmRoleService adminSmRoleService;

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;


    /**
     * 角色列表
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmRoleVo>> page(@RequestBody JClientReqEntity<AdminSmRoleQuery> query) {
        Page<AdminSmRoleVo> adminSmRoleVoPage = adminSmRoleService.queryPage(query.getBody());
        return JClientRspEntity.buildSuccess(adminSmRoleVoPage);
    }

    /**
     * 列表
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    @PostMapping("/auth/page")
    public JClientRspEntity<Page<AdminSmRoleVo>> pageForAuth(@RequestBody JClientReqEntity<AdminSmRoleQuery> query) {
        AdminSmRoleQuery roleQuery = query.getBody();
        //功能授权的角色列表只显示启用状态的角色
        roleQuery.setRoleSts(AvailableStateEnum.ENABLED);
        Page<AdminSmRoleVo> adminSmRoleVoPage = adminSmRoleService.queryPage(roleQuery);

        return JClientRspEntity.buildSuccess(adminSmRoleVoPage);
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    @PostMapping("/info/{roleId}")
    public JClientRspEntity<AdminSmRoleDetailVo> info(@PathVariable String roleId) {
        return JClientRspEntity.buildSuccess(adminSmRoleService.getDetailById(roleId));
    }

    /**
     * 保存
     *
     * @param entity 角色实体类
     * @return 成功返回 code = 0000，message"新增成功"
     */
    @PostMapping("/add")
    public JClientRspEntity<String> add(@Validated({Insert.class}) @RequestBody JClientReqEntity<AdminSmRoleEntity> entity) {
        adminSmRoleService.save(entity.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 修改
     *
     * @param entity 角色实体类
     * @return 成功返回 code = 0
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@RequestBody JClientReqEntity<AdminSmRoleEntity> entity) {
        adminSmRoleService.updateById(entity.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 批量启用角色
     *
     * @param ids 角色id数组
     * @return 成功返回 code = 0000，message"更新成功"
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<String> batchEnable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmRoleService.batchEnable(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 批量停用角色
     *
     * @param ids 角色id数组
     * @return 成功返回 code = 0000，message"更新成功"
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<String> batchDisable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmRoleService.batchDisable(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色id数组
     * @return 成功返回 code = 0000，message"更新成功"
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<String> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmRoleService.batchDelete(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 机构树查询，从数据库中查出全量机构数据，通过代码去组织树形数据结构，从输入orgId算起最多返回5级（）
     *
     * @param query 角色id
     * @return 全量机构数据
     */
    @PostMapping("/orgtree")
    public JClientRspEntity<List<AdminSmOrgTreeNodeBo>> orgTreeQuery(@RequestBody JClientReqEntity<AdminSmRoleUserRelQuery> query) {
        log.info("Query organization tree in role page");
        List<AdminSmOrgTreeNodeBo> list = adminSmUserRoleRelService.getOrgTree(query.getBody().getRoleId());
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 查询角色下的用户
     *
     * @param query 查询条件
     * @return 对应角色下的用户
     */
    @PostMapping("/userlist")
    public JClientRspEntity<Page<UserRelationshipVo>> userList(@RequestBody JClientReqEntity<AdminSmRoleUserRelQuery> query) {
        Page<UserRelationshipVo> userVoList = adminSmUserRoleRelService.memberPage(query.getBody());
        return JClientRspEntity.buildSuccess(userVoList);
    }

    /**
     * 给角色批量新增关联用户
     *
     * @param roleId 要关联的角色id
     * @param ids    要与之关联的用户id列表
     * @return 成功返回 code = 0000，message"关联关系新增成功"
     */
    @PostMapping("/adduserrolerel/{roleId}")
    public JClientRspEntity<String> addRel(@PathVariable String roleId, @RequestBody JClientReqEntity<String[]> ids) {
        List<String> idList = Arrays.asList(ids.getBody());
        List<AdminSmUserRoleRelEntity> entityList = idList.stream().map(userId -> new AdminSmUserRoleRelEntity(userId, roleId)).collect(Collectors.toList());
        adminSmUserRoleRelService.save(entityList);
        return JClientRspEntity.buildSuccess("关联关系新增成功");
    }

    /**
     * 给角色批量移除关联用户
     *
     * @param roleId 目标角色id
     * @param ids    与之解除关联关系的用户id列表
     * @return 成功返回 code = 0000，message"解除关联关系成功"
     */
    @PostMapping("/removeuserrolerel/{roleId}")
    public JClientRspEntity<String> removeRel(@PathVariable String roleId, @RequestBody JClientReqEntity<String[]> ids) {
        List<String> idList = Arrays.asList(ids.getBody());
        List<AdminSmUserRoleRelEntity> entityList = idList.stream().map(userId -> new AdminSmUserRoleRelEntity(userId, roleId)).collect(Collectors.toList());
        adminSmUserRoleRelService.remove(entityList);
        return JClientRspEntity.buildSuccess("解除关联关系成功");
    }


    /**
     * 功能授权 粘贴用
     * 分页查询当前用户有权访问的所有角色列表,排除指定roleId，按最后修改时间降序
     *
     * @param query 用户粘贴列表查询条件
     * @return 用户列表
     */
    @PostMapping("/waitpasterolepage")
    public JClientRspEntity<Page<AdminSmRoleVo>> pageAuth(@Validated @RequestBody JClientReqEntity<AdminSmPasteRoleQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmRoleService.queryPageExcept(query.getBody()));
    }

    @PostMapping("/switchrole/{roleId}")
    public JClientRspEntity<String> switchRole(@PathVariable String roleId) {
        return  adminSmUserRoleRelService.switchRole(roleId);
    }


}
