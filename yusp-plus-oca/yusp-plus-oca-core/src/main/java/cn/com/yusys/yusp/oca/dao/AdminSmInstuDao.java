package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmInstuEntity;
import cn.com.yusys.yusp.oca.domain.vo.InstuExtVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 金融机构表
 * 
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-19 14:30:22
 */

public interface AdminSmInstuDao extends BaseMapper<AdminSmInstuEntity> {

    /**
     * pageInstuExtVo
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<InstuExtVo> pageInstuExtVo(Page<InstuExtVo> page,@Param(Constants.WRAPPER)  QueryWrapper<InstuExtVo> queryWrapper);
}
