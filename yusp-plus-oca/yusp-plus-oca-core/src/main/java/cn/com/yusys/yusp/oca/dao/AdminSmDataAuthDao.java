package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.ResControlDataAuthTmplVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据权限表
 * 
 * @author wujp4
 * @date 2020-12-01 09:52:42
 */
public interface AdminSmDataAuthDao extends BaseMapper<AdminSmDataAuthEntity> {

    /**
     * 分页查询控制点下的数据权限模板列表
     * @param page 分页对象
     * @param queryWrapper 查询条件对象
     * @return 数据模板 vo 集合
     */
    IPage<ResControlDataAuthTmplVo> pageResControlDataAuthTmpl(Page<ResControlDataAuthTmplVo> page,
                                                               @Param(Constants.WRAPPER) QueryWrapper<ResControlDataAuthTmplVo> queryWrapper);

    /**
     * 根据数据模板id查询数据模板数据集合
     * @param wrapper 条件构造器
     * @return 数据权限模板表的模板id和模板名称组成对象的集合
     */
    List<AdminSmDataTmplVo> selectWithTmplIds(@Param(Constants.WRAPPER) QueryWrapper<AdminSmDataTmplVo> wrapper);
}
