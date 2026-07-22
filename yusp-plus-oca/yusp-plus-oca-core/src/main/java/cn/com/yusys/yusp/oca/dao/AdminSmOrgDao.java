package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.commons.mybatisplus.mapper.BaseMapper;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgVo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统机构表
 *
 * @author terry
 * @date 2020/11/18 23:35
 */
public interface AdminSmOrgDao extends BaseMapper<AdminSmOrgEntity> {
    /**
     * 查询所有机构
     *
     * @param sort 默认值是last_chg_dt desc
     * @return 子孙节点列表
     */
    List<AdminSmOrgVo> selectAllOrgExtVo(String sort);

    /**
     * 检查机构是否已关联其他信息
     *
     * @param orgId 上级机构记录编号
     * @return 关联用户、角色、岗位、部门的数量
     */
    Integer queryRelByOrgId(String orgId);

    /**
     * 获取指定机构所有先辈机构详情，默认取当前用户所在机构上级机构
     *
     * @param orgId 记录编号
     * @return 所有上级机构
     */
    AdminSmOrgTreeNodeBo getAllAncestryOrgs(String orgId);

    /**
     * 查询指定上级机构记录编号的机构信息
     *
     * @param orgId 上级机构记录编号
     * @return 上级机构信息
     */
    AdminSmOrgTreeNodeBo findParentByOrgId(String orgId);

    /**
     * 详情
     *
     * @param orgId 记录编号
     * @return 机构详情
     */
    AdminSmOrgDetailVo getDetailById(String orgId);

    /**
     * 工作流获取机构信息
     *
     * @param page    分页对象
     * @param wrapper 分页查询条件
     * @return 机构信息
     */
    Page<AdminSmOrgVo> getOrgsForWf(Page<AdminSmOrgVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmOrgVo> wrapper);

    /**
     * 工作流获取子机构
     *
     * @param wrapper 机构代码
     * @return 子机构
     */
    List<AdminSmOrgVo> getChildOrgCode(@Param(Constants.WRAPPER) QueryWrapper<AdminSmOrgVo> wrapper);

    /**
     * 查询要更新org_seq字段的数据
     * @param wrapper 机构级别、org_seq是否为空
     * @return 查询结果
     */
    List<AdminSmOrgEntity> getOrgSeqForUpdate(@Param(Constants.WRAPPER) QueryWrapper<AdminSmOrgEntity> wrapper);

    /**
     * 机构按租户id进行分组，获取租户id和租户id对应机构数
     * @return 租户分组
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String,Object>> groupDataTenantCount();

    /**
     * 查询机构及下级所有的机构
     * @param page
     * @param queryWrapper
     * @return 指定机构及所有下级机构
     */
    Page<AdminSmOrgVo> selectOrgAccessible(Page<AdminSmOrgVo> page, @Param(Constants.WRAPPER)  LambdaQueryWrapper<AdminSmOrgEntity> queryWrapper);
}
