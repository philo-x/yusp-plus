package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.user.impl.UserInformation;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.dao.AdminSmTenantDao;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.domain.query.AdminSmTenantQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmTenantVo;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 租户管理
 * @author zhanyq
 * @date 2021-09-18 10:18
 */
@Service("adminSmTenantService")
public class AdminSmTenantServiceImpl extends ServiceImpl<AdminSmTenantDao, AdminSmTenantEntity> implements AdminSmTenantService {

    private static final int ROOT_ORG_LEVEL = 1;

    private static final String DATA_TENANT_ID = "data_tenant_id";


    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Autowired
    private AdminSmRoleService adminSmRoleService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;

    @Autowired
    private AdminSmInstuService adminSmInstuService;

    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;


    @Override
    public Page<AdminSmTenantEntity> queryPage(AdminSmTenantQuery params) {

        Page<AdminSmTenantEntity> iPage = params.getIPage();
        String tenantSts = params.getTenantSts();
        String keyWord = params.getTenantName();
        QueryWrapper<AdminSmTenantEntity> query = new QueryWrapper<>();
        query.like(StringUtils.nonEmpty(keyWord),"tenant_name", keyWord);
        query.like(StringUtils.nonEmpty(params.getCompanyName()),"company_name", params.getCompanyName());
        query.eq(StringUtils.nonEmpty(tenantSts), "tenant_sts", tenantSts);

        query.ne("tenant_id","1");
        query.orderByDesc("last_chg_dt");
        return getBaseMapper().selectPage(iPage,query);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminSmTenantVo saveBaseInfo(AdminSmTenantVo adminSmTenantVo) {
        // 1.保存租户信息并返回租户ID
        String tenantId = saveTenantInfo(adminSmTenantVo);
        // 2.初始化一个虚拟的金融结构
        String instuId = saveRootInstu(adminSmTenantVo, tenantId);
        // 3.保存机构信息-顶级机构
        String orgId = saveRootOrg(adminSmTenantVo, tenantId, instuId);
        // 4.保存租户管理员角色
        String roleId = saveRootRole(adminSmTenantVo, tenantId, orgId);
        // 5.保存租户管理员用户信息
        String userId = saveRootUser(adminSmTenantVo, tenantId, orgId);
        // 6. 保存租户管理员用户和角色关联信息
        saveRootUserRoleRel(tenantId, roleId, userId);
        // 7.保存租户同用户的关联关系
        saveRootUserTenantRel(tenantId,userId);

        adminSmTenantVo.setTenantId(tenantId);
        adminSmTenantVo.setRoleId(roleId);
        adminSmTenantVo.setUserId(userId);
        return adminSmTenantVo;
    }

    /**
     *
     * 保存租户管理员同用户的关联关系
     * @param tenantId 租户id
     * @param userId 用户id
     *
     */
    private void saveRootUserTenantRel(String tenantId, String userId) {
        AdminSmTenantUserRelEntity adminSmTenantUserRelEntity=new AdminSmTenantUserRelEntity();
        adminSmTenantUserRelEntity.setdataTenantId(tenantId);
        adminSmTenantUserRelEntity.setUserId(userId);
        adminSmTenantUserRelService.save(adminSmTenantUserRelEntity);
    }

    /**
     * 保存租户管理员用户角色关联关系
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @param userId 用户ID
     * @return Void
     * @author zhanyq
     * @date 2021-09-30 16:03
     */
    private void saveRootUserRoleRel(String tenantId, String roleId, String userId) {
        AdminSmUserRoleRelEntity adminSmUserRoleRelEntity = new AdminSmUserRoleRelEntity();
        adminSmUserRoleRelEntity.setUserId(userId);
        adminSmUserRoleRelEntity.setRoleId(roleId);
        adminSmUserRoleRelEntity.setDataTenantId(tenantId);
        adminSmUserRoleRelService.save(adminSmUserRoleRelEntity);
    }

    /**
     * 保存租户管理员信息
     * @param adminSmTenantVo 租户基本信息
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 用户ID
     * @author zhanyq
     * @date 2021-09-30 16:04
     */
    private String saveRootUser(AdminSmTenantVo adminSmTenantVo, String tenantId, String orgId) {
        AdminSmUserEntity adminSmUserEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmUserEntity.class);
        String userId = IdWorker.get32UUID();
        adminSmUserEntity.setUserId(userId);
        adminSmUserEntity.setUserSts(AvailableStateEnum.ENABLED);
        adminSmUserEntity.setOrgId(orgId);
        adminSmUserEntity.setDataTenantId(tenantId);
        adminSmUserService.save(adminSmUserEntity);
        adminSmUserService.updateUserCache(adminSmUserEntity);
        return userId;
    }

