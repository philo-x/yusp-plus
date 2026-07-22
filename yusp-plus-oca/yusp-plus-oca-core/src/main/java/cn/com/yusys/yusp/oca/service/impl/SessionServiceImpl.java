package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.TableConstant;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.SessionService;
import cn.com.yusys.yusp.commons.session.constant.SessionConstants;
import cn.com.yusys.yusp.commons.session.context.UserContext;
import cn.com.yusys.yusp.commons.session.user.Control;
import cn.com.yusys.yusp.commons.session.user.DataControl;
import cn.com.yusys.yusp.commons.session.user.MenuControl;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.user.impl.*;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.domain.vo.AdminDataTmplControlVo;
import cn.com.yusys.yusp.oca.service.*;
import cn.cscb.uadp.tc.cachedependency.constant.CacheLevelEnum;
import cn.cscb.uadp.tc.cachedependency.service.CacheDependencyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * session业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service("ocaSessionService")
public class SessionServiceImpl implements SessionService {

    @Autowired
    private AdminSmMenuServiceImpl adminSmMenuService;

    @Autowired
    private AdminSmResContrServiceImpl adminSmResContrService;

    @Autowired
    private AdminSmDptService adminSmDptService;

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;

    @Autowired
    private AdminSmRoleService adminSmRoleService;

    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Autowired
    private AdminSmLogicSysService adminSmLogicSysService;

    @Autowired
    private AdminSmInstuService adminSmInstuService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;

    @Autowired
    private AdminSmUserDutyRelService adminSmUserDutyRelService;

    @Autowired
    private AdminSmDutyService adminSmDutyService;

    @Autowired
    private AdminSmTenantService adminSmTenantService;

    @Autowired
    private CacheDependencyService cacheDependencyService;

    @Override
    public List<? extends Control> getAllControls() {
        cacheDependencyService.recordDependencies(SessionConstants.CACHE_NAME_CONTROL + ":" + SessionConstants.CACHE_KEY_ALL_CONTROL,
                CacheLevelEnum.DAYS.getLevel(), TableConstant.ADMIN_SM_RES_CONTR);
        return adminSmResContrService.selectControlImplList();
    }

    /**
     * 获取用户信息
     *
     * @param clinetId 客户端id
     * @param userId   用户id
     * @return 用户信息
     */
    @Override
    public User getUserInfo(String clinetId, String userId) {

        UserInformation userInformation = new UserInformation();

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(clinetId)) {
            throw BizException.error(null, "500", "用户id或者客户端id为空");
        }
        UserContext userContext = UserContext.getUserContext();

        Map<String, String> extParam = new HashMap<>(1);
        Map<String, Object> additionalMap = new HashMap<>(1);
        //根据jwttoken信息解密出的sysId查询用户逻辑系统信息
        AdminSmLogicSysEntity logicSysEntity = adminSmLogicSysService.getOne(new LambdaQueryWrapper<AdminSmLogicSysEntity>()
                .eq(AdminSmLogicSysEntity::getSysId, clinetId));
        if (Objects.nonNull(logicSysEntity)) {
            ClientImpl client = new ClientImpl();
            BeanUtils.copyProperties(logicSysEntity, client);
            userInformation.setLogicSys(client);
        }

        //根据用户ID查询用户岗位信息
        //根据用户ID查询用户岗位关联表
        List<AdminSmUserDutyRelEntity> userDutyRelEntities = adminSmUserDutyRelService.list(new LambdaQueryWrapper<AdminSmUserDutyRelEntity>().eq(AdminSmUserDutyRelEntity::getUserId, userId));
        //根据岗位id查询岗位信息
        if (CollectionUtils.nonEmpty(userDutyRelEntities)) {
            List<DutyImpl> dutys = getDutys(userDutyRelEntities);
            userInformation.setDutys(dutys);
        }

