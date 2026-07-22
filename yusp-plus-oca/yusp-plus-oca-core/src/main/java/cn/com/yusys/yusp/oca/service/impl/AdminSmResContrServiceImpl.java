package cn.com.yusys.yusp.oca.service.impl;


import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;
import cn.com.yusys.yusp.common.utils.TableConstant;
import cn.com.yusys.yusp.commons.session.user.Control;
import cn.com.yusys.yusp.commons.session.user.UserIdentity;
import cn.com.yusys.yusp.commons.session.user.impl.ControlImpl;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmResContrDao;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.domain.form.AdminSmMenuConditionForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmResContrForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmResContrSaveForm;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import cn.cscb.uadp.tc.cachedependency.constant.CacheLevelEnum;
import cn.cscb.uadp.tc.cachedependency.service.CacheDependencyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 数据权限模板表与控制点的展示
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@Service("adminSmResContrService")
public class AdminSmResContrServiceImpl extends ServiceImpl<AdminSmResContrDao, AdminSmResContrEntity> implements AdminSmResContrService {

    private static final String C_CONTR_NAME = "c.CONTR_NAME";

    private static final String C_CONTR_CODE = "c.CONTR_CODE";

    private static final String C_CONTR_URL = "c.CONTR_URL";

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AdminSmFuncModService adminSmFuncModService;
    @Autowired
    AdminSmBusiFuncService adminSmBusiFuncService;
    @Autowired
    AdminSmDataAuthService adminSmDataAuthService;
    @Autowired
    AdminSmAuthRecoService adminSmAuthRecoService;
    @Autowired
    AdminSmResContrService adminSmResContrService;
    @Autowired
    AdminSmDataAuthTmplService adminSmDataAuthTmplService;
    @Autowired
    AdminSmMenuService adminSmMenuService;
    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;
    @Autowired
    private CacheDependencyService cacheDependencyService;


    @Override
    public List<ControlImpl> getAdminSmContr(String userId) {
        List<? extends UserIdentity> roles = SessionUtils.getUserInformation().getRoles();
        QueryWrapper<ControlImpl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.nonEmpty(userId), "t_user.USER_ID", userId);

