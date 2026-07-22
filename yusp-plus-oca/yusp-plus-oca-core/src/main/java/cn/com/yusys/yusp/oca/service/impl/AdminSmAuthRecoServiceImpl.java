package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.GenericBuilder;
import cn.com.yusys.yusp.commons.session.user.UserIdentity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmAuthRecoDao;
import cn.com.yusys.yusp.oca.domain.bo.MenuAndControlAuthRecoSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminTmplAuthForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminDataTmplControlVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminRecoWithTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthRecoVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmUserRoleRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源对象授权记录表(含菜单、控制点、数据权限) service 层实现类
 * @author zhangyt12
 * @date 2021/6/28 14:34
 */
@Service("adminSmAuthRecoService")
public class AdminSmAuthRecoServiceImpl extends ServiceImpl<AdminSmAuthRecoDao, AdminSmAuthRecoEntity> implements AdminSmAuthRecoService {

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;

    private static final String AUTHRES_TYPE = "authres_type";

    private static final String AUTHOBJ_ID = "authobj_id";

    private static final String SYS_ID = "sys_id";

    private static final String AUTHRES_ID = "authres_id";

    private static final String MENU_ID = "menu_id";

    @Override
    public List<MenuVo> selectDataPowerTree(String sysId) {

        List<MenuVo> menuTreeList = getBaseMapper().findMenuTreeList(sysId);
        List<MenuVo> contrTreeList = getBaseMapper().findContrTreeList(sysId);
        List<MenuVo> dataAuthTreeList = getBaseMapper().findDataAuthTreeList(sysId);

        menuTreeList.addAll(contrTreeList);
        menuTreeList.addAll(dataAuthTreeList);
        return menuTreeList;
    }

    @Override
    public List<AdminSmAuthRecoVo> queryRecoInfo(String objectType, String resType, String objectId, String sysId) {

        String[] resTypeArray = resType.replaceAll("^,*|,*$", "").split(",");
        List<AdminSmAuthRecoEntity> authRecoList = getBaseMapper().selectList(new QueryWrapper<AdminSmAuthRecoEntity>()
                .eq(!StringUtils.isEmpty(objectType), "authobj_type", objectType)
                .in(!StringUtils.isEmpty(resType), AUTHRES_TYPE, resTypeArray)
                .eq(!StringUtils.isEmpty(objectId), AUTHOBJ_ID, objectId)
                .eq(!StringUtils.isEmpty(sysId), SYS_ID, sysId)
        );

        return authRecoList.stream().map(adminSmAuthRecoEntity -> GenericBuilder.of(AdminSmAuthRecoVo::new)
                .with(AdminSmAuthRecoVo::setAuthRecoId, adminSmAuthRecoEntity.getAuthRecoId())
                .with(AdminSmAuthRecoVo::setSysId, adminSmAuthRecoEntity.getSysId())
                .with(AdminSmAuthRecoVo::setAuthobjType, adminSmAuthRecoEntity.getAuthobjType())
                .with(AdminSmAuthRecoVo::setAuthobjId, adminSmAuthRecoEntity.getAuthobjId())
                .with(AdminSmAuthRecoVo::setAuthresType, adminSmAuthRecoEntity.getAuthresType())
                .with(AdminSmAuthRecoVo::setAuthresId, adminSmAuthRecoEntity.getAuthresId())
                .with(AdminSmAuthRecoVo::setLastChgUsr, adminSmAuthRecoEntity.getLastChgUsr())
                .with(AdminSmAuthRecoVo::setLastChgDt, adminSmAuthRecoEntity.getLastChgDt())
                .with(AdminSmAuthRecoVo::setMenuId, adminSmAuthRecoEntity.getMenuId())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int adminSmAuthRecoService(List<AdminSmAuthRecoEntity> authRecoList) {

        //delete 之前设置数据关系
        AdminSmAuthRecoEntity adminSmAuthReco = authRecoList.get(0);
        int delete = getBaseMapper().delete(new QueryWrapper<AdminSmAuthRecoEntity>()
                .eq(!StringUtils.isEmpty(adminSmAuthReco.getAuthobjType()), "authobj_type", adminSmAuthReco.getAuthobjType())
                .eq(AUTHRES_TYPE, "D")
                .eq(!StringUtils.isEmpty(adminSmAuthReco.getAuthobjId()), AUTHOBJ_ID, adminSmAuthReco.getAuthobjId())
                .eq(!StringUtils.isEmpty(adminSmAuthReco.getSysId()), SYS_ID, adminSmAuthReco.getSysId())
        );
        boolean saveBatch = this.saveBatch(authRecoList);
        if (delete < 1 || !saveBatch) {
            return 0;
        }
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMenuAndControlAuthReco(MenuAndControlAuthRecoSaveBo authRecoSaveBo) {

        // 1. 删除原授权记录
        List<AdminSmAuthRecoVo> menuData = authRecoSaveBo.getMenuData();
        if (!CollectionUtils.isEmpty(menuData)) {
            AdminSmAuthRecoVo adminSmAuthRecoVo = menuData.get(0);
            AdminSmAuthRecoEntity adminSmAuthRecoEntity = new AdminSmAuthRecoEntity();
            BeanUtils.beanCopy(adminSmAuthRecoVo, adminSmAuthRecoEntity);
            QueryWrapper<AdminSmAuthRecoEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(!StringUtils.isEmpty(adminSmAuthRecoVo.getAuthobjId()), AUTHOBJ_ID, adminSmAuthRecoVo.getAuthobjId());
            queryWrapper.in(AUTHRES_TYPE, Arrays.asList("M", "C"));
            queryWrapper.eq(!StringUtils.isEmpty(adminSmAuthRecoVo.getSysId()), SYS_ID, adminSmAuthRecoVo.getSysId());

            this.remove(queryWrapper);
        }

        // 2. 新增授权记录表
        List<AdminSmAuthRecoEntity> collect = menuData.stream().map(item -> {
            AdminSmAuthRecoEntity adminSmAuthRecoEntity = new AdminSmAuthRecoEntity();
            BeanUtils.beanCopy(item, adminSmAuthRecoEntity);
            return adminSmAuthRecoEntity;
        }).collect(Collectors.toList());

        this.saveBatch(collect);
    }

    @Override
    public void deleteByList(List<String> authresIdList) {
        getBaseMapper().deleteBatchIds(authresIdList);
    }

    @Override
    public void deleteByAuthObjIds(String[] ids) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.in(AUTHOBJ_ID, ids);
        getBaseMapper().delete(wrapper);
    }

    @Override
    public void deleteByResContrList(List<String> contrIdList, List<String> dataAuthIdList) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        // 删除控制点授权及控制点关联的数据模板授权
        if (contrIdList != null && contrIdList.size() > 0) {
            List<String> list = new ArrayList<>();
            list.addAll(contrIdList);
            list.addAll(dataAuthIdList);
            wrapper.in(AUTHRES_ID, contrIdList);
            getBaseMapper().delete(wrapper);
        }
    }

    @Override
    public List<AdminSmAuthRecoEntity> getByAuthObjId(String authObjId) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(AUTHOBJ_ID, authObjId);
        return this.list(wrapper);
    }

