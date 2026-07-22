package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDptEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDptVo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统部门表
 *
 * @author danyb1
 * @date 2020-11-12 10:47:26
 */

public interface AdminSmDptDao extends BaseMapper<AdminSmDptEntity> {
    /**
     * 查询所有的部门
     * @param queryWrapper 条件构造器
     * @return 部门列表
     */
    List<AdminSmDptVo> selectAllDpt(@Param(Constants.WRAPPER) QueryWrapper<AdminSmDptVo> queryWrapper);

    /**
     * 工作流查询部门信息
     *
     * @param page 封装分页参数
     * @param queryWrapper 条件构造器
     * @return 部门信息
     */
    Page<AdminSmDptVo> getDeptsForWf(Page<AdminSmDptVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmDptVo> queryWrapper);
    /**
     * 部门按租户id进行分组，获取租户id和租户id对应部门个数
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String, Object>> groupDataTenantCount();

    /**
     * 机构及机构下级所有机构的部门
     *
     * @param queryWrapper 查询条件
     * @param page         分页
     * @return 机构及机构下级所有机构的部门
     */
    Page<AdminSmDptVo> selectOrgAccessible(@Param(Constants.WRAPPER) QueryWrapper<AdminSmDptVo> queryWrapper, Page<AdminSmDptVo> page);
}
