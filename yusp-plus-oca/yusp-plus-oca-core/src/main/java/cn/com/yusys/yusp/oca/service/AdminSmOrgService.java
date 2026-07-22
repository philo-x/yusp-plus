package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgExtQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgTreeQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 系统机构表
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
public interface AdminSmOrgService extends IService<AdminSmOrgEntity> {
    /**
     * 分页查询
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    Page<AdminSmOrgVo> queryPage(AdminSmOrgExtQuery query);

    /**
     * 详情
     *
     * @param orgId 记录编号
     * @return 机构详情
     */
    AdminSmOrgDetailVo getDetailById(String orgId);

    /**
     * 获取指定根节点本身及所有后裔列表
     *
     * @param orgId 记录编号
     * @return 指定根节点本身及所有后裔列表
     */
    List<AdminSmOrgVo> getAllProgeny(String orgId);

    /**
     * 获取指定根机构的所有子孙节点列表,包含根节点本身
     *
     * @param query - orgCode：默认值是当前用户所在机构
     *              - orgSts：为null或空字串时查询所有状态的机构，有值时只返回对应状态的机构树
     *              - sort：默认值是last_chg_dt desc
     * @return 子孙节点列表
     */
    List<AdminSmOrgVo> getAllProgeny(AdminSmOrgExtQuery query);

    /**
     * 角色关联用户弹框中的查询条件：机构树
     * 范围是角色所在机构及下级所有机构
     *
     * @param orgId 记录编号
     * @return 机构树
     */
    List<AdminSmOrgTreeNodeBo> getOrgTreeByOrgId(String orgId);

    /**
     * 不是严格的机构树，是一个多根树，其中一个根是用户所在机构，其他根是胡无交集的用户授权机构构成的若干子树，这些子树的逻辑层级不严格对应在树中的层级
     *
     * @param query - orgId：默认值是当前用户所在机构
     *              - orgSts：为null或空字串时查询所有状态的机构，有值时只返回对应状态的机构树
     *              - sort：默认值是last_chg_dt desc
     * @return orgId 为空时查当前登录用户所在机构，orgSts为空时查所有状态
     */
    List<AdminSmOrgTreeNodeBo> getOrgTree(AdminSmOrgTreeQuery query);

    /**
     * 批量查询机构树
     *
     * @param orgIds       要查的多根机构树的根节点，要求必须是同级机构
     * @param orgSts       需要的机构状态（其中某个上级机构不符合会级联排除其所有子节点）
     * @param exceptOrgIds 返回多根树中需要排除的节点（会级联）
     * @return 机构树
     */
    List<AdminSmOrgTreeNodeBo> getOrgTrees(List<String> orgIds, AvailableStateEnum orgSts, String... exceptOrgIds);

    /**
     * 获取指定用户有权访问的机构id集合，包含用户所在机构、其所有子孙机构以及用户授权机构
     *
     * @param userId 用户id
     * @return 指定用户有权访问的机构id集合
     */
    List<String> getAllAccessibleOrgIds(String userId);


    /**
     * 查询当前用户有权访问的机构下的所有部门
     *
     * @return 当前用户有权访问的机构下的所有部门
     */
    List<String> getAllAccessibleOrgIds();

    /**
     * 新增
     *
     * @param entity 机构实体
     * @return 是否插入成功
     */
    @Override
    boolean save(AdminSmOrgEntity entity);

    /**
     * 批量停用
     *
     * @param ids 记录编号数组
     */
    void batchDisable(String[] ids);

    /**
     * 批量删除
     *
     * @param ids 记录编号数组
     */
    void batchDelete(String[] ids);

    /**
     * 批量启用
     *
     * @param ids 记录编号数组
     */
    void batchEnable(String[] ids);

    /**
     * 修改
     *
     * @param entity 机构实体
     * @return 操作结果
     */
    boolean updateBy(AdminSmOrgEntity entity);


    /**
     * 获取指定机构所有先辈机构详情，默认取当前用户所在机构上级机构
     *
     * @param orgId 记录编号
     * @return 所有上级机构
     */
    Set<AdminSmOrgTreeNodeBo> getAllAncestryOrgs(String orgId);

    /**
     * 获取指定机构父机构详情，默认取当前用户所在机构上级机构
     *
     * @param orgId 记录编号
     * @return 上级机构
     */
    AdminSmOrgEntity getParentOrg(String orgId);

    /**
     * 获取指定机构兄弟机构详情，默认取当前用户所在机构上级机构
     *
     * @param orgId 记录编号
     * @return 兄弟机构
     */
    Set<AdminSmOrgEntity> getSiblingOrgs(String orgId);

    /**
     * 工作流获取机构信息
     *
     * @param query 分页查询条件
     * @return 机构信息
     */
    Page<AdminSmOrgVo> getOrgsForWf(AdminSmOrgExtQuery query);

    /**
     * 工作流获取子机构
     *
     * @param orgCode 机构代码
     * @return 子机构列表
     */
    List<String> getLowerOrgId(String orgCode);

    /**
     * 定时任务 更新org_seq字段的数据
     *
     * @param level 机构级别
     * @param flag  是否更新所有
     * @author zhanyq
     * @date 2021-08-16 18:05
     */
    void updateOrgSeqForTask(int level, boolean flag);

    /**
     * 获取orgId及其上级的orgId集合
     *
     * @param orgId 机构id
     * @return 机构id的集合
     */
    List<String> getUpOrgIdList(String orgId);

    /**
     * 下载Excel模板
     *
     * @return
     */
    ProgressDto asyncExportTemplate();

    /**
     * Excel导出数据
     *
     * @param query
     * @return
     */
    ProgressDto asyncExportData(AdminSmOrgExtQuery query);

    /**
     * Excel导入
     *
     * @param fileId fileId
     * @return ProgressDto
     * @exception IOException IOException异常
     */
    ProgressDto asyncImportData(String fileId) throws IOException;


    /**
     * 机构名称的缓存跟新
     *
     * @param entity 新增或者需要跟新的机构
     */
    void updateOrgCache(AdminSmOrgEntity entity);

    /**
     * 删除部分机构缓存
     *
     * @param ids 需要被删除缓存的机构id
     */
    void deletePartOrgCache(String[] ids);

}