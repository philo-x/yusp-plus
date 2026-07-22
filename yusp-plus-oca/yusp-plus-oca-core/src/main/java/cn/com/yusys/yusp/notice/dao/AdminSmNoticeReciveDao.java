package cn.com.yusys.yusp.notice.dao;

import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReciveEntity;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeReciveVo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统公告表发布对象记录表对应 mapper 接口类
 * @author zhangyt12
 * @date 2021/6/24 15:32
 */
public interface AdminSmNoticeReciveDao extends BaseMapper<AdminSmNoticeReciveEntity> {
    /**
     * 按照条件查询公告发布的数据
     * @param query 查询条件封装类
     * @return 公告发布数据的集合
     */
    List<AdminSmNoticeReciveEntity> selectByCondition(@Param(Constants.WRAPPER) QueryWrapper<AdminSmNoticeReciveEntity> query);

    /**
     * 查询该公告角色权限的角色ID和角色名称
     * @param queryWrapper 查询条件封装类
     * @return 按照角色发布公告的数据集合
     */
    List<AdminSmRoleEntity> selectRoles(@Param(Constants.WRAPPER) QueryWrapper<AdminSmRoleEntity> queryWrapper);

    /**
     * 查询该公告部门权限的角色ID和部门名称
     * @param queryWrapper 查询条件封装类
     * @return 按照机构发布公告的数据集合
     */
    List<AdminSmOrgEntity> selectOrgs(@Param(Constants.WRAPPER) QueryWrapper<AdminSmOrgEntity> queryWrapper);

    /**
     * 查询公告权限的机构信息和角色信息
     * @param noticeId 公告 id
     * @return 自定义公告发布数据实体类集合
     */
    List<AdminSmNoticeReciveVo> selectReciveIdAndName(String noticeId);
}
