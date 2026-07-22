package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmLogicSysBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogicSysEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogicSysVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统逻辑系统表
 *
 * @author wujp4
 * @date 2020-11-19 14:30:22
 */

public interface AdminSmLogicSysDao extends BaseMapper<AdminSmLogicSysEntity> {


    /**
     * 逻辑系统列表查询
     *
     * @param page              分页对象
     * @param adminSmLogicSysBo 查询餐厨
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 11:04
     */
    Page<AdminSmLogicSysVo> getAdminSmLogicSys(IPage<AdminSmLogicSysVo> page, @Param("adminSmLogicSysBo") AdminSmLogicSysBo adminSmLogicSysBo);

}
