package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.GenericBuilder;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;
import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.user.UserIdentity;
import cn.com.yusys.yusp.commons.session.user.impl.MenuImpl;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.commons.util.collection.SetUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmMenuDao;
import cn.com.yusys.yusp.oca.domain.bo.DragMenuBo;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMenuEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmMenuConditionForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmMenuVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmTenantUserRelService;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 菜单管理业务实现类
 *
 * @author zhanyq
 * @date 2021-06-24 15:45
 */
@Service("adminSmMenuService")
public class AdminSmMenuServiceImpl extends ServiceImpl<AdminSmMenuDao, AdminSmMenuEntity> implements AdminSmMenuService {

    @Autowired
    AdminSmAuthRecoService adminSmAuthRecoService;
    @Autowired
    AdminSmBusiFuncService adminSmBusiFuncService;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;

    private static final String UP_MENU_ID = "up_menu_id";

    private static final String MENU_ORDER = "menu_order";

    private static final String I_18_N_KEY = "i18n_key";

    private static final String MENU_NAME = "menu_name";

    private static final String MENU_ID = "menu_id";

    private static final String SYS_ID = "sys_id";

    private static final String MENU_CLASSIFY = "menu_classify";

    private static final String FUNC_ID = "func_id";


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminSmMenuEntity> page = this.page(
                new Query<AdminSmMenuEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<MenuImpl> getAdminSmMenu(String userId) {
        List<? extends UserIdentity> roles = SessionUtils.getUserInformation().getRoles();
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.nonEmpty(userId), "t_user.user_id", userId);
        if(roles != null && roles.size() >0){
            String roleId=roles.get(0).getId();
            queryWrapper.eq(StringUtils.nonEmpty(roleId),"t_rolerel.role_id",roleId);
        }

        List<MenuImpl> adminSmMenuVoList = getBaseMapper().getAdminSmMenuVoList(queryWrapper);

        return adminSmMenuVoList;
    }

