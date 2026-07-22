package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserMgrOrgEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户授权管理机构表
 * 
 * @author danyb1
 * @date 2020-12-02 16:29:50
 */
public interface AdminSmUserMgrOrgDao extends BaseMapper<AdminSmUserMgrOrgEntity> {

    /**
     * 查询用户授权的机构机构
     * @param userId 用户id
     * @return 用户关联的机构id合集
     */
    List<String> findOrgRelsByUser(@Param("userId") String userId);
}
