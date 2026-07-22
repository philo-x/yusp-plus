package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 认证策略参数表
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2021-03-30 11:27:32
 */
@Mapper
public interface AdminSmCrelStraDao extends BaseMapper<AdminSmCrelStraEntity> {

    /**
     * getAll
     * @return List<AdminSmCrelStraEntity>
     */
    List<AdminSmCrelStraEntity> getAll();

    /**
     * inserts
     * @param list list
     *
     */
    void inserts(@Param("list") List<AdminSmCrelStraEntity> list);
}
