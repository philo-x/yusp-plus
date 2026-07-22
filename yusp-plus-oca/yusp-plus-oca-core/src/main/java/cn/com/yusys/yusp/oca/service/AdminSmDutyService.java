package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDutyEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDutyVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDutyRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统岗位表
 *
 * @author terry
 * @date 2020-12-01 21:55:19
 */
public interface AdminSmDutyService extends IService<AdminSmDutyEntity> {
    /**
     * 分页列表
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    Page<AdminSmDutyVo> queryPage(AdminSmDutyQuery query);

    /**
     * 条件分页查询岗位下所有用户
     *
     * @param query 查询条件
     * @return 用户列表
     */
    Page<AdminSmUserDutyRelVo> memberPage(AdminSmDutyUserQuery query);

    /**
     * 批量停用岗位
     *
     * @param ids 岗位id数组
     */
    void batchDisable(String[] ids);

    /**
     * 批量启用岗位
     *
     * @param ids 岗位id数组
     */
    void batchEnable(String[] ids);


    /**
     * 批量删除
     *
     * @param ids 岗位id数组
     */
    void batchDelete(String[] ids);

    /**
     * 工作流获取岗位信息
     *
     * @param query 查询条件
     * @return 岗位信息
     */
    Page<AdminSmDutyVo> getDutysForWf(AdminSmDutyQuery query);

    /**
     * 根据用户id查询岗位代码
     *
     * @param userId 用户id
     * @return 岗位代码
     */
    List<String> getDutysByUserIdForWf(String userId);
}

