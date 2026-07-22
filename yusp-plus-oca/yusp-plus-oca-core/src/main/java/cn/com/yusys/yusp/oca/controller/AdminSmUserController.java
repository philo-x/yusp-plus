package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserMgrOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserDutyRelQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserRoleRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统用户表
 *
 * @author terry
 * @date 2020-11-16 15:31:54
 */

@RestController
@RequestMapping("/api/adminsmuser")
public class AdminSmUserController {

    private final Logger log = LoggerFactory.getLogger(AdminSmUserController.class);

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private AdminSmRoleService adminSmRoleService;

    /**
     * 分页查询orgId下所有用户列表,按最后修改时间降序
     *
     * @param req 分页查询条件
     * @return 用户列表
     */
    @PostMapping("/auth/page")
    public JClientRspEntity<Page<AdminSmUserVo>> page(@RequestBody JClientReqEntity<AdminSmUserQuery> req) {
        AdminSmUserQuery query = req.getBody();
        query.setUserSts(AvailableStateEnum.ENABLED);
        Page<AdminSmUserVo> adminSmUserVoPage = adminSmUserService.queryPage(query);
        return JClientRspEntity.buildSuccess(adminSmUserVoPage);
    }

    /**
     * 分页查询orgId下所有用户列表,按最后修改时间降序
     *
     * @param query 分页查询条件
     * @return 用户列表
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmUserVo>> pageForAuth(@RequestBody JClientReqEntity<AdminSmUserQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmUserService.queryPage(query.getBody()));
    }

    /**
     * 详情-限制只能查本机构和子机构的，并且不能查超管，跟page接口限制保持一致
     *
     * @param userId 用户id
     * @return 用户详情
     */
    @PostMapping("/info/suborg/{userId}")
    public JClientRspEntity<AdminSmUserDetailVo> info(@PathVariable String userId) {
        return JClientRspEntity.buildSuccess(adminSmUserService.getDetailByIdAndOrg(userId));
    }

    /**
     * 详情
     *
     * @return 用户详情
     */
    @PostMapping("/info")
    public JClientRspEntity<AdminSmUserDetailVo> info() {
        return JClientRspEntity.buildSuccess(adminSmUserService.getDetailById(SessionUtils.getUserId()));
    }

    /**
     * 新增用户
     *
     * @param req 用户实体类
     * @return 成功返回 code = 0000，message"新增成功"
     */
    @PostMapping("/add")
    public JClientRspEntity<String> add(@Validated({Insert.class}) @RequestBody JClientReqEntity<AdminSmUserEntity> req) {
        AdminSmUserEntity entity = req.getBody();
        boolean existEntity=adminSmUserService.judgeExistEntity(entity);
        if (existEntity){
            throw BizException.error("exist", "51100005", entity.getLoginCode());
        }
        adminSmUserService.save(entity);
        //更新user信息的缓存
        adminSmUserService.updateUserCache(entity);
        return JClientRspEntity.buildSuccess("新增成功");
    }

    /**
     * 修改用户
     *
     * @param entity 用户实体类
     * @return 成功返回 code = 0000，message"修改成功"
     */
    @PostMapping("/update")
    public JClientRspEntity<Object> update(@Validated({Update.class}) @RequestBody JClientReqEntity<AdminSmUserEntity> entity) {
        adminSmUserService.updateById(entity.getBody());
        //更新user信息的缓存
        adminSmUserService.updateUserCache(entity.getBody());
        return JClientRspEntity.buildSuccess("修改成功");
    }

