package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.commons.session.user.impl.ControlImpl;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmResContrEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统功能控制点表
 *
 * @author zhangyt12
 * @date 2020-11-19 16:38:48
 */

public interface AdminSmResContrDao extends BaseMapper<AdminSmResContrEntity> {

    /**
     * 控制点功能列表查询
     *
     * @param page 分页查询条件
     * @param wrapper 条件控制器
     * @return 分页查询的控制点列表
     */
    Page<AdminSmResContrVo> queryPageWithCondition(IPage<AdminSmResContrVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmResContrVo> wrapper);

    /**
     * 查询当前用户已授权的控制点数据
     * @param queryWrapper 条件对象
     * @return 已授权的控制点数据
     */
    List<ControlImpl> getAdminSmContrList(@Param(Constants.WRAPPER) QueryWrapper<ControlImpl> queryWrapper);

    /**
     * 联合admin_sm_auth_reco表
     * @param id 授权对象记录编号
     * @param dataTenantId 租户ID
     * @return list 授权的实体
     */
    List<AdminSmAuthTreeVo> selectAuthTree(@Param("id") String id, @Param("dataTenantId") String dataTenantId);

    /**
     * 缓存中添加 userId 现有的控制点
     * @param wrapper 条件控制器
     * @return 控制点数据
     */
    List<ControlImpl> selectControlImplList(@Param(Constants.WRAPPER) QueryWrapper<ControlImpl> wrapper);

    /**
     * 分页查询权限表中的控制点数据
     * @param result 分页条件
     * @param wrapper 授权对象id和关键字组装条件查询器
     * @return 分页权限表中控制点数据
     */
    IPage<AdminSmAuthTreeVo> selectAuthTreePage(IPage<AdminSmAuthTreeVo> result,@Param(Constants.WRAPPER) QueryWrapper<AdminSmAuthTreeVo> wrapper);


    /**
     * 查询已授权的控制点信息
     * @param page 分页对象
     * @param wrapper 查询条件
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-09-29 9:33
     */
    IPage<AdminSmResContrVo> queryAuthContrPage(IPage<AdminSmResContrVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmResContrVo> wrapper);

    /**
     * 查询请求控制交易
     * @return
     */
    List<AdminSmResContrVo> queryRequestCheckList();
}
