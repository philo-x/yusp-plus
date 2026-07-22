package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDutyRelVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统岗位用户关联表
 *
 * @author terry
 * @date 2020-12-30 21:55:19
 */
public interface AdminSmDutyUserDao extends BaseMapper<AdminSmUserDutyRelVo> {
    /**
     * pageDutyUserByDutyId
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<AdminSmUserDutyRelVo> pageDutyUserByDutyId(Page<AdminSmUserDutyRelVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmUserDutyRelVo> queryWrapper);
}

