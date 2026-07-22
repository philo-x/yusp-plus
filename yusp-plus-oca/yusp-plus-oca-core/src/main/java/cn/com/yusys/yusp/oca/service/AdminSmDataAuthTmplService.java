package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthTmplEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmDataAuthTmplForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmDataTmplConditionForm;
import cn.com.yusys.yusp.oca.domain.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 数据权限模板表
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public interface AdminSmDataAuthTmplService extends IService<AdminSmDataAuthTmplEntity> {
    /**
     * 查询模板
     * @param adminSmDataAuthTmplForm 数据模板条件查询
     * @return 统一返回结果实体类
     */
    Page<AdminSmDataAuthTmplVo> queryPage(AdminSmDataAuthTmplForm adminSmDataAuthTmplForm);


    /**
     * 批量删除
     * 同时删除admin_sm_data_auth关联表中的记录
     *
     * @param ids 数据权限模板记录编号数组
     * @return 数据权限模板表的模板id和模板名称组成的实体类的集合
     */
    List<AdminSmDataTmplVo> deleteTmpl(String[] ids);

    /**
     * 新增数据权限模板
     * @param adminSmDataAuthTmplEntity 数据权限模板表实体类
     */
    void updateTmpl(AdminSmDataAuthTmplEntity adminSmDataAuthTmplEntity);

    /**
     * 新增模板
     * @param adminSmDataAuthTmplEntity 数据权限模板表实体类
     * @return 数据权限模板表实体类
     */
    AdminSmDataAuthTmplEntity addAuthTemplate(AdminSmDataAuthTmplEntity adminSmDataAuthTmplEntity);

    /**
     * 修改之前查询模板信息
     * @param authTmplId 模板记录编号
     * @return 数据权限模板表实体类
     */
    AdminSmDataAuthTmplEntity getInfo(String authTmplId);

    /**
     * 权限授权树数据查询，联合admin_sm_auth_reco表及admin_sm_data_auth表
     * @param id 权限授权记录号
     * @param dataTenantId 租户ID
     * @return 权限授权树列表
     */
    List<AdminSmAuthTreeVo> selectAuthTree(String id, String dataTenantId);

    /**
     * 查询控制点已经关联的模板
     * @param contrId 控制点记录编号
     * @return 控制点同模板关联信息查询
     */
    List<AdminDataAuthVo> associatedTmpl(String contrId);

    /**
     * 控制点页面需求的数据模板条件查询列表
     * @param form 查询控制点可授权的数据权限模板列表条件封装类
     * @return IPage分页类
     */
    IPage<AdminSmDataTmplListVo> getListForContr(AdminSmDataTmplConditionForm form);


    /**
     * 获取控制点已关联的数据模板
     * @param contrId 控制点记录编号
     * @param dataTenantId dataTenantId
     * @return 数据权限模板表集合
     */
    List<AdminSmDataAuthTmplEntity> getByContrId(String contrId, String dataTenantId);


    /**
     * 查询数据模板授权记录
     * @param contrIdList 控制点记录编号集合
     * @param authobjId 授权对象id
     * @param dataTenantId 租户ID
     * @return 数据模板授权页面列表数据
     */
    List<AdminTmplAndRecoVo> getTmplAndRecoVoList(List<String> contrIdList, String authobjId, String dataTenantId);

}

