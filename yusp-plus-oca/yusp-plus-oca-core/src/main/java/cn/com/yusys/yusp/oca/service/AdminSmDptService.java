package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDptEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDptQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDptVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统部门表
 *
 * @author terry
 * @date 2020-11-12 10:47:26
 */
public interface AdminSmDptService extends IService<AdminSmDptEntity> {

    /**
     * 部门管理主页列表查询（分页）
     *
     * @param query 费用查询条件
     * @return 部门列表
     */
    Page<AdminSmDptVo> queryPage(AdminSmDptQuery query);

    /**
     * 批量停用部门
     *
     * @param ids 部门id数组
     */
    void batchDisable(String[] ids);

    /**
     * 批量启用部门
     *
     * @param ids 部门id数组
     */
    void batchEnable(String[] ids);

    /**
     * 批量删除
     *
     * @param ids 部门id数组
     */
    void batchDelete(String[] ids);

    /**
     * 部门用户
     *
     * @param query 分页查询实体类
     * @return 部门用户列表
     */
    Page<AdminSmUserVo> memberPage(AdminSmUserQuery query);

    /**
     * 查出指定机构下所有部门以及子部门，以树形结构组装起来
     *
     * @param query 分页查询条件 这里可以根据需要传入条件，查询部分的部门,这里只用了orgId和dptSts两个参数
     * @return 机构下所有部门以及子部门树形结构
     */
    List<AdminSmDptEntity> dptTree(AdminSmDptQuery query);

    /**
     * 工作流查询部门信息
     *
     * @param query 分页查询条件
     * @return 部门信息
     */
    Page<AdminSmDptVo> getDeptsForWf(AdminSmDptQuery query);

    /**
     * 部门名称的缓存跟新
     *
     * @param entity 新增或者需要跟新的部门
     */
    void updateDepCache(AdminSmDptEntity entity);

    /**
     * 删除部分部门缓存
     *
     * @param ids 需要被删除缓存的部门id
     */
    void deletePartDepCache(String[] ids);
}
