package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmLogicSysBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogicSysEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 系统逻辑系统表
 *
 * @author wujp4
 * @date 2020-11-19 14:30:22
 */
public interface AdminSmLogicSysService extends IService<AdminSmLogicSysEntity> {

    /**
     * 逻辑系统列表查询
     *
     * @param params 查询参数
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 10:30
     */
    PageUtils getAdminSmLogicSys(Map<String, Object> params);


    /**
     * 逻辑系统-修改，并设置逻辑系统登陆策略
     *
     * @param adminSmLogicSysBo 要修改的逻辑系统数据
     * @param funcId            业务功能ID
     * @return 修改成功数
     * @author zhanyq
     * @date 2021-06-25 10:31
     */
    int updateAdminSmLogic(AdminSmLogicSysBo adminSmLogicSysBo, String funcId);

    /**
     * 逻辑系统设置 状态生效/失效
     *
     * @param adminSmLogicSysBo 要修改的逻辑系统数据
     * @author zhanyq
     * @date 2021-06-25 10:32
     */
    void updateAdminSmLogicStat(AdminSmLogicSysBo adminSmLogicSysBo);

    /**
     * 删除逻辑系统，并删除响应的登录策略
     *
     * @param sysId 逻辑系统ID
     * @return 删除成功数
     * @author zhanyq
     * @date 2021-06-25 10:33
     */
    int deleteLogicAndCrelInfo(String sysId);

    /**
     * 新增和初始化逻辑系统
     *
     * @param adminSmLogicSys 要插入的逻辑系统数据
     * @return 将插入成功的数据返回
     * @author zhanyq
     * @date 2021-06-25 10:34
     */
    AdminSmLogicSysEntity insertAndCopy(AdminSmLogicSysBo adminSmLogicSys);
}

