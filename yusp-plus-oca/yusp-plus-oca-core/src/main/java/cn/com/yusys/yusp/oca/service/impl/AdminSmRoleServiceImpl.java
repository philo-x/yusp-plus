package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.ObjectUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmRoleDao;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteRoleQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;
import cn.com.yusys.yusp.oca.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 系统角色表
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
@Service("adminSmRoleService")
public class AdminSmRoleServiceImpl extends ServiceImpl<AdminSmRoleDao, AdminSmRoleEntity> implements AdminSmRoleService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmRoleServiceImpl.class);

    private static final String R_ROLE_CODE = "r.role_code";
    private static final String R_ROLE_NAME = "r.role_name";
    private static final String O_ORG_SEQ = "o.org_seq";

    @Autowired
    AdminSmOrgService adminSmOrgService;

    @Autowired
    AdminSmUserRoleRelService userRoleRelService;


    @Autowired
    AdminSmUserMgrOrgService adminSmUserMgrOrgService;

    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;


    @Override
    public Page<AdminSmRoleVo> queryPage(AdminSmRoleQuery query) {

        Page<AdminSmRoleVo> page = query.getIPage();
        //没有对应mapper的Vo或带有@TableField(exist = false) 注解的属性不能作为lambda查询的条件，因为没有加载对应缓存，需要手动缓存
        QueryWrapper<AdminSmRoleVo> wrapper = new QueryWrapper<AdminSmRoleVo>()
                .like(StringUtils.nonEmpty(query.getRoleCode()), R_ROLE_CODE, query.getRoleCode())
                .like(StringUtils.nonEmpty(query.getRoleName()), R_ROLE_NAME, query.getRoleName())
                .eq(ObjectUtils.nonNull(query.getRoleSts()), "r.role_sts", query.getRoleSts());

        if (StringUtils.nonEmpty(query.getKeyWord())) {
            wrapper.and(r -> r
                    .like(StringUtils.nonEmpty(query.getKeyWord()), R_ROLE_CODE, query.getKeyWord())//关键字模糊匹配角色编码
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), R_ROLE_NAME, query.getKeyWord())//关键字模糊匹配角色名称
            );
        }
        wrapper.orderByDesc("r.last_chg_dt");


        AdminSmOrgEntity adminSmOrgEntity;
        //未指定机构时，获取登录用户对应机构及下级所有机构和登录人被授权的机构下的角色）
        if (StringUtils.isEmpty(query.getOrgId())) {
            //查询登录人机构的信息
            adminSmOrgEntity = adminSmOrgService.getById(SessionUtils.getUserOrganizationId());
            final String orgSeq=adminSmOrgEntity.getOrgSeq();
            //获取登录人被授权的机构
            List<String> orgIds =adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());

            if(CollectionUtils.nonEmpty(orgIds)){
                //登录人存在被授权的机构,获取登录用户对应机构及下级所有机构和登录人被授权的机构下的角色

                wrapper.and(r -> r
                        .likeRight(O_ORG_SEQ, orgSeq)//机构序列模糊右匹配
                        .or()
                        .in("r.org_id", orgIds)//用户已授权的机构
                );
            }else{
                //登录用户没有被授权机构，那就是获取登录用户对应的机构及下级所有机构的角色
                wrapper.likeRight(O_ORG_SEQ,orgSeq);
            }
        }else{
            adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
            wrapper.likeRight(O_ORG_SEQ,adminSmOrgEntity.getOrgSeq());
        }
        return getBaseMapper().selectOrgAccessible(page,wrapper);

    }

    @Override
    public AdminSmRoleDetailVo getDetailById(String roleId) {
        if (roleId == null) {
            throw BizException.error(null, "50800003", "角色代码为空");
        }
        return getBaseMapper().selectDetailById(roleId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmRoleEntity entity = new AdminSmRoleEntity();
                entity.setRoleId(id);
                entity.setRoleSts(AvailableStateEnum.ENABLED);
                getBaseMapper().updateById(entity);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);

            idList.forEach((id) -> {
                AdminSmRoleEntity entity = new AdminSmRoleEntity();
                entity.setRoleId(id);

                entity.setRoleSts(AvailableStateEnum.DISABLED);
                getBaseMapper().updateById(entity);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                if (checkBlocked(id)) {
                    throw BizException.error(null, "50800001", "该角色绑定有用户，请删除关联信息后操作");
                } else {
                    //删除角色的授权信息
                    adminSmAuthRecoService.deleteByAuthObjIds(ids);
                    getBaseMapper().deleteById(id);
                }
            });
        }
    }

    @Override
    public Page<AdminSmRoleVo> queryPageExcept(AdminSmPasteRoleQuery query) {

        //你想除去哪个角色
        if (query.getExpectedRoleId() == null || query.getExpectedRoleId().trim().length() == 0) {
            throw BizException.error(null, "50800003", "角色代码为空");
        }
        Page<AdminSmRoleVo> page = query.getIPage();

        QueryWrapper<AdminSmRoleVo> wrapper = new QueryWrapper<>();

        if (StringUtils.nonEmpty(query.getKeyWord())) {
            wrapper.and(r -> r
                    .like(StringUtils.nonEmpty(query.getKeyWord()), R_ROLE_CODE, query.getKeyWord())//关键字模糊匹配角色编码
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), R_ROLE_NAME, query.getKeyWord())//关键字模糊匹配角色名称
            );
        }

        wrapper.notIn("r.role_id",query.getExpectedRoleId());

        //查询登录人机构的信息
        AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(SessionUtils.getUserOrganizationId());
        final String orgSeq=adminSmOrgEntity.getOrgSeq();
        //获取登录人被授权的机构
        List<String> orgIds =adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());
        if(CollectionUtils.nonEmpty(orgIds)){
            //登录人存在被授权的机构,获取登录用户对应机构及下级所有机构和登录人被授权的机构下的角色
            wrapper.and(r -> r
                    .likeRight(O_ORG_SEQ, orgSeq)//机构序列模糊右匹配
                    .or()
                    .in("r.org_id", orgIds)//用户已授权的机构
            );
        }else{
            //登录用户没有被授权机构，那就是获取登录用户对应的机构及下级所有机构的角色
            wrapper.likeRight(O_ORG_SEQ,orgSeq);
        }
        wrapper.orderByDesc("r.last_chg_dt");
        return getBaseMapper().getAccessRoleList(page,wrapper);
    }

    @Override
    public Page<AdminSmRoleVo> getRolesForWf(AdminSmRoleQuery query) {
        Page<AdminSmRoleVo> page = query.getIPage();
        QueryWrapper<AdminSmRoleVo> wrapper = new QueryWrapper<>();
        wrapper.eq("T1.ROLE_STS", "A");
        creatWrapper(wrapper, "T1.ORG_ID", query.getOrgId());
        creatWrapper(wrapper, "T1.ROLE_CODE", query.getRoleCode());
        creatWrapper(wrapper, "T1.ROLE_NAME", query.getRoleName());
        return getBaseMapper().getRolesForWf(page, wrapper);
    }

    @Override
    public List<AdminSmRoleVo> getUserRoleByLoginCode(String loginCode) {
        return getBaseMapper().getUserRoleByLoginCode(loginCode);
    }

    private void creatWrapper(QueryWrapper<AdminSmRoleVo> wrapper, String column, String value) {
        if (StringUtils.nonEmpty(value)) {
            boolean condition = value.startsWith("%") || value.endsWith("%");
            wrapper.like(condition, column, value);
            wrapper.eq(!condition, column, value);
        }
    }

    /**
     * 检查角色是否已关联用户信息
     *
     * @param roleId 角色id
     * @return true已关联用户信息，false未关联用户信息
     */
    private boolean checkBlocked(String roleId) {
        LambdaQueryWrapper<AdminSmUserRoleRelEntity> relWrapper = new QueryWrapper<AdminSmUserRoleRelEntity>().lambda();
        relWrapper.eq(AdminSmUserRoleRelEntity::getRoleId, roleId);
        //是否有关联用户
        long countUser = this.userRoleRelService.count(relWrapper);
        return countUser > 0;
    }

    @Override
    public boolean save(AdminSmRoleEntity entity) {
        LambdaQueryWrapper<AdminSmRoleEntity> codeWrapper = new QueryWrapper<AdminSmRoleEntity>().lambda();
        codeWrapper.eq(AdminSmRoleEntity::getRoleCode, entity.getRoleCode());
        codeWrapper.eq(StringUtils.nonEmpty(entity.getDataTenantId()), AdminSmRoleEntity::getDataTenantId, entity.getDataTenantId());
        AdminSmRoleEntity check = getBaseMapper().selectOne(codeWrapper);
        if (Objects.nonNull(check)) {
            throw BizException.error("exist", "50800002", entity.getRoleCode());
        }
        AdminSmOrgEntity org = adminSmOrgService.getById(entity.getOrgId());
        if (null == entity.getRoleLevel() && null != org) {
            entity.setRoleLevel(org.getOrgLevel());
        }
        //新增的数据默认是待生效的
        entity.setRoleSts(Optional.ofNullable(entity.getRoleSts()).orElse(AvailableStateEnum.UNENABLED));
        log.info("New role data: [new role: {}] ", entity.getRoleName());
        return getBaseMapper().insert(entity) > 0;
    }


    @Override
    public boolean updateById(AdminSmRoleEntity entity) {
        entity.setRoleSts(Optional.ofNullable(entity.getRoleSts()).orElse(AvailableStateEnum.UNENABLED));
        return super.updateById(entity);
    }
}