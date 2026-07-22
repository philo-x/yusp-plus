package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 类说明
 * @author: zhangsong
 * @date: 2021/4/1
 */
@Mapper
public interface AdminSmLoginLogDao  extends BaseMapper<AdminSmLoginLogEntity> {

    /**
     * getLastLoginLog
     * @param loginCode
     * @param operResult
     * @return
     */
    AdminSmLoginLogEntity getLastLoginLog(@Param("loginCode") String loginCode, @Param("operResult") String operResult);
}
