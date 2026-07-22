package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.GenericBuilder;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.exception.PlatformException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmLogicSysDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmLogicSysBo;
import cn.com.yusys.yusp.oca.domain.constants.AdminSmLogicSysEnum;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogicSysEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMenuEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogicSysVo;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.AdminSmLogicSysService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 逻辑系统业务实现类
 *
 * @author zhanyq
 * @date 2021-06-25 10:36
 */
@Service("adminSmLogicSysService")
public class AdminSmLogicSysServiceImpl extends ServiceImpl<AdminSmLogicSysDao, AdminSmLogicSysEntity> implements AdminSmLogicSysService {

    @Autowired
    private AdminSmMenuService adminSmMenuService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;
    @Autowired
    private AdminSmUserService adminSmUserService;
    @Autowired
    private AdminSmRoleService adminSmRoleService;
    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;

    /**
     * 菜单主键新老映射关系
     */
    private Map<String, String> menuIdRel;

    private static final String SYS_ID = "SYS_ID";


    @Override
    public PageUtils getAdminSmLogicSys(Map<String, Object> params) {

        IPage<AdminSmLogicSysVo> page = new Query<AdminSmLogicSysVo>().getPage(params, "SYS_NAME", false);

        AdminSmLogicSysBo adminSmLogicSysBo = new AdminSmLogicSysBo();
        String json = (String) params.get("condition");
        if (json != null) {
            try {
                adminSmLogicSysBo = objectMapper.readValue(json, AdminSmLogicSysBo.class);
            } catch (Exception e) {
                log.error("查询条件解析失败：{}", e);
                throw BizException.error(null, "50400002", "System Unknown Error");
            }
        }
        Page<AdminSmLogicSysVo> adminSmLogicSysVoPage = getBaseMapper().
                getAdminSmLogicSys(page, adminSmLogicSysBo);
        return new PageUtils(adminSmLogicSysVoPage);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int updateAdminSmLogic(AdminSmLogicSysBo adminSmLogicSysBo, String funcId) {

        // bean copy
        AdminSmLogicSysEntity adminSmLogicSys = BeanUtils.beanCopy(adminSmLogicSysBo, AdminSmLogicSysEntity.class);

        // 更新admin_sm_logic_sys表
        int isLogicSysBoolean = getBaseMapper().updateById(adminSmLogicSys);

        // 更新 admin_sm_menu表
        boolean isMenuBoolean = adminSmMenuService.update(
                GenericBuilder.of(AdminSmMenuEntity::new).
                        with(AdminSmMenuEntity::setFuncId, funcId).build(),
                Wrappers.<AdminSmMenuEntity>lambdaUpdate()
                        .eq(AdminSmMenuEntity::getSysId, adminSmLogicSys.getSysId())
                        .eq(AdminSmMenuEntity::getMenuName, "首页")
        );
        if (isMenuBoolean && isLogicSysBoolean > 0) {
            return 1;
        }
        log.error("修改逻辑系统认证策略失败");
        return 0;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateAdminSmLogicStat(AdminSmLogicSysBo adminSmLogicSysBo) {

        // bean copy
        AdminSmLogicSysEntity adminSmLogicSys = BeanUtils.beanCopy(adminSmLogicSysBo, AdminSmLogicSysEntity.class);

        // query pre-status before updating method
        AdminSmLogicSysEntity adminSmLogicSysFromDb =
                this.getOne(new QueryWrapper<AdminSmLogicSysEntity>()
                        .eq(!StringUtils.isEmpty(adminSmLogicSys.getSysId()), SYS_ID, adminSmLogicSys.getSysId()));
        String dbSysSts = adminSmLogicSysFromDb.getSysSts();
        if (dbSysSts.equals(adminSmLogicSys.getSysSts())) {
            if (Constants.AdminSmLogicSysConstance.SYS_STATUS_ENABLE.equals(dbSysSts)) {
                return;
            }
            return;
        }
        // update sys status
        boolean updateById = this.updateById(adminSmLogicSys);
        if (updateById && Constants.AdminSmLogicSysConstance.SYS_STATUS_ENABLE.equals(adminSmLogicSys.getSysSts())) {
            JClientRspEntity.buildSuccess(String.valueOf(AdminSmLogicSysEnum.LOGIC_SYS_EXIT.getMessage()));
            return;
        }
        JClientRspEntity.buildSuccess(String.valueOf(AdminSmLogicSysEnum.LOGIC_SYS_NO_EXIT.getMessage()));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int deleteLogicAndCrelInfo(String sysId) {

        // 如果根据ID查到的逻辑系统中状态为生效不能删除
        AdminSmLogicSysEntity logicSysEntity = getBaseMapper().selectById(sysId);
        if (Constants.AdminSmLogicSysConstance.SYS_STATUS_ENABLE.equals(logicSysEntity.getSysSts())) {
            return -1;
        }
        int delSysIdInt = getBaseMapper().deleteById(sysId);
        // 删除认证信息
        adminSmCrelStraService.remove(new QueryWrapper<AdminSmCrelStraEntity>()
                .eq(SYS_ID, sysId));
        // 删除菜单信息
        adminSmMenuService.remove(new QueryWrapper<AdminSmMenuEntity>()
                .eq(SYS_ID, sysId));
        // 删除授权信息
        adminSmAuthRecoService.remove(new QueryWrapper<AdminSmAuthRecoEntity>()
                .eq(SYS_ID, sysId));

        // 删除角色       安全考虑，在页面模块删除
        // 删除角色关系  安全考虑，在页面模块删除

        return delSysIdInt;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public AdminSmLogicSysEntity insertAndCopy(AdminSmLogicSysBo adminSmLogicSysBo) {

        // bean copy
        AdminSmLogicSysEntity adminSmLogicSys =
                BeanUtils.beanCopy(adminSmLogicSysBo, AdminSmLogicSysEntity.class);

        // 初始逻辑系统ID
        adminSmLogicSys.setSysId(StringUtils.getUUID());
        // 校验逻辑系统，检查数据有效性
        Long sysNameInt = getBaseMapper().selectCount(new QueryWrapper<AdminSmLogicSysEntity>()
                .eq(!StringUtils.isEmpty(adminSmLogicSys.getSysName()), "SYS_NAME", adminSmLogicSys.getSysName())
                .or()
                .eq(!StringUtils.isEmpty(adminSmLogicSys.getSysCode()), "SYS_CODE", adminSmLogicSys.getSysCode().toUpperCase())
        );
        if (sysNameInt > 0) {
            log.error("逻辑系统名称或者简称code已存在!");
            throw BizException.error(null, "50400001", "逻辑系统名称或者简称编号已存在");
        }

        // 获取用户信息，不新增用户，为老用户增加新角色
        AdminSmUserEntity userEntity = adminSmUserService.getById(adminSmLogicSysBo.getUserId());
        // 初始化角色信息
        AdminSmRoleEntity adminSmRole = initRole(adminSmLogicSysBo.getUserId(),
                adminSmLogicSysBo.getSysCode().toUpperCase(), userEntity.getOrgId());
        // 角色 role_code 校验
        List<AdminSmRoleEntity> roleList = adminSmRoleService.list(new QueryWrapper<AdminSmRoleEntity>()
                .eq(!StringUtils.isEmpty(adminSmRole.getRoleCode()), "ROLE_CODE", adminSmRole.getRoleCode()));
        if (roleList.size() > 0) {
            throw new PlatformException("用户角色role_code已存在!");
        }

        // 初始化用户角色关系
        AdminSmUserRoleRelEntity adminSmUserRoleRel = initUserRole(adminSmLogicSysBo.getUserId(),
                userEntity.getUserId(), adminSmRole.getRoleId());
        // 初始化菜单列表，复制系统管理的所有菜单
        List<AdminSmMenuEntity> adminSmMenuList = initMenu(adminSmLogicSysBo.getSysId(),
                adminSmLogicSys.getSysId(), adminSmLogicSysBo.getFuncId());
        // 初始化授权信息
        List<AdminSmAuthRecoEntity> adminSmAuthRecoList = initResReco(adminSmLogicSysBo.getSysId(), adminSmLogicSys.getSysId(),
                adminSmLogicSysBo.getRoleId(), adminSmRole.getRoleId(), adminSmLogicSysBo.getUserId());
        // 初始化认证策略
        List<AdminSmCrelStraEntity> adminSmCrelStraList = initCrelStra(adminSmLogicSys.getSysId(), adminSmLogicSysBo.getUserId());
        // 持久化逻辑系统 不新增用户，为老用户增加新角色
        getBaseMapper().insert(adminSmLogicSys);
        // 持久化角色
        adminSmRoleService.save(adminSmRole);
        // 持久化用户角色关系
        adminSmUserRoleRelService.save(adminSmUserRoleRel);
        // 持久化菜单信息
        adminSmMenuService.saveBatch(adminSmMenuList);
        // 持久化授权信息
        adminSmAuthRecoService.saveBatch(adminSmAuthRecoList);
        // 持久化认证策略
        adminSmCrelStraService.saveBatch(adminSmCrelStraList);

        return adminSmLogicSys;
    }

    /**
     * 组装初始化角色信息
     *
     * @param userId  用户ID
     * @param sysCode 逻辑系统编码
     * @param orgId   机构ID
     * @return 组装好的角色数据
     * @author zhanyq
     * @date 2021-06-25 10:45
     */
    private AdminSmRoleEntity initRole(String userId, String sysCode, String orgId) {

        AdminSmRoleEntity adminSmRole = GenericBuilder.of(AdminSmRoleEntity::new)
                .with(AdminSmRoleEntity::setRoleCode, sysCode
                        + Constants.AdminSmLogicSysConstance.DEFAULT_USER)
                .with(AdminSmRoleEntity::setRoleName, sysCode
                        + Constants.AdminSmLogicSysConstance.DEFAULT_ROLE_NAME)
                .with(AdminSmRoleEntity::setRoleSts, AvailableStateEnum.ENABLED)
                .with(AdminSmRoleEntity::setRoleLevel, Constants.AdminSmLogicSysConstance.DEFAULT_ROLE_LEVEL)
                .with(AdminSmRoleEntity::setOrgId, orgId)
                .with(AdminSmRoleEntity::setRoleId, StringUtils.getUUID())
                .with(AdminSmRoleEntity::setLastChgUsr, userId)
                .with(AdminSmRoleEntity::setLastChgDt, new Date())
                .build();

        return adminSmRole;
    }

    /**
     * 组装初始化登录策略信息
     *
     * @param sysId    系统ID
     * @param opUserId 操作用户ID
     * @return 组装好的登录策略信息
     * @author zhanyq
     * @date 2021-06-25 10:47
     */
    private List<AdminSmCrelStraEntity> initCrelStra(String sysId, String opUserId) {

        ArrayList<AdminSmCrelStraEntity> adminSmCrelStraList = new ArrayList<>();
        AdminSmCrelStraEntity adminSmCrelStraEntity =
                GenericBuilder.of(AdminSmCrelStraEntity::new)
                        .with(AdminSmCrelStraEntity::setActionType, "3")
                        .with(AdminSmCrelStraEntity::setCrelName, Constants.AdminSmLogicSysConstance.DEFAULT_CRELSTRA_RULE)
                        .with(AdminSmCrelStraEntity::setCrelDetail, "1")
                        .with(AdminSmCrelStraEntity::setCrelId, StringUtils.getUUID())
                        .with(AdminSmCrelStraEntity::setEnableFlag, "1")
                        .with(AdminSmCrelStraEntity::setLastChgUsr, opUserId)
                        .with(AdminSmCrelStraEntity::setSysId, sysId)
                        .build();
        adminSmCrelStraList.add(adminSmCrelStraEntity);

        return adminSmCrelStraList;
    }


    /**
     * 组装初始化授权信息
     *
     * @param oldSysId  老系统ID
     * @param newSysId  新系统ID
     * @param oldRoleId 老角色ID
     * @param newRoleId 新角色
     * @param opUserId  操作用户ID
     * @return 组装好的授权信息
     * @author zhanyq
     * @date 2021-06-25 10:48
     */
    private List<AdminSmAuthRecoEntity> initResReco(String oldSysId, String newSysId, String oldRoleId, String newRoleId, String opUserId) {

        List<AdminSmAuthRecoEntity> adminSmAuthRecoList = adminSmAuthRecoService.list(new QueryWrapper<AdminSmAuthRecoEntity>()
                .eq(!StringUtils.isEmpty(oldSysId), SYS_ID, oldSysId)
                .eq(!StringUtils.isEmpty(oldRoleId), "AUTHOBJ_ID", oldRoleId)
                .eq("AUTHOBJ_TYPE", Constants.AdminSmLogicSysConstance.DEFAULT_AUTH_OBJ_TYPE)
        );
        List<AdminSmAuthRecoEntity> authRecoList = adminSmAuthRecoList.stream().peek(adminSmAuthReco -> {
            if (Constants.AdminSmLogicSysConstance.DEFAULT_AUTH_RES_TYPE.equals(adminSmAuthReco.getAuthresType())) {
                if (menuIdRel.containsKey(adminSmAuthReco.getAuthresId())) {
                    adminSmAuthReco.setAuthresId(menuIdRel.get(adminSmAuthReco.getAuthresId()));
                }
            }
            if (menuIdRel.containsKey(adminSmAuthReco.getMenuId())) {
                adminSmAuthReco.setMenuId(menuIdRel.get(adminSmAuthReco.getMenuId()));
            }
            adminSmAuthReco.setAuthRecoId(StringUtils.getUUID());
            adminSmAuthReco.setSysId(newSysId);
            adminSmAuthReco.setAuthobjId(newRoleId);
            adminSmAuthReco.setLastChgUsr(opUserId);
        }).collect(Collectors.toList());

        return authRecoList;
    }


    /**
     * 组装用户角色关系
     *
     * @param oldUserId 老用户ID
     * @param userId    新用户ID
     * @param roleId    角色ID
     * @return 组装好的用户角色关系实体
     * @author zhanyq
     * @date 2021-06-25 10:54
     */
    private AdminSmUserRoleRelEntity initUserRole(String oldUserId, String userId, String roleId) {

        AdminSmUserRoleRelEntity adminSmUserRoleRel = GenericBuilder.of(AdminSmUserRoleRelEntity::new)
                .with(AdminSmUserRoleRelEntity::setUserId, userId)
                .with(AdminSmUserRoleRelEntity::setRoleId, roleId)
                .with(AdminSmUserRoleRelEntity::setLastChgDt, new Date())
                .with(AdminSmUserRoleRelEntity::setLastChgUsr, oldUserId)
                .with(AdminSmUserRoleRelEntity::setUserRoleRelId, oldUserId)
                .build();

        return adminSmUserRoleRel;
    }

    /**
     * 组装初始化菜单信息
     *
     * @param oldSysId 老系统ID
     * @param newSysId 新系统ID
     * @param funcId   业务功能ID
     * @return 组装好的菜单实体
     * @author zhanyq
     * @date 2021-06-25 10:56
     */
    private List<AdminSmMenuEntity> initMenu(String oldSysId, String newSysId, String funcId) {

        // 根据sysId查询menu
        List<AdminSmMenuEntity> menuList = adminSmMenuService.list(new QueryWrapper<AdminSmMenuEntity>()
                .eq(!StringUtils.isEmpty(oldSysId), SYS_ID, oldSysId)
                .ne("MENU_NAME", "逻辑系统管理")
                .or()
                .ne("FUNC_ID", "beb23fb37bc548b39b52d938f75470fe")
        );
        // 如果新增逻辑系统配置了首页，则将首页funcId替换成新的funcId
        List<AdminSmMenuEntity> adminSmMenuList = menuList.stream()
                .map(adminSmMenuEntity -> {
                    // 如果新增逻辑系统配置了首页，则将首页funcId替换成新的funcId
                    if (Constants.AdminSmLogicSysConstance.HOME_PAGE_ID.equals(adminSmMenuEntity.getFuncId()) ||
                            "首页".equals(adminSmMenuEntity.getMenuName())) {
                        adminSmMenuEntity.setFuncId(funcId);
                    }
                    // 记录menuId新老关系
                    String newMenuId;
                    if (menuIdRel.containsKey(adminSmMenuEntity.getMenuId())) {
                        newMenuId = StringUtils.getUUID();
                        menuIdRel.put(adminSmMenuEntity.getMenuId(), newMenuId);
                    } else {
                        newMenuId = menuIdRel.get(adminSmMenuEntity.getMenuId());
                    }
                    adminSmMenuEntity.setMenuId(newMenuId);
                    adminSmMenuEntity.setSysId(newSysId);
                    return adminSmMenuEntity;
                }).peek(adminSmMenuEntity -> {
                    // 更新所有老的menuId,如果有对应关系，才做菜单关系更新
                    if (menuIdRel.containsKey(adminSmMenuEntity.getUpMenuId())) {
                        adminSmMenuEntity.setUpMenuId(menuIdRel.get(adminSmMenuEntity.getUpMenuId()));
                    }
                }).collect(Collectors.toList());

        return adminSmMenuList;
    }

}