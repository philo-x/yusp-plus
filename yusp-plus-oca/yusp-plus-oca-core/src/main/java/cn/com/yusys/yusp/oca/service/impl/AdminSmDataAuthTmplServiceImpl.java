package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmDataAuthTmplDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthTmplEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmDataAuthTmplForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmDataTmplConditionForm;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthService;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthTmplService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据权限模板表
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@Service("adminSmDataAuthTmplService")
public class AdminSmDataAuthTmplServiceImpl extends ServiceImpl<AdminSmDataAuthTmplDao, AdminSmDataAuthTmplEntity> implements AdminSmDataAuthTmplService {

    @Autowired
    private AdminSmDataAuthService adminSmDataAuthService;
    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;
    public static final QueryWrapper<AdminSmDataAuthTmplEntity> WRAPPER = new QueryWrapper<>();

    private static final String AUTH_TMPL_NAME = "auth_tmpl_name";

    private static final String SQL_NAME = "sql_name";




    @Override
    public Page<AdminSmDataAuthTmplVo> queryPage(AdminSmDataAuthTmplForm adminSmDataAuthTmplForm) {
        String keyWord = adminSmDataAuthTmplForm.getKeyWord();
        String authTmplName = adminSmDataAuthTmplForm.getAuthTmplName();
        String sqlName = adminSmDataAuthTmplForm.getSqlName();
        // 组装查询条件
        Page<AdminSmDataAuthTmplVo> page = new Page<>(adminSmDataAuthTmplForm.getPage(), adminSmDataAuthTmplForm.getSize());
        QueryWrapper<AdminSmDataAuthTmplVo> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isEmpty(keyWord)) {
            queryWrapper.like(!StringUtils.isEmpty(authTmplName), AUTH_TMPL_NAME, authTmplName);
            queryWrapper.like(!StringUtils.isEmpty(sqlName), SQL_NAME, sqlName);
        } else {
            queryWrapper.like(AUTH_TMPL_NAME, keyWord).or();
            queryWrapper.like(SQL_NAME, keyWord);
        }
        queryWrapper.orderByDesc("last_chg_dt");
        // 返回page分页对象
        return getBaseMapper().selectByCondition(page, queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AdminSmDataTmplVo> deleteTmpl(String[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        List<String> idList = new ArrayList<>(Arrays.asList(ids));
        //查询删除的模板是否和控制点有关联 如果有，已经关联的模板不能删除
        List<AdminSmDataTmplVo> tmplVoList = adminSmDataAuthService.selectWithTmplIds(idList);
        for (AdminSmDataTmplVo adminSmDataTmplVo : tmplVoList) {
            String authTmplId = adminSmDataTmplVo.getAuthTmplId();
            idList.remove(authTmplId);
        }

        if (idList.size() > 0) {
            getBaseMapper().deleteBatchIds(idList);
        }
        return tmplVoList;
    }


    @Override
    public void updateTmpl(AdminSmDataAuthTmplEntity adminSmDataAuthTmplEntity) {
        //校验数据权限模板名是否已经存在
        List<AdminSmDataAuthTmplEntity> adminSmPropEntities = this.list(new QueryWrapper<AdminSmDataAuthTmplEntity>().eq("AUTH_TMPL_NAME", adminSmDataAuthTmplEntity.getAuthTmplName()).ne("AUTH_TMPL_ID", adminSmDataAuthTmplEntity.getAuthTmplId()));
        if (CollectionUtils.nonEmpty(adminSmPropEntities)) {
            throw BizException.error(null, "50600004",
                    null, "数据权限模板名:" + adminSmDataAuthTmplEntity.getAuthTmplName() + "已存在！");
        }
        adminSmDataAuthTmplEntity.setLastChgUsr(SessionUtils.getUserId());
        this.updateById(adminSmDataAuthTmplEntity);
    }


    @Override
    public AdminSmDataAuthTmplEntity addAuthTemplate(AdminSmDataAuthTmplEntity adminSmDataAuthTmplEntity) {
        //校验数据权限模板名是否已经存在
        List<AdminSmDataAuthTmplEntity> adminSmPropEntities = this.list(new QueryWrapper<AdminSmDataAuthTmplEntity>().eq("AUTH_TMPL_NAME", adminSmDataAuthTmplEntity.getAuthTmplName()));
        if (CollectionUtils.nonEmpty(adminSmPropEntities)) {
            throw BizException.error(null, "50600004",
                    null, "数据权限模板名:" + adminSmDataAuthTmplEntity.getAuthTmplName() + "已存在！");
        }
        adminSmDataAuthTmplEntity.setAuthTmplId(StringUtils.getUUID());
        adminSmDataAuthTmplEntity.setStatus(0);
        adminSmDataAuthTmplEntity.setLastChgUsr(SessionUtils.getUserId());
        this.save(adminSmDataAuthTmplEntity);
        return adminSmDataAuthTmplEntity;
    }


    @Override
    public AdminSmDataAuthTmplEntity getInfo(String authTmplId) {
        return this.getById(authTmplId);
    }


    @Override
    public List<AdminSmAuthTreeVo> selectAuthTree(String id, String dataTenantId) {
        return getBaseMapper().selectAuthTree(id, dataTenantId);
    }


    @Override
    public List<AdminDataAuthVo> associatedTmpl(String contrId) {
        QueryWrapper<AdminDataAuthVo> wrapper = new QueryWrapper<>();
        wrapper.eq("contr_id", contrId);

        return getBaseMapper().selectAssociatedTmpl(wrapper);
    }



    @Override
    public IPage<AdminSmDataTmplListVo> getListForContr(AdminSmDataTmplConditionForm form) {
        // 分页参数
        IPage<AdminSmDataTmplListVo> page = new Page<>(form.getPage(), form.getSize());
        // 查询条件
        QueryWrapper<AdminSmDataTmplListVo> wrapper = new QueryWrapper<>();
        wrapper.and(!StringUtils.isEmpty(form.getKeyWord()),
                w -> w.like(AUTH_TMPL_NAME, form.getKeyWord())
                        .or().like(SQL_NAME, form.getKeyWord())
                        .or().like("sql_string", form.getKeyWord()));
        //如果cornID是null，那么一定是新增，没有与数据模板关联
        if(StringUtils.isBlank(form.getContrId())){
            wrapper.eq(!StringUtils.isEmpty(form.getDataTenantId()),"data_tenant_id",form.getDataTenantId());
            return getBaseMapper().selectTmplist(page,wrapper);
        }

        //check=1只查看已选择的
        wrapper.eq(form.getCheck() == 1, "a.CONTR_ID", form.getContrId());
        // 分页查询
        getBaseMapper().selectAllTmpl(page, wrapper, form.getContrId(), form.getCheck(),form.getDataTenantId());

        // 结果中是否已关联，已授权
        page.getRecords().forEach(temp -> {
            if (form.getContrId().equals(temp.getContrId())) {
                // 控制点与模板已关联
                temp.setMark(1);
                // 查询是否已授权
                List<AdminSmAuthRecoEntity> authRecoEntity = adminSmAuthRecoService.list(new QueryWrapper<AdminSmAuthRecoEntity>()
                        .eq("MENU_ID", form.getContrId())
                        .eq("AUTHRES_ID", temp.getAuthTmplId())
                        .eq(StringUtils.nonEmpty(form.getDataTenantId()),"data_tenant_id",form.getDataTenantId()));
                if (CollectionUtils.nonEmpty(authRecoEntity)) {
                    temp.setAuthRecoId(authRecoEntity.get(0).getAuthRecoId());
                }
            } else {
                // 控制点与模板未关联
                temp.setMark(0);
            }
        });
        return page;
    }



    @Override
    public List<AdminSmDataAuthTmplEntity> getByContrId(String contrId, String dataTenantId) {
        return getBaseMapper().selectByContrId(contrId, dataTenantId);
    }


    @Override
    public List<AdminTmplAndRecoVo> getTmplAndRecoVoList(List<String> contrIdList, String authobjId, String dataTenantId) {
        QueryWrapper<AdminTmplAndRecoVo> wrapper = new QueryWrapper<>();
        wrapper.in("r.menu_id", contrIdList);
        wrapper.eq("r.authobj_id", authobjId);
        wrapper.eq(StringUtils.nonEmpty(dataTenantId),"r.data_tenant_id",dataTenantId);
        return getBaseMapper().getTmplAndRecoVoList(wrapper);
    }

}