    /**
     * 保存租户管理员角色信息
     * @param adminSmTenantVo 租户基本信息
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 角色ID
     * @author zhanyq
     * @date 2021-09-30 16:06
     */
    private String saveRootRole(AdminSmTenantVo adminSmTenantVo, String tenantId, String orgId) {
        AdminSmRoleEntity roleEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmRoleEntity.class);
        String roleId = IdWorker.get32UUID();
        roleEntity.setRoleId(roleId);
        roleEntity.setOrgId(orgId);
        roleEntity.setRoleLevel(Constants.TenantConst.ROOT_ROLE_LEVEL);
        roleEntity.setRoleSts(AvailableStateEnum.ENABLED);
        roleEntity.setDataTenantId(tenantId);
        adminSmRoleService.save(roleEntity);
        return roleId;
    }

    /**
     * 保存租户机构信息
     * @param adminSmTenantVo 租户基本信息
     * @param tenantId 租户ID
     * @param instuId 金融机构ID
     * @return 返回机构ID
     * @author zhanyq
     * @date 2021-09-30 16:07
     */
    private String saveRootOrg(AdminSmTenantVo adminSmTenantVo, String tenantId, String instuId) {
        AdminSmOrgEntity adminSmOrgEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmOrgEntity.class);
        String orgId = IdWorker.get32UUID();
        adminSmOrgEntity.setOrgId(orgId);
        adminSmOrgEntity.setOrgLevel(ROOT_ORG_LEVEL);
        adminSmOrgEntity.setOrgSeq(adminSmTenantVo.getOrgCode());
        adminSmOrgEntity.setOrgSts(AvailableStateEnum.ENABLED);
        // 默认顶级机构的父级机构ID为1
        adminSmOrgEntity.setUpOrgId("1");
        adminSmOrgEntity.setInstuId(instuId);
        adminSmOrgEntity.setDataTenantId(tenantId);
        adminSmOrgService.getBaseMapper().insert(adminSmOrgEntity);
        adminSmOrgService.updateOrgCache(adminSmOrgEntity);
        return orgId;
    }

    /**
     * 保存租户金融机构信息
     * @param adminSmTenantVo 租户基本信息
     * @param tenantId 租户ID
     * @return 金融机构信息
     * @author zhanyq
     * @date 2021-09-30 16:08
     */
    private String saveRootInstu(AdminSmTenantVo adminSmTenantVo, String tenantId) {
        AdminSmInstuEntity adminSmInstuEntity = new AdminSmInstuEntity();
        String instuId = IdWorker.get32UUID();
        adminSmInstuEntity.setInstuId(instuId);
        adminSmInstuEntity.setInstuCde(instuId.substring(0,9));
        adminSmInstuEntity.setInstuSts(AvailableStateEnum.ENABLED.getCode());
        adminSmInstuEntity.setSysId(OcaCommonInfoUtils.getUserSysId());
        adminSmInstuEntity.setDataTenantId(tenantId);
        adminSmInstuEntity.setInstuName(adminSmTenantVo.getTenantName()+"金融机构");
        adminSmInstuService.save(adminSmInstuEntity);
        return instuId;
    }

    /**
     * 保存租户信息
     * @param adminSmTenantVo 租户基本信息
     * @return 租户ID
     * @author zhanyq
     * @date 2021-09-30 16:09
     */
    private String saveTenantInfo(AdminSmTenantVo adminSmTenantVo) {
        boolean existEntity=this.judgeExistEntity(adminSmTenantVo);
        if (existEntity) {
            throw BizException.error("exist", "51100001", "租户名称:" + adminSmTenantVo.getTenantName());
        }
        AdminSmTenantEntity adminSmTenantEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmTenantEntity.class);
        String tenantId = IdWorker.get32UUID();
        adminSmTenantEntity.setTenantId(tenantId);
        getBaseMapper().insert(adminSmTenantEntity);
        return tenantId;
    }

    private boolean judgeExistEntity(AdminSmTenantVo adminSmTenantVo) {
        AdminSmTenantEntity entity= getBaseMapper().selectOne(new QueryWrapper<AdminSmTenantEntity>().eq("TENANT_NAME", adminSmTenantVo.getTenantName()));
        return entity != null;
    }

    @Override
    public AdminSmTenantVo getBaseDetail(String tenantId) {
        // 1.查询租户基本信息
        AdminSmTenantEntity adminSmtenantEntity = this.getById(tenantId);

        // 2.查询租户顶级机构信息
        QueryWrapper<AdminSmOrgEntity> orgWrapper = new QueryWrapper<>();
        orgWrapper.eq(StringUtils.nonEmpty(tenantId),DATA_TENANT_ID,tenantId);
        orgWrapper.eq("org_level",ROOT_ORG_LEVEL);
        AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getOne(orgWrapper);

        // 3.查询租户管理员角色信息
        QueryWrapper<AdminSmRoleEntity> roleWrapper = new QueryWrapper<>();
        roleWrapper.eq("org_id",adminSmOrgEntity.getOrgId());
        roleWrapper.eq(DATA_TENANT_ID,tenantId);
        roleWrapper.eq("role_level",Constants.TenantConst.ROOT_ROLE_LEVEL);
        AdminSmRoleEntity adminSmRoleEntity = adminSmRoleService.getOne(roleWrapper);

        // 4.查询租户管理员用户信息
        AdminSmUserEntity adminSmUserEntity = getTenantUserInfo(tenantId);


        // 5.数据拼装
        AdminSmTenantVo adminSmTenantVo = new AdminSmTenantVo();
        BeanUtils.beanCopy(adminSmtenantEntity,adminSmTenantVo);
        BeanUtils.beanCopy(adminSmOrgEntity,adminSmTenantVo);
        BeanUtils.beanCopy(adminSmRoleEntity,adminSmTenantVo);
        BeanUtils.beanCopy(adminSmUserEntity,adminSmTenantVo);
        return adminSmTenantVo;
    }

    public AdminSmUserEntity getTenantUserInfo(String tenantId) {
        //获取租户关联的超级用户
        AdminSmTenantUserRelEntity adminSmTenantUserRelEntity = adminSmTenantUserRelService.getTenantUserRelInfo(tenantId);

        QueryWrapper<AdminSmUserEntity> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id",adminSmTenantUserRelEntity.getUserId());
        userWrapper.eq(DATA_TENANT_ID,tenantId);
        return adminSmUserService.getOne(userWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBaseInfo(AdminSmTenantVo adminSmTenantVo) {

        // 1.修改租户信息
        AdminSmTenantEntity adminSmTenantEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmTenantEntity.class);
        this.updateById(adminSmTenantEntity);

        // 2.修改租户顶级机构信息
        AdminSmOrgEntity adminSmOrgEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmOrgEntity.class);
        adminSmOrgEntity.setOrgSeq(adminSmTenantVo.getOrgCode());
        adminSmOrgService.updateById(adminSmOrgEntity);

        // 3.修改租户管理员角色信息
        AdminSmRoleEntity roleEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmRoleEntity.class);
        roleEntity.setRoleSts(AvailableStateEnum.ENABLED);
        adminSmRoleService.updateById(roleEntity);

        // 4.修改租户管理员用户信息
        AdminSmUserEntity adminSmUserEntity = BeanUtils.beanCopy(adminSmTenantVo, AdminSmUserEntity.class);
        //租户的用户状态是启用状态
        adminSmUserEntity.setUserSts(AvailableStateEnum.ENABLED);
        adminSmUserService.updateById(adminSmUserEntity);
    }

    @Override
    public void updateTenantSts(String[] tenantIds, String tenantSts) {
        List<AdminSmTenantEntity> adminSmTenantEntityList = new ArrayList<>();
        for (String tenantId : tenantIds) {
            AdminSmTenantEntity adminSmTenantEntity = new AdminSmTenantEntity();
            adminSmTenantEntity.setTenantId(tenantId);
            adminSmTenantEntity.setTenantSts(tenantSts);
            adminSmTenantEntityList.add(adminSmTenantEntity);
        }
        this.updateBatchById(adminSmTenantEntityList);
    }

    @Override
    public String getTanentId() {
        UserInformation userInformation;
        try {
            userInformation = (UserInformation) SessionUtils.getUserInformation();
        } catch (BizException e) {
            return null;
        }
        if (Objects.nonNull(userInformation) && Objects.nonNull(userInformation.getAdditionals()) && Objects.nonNull(userInformation.getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY))) {
            return String.valueOf(userInformation.getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY));
        }
        return null;
    }

    @Override
    public Set<String> getCanOperatTenants() {
        Set<String> tenentIds=new HashSet<>();
        //租户id为1的的租户系统有些基础数据是公用的（如菜单，模块，控制点等）
        tenentIds.add("1");
        String tanentId = this.getTanentId();
        tenentIds.add(tanentId);
        return tenentIds;
    }


}