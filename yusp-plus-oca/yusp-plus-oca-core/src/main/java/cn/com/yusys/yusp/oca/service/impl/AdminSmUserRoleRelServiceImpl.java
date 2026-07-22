package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.RamPager;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.module.constant.Constants;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmUserRoleRelDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleUserRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.UserRelationshipVo;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmRoleService;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户角色关联表
 *
 * @author terry
 * @date 2020-12-11 18:06:35
 */

@Service("adminSmUserRoleRelService")
public class AdminSmUserRoleRelServiceImpl extends ServiceImpl<AdminSmUserRoleRelDao, AdminSmUserRoleRelEntity> implements AdminSmUserRoleRelService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmUserRoleRelServiceImpl.class);

    private static final String EXIST = "exist";

    @Autowired
    private AdminSmOrgService adminSmOrgService;
    @Autowired
    private AdminSmRoleService adminSmRoleService;



    @Override
    public List<AdminSmOrgTreeNodeBo> getOrgTree(String roleId) {
        if (roleId == null || roleId.trim().isEmpty()) {
            throw BizException.error(EXIST, "51000003", "roleId can not be null or empty!!");
        }
        AdminSmRoleEntity role = adminSmRoleService.getById(roleId);
        return adminSmOrgService.getOrgTreeByOrgId(role.getOrgId());
    }



    @Override
    public Page<UserRelationshipVo> memberPage(AdminSmRoleUserRelQuery query) {
        String queryRoleId=query.getRoleId();
        if (queryRoleId == null || queryRoleId.trim().length() == 0) {
            throw BizException.error(EXIST, "51000003", "roleId can not be null or empty!!");
        }

        Page<UserRelationshipVo> page=query.getIPage();
        QueryWrapper<UserRelationshipVo> userWrapper = new QueryWrapper<>();
        userWrapper.like(StringUtils.nonEmpty(query.getUserCode()), "u.user_code", query.getUserCode());
        userWrapper.like(StringUtils.nonEmpty(query.getUserName()), "u.user_name", query.getUserName());
        userWrapper.like(StringUtils.nonEmpty(query.getLoginCode()), "u.login_code", query.getLoginCode());
        userWrapper.eq("u.user_sts","A");

        userWrapper.orderByDesc("u.last_chg_dt");

        boolean checked=query.getChecked();
        List<UserRelationshipVo> userRelationshipVoList;
        //true查看已关联的用户；false是查看未关联的用户
        if(checked){
            //角色关联的用户
            userWrapper.eq("r.role_id",queryRoleId);
            //指定了机构就查询指定机构及下架机构下角色关联的用户，未指定机构就查询角色关联的所有用户（角色关联的用户一定是在角色的有权限机构下）
            if(!StringUtils.isEmpty(query.getOrgId())){
                //获取机构信息
                AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
                userWrapper.likeRight("o.org_seq",adminSmOrgEntity.getOrgSeq());
            }
            //已关联的用户直接通过用户表和机构表关联角色用户关联表就可以
            Page<UserRelationshipVo> userRelationshipVoPage = getBaseMapper().selectRoleRelUserList(page, userWrapper);
            if(!CollectionUtils.isEmpty(userRelationshipVoPage.getRecords())){
                userRelationshipVoPage.getRecords().forEach((userRelationshipVo -> {
                    userRelationshipVo.setChecked(true);
                }));
            }
            return  userRelationshipVoPage;
        }else{
            //未指定机构，就是查询角色对应机构下及下级机构的所有用户
            if(StringUtils.isEmpty(query.getOrgId())){
                AdminSmRoleEntity role = adminSmRoleService.getById(query.getRoleId());
                query.setOrgId(role.getOrgId());
            }
            //获取机构信息
            AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
            //查询的用户在机构及其所有下级机构
            userWrapper.likeRight("o.org_seq",adminSmOrgEntity.getOrgSeq());
            userRelationshipVoList=getBaseMapper().allRoleUnRelUserList(userWrapper,query.getRoleId());
            RamPager<UserRelationshipVo> pager = new RamPager<>(userRelationshipVoList, query.getSize());
            page.setRecords(pager.page(query.getPage()));
            page.setTotal(userRelationshipVoList.size());
            return page;
        }
    }


    @Override
    public List<String> findUserRoleRelsByUser(AdminSmUserEntity user) {
        if (user.getUserId() == null) {
            throw BizException.error(null, "51100004", "用户编号不能为空");
        }
        return getBaseMapper().findUserRoleRelsByUser(user.getUserId());
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean  save(List<AdminSmUserRoleRelEntity> entityList) {
        Integer count = 0;
        for (AdminSmUserRoleRelEntity entity : entityList) {
            LambdaQueryWrapper<AdminSmUserRoleRelEntity> checkWrapper = new QueryWrapper<AdminSmUserRoleRelEntity>().lambda();
            checkWrapper.eq(AdminSmUserRoleRelEntity::getRoleId, entity.getRoleId());
            checkWrapper.eq(AdminSmUserRoleRelEntity::getUserId, entity.getUserId());
            AdminSmUserRoleRelEntity check = getBaseMapper().selectOne(checkWrapper);
            if (Objects.nonNull(check)) {
                throw BizException.error(EXIST, "51000001", "roleId:" + entity.getRoleId() + ",userId:" + entity.getUserId());
            }
            log.info("New userRoleRel data: [userId: {},roleId={}] ", entity.getUserId(), entity.getRoleId());
            count += getBaseMapper().insert(entity);
        }
        return count.equals(entityList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(List<AdminSmUserRoleRelEntity> entityList) {
        List<String> ids = new ArrayList<>();
        for (AdminSmUserRoleRelEntity entity : entityList) {
            LambdaQueryWrapper<AdminSmUserRoleRelEntity> checkWrapper = new QueryWrapper<AdminSmUserRoleRelEntity>().lambda();
            checkWrapper.eq(AdminSmUserRoleRelEntity::getRoleId, entity.getRoleId());
            checkWrapper.eq(AdminSmUserRoleRelEntity::getUserId, entity.getUserId());
            AdminSmUserRoleRelEntity check = getBaseMapper().selectOne(checkWrapper);
            if (Objects.isNull(check)) {
                throw BizException.error("notExist", "51000002", "roleId:" + entity.getRoleId() + ",userId:" + entity.getUserId());
            }
            log.info("Delete userRoleRel data: [userId: {},roleId={}] ", entity.getUserId(), entity.getRoleId());
            ids.add(check.getUserRoleRelId());
        }
        Integer count = getBaseMapper().deleteByIds(ids);
        return count.equals(entityList.size());
    }

    @Override
    public JClientRspEntity<String> switchRole(String roleId) {
        String userId= SessionUtils.getUserId();
        QueryWrapper<AdminSmUserRoleRelEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("checked",1);
        List<AdminSmUserRoleRelEntity> adminSmUserRoleRelEntities = getBaseMapper().selectList(queryWrapper);
        if(adminSmUserRoleRelEntities.size() > 1){
            return JClientRspEntity.buildFail(Constants.DEFAULT_ERROR_CODE, "用户选中的角色有多个！");
        }else if(adminSmUserRoleRelEntities.size() == 1){
            AdminSmUserRoleRelEntity adminSmUserRoleRelEntity=adminSmUserRoleRelEntities.get(0);
            adminSmUserRoleRelEntity.setChecked(0);
            getBaseMapper().updateById(adminSmUserRoleRelEntity);
        }
        getBaseMapper().updateCheckedRole(userId,roleId);

        return JClientRspEntity.buildSuccess("用户切换角色成功");


    }


}