    @Override
    public List<MenuVo> getMenuTree(String sysId, boolean lazy, String menuId) {

        // 如果前端未传 sysId，后端默认取 clientid test
        if (StringUtils.isEmpty(sysId)) {
            sysId = OcaCommonInfoUtils.getUserSysId();
        }

        List<MenuVo> menuVoList = new ArrayList<>();
        List<AdminSmMenuEntity> entityList;
        // 懒加载
        if (lazy) {
            entityList = this.list(new QueryWrapper<AdminSmMenuEntity>()
                    .select(MENU_ID, MENU_NAME, UP_MENU_ID, MENU_ORDER, I_18_N_KEY)
                    .eq(!StringUtils.isEmpty(sysId), SYS_ID, sysId)
                    .eq(!StringUtils.isEmpty(menuId), UP_MENU_ID, menuId)
                    .orderByAsc(MENU_ORDER)
            );
        } else {
            // 菜单树全查询
            List<MenuVo> menuVos = getBaseMapper().queryMenuTreeBySysId(sysId);
            List<MenuVo> menuVosResContrs = getBaseMapper().queryMenuTreeResContrBySysId(sysId);
            menuVos.addAll(menuVosResContrs);
            return menuVos;
        }
        entityList.forEach(adminSmMenuEntity ->
                menuVoList.add(
                        GenericBuilder.of(MenuVo::new)
                                .with(MenuVo::setI18nKey, adminSmMenuEntity.getI18nKey())
                                .with(MenuVo::setMenuId, adminSmMenuEntity.getMenuId())
                                .with(MenuVo::setMenuName, adminSmMenuEntity.getMenuName())
                                .with(MenuVo::setUpMenuId, adminSmMenuEntity.getUpMenuId())
                                .with(MenuVo::setMenuOrder, adminSmMenuEntity.getMenuOrder())
                                .with(MenuVo::setMenuClassify, adminSmMenuEntity.getMenuClassify())
                                .with(MenuVo::setMenuTip, adminSmMenuEntity.getMenuTip())
                                .build()
                )
        );
        return menuVoList;

    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public MenuVo saveAdminSmMenu(AdminSmMenuEntity adminSmMenu) {

        Long menuNameCount = getBaseMapper().selectCount(new QueryWrapper<AdminSmMenuEntity>()
                .eq(MENU_CLASSIFY, adminSmMenu.getMenuClassify())
                .eq(StringUtils.nonEmpty(adminSmMenu.getMenuName()), MENU_NAME, adminSmMenu.getMenuName())
                .eq("deleted", 0)
        );

        if (menuNameCount > 0) {
            throw BizException.error(null, "50600001",
                    null, "菜单名：" + adminSmMenu.getMenuName() + "已存在！");
        }
        // 判断当前菜单的父菜单下，有多少子菜单
        List<AdminSmMenuEntity> adminSmMenuEntityList = getBaseMapper().selectList(new QueryWrapper<AdminSmMenuEntity>()
                .select(MENU_ID)
                .eq(UP_MENU_ID, adminSmMenu.getUpMenuId())
        );
        String sysId = OcaCommonInfoUtils.getUserSysId();
        String userId = SessionUtils.getUserId();
        String menuId = UUID.randomUUID().toString().replace("-", "");
        adminSmMenu.setMenuId(menuId);
        adminSmMenu.setSysId(sysId);
        adminSmMenu.setLastChgDt(DateUtils.getCurrDate());
        adminSmMenu.setLastChgUsr(userId);
        adminSmMenu.setMenuOrder(new AtomicInteger(5 * (adminSmMenuEntityList.size() + 1)).intValue());
        getBaseMapper().insert(adminSmMenu);

//        // 新增授权记录表
//        adminSmAuthRecoService.getBaseMapper().insert(
//                GenericBuilder.of(AdminSmAuthRecoEntity::new)
//                        .with(AdminSmAuthRecoEntity::setAuthRecoId, StringUtils.getUUID())
//                        .with(AdminSmAuthRecoEntity::setSysId, sysId)
//                        .with(AdminSmAuthRecoEntity::setAuthobjType, "R")
//                        .with(AdminSmAuthRecoEntity::setAuthobjId, "cc81a8d86f274c81bc680a0bbd27e358")
//                        .with(AdminSmAuthRecoEntity::setAuthresType, "M")
//                        .with(AdminSmAuthRecoEntity::setAuthresId, adminSmMenu.getMenuId())
//                        .with(AdminSmAuthRecoEntity::setMenuId, adminSmMenu.getMenuId())
//                        .build()
//        );

        //获取租户对应的超级管理用户id
        String authobjId=adminSmTenantUserRelService.getTenantUserId();

        // 新增授权记录表
        adminSmAuthRecoService.getBaseMapper().insert(
                GenericBuilder.of(AdminSmAuthRecoEntity::new)
                        .with(AdminSmAuthRecoEntity::setAuthRecoId, StringUtils.getUUID())
                        .with(AdminSmAuthRecoEntity::setSysId, sysId)
                        .with(AdminSmAuthRecoEntity::setAuthobjType, "U")
                        .with(AdminSmAuthRecoEntity::setAuthobjId, authobjId)
                        .with(AdminSmAuthRecoEntity::setAuthresType, "M")
                        .with(AdminSmAuthRecoEntity::setAuthresId, adminSmMenu.getMenuId())
                        .with(AdminSmAuthRecoEntity::setMenuId, adminSmMenu.getMenuId())
                        .build()
        );
        return GenericBuilder.of(MenuVo::new).with(MenuVo::setMenuId, menuId).build();
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateMenuById(AdminSmMenuEntity adminSmMenu) {

        Long menuNameCount = getBaseMapper().selectCount(new QueryWrapper<AdminSmMenuEntity>()
                .eq(MENU_CLASSIFY, adminSmMenu.getMenuClassify())
                .eq(StringUtils.nonEmpty(adminSmMenu.getMenuName()), MENU_NAME, adminSmMenu.getMenuName())
                .ne(StringUtils.nonEmpty(adminSmMenu.getMenuId()), MENU_ID, adminSmMenu.getMenuId())
        );
        if (menuNameCount > 0) {
            throw BizException.error(null, "50600001",
                    null, "菜单名:" + adminSmMenu.getMenuName() + "已存在！");
        }

        String userId = SessionUtils.getUserId();
        adminSmMenu.setLastChgDt(DateUtils.getCurrDate());
        adminSmMenu.setLastChgUsr(userId);
        getBaseMapper().updateById(adminSmMenu);

    }

    /**
     * 向下递归 menu菜单，删除菜单使用
     *
     * @param set      用来存储筛选到的数据
     * @param pid      菜单父ID
     * @param allMenus 所有菜单集合
     * @return void
     * @author zhanyq
     * @date 2021-06-24 16:29
     */
    private void getDownMenuChildren(HashSet<String> set, String pid, List<AdminSmMenuEntity> allMenus) {

        List<AdminSmMenuEntity> children = allMenus.stream().filter(menu -> menu.getUpMenuId().equals(pid)).collect(Collectors.toList());
        children.forEach(child -> {
            set.add(child.getMenuId());
            getDownMenuChildren(set, child.getMenuId(), allMenus);
        });
    }

    @Override
    public MenuVo getMenuInfo(String menuId) {
        return getBaseMapper().getMenuInfoByMenuId(menuId);
    }

    @Override
    public List<MenuVo> queryTreeMenuList(String sysId, String pid, String treeDeep) {

        AtomicInteger atomicInteger = new AtomicInteger(0);

        // 参数判断
        if (StringUtils.isEmpty(sysId)) {
            sysId = OcaCommonInfoUtils.getUserSysId();
        }
        if (StringUtils.isEmpty(pid)) {
            pid = "-1";
        }
        // 树的递归深度，默认树递归深度5级
        int treeDeepInt = !("-1").equals(treeDeep) ? 5 : Integer.MAX_VALUE;


        List<AdminSmMenuEntity> adminSmMenuList = getBaseMapper().selectList(new QueryWrapper<AdminSmMenuEntity>()
                .select(MENU_ID, FUNC_ID, UP_MENU_ID, MENU_NAME, MENU_ORDER, "menu_icon",
                        "menu_tip", I_18_N_KEY, MENU_CLASSIFY)
                .eq(SYS_ID, sysId));
        // 如果查询菜单list为空
        if (CollectionUtils.isEmpty(adminSmMenuList)) {
            return null;
        }
        return getChildrenList(pid, adminSmMenuList, treeDeepInt, atomicInteger);
    }


    /**
     * 递归查找传入菜单的子菜单，拼装成子菜单树结构
     *
     * @param pid           上级菜单ID
     * @param all           所有菜单集合
     * @param treeDeep      递归深度
     * @param atomicInteger 当前递归层级
     * @return 所有子菜单树形集合
     * @author zhanyq
     * @date 2021-06-24 16:35
     */
    private List<MenuVo> getChildrenList(String pid, List<AdminSmMenuEntity> all, int treeDeep, AtomicInteger atomicInteger) {
        // 原子+1
        int cirVar = atomicInteger.incrementAndGet();

        return all.stream()
                .filter(mevuEntity -> !StringUtils.isEmpty(mevuEntity.getUpMenuId())
                        && mevuEntity.getUpMenuId().equals(pid) && cirVar < treeDeep)
                .map(menuEntity -> {
                    // 1. 递归查找子菜单
                    return GenericBuilder.of(MenuVo::new)
                            .with(MenuVo::setMenuId, menuEntity.getMenuId())
                            .with(MenuVo::setUpMenuId, menuEntity.getUpMenuId())
                            .with(MenuVo::setMenuName, menuEntity.getMenuName())
                            .with(MenuVo::setMenuOrder, menuEntity.getMenuOrder())
                            .with(MenuVo::setFuncId, menuEntity.getFuncId())
                            .with(MenuVo::setMenuIcon, menuEntity.getMenuIcon())
                            .with(MenuVo::setMenuTip, menuEntity.getMenuTip())
                            .with(MenuVo::setMenuClassify, menuEntity.getMenuClassify())
                            .with(MenuVo::setI18nKey, menuEntity.getI18nKey())
                            .with(MenuVo::setChildrenList, getChildrenList(menuEntity.getMenuId(), all, treeDeep, atomicInteger))
                            .build();
                })
                .sorted(Comparator.comparing(MenuVo::getMenuOrder))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuVo> querySearchTree(String sysId, String menuName) {

        // 参数判断
        if (StringUtils.isEmpty(sysId)) {
            sysId = OcaCommonInfoUtils.getUserSysId();
        }
        // 菜单列表
        List<AdminSmMenuEntity> allSmMenuList = getBaseMapper().selectList(new QueryWrapper<AdminSmMenuEntity>()
                .select(MENU_ID, FUNC_ID, UP_MENU_ID, MENU_NAME, MENU_ORDER, "menu_icon",
                        "menu_tip", I_18_N_KEY, MENU_CLASSIFY)
                .eq(SYS_ID, sysId));
        List<Object> filterMenuIdList = getBaseMapper().selectObjs(new QueryWrapper<AdminSmMenuEntity>()
                .select(MENU_ID)
                .eq(SYS_ID, sysId)
                .like(MENU_NAME, menuName));
        // 拼装搜索树
        return querySearchTreeByMenuName(allSmMenuList, filterMenuIdList);
    }

    /**
     * 搜索菜单树,list-->tree
     *
     * @param allMenuList    所有菜单集合
     * @param filterMenuList 过滤条件
     * @return 搜索后的树形菜单数据集合
     * @author zhanyq
     * @date 2021-06-24 16:41
     */
    private List<MenuVo> querySearchTreeByMenuName(List<AdminSmMenuEntity> allMenuList, List<Object> filterMenuList) {

        List<AdminSmMenuEntity> menulist = new ArrayList<>();
        ArrayList<AdminSmMenuEntity> objects = new ArrayList<>(filterMenuList.size());
        filterMenuList.forEach(menuStr -> menulist.addAll(getUpMenuTree(objects, (String) menuStr, allMenuList)));
        // 去重
        List<AdminSmMenuEntity> menuDistinctlist = menulist.parallelStream().distinct().collect(Collectors.toList());
        // list to tree
        return getChildrenList("-1", menuDistinctlist, Integer.MAX_VALUE, new AtomicInteger(0));
    }

    /**
     * 搜索菜单树-查询父菜单list-递归
     *
     * @param menus    存储匹配的福彩但信息
     * @param menuId   菜单ID
     * @param allMenus 所有菜单集合
     * @return 父菜单集合
     * @author zhanyq
     * @date 2021-06-24 16:49
     */
    private List<AdminSmMenuEntity> getUpMenuTree(ArrayList<AdminSmMenuEntity> menus, String menuId,
                                                  List<AdminSmMenuEntity> allMenus) {
        allMenus.stream().filter(menu -> menu.getMenuId().equals(menuId))
                .forEach(menuEntity -> {
                    menus.add(GenericBuilder.of(AdminSmMenuEntity::new)
                            .with(AdminSmMenuEntity::setMenuId, menuEntity.getMenuId())
                            .with(AdminSmMenuEntity::setUpMenuId, menuEntity.getUpMenuId())
                            .with(AdminSmMenuEntity::setMenuName, menuEntity.getMenuName())
                            .with(AdminSmMenuEntity::setMenuOrder, menuEntity.getMenuOrder())
                            .with(AdminSmMenuEntity::setFuncId, menuEntity.getFuncId())
                            .with(AdminSmMenuEntity::setMenuIcon, menuEntity.getMenuIcon())
                            .with(AdminSmMenuEntity::setMenuTip, menuEntity.getMenuTip())
                            .with(AdminSmMenuEntity::setMenuClassify, menuEntity.getMenuClassify())
                            .with(AdminSmMenuEntity::setI18nKey, menuEntity.getI18nKey())
                            .build());
                    // 递归查询父菜单
                    getUpMenuTree(menus, menuEntity.getUpMenuId(), allMenus);
                });

        return menus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int updateAllMenuTree(List<MenuVo> menuVoList) {

        AtomicInteger atomicInteger = new AtomicInteger(0);
        LinkedList<AdminSmMenuEntity> adminSmMenuList = new LinkedList<>();

        menuVoList.forEach(menuVo -> {
            atomicInteger.getAndAdd(5);
            adminSmMenuList.addAll(getChildrenList(adminSmMenuList, menuVo, atomicInteger));
        });
        // 去重
        List<AdminSmMenuEntity> menuList = adminSmMenuList.parallelStream().distinct().collect(Collectors.toList());
        // 批量更新
        int batchMenuTree = getBaseMapper().updateBatchMenuTree(menuList);
        if (batchMenuTree > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<AdminSmAuthTreeVo> selectAuthTree(String id, String dataTenantId) {
        return getBaseMapper().selectAuthTree(id, dataTenantId);
    }

    /**
     * 递归查询子菜单-筛选对象是树形数据结构
     *
     * @param adminSmMenuList 存储筛选后的子菜单
     * @param menuVo          要筛选的菜单树
     * @param atomicInteger   当前递归层级
     * @return 筛选后的子菜单集合
     * @author zhanyq
     * @date 2021-06-24 16:53
     */
    private List<AdminSmMenuEntity> getChildrenList(List<AdminSmMenuEntity> adminSmMenuList,
                                                    MenuVo menuVo, AtomicInteger atomicInteger) {
        // 添加menu
        String sysId = OcaCommonInfoUtils.getUserSysId();
        adminSmMenuList.add(GenericBuilder.of(AdminSmMenuEntity::new)
                .with(AdminSmMenuEntity::setMenuId, menuVo.getMenuId())
                .with(AdminSmMenuEntity::setSysId, sysId)
                .with(AdminSmMenuEntity::setUpMenuId, menuVo.getUpMenuId())
                .with(AdminSmMenuEntity::setMenuOrder, atomicInteger.intValue())
                .with(AdminSmMenuEntity::setLastChgDt, DateUtils.getCurrDate())
                .with(AdminSmMenuEntity::setLastChgUsr, SessionUtils.getUserId())
                .build()
        );
        // children list 为null,直接返回menuList
        if (CollectionUtils.isEmpty(menuVo.getChildrenList())) {
            return null;
        }
        // children list不为null,递归查询
        List<MenuVo> menuChildrenList = menuVo.getChildrenList();
        AtomicInteger atomicInt = new AtomicInteger(0);
        menuChildrenList.forEach(menuVo1 -> {
            atomicInt.getAndAdd(5);
            getChildrenList(adminSmMenuList, menuVo1, atomicInt);
        });

        return adminSmMenuList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int updateMenuTreeByDragMenu(DragMenuBo dragMenuBo) {
        // 查询拖动菜单
        List<String> menuIds = Arrays.asList(dragMenuBo.getDragMenuId(), dragMenuBo.getRefMenuId());
        List<AdminSmMenuEntity> dragMenuList = getBaseMapper().selectBatchIds(menuIds);
        if (dragMenuList.size() != Constants.AdminSmMenuConstance.DRUG_MENU_SIZE) {
            return -1;
        }
        // 菜单对象赋值
        AdminSmMenuEntity dragMenu;
        AdminSmMenuEntity refMenu;
        if (dragMenuList.get(0).getMenuId().matches(dragMenuBo.getDragMenuId())) {
            dragMenu = dragMenuList.get(0);
            refMenu = dragMenuList.get(1);
        } else {
            dragMenu = dragMenuList.get(1);
            refMenu = dragMenuList.get(0);

        }
        // 查询拖动父菜单下的所有菜单
        String upMenuId = dragMenuList.get(0).getUpMenuId();
        List<AdminSmMenuEntity> menuList = getBaseMapper().selectList(new QueryWrapper<AdminSmMenuEntity>()
                .in(StringUtils.nonEmpty(upMenuId), UP_MENU_ID, upMenuId));
        List<AdminSmMenuEntity> sortedMenus = this.sortDragMenuList(dragMenu, refMenu, menuList, dragMenuBo);
        // 批量修改
        int updateBatch = getBaseMapper().updateBatchMenuTree(sortedMenus);
        if (updateBatch > 0) {
            return 1;
        }
        return -1;
    }


    @Override
    public Page<AdminSmMenuVo> getMenuListForContr(AdminSmMenuConditionForm form) {
        String keyWord = form.getKeyWord();
        Page<AdminSmMenuVo> page = new Page<>(form.getPage(), form.getSize());
        QueryWrapper<AdminSmMenuVo> wrapper = new QueryWrapper<>();
        wrapper.eq(form.getCheck() == 1, "m1.menu_id", form.getLastMenuId());
        wrapper.eq("m1.menu_classify", "0");
        wrapper.eq("m1.deleted", 0);
        wrapper.and(!StringUtils.isEmpty(keyWord) && form.getCheck() != 1,
                w -> w.like("m1.menu_name", keyWord).or().like("m2.menu_name", keyWord));
        return getBaseMapper().getMenuListForContr(page, wrapper, form.getLastMenuId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteMenuByIds(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            HashSet<String> idSet =(HashSet<String>) SetUtils.newHashSet(ids);
            // 批量删除菜单
            getBaseMapper().deleteBatchIds(idSet);
            // 批量删除菜单相关授权记录
            adminSmAuthRecoService.deleteMenuAuthData(idSet);
        }
    }

    @Deprecated
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int removeMenuByIds(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            String sysId = OcaCommonInfoUtils.getUserSysId();
            // 查询菜单表中所有菜单
            List<AdminSmMenuEntity> allMenuList = getBaseMapper().selectList(new QueryWrapper<AdminSmMenuEntity>()
                    .select(MENU_ID, FUNC_ID, UP_MENU_ID)
                    .eq(SYS_ID, sysId));
            // 获取要删除菜单
            List<AdminSmMenuEntity> selMenuList = allMenuList.stream().filter(menu ->
                    Arrays.asList(ids).contains(menu.getMenuId())
            ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(selMenuList)) {
                throw BizException.error(null, "50600002",
                        null, "未查询到菜单，所选菜单或已被删除，请检查！");
            }

            // 根据menuId向下递归查询list
            HashSet<String> delMenuIdHash = new HashSet<>();
            int deleteBatchIds;
            int authresId;
            selMenuList
                    .forEach(menu -> {
                        delMenuIdHash.add(menu.getMenuId());
                        getDownMenuChildren(delMenuIdHash, menu.getMenuId(), allMenuList);
                    });
            // 批量删除菜单
            deleteBatchIds = getBaseMapper().deleteBatchIds(delMenuIdHash);
            // 批量删除菜单相关授权记录
            authresId = adminSmAuthRecoService.deleteMenuAuthData(delMenuIdHash);
            if (deleteBatchIds > 0 && authresId > 0) {
                return 1;
            }
            return 0;
        }
        return 0;
    }

    /**
     * 菜单拖拽排序
     *
     * @param dragMenu   被拖拽的菜单
     * @param refMenu    关联菜单
     * @param menuList   所有菜单集合
     * @param dragMenuBo 菜单拖拽实体
     * @return 排序后的菜单集合
     * @author zhanyq
     * @date 2021-06-24 17:01
     */
    private List<AdminSmMenuEntity> sortDragMenuList(AdminSmMenuEntity dragMenu, AdminSmMenuEntity refMenu,
                                                     List<AdminSmMenuEntity> menuList, DragMenuBo dragMenuBo) {

        // 删除拖动的菜单
        menuList.remove(dragMenu);
        // 排序
        List<AdminSmMenuEntity> sortedMenus = menuList.stream().sorted(Comparator.comparing(AdminSmMenuEntity::getMenuOrder))
                .collect(Collectors.toList());
        // 确定指定元素的索引
        int indexRefMenuOfList = sortedMenus.indexOf(refMenu);
        if (indexRefMenuOfList == -1) {
            return null;
        }
        // 参照元素 前插入
        if (Constants.AdminSmMenuConstance.MENU_ORDER_BEFORE.equals(dragMenuBo.getMenuOrder())) {
            sortedMenus.add(indexRefMenuOfList, dragMenu);
        } else {
            // 参照元素 后插入
            sortedMenus.add(indexRefMenuOfList + 1, dragMenu);
        }
        AtomicInteger atomicInt = new AtomicInteger(0);
        // 重新赋值 menu order
        return sortedMenus.stream().peek(menu -> {
            menu.setLastChgUsr(SessionUtils.getUserId());
            menu.setMenuOrder(atomicInt.getAndAdd(5));
            menu.setLastChgDt(DateUtils.getCurrDate());
        }).collect(Collectors.toList());
    }

}