        if (roles != null && roles.size() > 0) {
            String roleId = roles.get(0).getId();
            queryWrapper.eq(StringUtils.nonEmpty(roleId), "t_roelrel.role_id", roleId);
        }
        return getBaseMapper().getAdminSmContrList(queryWrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminSmResContrEntity> page = this.page(
                new Query<AdminSmResContrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    @Override
    public Page<AdminSmResContrVo> queryPageWithCondition(AdminSmResContrForm form) {
        Page<AdminSmResContrVo> pageVo = new Page<>(form.getPage(), form.getSize());
        if (!StringUtils.isEmpty(form.getMenuId()) && StringUtils.isEmpty(form.getFuncId())) {
            return pageVo;
        }
        QueryWrapper<AdminSmResContrVo> wrapper = new QueryWrapper<>();
        String keyWord = form.getKeyWord();
        String funcId = form.getFuncId();
        if (StringUtils.isEmpty(keyWord)) {
            String contrName = form.getContrName();
            String contrCode = form.getContrCode();
            String contrUrl = form.getContrUrl();
            String encodeCheck = form.getEncodeCheck();
            String nonceCheck = form.getNonceCheck();
            wrapper.eq(!StringUtils.isEmpty(funcId), "c.func_id", funcId);
            wrapper.like(!StringUtils.isEmpty(contrName), C_CONTR_NAME, contrName);
            wrapper.like(!StringUtils.isEmpty(contrCode), C_CONTR_CODE, contrCode);
            wrapper.like(!StringUtils.isEmpty(contrUrl), C_CONTR_URL, contrUrl);
            wrapper.eq(!StringUtils.isEmpty(form.getMethodType()), "c.method_type", form.getMethodType());
            wrapper.eq(!StringUtils.isEmpty(encodeCheck), "c.encode_check", funcId);
            wrapper.eq(!StringUtils.isEmpty(nonceCheck), "c.nonce_check", funcId);
        } else {
            wrapper.eq(!StringUtils.isEmpty(funcId), "c.func_id", funcId);
            wrapper.like(C_CONTR_NAME, keyWord).or();
            wrapper.like(C_CONTR_CODE, keyWord).or();
            wrapper.like(C_CONTR_URL, keyWord).or();
            wrapper.like("m.menu_name", keyWord);
        }
//        //登录用户可操作租户系统id为1的菜单和自身的菜单
        wrapper.orderByAsc("c.contr_id");
        getBaseMapper().queryPageWithCondition(pageVo, wrapper);
        return pageVo;
    }


    @Override
    public List<AdminSmContrTreeVo> getFuncTree() {

        List<AdminSmBusiFuncEntity> funcEntityList = adminSmBusiFuncService.list();
        List<AdminSmFuncModEntity> modEntityList = adminSmFuncModService.list();
        List<AdminSmContrTreeVo> treeList = new ArrayList<>();

        funcEntityList.forEach(func -> {
            func.setNodeId(func.getFuncId());
            func.setNodeName(func.getFuncName());
            func.setUpTreeId(func.getModId());
        });
        Map<String, List<AdminSmContrTreeVo>> funcGroupByMod = funcEntityList.stream().collect(Collectors.groupingBy(AdminSmBusiFuncEntity::getModId, Collectors.toList()));

        modEntityList.forEach(mod -> {
            mod.setNodeId(mod.getModId());
            mod.setNodeName(mod.getModName());
            mod.setUpTreeId("0");
            mod.setChildren(funcGroupByMod.get(mod.getModId()));
            treeList.add(mod);
        });

        return treeList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContr(String[] ids) {
        if (ids != null && ids.length != 0) {
            // 获取将要删除控制点下已经关联的数据权限模板 IdList
            List<AdminSmDataAuthEntity> dataAuthEntityList = adminSmDataAuthService.getListByContrIds(Arrays.asList(ids));
            List<String> dataAuthIdList = dataAuthEntityList.stream().map(AdminSmDataAuthEntity::getAuthTmplId).collect(Collectors.toList());
            List<String> contrIdList = Arrays.asList(ids);

            // 删除admin_sm_data_auth关联表记录
            adminSmDataAuthService.deleteByContrIds(ids);

            // 删除控制点
            getBaseMapper().deleteBatchIds(contrIdList);

            // 删除控制点授权，同时删除控制点关联的数据权限模板授权
            adminSmAuthRecoService.deleteByResContrList(contrIdList, dataAuthIdList);
        }
    }


    @Override
    public long checkCode(AdminSmResContrForm adminSmResContrForm) {
        String contrId = adminSmResContrForm.getContrId();
        String contrCode = adminSmResContrForm.getContrCode();
        String funcId = adminSmResContrForm.getFuncId();
        if (!StringUtils.isEmpty(contrId)) {
            AdminSmResContrEntity entity = this.getById(contrId);
            if (entity != null) {
                QueryWrapper<AdminSmResContrEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("CONTR_CODE", contrCode);
                wrapper.eq("FUNC_ID", funcId);
                wrapper.ne("CONTR_ID", contrId);
                return getBaseMapper().selectCount(wrapper);
            }
        }
        if (StringUtils.isEmpty(funcId) || StringUtils.isEmpty(contrCode)) {
            return -1;
        }
        QueryWrapper<AdminSmResContrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("CONTR_CODE", contrCode);
        wrapper.eq("FUNC_ID", funcId);
        return getBaseMapper().selectCount(wrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createContr(AdminSmResContrSaveForm adminSmResContrSaveForm) {
        if (StringUtils.isEmpty(adminSmResContrSaveForm.getContrId())) {
            adminSmResContrSaveForm.setContrId(StringUtils.getUUID());
        }
        AdminSmResContrEntity adminSmResContrEntity = BeanUtils.beanCopy(adminSmResContrSaveForm, AdminSmResContrEntity.class);

        // 控制点关联数据模板
        relationTmpl(adminSmResContrSaveForm, false);

        // 所有新增控制点，自动给id为 40 的系统管理员分配；
        this.save(adminSmResContrEntity);
        AdminSmAuthRecoEntity adminSmAuthRecoEntity = getAuthEntityWithContr(adminSmResContrSaveForm);
        adminSmAuthRecoService.save(adminSmAuthRecoEntity);
    }


    @Override
    public Page<AdminSmResContrVo> getAuthedContrPage(AdminSmResContrForm form) {

        Page<AdminSmResContrVo> pageVo = new Page<>(form.getPage(), form.getSize());
        QueryWrapper<AdminSmResContrVo> wrapper = new QueryWrapper<>();
        String keyWord = form.getKeyWord();
        String menuId = form.getMenuId();
        wrapper.eq("a.authobj_id", form.getAuthObjId());
        wrapper.eq(StringUtils.nonEmpty(form.getDataTenantId()), "a.data_tenant_id", form.getDataTenantId());
        wrapper.eq(!StringUtils.isEmpty(menuId), "m.menu_id", menuId);
        if (StringUtils.nonEmpty(keyWord)) {
            wrapper.like(C_CONTR_NAME, keyWord).or();
            wrapper.like(C_CONTR_CODE, keyWord).or();
            wrapper.like(C_CONTR_URL, keyWord).or();
            wrapper.like("m.menu_name", keyWord);
        }
        getBaseMapper().queryAuthContrPage(pageVo, wrapper);
        return pageVo;
    }

    @Override
    @Cacheable(value = "RequestCheckList@1H", key = "#type")
    public Set<String> getRequestCheckList(String type) {
        cacheDependencyService.recordDependencies("RequestCheckList:" + type, CacheLevelEnum.HOURS.getLevel(), TableConstant.ADMIN_SM_RES_CONTR);
        List<AdminSmResContrVo> vos = getBaseMapper().queryRequestCheckList();
        if (Constant.REQUEST_ENCODE.equals(type)) {
            return vos.stream().filter(vo -> Constant.YES.equals(vo.getEncodeCheck())).map(AdminSmResContrVo::getContrUrl).collect(Collectors.toSet());
        } else {
            return vos.stream().filter(vo -> Constant.YES.equals(vo.getNonceCheck())).map(AdminSmResContrVo::getContrUrl).collect(Collectors.toSet());
        }
    }

    private AdminSmAuthRecoEntity getAuthEntityWithContr(AdminSmResContrSaveForm form) {
        String sysId = OcaCommonInfoUtils.getUserSysId();
        AdminSmAuthRecoEntity entity = new AdminSmAuthRecoEntity();
        entity.setAuthRecoId(StringUtils.getUUID());
        entity.setSysId(sysId);
        entity.setLastChgUsr(SessionUtils.getUserId());
        entity.setAuthresId(form.getContrId());
        entity.setAuthresType("C");
        // 给超级管理员用户分配权限
        entity.setAuthobjType("U");
        entity.setAuthobjId(adminSmTenantUserRelService.getTenantUserId());
        entity.setMenuId(form.getMenuId());
        return entity;
    }

    /**
     * 取功能控制点表新建/修改表单数据封装成成系统功能控制点实体类
     *
     * @param form 系统功能控制点表新建/修改表单数据
     * @return 系统功能控制点表
     */
    private AdminSmResContrEntity getAdminSmResContrEntity(AdminSmResContrSaveForm form) {
        AdminSmResContrEntity entity = new AdminSmResContrEntity();
        if (StringUtils.isEmpty(form.getContrId())) {
            form.setContrId(StringUtils.getUUID());
        }
        entity.setContrId(form.getContrId());
        entity.setLastChgUsr(SessionUtils.getUserId());
        entity.setContrCode(form.getContrCode());
        entity.setContrName(form.getContrName());
        entity.setContrRemark(form.getContrRemark());
        entity.setContrUrl(form.getContrUrl());
        entity.setFuncId(form.getFuncId());
        entity.setMethodType(form.getMethodType());
        entity.setEncodeCheck(form.getEncodeCheck());
        entity.setNonceCheck(form.getNonceCheck());
        return entity;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContr(AdminSmResContrSaveForm adminSmResContrSaveForm) {
        AdminSmResContrEntity adminSmResContrEntity = getAdminSmResContrEntity(adminSmResContrSaveForm);

        this.updateById(adminSmResContrEntity);
        // 关联控制点和数据模板 删除原有授权记录 给系统管理员 40 授权新数据模板
        relationTmpl(adminSmResContrSaveForm, true);
    }


    @Override
    public List<AdminSmAuthTreeVo> selectAuthTree(String id, String dataTenantId) {
        return getBaseMapper().selectAuthTree(id, dataTenantId);
    }


    @Override
    public IPage<AdminSmAuthTreeVo> selectAuthTreePage(String id, String keyWord, String dataTenantId, int page, int size) {
        IPage<AdminSmAuthTreeVo> result = new Page<>(page, size);
        QueryWrapper<AdminSmAuthTreeVo> wrapper = new QueryWrapper<>();
        wrapper.eq("r.authobj_id", id);
        wrapper.like(!StringUtils.isEmpty(keyWord), C_CONTR_NAME, keyWord);
        wrapper.eq(StringUtils.nonEmpty(dataTenantId), "r.data_tenant_id", dataTenantId);
        result = getBaseMapper().selectAuthTreePage(result, wrapper);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String relationTmpl(AdminSmResContrSaveForm adminSmResContrSaveForm, boolean createOrUpdate) {
        List<String> authDataTmplIdList = adminSmResContrSaveForm.getAuthDataTmplIdList();
        if (createOrUpdate) {
            // 0、判断修改的控制点模板是否能修改删除
            String cannotBeUpdateDeleteResult = checkAuthRecoCanBeUpdateDelete(adminSmResContrSaveForm, authDataTmplIdList);
            if (cannotBeUpdateDeleteResult != null) {
                return cannotBeUpdateDeleteResult;
            }
        }

        // 1、如果是修改已关联的模板，首先修改之前关联模板数据的status状态码

        // 2、删除之前关联模板的数据
        String[] ids = {adminSmResContrSaveForm.getContrId()};
        adminSmDataAuthService.deleteByContrIds(ids);

        // 3、添加新的关联模板数据
        addNewDataAuth(adminSmResContrSaveForm, authDataTmplIdList);
        return "修改关联成功！";
    }

    /**
     * 添加新的关联模板数据
     * @param adminSmResContrSaveForm
     * @param authDataTmplIdList
     */
    private void addNewDataAuth(AdminSmResContrSaveForm adminSmResContrSaveForm, List<String> authDataTmplIdList) {
        if (authDataTmplIdList != null && !authDataTmplIdList.isEmpty()) {
            Set<AdminSmDataAuthEntity> authEntityList = new HashSet<>();
            for (String tmplId : authDataTmplIdList) {
                AdminSmDataAuthEntity entity = new AdminSmDataAuthEntity();
                entity.setAuthId(StringUtils.getUUID());
                entity.setAuthTmplId(tmplId);
                entity.setContrId(adminSmResContrSaveForm.getContrId());
                entity.setLastChgUsr(SessionUtils.getUserId());
                if (StringUtils.nonEmpty(adminSmResContrSaveForm.getDataTenantId())) {
                    entity.setDataTenantId(adminSmResContrSaveForm.getDataTenantId());
                }
                authEntityList.add(entity);
            }
            adminSmDataAuthService.saveBatch(authEntityList);
        }
    }

    /**
     * 判断修改的控制点模板是否能修改删除
     * @param adminSmResContrSaveForm
     * @param authDataTmplIdList
     * @return
     */
    private String checkAuthRecoCanBeUpdateDelete(AdminSmResContrSaveForm adminSmResContrSaveForm, List<String> authDataTmplIdList) {
        List<AdminRecoWithTmplVo> recoTmplVoList = adminSmAuthRecoService.getByMenuIdWithTmpl(adminSmResContrSaveForm.getContrId(), adminSmResContrSaveForm.getDataTenantId());
        StringBuilder stringBuilder = new StringBuilder();
        if (recoTmplVoList != null && !recoTmplVoList.isEmpty()) {
            // flag：是否有不能修改数据模板
            boolean flag = buildResultAndReturnFlag(authDataTmplIdList, stringBuilder, recoTmplVoList);
            if (flag) {
                stringBuilder.append("如需修改关联的数据模板，请先删除相关的授权记录！");
                return stringBuilder.toString();
            }
        }
        return null;
    }

    private static boolean buildResultAndReturnFlag(List<String> authDataTmplIdList, StringBuilder stringBuffer, List<AdminRecoWithTmplVo> recoTmplVoList) {
        boolean flag = false;
        stringBuffer.append("这些数据模板已授权，不能取消关联！");
        if (authDataTmplIdList == null || authDataTmplIdList.isEmpty()) {
            flag = true;
            for (AdminRecoWithTmplVo vo : recoTmplVoList) {
                stringBuffer.append("已被授权的数据模板名称：").append(vo.getAuthTmplName());
            }

        } else {
            for (AdminRecoWithTmplVo vo : recoTmplVoList) {
                if (!authDataTmplIdList.contains(vo.getAuthTmplId())) {
                    flag = true;
                    stringBuffer.append("已被授权的数据模板名称：").append(vo.getAuthTmplName());
                }
            }
        }
        return flag;
    }


    @Override
    public Page<AdminSmMenuVo> getMenuList(AdminSmMenuConditionForm adminSmMenuConditionForm) {
        return adminSmMenuService.getMenuListForContr(adminSmMenuConditionForm);
    }


    @Override
    public List<? extends Control> selectControlImplList() {
        return this.list().stream().map(c -> {
            ControlImpl control = new ControlImpl();
            BeanUtils.beanCopy(c, control);
            return control;
        }).collect(Collectors.toList());
    }


}