    @Override
    public AdminSmAuthRecoEntity getByAuthresId(String authresId) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(AUTHRES_ID, authresId);
        return this.getOne(wrapper, false);
    }

    @Override
    public List<AdminSmAuthRecoEntity> getByAuthresType(String d) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(AUTHRES_TYPE, d);
        return this.list(wrapper);
    }

    @Override
    public void removeByMenuIdAndAuthobjId(AdminTmplAuthForm form) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(MENU_ID, form.getContrId());
        wrapper.eq(AUTHOBJ_ID, form.getAuthobjId());
        wrapper.eq(AUTHRES_ID, form.getLastAuthresId());
        this.remove(wrapper);
    }

    @Override
    public List<AdminSmAuthRecoEntity> getByAuthresIds(List<String> tmplIdList) {
        QueryWrapper<AdminSmAuthRecoEntity> recoWrapper = new QueryWrapper<>();
        recoWrapper.in(tmplIdList != null && tmplIdList.size() > 0, AUTHRES_ID, tmplIdList);
        return list(recoWrapper);
    }


    @Override
    public List<AdminRecoWithTmplVo> getByMenuIdWithTmpl(String contrId, String dataTenantId) {
        QueryWrapper<AdminRecoWithTmplVo> wrapper = new QueryWrapper<>();
        wrapper.eq("r.menu_id", contrId);
        wrapper.eq(StringUtils.nonEmpty(dataTenantId),"r.data_tenant_id",dataTenantId);
        return getBaseMapper().getByMenuIdWithTmpl(wrapper);
    }

    @Override
    public List<AdminDataTmplControlVo> getDataTmplControl(String userId) {
//        查询出该用户所选的角色id
        List<? extends UserIdentity> roles = SessionUtils.getUserInformation().getRoles();
        QueryWrapper<AdminDataTmplControlVo> wrapper = new QueryWrapper<>();
        wrapper.eq(AUTHRES_TYPE, "D");
//        用户的数据模板权限分给用户和角色
        if(roles != null && roles.size()>0){
            String roleId= roles.get(0).getId();
            wrapper.and(wr ->wr.eq(AUTHOBJ_ID, roleId).or().eq(AUTHOBJ_ID, userId));
        }else{
            wrapper.eq(AUTHOBJ_ID, userId);
        }
        return getBaseMapper().getDataTmplControl(wrapper);
    }

    @Override
    public int deleteMenuAuthData(HashSet<String> deleteIds) {
        // 1、查询菜单相关已授权控制点的id集合
        QueryWrapper<String> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(MENU_ID, deleteIds);
        List<String> resContrIdList = getBaseMapper().selectResContrIdByMenuIds(queryWrapper);
        // 2、将查询出的控制点id集合添加到deleteIds集合当中
        deleteIds.addAll(resContrIdList);
        // 3、组合删除条件，authres_id字段会删除deleteIds直接相关的授权数据，menu_id会删除上级id为deleteIds对象的授权数据
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.in(AUTHRES_ID, deleteIds);
        wrapper.or().in(MENU_ID, deleteIds);
        return getBaseMapper().delete(wrapper);
    }

    @Override
    public void deleteByAuthresAndTenant(List<String> authresIds, String tanentId) {
        QueryWrapper<AdminSmAuthRecoEntity> wrapper = new QueryWrapper<>();
        wrapper.in(AUTHRES_ID, authresIds);
        wrapper.eq("data_tenant_id",tanentId);
        getBaseMapper().delete(wrapper);
    }

    private List<AdminDataTmplControlVo> getDataTmplControl(List<String> roleIds) {
        //修改orgIds为null时的bug   chenjing  2021/9/29 14:41
        if(CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>();
        }
        QueryWrapper<AdminDataTmplControlVo> wrapper = new QueryWrapper<>();
        wrapper.in(AUTHOBJ_ID, roleIds).eq(AUTHRES_TYPE, "D");
        return getBaseMapper().getDataTmplControl(wrapper);
    }
}