    /**
     * 批量启用
     *
     * @param ids 用户id数组
     * @return 成功返回 code = 0000，message"启用成功"
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<Object> batchEnable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmUserService.batchEnable(ids.getBody());
        return JClientRspEntity.buildSuccess("启用成功");
    }

    /**
     * 批量停用
     *
     * @param ids 用户id数组
     * @return 成功返回 code = 0000，message"停用成功"
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<Object> batchDisable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmUserService.batchDisable(ids.getBody());
        return JClientRspEntity.buildSuccess("停用成功");
    }

    /**
     * 批量删除
     *
     * @param ids 用户id数组
     * @return 成功返回 code = 0000，message"删除成功"
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<Object> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmUserService.batchDelete(ids.getBody());
        //删除user的部分信息
        adminSmUserService.deletePartUserCache(ids.getBody());
        return JClientRspEntity.buildSuccess("删除成功");
    }

    /**
     * 查询用户所在机构及其所有上级机构下可选的所有角色列表
     *
     * @param query 查询用户角色列表条件
     * @return 附带该用户与这些角色的关联状态
     */
    @PostMapping("/queryuserrole")
    public JClientRspEntity<List<UserRoleRelVo>> getUserRoleList(@RequestBody JClientReqEntity<AdminSmUserRoleRelQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmUserService.getUserRoleList(query.getBody()));
    }

    /**
     * 批量保存用户角色关联数据
     *
     * @param userId 被关联的用户id
     * @param ids    将要关联的角色Ids
     * @return 成功返回 code = 0000，message"保存成功"
     */
    @PostMapping("/saveuserrole/{userId}")
    public JClientRspEntity<Object> saveUserRoleRelList(@PathVariable() String userId, @RequestBody(required = false) JClientReqEntity<String[]> ids) {
        List<String> roleIds = new ArrayList<>();
        if (CollectionUtils.nonEmpty(ids.getBody())) {
            roleIds = Arrays.asList(ids.getBody());
        }
        List<AdminSmUserRoleRelEntity> list = roleIds.stream().map(roleId -> new AdminSmUserRoleRelEntity(userId, roleId)).collect(Collectors.toList());
        adminSmUserService.saveUserRoleList(userId, list);
        return JClientRspEntity.buildSuccess("保存成功");
    }

    /**
     * 查询用户所在机构及其所有上级机构下可选的所有岗位列表
     *
     * @param query 查询用户岗位列表条件
     * @return 附带该用户与这些岗位的关联状态
     */
    @PostMapping("/queryuserduty")
    public JClientRspEntity<List<UserDutyRelVo>> getUserDutyList(@RequestBody JClientReqEntity<AdminSmUserDutyRelQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmUserService.getUserDutyList(query.getBody()));
    }

    /**
     * 批量保存用户岗位关联数据
     *
     * @param userId 被关联的用户id
     * @param ids    将要关联的岗位Ids
     * @return 成功返回 code = 0000，message"保存成功"
     */
    @PostMapping("/saveuserduty/{userId}")
    public JClientRspEntity<Object> saveUserDutyRelList(@PathVariable() String userId, @RequestBody(required = false) JClientReqEntity<String[]> ids) {
        List<String> dutyIds = new ArrayList<>();
        if (CollectionUtils.nonEmpty(ids.getBody())) {
            dutyIds = Arrays.asList(ids.getBody());
        }
        List<AdminSmUserDutyRelEntity> list = dutyIds.stream().map(dutyId -> new AdminSmUserDutyRelEntity(userId, dutyId)).collect(Collectors.toList());
        adminSmUserService.saveUserDutyList(userId, list);
        return JClientRspEntity.buildSuccess("保存成功");
    }

    /**
     * 查询用户可授权机构树
     *
     * @param req 用户id
     * @return 附带该用户与这些机构的授权状态
     */
    @PostMapping("/queryuserorg")
    public JClientRspEntity<List<UserMgrOrgVo>> queryUserMgrOrgList(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        List<UserMgrOrgVo> list = adminSmUserService.getUserMgrOrgList(params.get("userId"));
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 批量保存用户授权机构数据
     *
     * @param userId 被授权的用户id
     * @param ids    将要授权的机构Ids
     * @return 成功返回 code = 0000，message"保存成功"
     */
    @PostMapping("/saveusermgrorg/{userId}")
    public JClientRspEntity<Object> saveUserMgrOrg(@PathVariable() String userId, @RequestBody(required = false) JClientReqEntity<String[]> ids) {
        List<String> orgIds = new ArrayList<>();
        if (CollectionUtils.nonEmpty(ids.getBody())) {
            orgIds = Arrays.asList(ids.getBody());
        }
        List<AdminSmUserMgrOrgEntity> list = orgIds.stream().map(orgId -> new AdminSmUserMgrOrgEntity(userId, orgId)).collect(Collectors.toList());
        adminSmUserService.saveUserMgrOrg(userId, list);
        return JClientRspEntity.buildSuccess("保存成功");
    }

    /**
     * 功能授权 粘贴用
     * 分页查询当前用户有权访问的所有用户列表,排除指定userId，按最后修改时间降序
     *
     * @param query 用户粘贴列表查询条件
     * @return 用户列表
     */
    @PostMapping("/waitpasteuserpage")
    public JClientRspEntity<Page<AdminSmUserVo>> pageAuth(@Validated @RequestBody JClientReqEntity<AdminSmPasteUserQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmUserService.queryPageExcept(query.getBody()));
    }

    /**
     * 获取机构+角色列表
     *
     * @param loginCode 账号
     * @return 用户详情
     */
    @PostMapping("/listorgroles/{loginCode}")
    public JClientRspEntity<List<AdminSmOrgRoleVo>> listOrgRoles(@PathVariable String loginCode) {
        List<AdminSmOrgRoleVo> list = adminSmUserService.listOrgRolesByCode(loginCode);
        return JClientRspEntity.buildSuccess(list);
    }
}
