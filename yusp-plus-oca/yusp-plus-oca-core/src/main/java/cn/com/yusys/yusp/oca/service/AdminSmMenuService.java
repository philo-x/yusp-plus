package cn.com.yusys.yusp.oca.service;


import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.session.user.impl.MenuImpl;
import cn.com.yusys.yusp.oca.domain.bo.DragMenuBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMenuEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmMenuConditionForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmMenuVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单表
 *
 * @author wujp4
 * @date 2020-11-19 16:32:07
 */
public interface AdminSmMenuService extends IService<AdminSmMenuEntity> {

    /**
     * 分页查询
     *
     * @param params 查询条件
     * @return 菜单分页查询结果
     * @author zhanyq
     * @date 2021-06-24 15:43
     */
    PageUtils queryPage(Map<String, Object> params);


    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单信息列表
     * @author zhanyq
     * @date 2021-06-24 15:26
     */
    List<MenuImpl> getAdminSmMenu(String userId);

    /**
     * 菜单树初始化查询
     *
     * @param sysId  逻辑系统ID
     * @param lazy   是否懒加载
     * @param menuId 菜单ID
     * @return 菜单树查询结果数据
     * @author zhanyq
     * @date 2021-06-24 15:14
     */
    List<MenuVo> getMenuTree(String sysId, boolean lazy, String menuId);

    /**
     * 菜单数据保存,同时删除缓存(写库删缓存，失效模式)
     * 缓存,此处不处理，由 menuandcontro 接口处理
     * 缓存一致性需求高，可以采用：canal订阅binlog，异步更新缓存方式
     *
     * @param adminSmMenu 要保存的菜单数据
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:18
     */
    MenuVo saveAdminSmMenu(AdminSmMenuEntity adminSmMenu);

    /**
     * 修改菜单
     *
     * @param adminSmMenu 修改菜单的form数据
     * @author zhanyq
     * @date 2021-06-24 15:19
     */
    void updateMenuById(AdminSmMenuEntity adminSmMenu);

    /**
     * 菜单节点信息查询
     *
     * @param menuId 菜单ID
     * @return 菜单节点信息数据
     * @author zhanyq
     * @date 2021-06-24 15:23
     */
    MenuVo getMenuInfo(String menuId);

    /**
     * 递归查询菜单树列表
     *
     * @param sysId    逻辑系统ID
     * @param pid      上级菜单ID
     * @param treeDeep 递归层数
     * @return 菜单树过节列表
     * @author zhanyq
     * @date 2021-06-24 15:09
     */
    List<MenuVo> queryTreeMenuList(String sysId, String pid, String treeDeep);

    /**
     * 搜索菜单树
     *
     * @param menuName 菜单名称
     * @param sysId    逻辑系统ID
     * @return 搜索结果数据
     * @author zhanyq
     * @date 2021-06-24 15:11
     */
    List<MenuVo> querySearchTree(String sysId, String menuName);

    /**
     * 修改菜单树,前端返回menu tree,不再使用此方法
     * 待优化点：
     * 1）整个菜单树修改保存，耗时 8s
     * 2) mp updateBatchById 是一条一条更新数据库
     *
     * @param menuVoList 前端传menu tree
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:20
     */
    int updateAllMenuTree(List<MenuVo> menuVoList);


    /**
     * 根据授权对象ID查询权限管理树
     *
     * @param id 授权对象ID
     * @param dataTenantId 租户ID
     * @return 授权菜单树形数据集合
     * @author zhanyq
     * @date 2021-06-24 15:32
     */
    List<AdminSmAuthTreeVo> selectAuthTree(String id,String dataTenantId);


    /**
     * 更新菜单指定位置，菜单树可拖动
     *
     * @param dragMenuBo 前端传指定 拖动的menuId 参照的menuId 相对于参照点的前后顺序
     * @return 成功修改条数
     * @author zhanyq
     * @date 2021-06-24 15:21
     */
    int updateMenuTreeByDragMenu(DragMenuBo dragMenuBo);


    /**
     * 查询控制点关联菜单列表
     *
     * @param adminSmMenuConditionForm 查询条件
     * @return 菜单分页查询结果
     * @author zhanyq
     * @date 2021-06-24 15:34
     */
    Page<AdminSmMenuVo> getMenuListForContr(AdminSmMenuConditionForm adminSmMenuConditionForm);


    /**
     * 批量删除菜单-逻辑删除
     *
     * @param ids 菜单ID
     * @return 成功条数
     * @author zhanyq
     * @date 2021-06-24 15:39
     * --------------------------------------------
     * 弃用，原因：逻辑复杂，性能低
     * @author zhanyq
     * @date 2021-07-13
     */
    @Deprecated
    int removeMenuByIds(String[] ids);


    /**
     * 批量删除菜单-使用物理删除
     *
     * @param ids 菜单ID
     * @return void
     * @author zhanyq
     * @date 2021-07-13 9:49
     */
    void deleteMenuByIds(String[] ids);

}

