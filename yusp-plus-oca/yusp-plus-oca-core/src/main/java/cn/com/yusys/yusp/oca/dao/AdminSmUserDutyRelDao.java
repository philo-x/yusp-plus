package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联表
 * 
 * @author danyb1
 * @date 2020-12-01 21:55:19
 */
public interface AdminSmUserDutyRelDao extends BaseMapper<AdminSmUserDutyRelEntity> {

    /**
     * 查询用户关联的岗位id
     * @param userId 用户id
     * @return 用户关联的岗位id集合
     */
    List<String> findUserDutyRelsByUser(@Param("userId") String userId);
}