        //根据jwttoken信息解密出的loginCode查询用户详细信息
        fillUserDetailInfo(userId, extParam, additionalMap, userInformation);
        userContext.setExtParams(extParam);
        UserContext.addUserContext(userContext);
        return userInformation;
    }

    private List<DutyImpl> getDutys(List<AdminSmUserDutyRelEntity> userDutyRelEntities) {
        return userDutyRelEntities.stream().map(item -> {
            DutyImpl duty = new DutyImpl();
            if (StringUtils.nonEmpty(item.getDutyId())) {
                AdminSmDutyEntity dutyEntity = adminSmDutyService.getOne(new LambdaQueryWrapper<AdminSmDutyEntity>()
                        .eq(AdminSmDutyEntity::getDutyId, item.getDutyId())
                        .eq(AdminSmDutyEntity::getDutySts, AvailableStateEnum.ENABLED));
                if (!ObjectUtils.isEmpty(dutyEntity)) {
                    BeanUtils.copyProperties(dutyEntity, duty);
                }
            }
            return duty;
        }).collect(Collectors.toList());
    }

    private void fillUserDetailInfo(String userId, Map<String, String> extParam, Map<String, Object> additionalMap, UserInformation userInformation) {
        AdminSmUserEntity userEntity = adminSmUserService.getOne(new LambdaQueryWrapper<AdminSmUserEntity>()
                .eq(AdminSmUserEntity::getUserId, userId)
                .eq(AdminSmUserEntity::getUserSts, AvailableStateEnum.ENABLED));

        if (Objects.nonNull(userEntity)) {
            // 查询用户所属租户是否是活跃状态，如果租户不是活跃状态，则阻止该租户下的用户登录
            AdminSmTenantEntity tenantEntity = adminSmTenantService.getById(userEntity.getDataTenantId());
            if (null == tenantEntity || Constants.TenantConst.TENANT_DISENABLE.equals(tenantEntity.getTenantSts())) {
                throw BizException.error(null, "50700012", "您所属的租户已注销，您暂时没有权限登录系统，请联系系统管理员!");
            }
            // session额外参数 租户ID
            extParam.put(OcaCommonConstants.SESSION_DATATENANT_KEY, tenantEntity.getTenantId());
            additionalMap.put(OcaCommonConstants.SESSION_DATATENANT_KEY, tenantEntity.getTenantId());
            // yusp-common-session中新扩展字段，用户自定义添加用户其他信息
            userInformation.setAdditionals(additionalMap);
            BeanUtils.copyProperties(userEntity, userInformation);
            userInformation.setLastLoginTime(DateUtils.formatDateTimeByDef(userEntity.getLastLoginTime()));
            //根据用户部门id查询部门详细信息
            fillDptDetail(userEntity, userInformation);

            //根据用户id查询用户角色
            List<RoleImpl> roles = getUserRoles(userEntity);
            userInformation.setRoles(roles);

            //根据用户机构Id查询机构详细信息
            fillOrgDetailInfo(userEntity, userInformation);

            // 如果用户不存在对应的机构，或者机构未启用，就进行提示，否则登录的时候没任何反应
            if (userInformation.getOrg() == null) {
                throw BizException.error(null, "50700004", "该用户对应的机构不存在或者未启用，请联系管理员");
            }
        }
    }

    private void fillDptDetail(AdminSmUserEntity userEntity, UserInformation userInformation) {
        if (StringUtils.nonEmpty(userEntity.getDptId())) {
            AdminSmDptEntity dptEntity = adminSmDptService.getOne(new LambdaQueryWrapper<AdminSmDptEntity>()
                    .eq(AdminSmDptEntity::getDptId, userEntity.getDptId())
                    .eq(AdminSmDptEntity::getDptSts, AvailableStateEnum.ENABLED));

            if (Objects.nonNull(dptEntity)) {
                DepartmentImpl department = new DepartmentImpl();
                BeanUtils.copyProperties(dptEntity, department);
                userInformation.setDpt(department);

                //根据用户部门id查询上级部门部门详细信息
                if (StringUtils.nonEmpty(dptEntity.getUpDptId())) {
                    AdminSmDptEntity upDptEntity = adminSmDptService.getOne(new LambdaQueryWrapper<AdminSmDptEntity>()
                            .eq(AdminSmDptEntity::getDptId, dptEntity.getUpDptId())
                            .eq(AdminSmDptEntity::getDptSts, AvailableStateEnum.ENABLED));

                    if (Objects.nonNull(upDptEntity)) {
                        DepartmentImpl department1 = new DepartmentImpl();
                        BeanUtils.copyProperties(upDptEntity, department1);
                        userInformation.setUpDpt(department1);
                    }
                }
            }
        }
    }

    private void fillOrgDetailInfo(AdminSmUserEntity userEntity, UserInformation userInformation) {
        if (StringUtils.nonEmpty(userEntity.getOrgId())) {
            AdminSmOrgEntity orgEntity = adminSmOrgService.getOne(new LambdaQueryWrapper<AdminSmOrgEntity>()
                    .eq(AdminSmOrgEntity::getOrgId, userEntity.getOrgId())
                    .eq(AdminSmOrgEntity::getOrgSts, AvailableStateEnum.ENABLED));

            if (Objects.nonNull(orgEntity)) {
                fillUserInformation(userInformation, orgEntity);
            }
        }
    }

    private void fillUserInformation(UserInformation userInformation, AdminSmOrgEntity orgEntity) {
        OrganizationImpl organization = new OrganizationImpl();
        BeanUtils.copyProperties(orgEntity, organization);
        userInformation.setOrg(organization);

        //根据机构id查询上级机构详细信息
        fillInformationUpOrg(userInformation, orgEntity);

        if (Objects.nonNull(orgEntity.getOrgLevel())) {
            userInformation.setOrgLevel(orgEntity.getOrgLevel().toString());
        }

        //根据金融机构ID查询金融机构信息
        fillInformationInstuOrg(userInformation, orgEntity);
    }

    private void fillInformationInstuOrg(UserInformation userInformation, AdminSmOrgEntity orgEntity) {
        if (StringUtils.nonEmpty(orgEntity.getInstuId())) {
            AdminSmInstuEntity instuEntity = adminSmInstuService.getOne(new LambdaQueryWrapper<AdminSmInstuEntity>()
                    .eq(AdminSmInstuEntity::getInstuId, orgEntity.getInstuId())
                    .eq(AdminSmInstuEntity::getInstuSts, AvailableStateEnum.ENABLED));

            if (Objects.nonNull(instuEntity)) {
                FinancialOrganizationsImpl financialOrganizations = new FinancialOrganizationsImpl();
                BeanUtils.copyProperties(instuEntity, financialOrganizations);
                userInformation.setInstuOrg(financialOrganizations);
            }
        }
    }

    private void fillInformationUpOrg(UserInformation userInformation, AdminSmOrgEntity orgEntity) {
        if (StringUtils.nonEmpty(orgEntity.getUpOrgId())) {
            AdminSmOrgEntity upOrgEntity = adminSmOrgService.getOne(new LambdaQueryWrapper<AdminSmOrgEntity>()
                    .eq(AdminSmOrgEntity::getOrgId, orgEntity.getUpOrgId())
                    .eq(AdminSmOrgEntity::getOrgSts, AvailableStateEnum.ENABLED));

            if (Objects.nonNull(upOrgEntity)) {
                OrganizationImpl organization1 = new OrganizationImpl();
                BeanUtils.copyProperties(upOrgEntity, organization1);
                userInformation.setUpOrg(organization1);
            }
        }
    }

    private List<RoleImpl> getUserRoles(AdminSmUserEntity userEntity) {
        List<AdminSmUserRoleRelEntity> userRoleList = adminSmUserRoleRelService.list(new LambdaQueryWrapper<AdminSmUserRoleRelEntity>()
                .eq(AdminSmUserRoleRelEntity::getUserId, userEntity.getUserId()));
        List<RoleImpl> roles = new ArrayList<>();
        if (CollectionUtils.nonEmpty(userRoleList)) {
            boolean isSorted = true;
            for (AdminSmUserRoleRelEntity userRole : userRoleList) {
                isSorted = fillRoleAndReturnSortedFlag(userRole, roles, isSorted);
            }
            if (isSorted) {
                roles = (ArrayList<RoleImpl>) roles.stream().sorted(Comparator.comparing(RoleImpl::getCode)).collect(Collectors.toList());
            }
        }
        return roles;
    }

    private boolean fillRoleAndReturnSortedFlag(AdminSmUserRoleRelEntity userRole, List<RoleImpl> roles, boolean isSorted) {
        if (StringUtils.nonEmpty(userRole.getRoleId())) {
            AdminSmRoleEntity roleEntity = adminSmRoleService.getOne(new LambdaQueryWrapper<AdminSmRoleEntity>()
                    .eq(AdminSmRoleEntity::getRoleId, userRole.getRoleId())
                    .eq(AdminSmRoleEntity::getRoleSts, AvailableStateEnum.ENABLED));
            if (Objects.nonNull(roleEntity)) {
                RoleImpl role = new RoleImpl();
                BeanUtils.copyProperties(roleEntity, role);
                if (userRole.getChecked() == 1) {
                    roles.add(0, role);
                    isSorted = false;
                } else {
                    roles.add(role);
                }
            }
        }
        return isSorted;
    }

    @Override
    public MenuControl getMenuControl(String clientId, String userId) {
        List<MenuImpl> menuVoList = adminSmMenuService.getAdminSmMenu(userId);
        List<ControlImpl> contrVoList = adminSmResContrService.getAdminSmContr(userId);
        MenuControlImpl menuControlBean = new MenuControlImpl();
        menuControlBean.setContr(contrVoList);
        menuControlBean.setMenu(menuVoList);
        return menuControlBean;
    }

    @Override
    public List<? extends DataControl> getDataControl(String clientId, String userId) {
        List<AdminDataTmplControlVo> tmplControlVoList = adminSmAuthRecoService.getDataTmplControl(userId);
        List<DataControlImpl> list = new ArrayList<>();
        for (AdminDataTmplControlVo vo : tmplControlVoList) {
            DataControlImpl controlVo = new DataControlImpl();
            BeanUtils.copyProperties(vo, controlVo);
            list.add(controlVo);
        }
        return list;
    }
}
