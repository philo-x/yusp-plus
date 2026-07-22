package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmPasswordLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 密码修改记录表
 * 
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-02 15:07:44
 */

public interface AdminSmPasswordLogDao extends BaseMapper<AdminSmPasswordLogEntity> {

    /**
     * getLastChangeLog
     * @param userId
     * @return
     */
    AdminSmPasswordLogEntity getLastChangeLog(@Param("userId") String userId);
}
