package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthTmplEntity;
import cn.com.yusys.yusp.oca.domain.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据权限模板表
 *
 * @author danyb1
 * @date 2020-12-08 18:53:56
 */

public interface AdminSmDataAuthTmplDao extends BaseMapper<AdminSmDataAuthTmplEntity> {

    /**
     * 条件查询数据模板集合
     * @param page 分页查询类
     * @param queryWrapper 条件构造器
     * @return 分页查询出的数据权限模板表
     */
    Page<AdminSmDataAuthTmplVo> selectByCondition(Page<AdminSmDataAuthTmplVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataAuthTmplVo> queryWrapper);

    /**
     * 权限授权树数据查询，联合admin_sm_auth_reco表及admin_sm_data_auth表
     *
     * @param id 授权对象id
     * @param dataTenantId 租户ID
     * @return 权限授权树数据
     */
    List<AdminSmAuthTreeVo> selectAuthTree(@Param("id") String id, @Param("dataTenantId") String dataTenantId);

    /**
     * 查询控制点已经关联的模板
     *
     * @param wrapper  条件构造器
     * @return 查询控制点关联的模板的信息列表
     */
    List<AdminDataAuthVo> selectAssociatedTmpl(@Param(Constants.WRAPPER) QueryWrapper<AdminDataAuthVo> wrapper);


    /**
     *
     * 控制点页面需求的数据模板条件查询列表
     *
     * @param page 页码
     * @param wrapper 条件控制前
     * @param contrId 控制点的id
     * @param lastTmplIds 控制点关联的数据模板
     * @return 据权限模板表与控制点的展示
     */
    Page<AdminSmDataTmplListVo> getListForContr(
            IPage<AdminSmDataTmplListVo> page,
            @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplListVo> wrapper,
            @Param("contrId") String contrId,
            @Param("lastTmplIds") String[] lastTmplIds);

    /**
     * 获取与控制点绑定的所有数据模板
     *
     * @param wrapper 条件控制器
     * @return 获取与控制点绑定的所有数据模板
     */
    List<AdminDataAuthVo> associatedTmplList(@Param(Constants.WRAPPER) QueryWrapper<AdminDataAuthVo> wrapper);

    /**
     * 获取控制点已关联的数据模板
     *
     * @param contrId 控制点记录编号
     * @param dataTenantId dataTenantId
     * @return 数据权限模板表集合
     */
    List<AdminSmDataAuthTmplEntity> selectByContrId(@Param("contrId") String contrId, @Param("dataTenantId") String dataTenantId);

    /**
     * 数据模板授权页面列表数据
     *
     * @param queryWrapper 条件控制器
     * @return 已被授权的数据权限模板列表
     */
    List<AdminTmplAndRecoVo> getTmplAndRecoVoList(@Param(Constants.WRAPPER) QueryWrapper<AdminTmplAndRecoVo> queryWrapper);

    /**
     * 查询条件为已经勾选的数据
     * @param page 分页对象
     * @param wrapper 条件对象
     * @param contrId 控制点 id
     * @return 数据模板 voList
     */
    IPage<AdminSmDataTmplListVo> selectByCheck(
            IPage<AdminSmDataTmplListVo> page,
            @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplListVo> wrapper,
            @Param("contrId") String contrId);

    /**
     * 查询条件为已经勾选的数据
     * @param page 分页对象
     * @param wrapper 条件对象
     * @param contrId 控制点 id
     * @return 数据模板 voList
     */
    IPage<AdminSmDataTmplListVo> selectByContrCondition(
            IPage<AdminSmDataTmplListVo> page,
            @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplListVo> wrapper,
            @Param("contrId") String contrId);

    /**
     * 关联DataAuth表，使用contrId查询，返回AdminSmDataTmplListVo
     *
     * @param contrId 控制点id
     * @return 控制点关联的数据权限模板列表
     */
    List<AdminSmDataTmplListVo> getByContrIdForVo(@Param("contrId") String contrId);

    /**
     * 分页查询所有模板
     *
     * @param page 封装分页参数
     * @param wrapper 条件构造器 查询条件
     * @param contrId 控制点的记录编号
     * @param check int
     * @param dataTenantId dataTenantId
     * @return 分页模板数据
     */
    IPage<AdminSmDataTmplListVo> selectAllTmpl(IPage<AdminSmDataTmplListVo> page,
                                               @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplListVo> wrapper,
                                               @Param("contrId") String contrId,
                                               @Param("check") int check,
                                               @Param("dataTenantId") String dataTenantId);

    /**
     * 分页查询所有模板
     * @param page 封装分页参数
     * @param wrapper 条件构造器 查询条件
     * @return 分页模板数据
     */
    IPage<AdminSmDataTmplListVo> selectTmplist(IPage<AdminSmDataTmplListVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplListVo> wrapper);
}
