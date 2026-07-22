package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.commons.session.user.impl.MenuImpl;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMenuEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmMenuVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单表
 *
 * @author wujp4
 * @date 2020-11-19 16:32:07
 */
public interface AdminSmMenuDao extends BaseMapper<AdminSmMenuEntity> {

    /**
     * 菜单权限树查询
     * @param queryWrapper 查询条件
     * @return 菜单权限树查询结果
     * @author zhanyq
     * @date 2021-06-24 15:48
     */
    List<MenuImpl> getAdminSmMenuVoList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    /**
     * 根据菜单ID查询
     * @param menuId 菜单ID
     * @return 菜单详情
     * @author zhanyq
     * @date 2021-06-24 15:50
     */
    MenuVo getMenuInfoByMenuId(String menuId);

    /**
     * 根据逻辑系统查询菜单树
     * @param sysId 逻辑系统ID
     * @return 菜单树型结构数据
     * @author zhanyq
     * @date 2021-06-24 16:04
     */
    List<MenuVo> queryMenuTreeBySysId(String sysId);

    /**
     * 根据逻辑系统查询菜单控制权限树
     * @param sysId 逻辑系统ID
     * @return 菜单树型结构数据
     * @author zhanyq
     * @date 2021-06-24 16:04
     */
    List<MenuVo> queryMenuTreeResContrBySysId(String sysId);


    /**
     * 关联 admin_sm_auth_reco 创建权限管理树，获取所有菜单数据
     * @param id authobj_id授权对象ID
     * @param dataTenantId 租户ID
     * @return 菜单权限树数据
     * @author zhanyq
     * @date 2021-06-24 16:12
     */
    List<AdminSmAuthTreeVo> selectAuthTree(@Param("id") String id,@Param("dataTenantId") String dataTenantId);

    /**
     * 批量修改菜单数据 拖拽等
     * @param list 要修改的菜单数据
     * @return 修成成功条数
     * @author zhanyq
     * @date 2021-06-24 16:15
     */
    int updateBatchMenuTree(@Param("list") List<AdminSmMenuEntity> list);


    /**
     * 控制点页面需求的菜单列表
     * @param page 分页对象
     * @param wrapper 查询条件
     * @param lastMenuId 前端最后选择的那个菜单ID， 如果是修改，上次选择菜单的 id
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-24 16:16
     */
    Page<AdminSmMenuVo> getMenuListForContr(
            Page<AdminSmMenuVo> page,
            @Param(Constants.WRAPPER) QueryWrapper<AdminSmMenuVo> wrapper,
            @Param("lastMenuId") String lastMenuId);
    /**
     * 不能授权的菜单列表
     * @return 不能授权的菜单列表id
     * @author chenjing
     * @date 2022-04-01 16:16
     */
    List<String> getExcludeMenuId();


}
