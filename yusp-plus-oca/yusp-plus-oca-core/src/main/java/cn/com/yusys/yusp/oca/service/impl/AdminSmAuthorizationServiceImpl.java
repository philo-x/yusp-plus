package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.controller.AdminMenuAndContrController;
import cn.com.yusys.yusp.oca.dao.AdminSmMenuDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantUserRelEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthTreeForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthUpdateForm;
import cn.com.yusys.yusp.oca.domain.form.AdminTmplAuthForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrAuthVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminTmplAndRecoVo;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.service.cache.AuthTreeService;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
/**
 * 数据权限模板表
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@Service("adminSmAuthorizationService")
public class AdminSmAuthorizationServiceImpl implements AdminSmAuthorizationService {

    @Autowired
    @Qualifier("authTreeNoCacheService")
    private AuthTreeService authTreeService;
    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;
    @Autowired
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;
    @Autowired
    private AdminSmResContrService adminSmResContrService;

    @Autowired
    private AdminSmMenuDao adminSmMenuDao;

    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;


    @Autowired
    private AdminSmTenantService adminSmTenantService;


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminMenuAndContrController.class);

    private static final String TOP_MENU_ID = "0";
    private static final String MENU_CODE = "M";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAuth(AdminSmAuthUpdateForm adminSmAuthUpdateForm) {

        // authObjId：被授权人Id
        // authFormList：被授权人新权限
        List<AdminSmAuthTreeVo> authFormList = adminSmAuthUpdateForm.getAuthFormList();
        if (authFormList == null || authFormList.isEmpty()) {
            return;
        }

        // 按照 state 属性分组：
        // state为 0 表示之前的权限被取消，state为 1 表示新增加的授权获取
        Map<Integer, List<AdminSmAuthTreeVo>> authGroup = authFormList.stream().collect(Collectors.groupingBy(AdminSmAuthTreeVo::getState, Collectors.toList()));
        Set<AdminSmAuthRecoEntity> entitySet = new HashSet<>();
        List<AdminSmAuthTreeVo> deleteAuthList = authGroup.get(0);

        // 批量删除授权记录
        if (deleteAuthList != null && !deleteAuthList.isEmpty()) {
            //如果是admin删除租户的数据，那么就会删除租户下用户的所有数据
            String tanentId = adminSmAuthUpdateForm.getDataTenantId();
            if (StringUtils.isBlank(tanentId)) {
                tanentId = adminSmTenantService.getTanentId();
            }
            AdminSmTenantUserRelEntity tenantUserRelInfo = adminSmTenantUserRelService.getTenantUserRelInfo(tanentId);
            if ("40".equals(SessionUtils.getUserId()) && tenantUserRelInfo.getUserId().equals(adminSmAuthUpdateForm.getAuthObjId())) {
                List<String> authresIds = deleteAuthList.stream().map(AdminSmAuthTreeVo::getAuthresId).collect(Collectors.toList());
                adminSmAuthRecoService.deleteByAuthresAndTenant(authresIds, tanentId);
            } else {
                List<String> authresIdList = deleteAuthList.stream().map(AdminSmAuthTreeVo::getAuthRecoId).collect(Collectors.toList());
                //如果删除的是租户对应的超级管理员的功能菜单，那么这个租户下的所有用户都需要删除这些菜单
                adminSmAuthRecoService.deleteByList(authresIdList);
            }

        }

        // insertAuthList：新增授权
        List<AdminSmAuthTreeVo> insertAuthList = authGroup.get(1);
        if (insertAuthList == null || insertAuthList.isEmpty()) {
            return;
        }
        insertAuthList.forEach(vo -> {
            AdminSmAuthRecoEntity saveEntity = getAdminSmAuthRecoEntity(vo, adminSmAuthUpdateForm);
            if(StringUtils.nonEmpty(adminSmAuthUpdateForm.getDataTenantId())) {
                saveEntity.setDataTenantId(adminSmAuthUpdateForm.getDataTenantId());
            }
            entitySet.add(saveEntity);
        });
        // 批量保存新增授权
        adminSmAuthRecoService.saveBatch(entitySet);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyAuth(AdminSmAuthUpdateForm adminSmAuthUpdateForm) {
        String copyForAuthObjIds = adminSmAuthUpdateForm.getCopyForAuthObjIds();
        if (StringUtils.isEmpty(copyForAuthObjIds) || adminSmAuthUpdateForm.getAuthFormList() == null) {
            return;
        }
        String[] ids = copyForAuthObjIds.split(",");
        // 删除被拷贝角色或用户原有的权限
        adminSmAuthRecoService.deleteByAuthObjIds(ids);
        // 将数据转换为 entity 集合
        Set<AdminSmAuthRecoEntity> entityList = new HashSet<>();
        List<AdminSmAuthTreeVo> authFormList = adminSmAuthUpdateForm.getAuthFormList();
        authFormList.forEach(treeVo -> {
            // 循环 ids，添加不同 authresId 的 entity 到新增列表中
            for (String id : ids) {
                AdminSmAuthRecoEntity entity = getAdminSmAuthRecoEntity(treeVo, adminSmAuthUpdateForm);
                // 如果是权限模板，menuId为上级菜单id，不是上级控制点id
                if ("D".equals(entity.getAuthresType())) {
                    AdminSmAuthTreeVo vo = new AdminSmAuthTreeVo();
                    vo.setAuthresId(treeVo.getUpTreeId());
                    entity.setMenuId(authFormList.get(authFormList.indexOf(vo)).getUpTreeId());
                }
                entity.setAuthRecoId(StringUtils.getUUID());
                entity.setAuthobjId(id);
                entityList.add(entity);
            }
        });
        // 批量保存复制拷贝授权
        adminSmAuthRecoService.saveBatch(entityList);
    }


    @Override
    public IPage<AdminSmResContrAuthVo> getAuthTmplList(AdminSmAuthTreeForm form) {
        String authObjId = form.getAuthObjId();
        // 1、分页查询被授权用户的控制点
        IPage<AdminSmAuthTreeVo> resContrPage = adminSmResContrService.selectAuthTreePage(authObjId, form.getKeyWord(),form.getDataTenantId(), form.getPage(), form.getSize());
        log.info("数据权限模板获取到的权限树数据: {}", resContrPage.getRecords());
        IPage<AdminSmResContrAuthVo> voPage = new Page<>(form.getPage(), form.getSize());
        if (resContrPage.getRecords() != null && !resContrPage.getRecords().isEmpty()) {
            // 2、组装返回数据
            voPage.setRecords(getResContrAuthVoList(authObjId, form.getDataTenantId(), resContrPage.getRecords()));
            return voPage.setTotal(resContrPage.getTotal());
        }
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTmplAuth(AdminTmplAuthForm form) {

        if (!StringUtils.isEmpty(form.getLastAuthresId())) {
            // 1、删除之前的数据模板授权记录
            adminSmAuthRecoService.removeByMenuIdAndAuthobjId(form);
        }
        // 2、新增授权记录
        if (!StringUtils.isEmpty(form.getAuthresId())) {
            AdminSmAuthRecoEntity authRecoEntity = BeanUtils.beanCopy(form, AdminSmAuthRecoEntity.class);
            log.info("2、新增授权记录，authRecoEntity：{}", authRecoEntity);
            if(StringUtils.nonBlank(form.getDataTenantId())){
                authRecoEntity.setDataTenantId(form.getDataTenantId());
            }
            authRecoEntity.setAuthRecoId(StringUtils.getUUID());
            authRecoEntity.setMenuId(form.getContrId());
            authRecoEntity.setAuthresType("D");
            authRecoEntity.setLastChgDt(new Date());
            authRecoEntity.setLastChgUsr(SessionUtils.getUserId());
            authRecoEntity.setSysId(SessionUtils.getClientId());
            adminSmAuthRecoService.save(authRecoEntity);
        }
    }

    @Override
    public List<AdminSmAuthTreeVo> getAuthedMenuTree(AdminSmAuthTreeForm adminSmAuthTreeForm) {
        String authObjId = adminSmAuthTreeForm.getAuthObjId();
        // 获取被授权人已经授权的对象
        List<AdminSmAuthTreeVo> menuList = authTreeService.getUserAuthMenuData(authObjId,adminSmAuthTreeForm.getDataTenantId());
        // 组装树结构
        return this.composeAuthedTreeData(menuList);
    }

    /**
     * 已授权的菜单树组装
     * @param authData 被授权人已经授权的数据
     * @return 组装返回的树结构
     */
    private List<AdminSmAuthTreeVo> composeAuthedTreeData(List<AdminSmAuthTreeVo> authData) {



        // 将所有的树结构数据按照 upTreeId 进行分组
        Map<String, List<AdminSmAuthTreeVo>> userGroup = authData.stream()
                .collect(Collectors.groupingBy(AdminSmAuthTreeVo::getUpTreeId, Collectors.toList()));

        // 设置虚构顶级树节点，该树节点 nodeId 为 "0"
        AdminSmAuthTreeVo topVo = new AdminSmAuthTreeVo();
        topVo.setAuthresId("0");

        // 使用递归进行数据的组装
        this.composeMenuTreeData(userGroup, topVo);

        // 树结构排序
        return treeSort(topVo.getChildren());
    }

    private List<AdminSmResContrAuthVo> getResContrAuthVoList(String authObjId, String dataTenantId, List<AdminSmAuthTreeVo> authContrData) {
        List<AdminSmResContrAuthVo> resContrAuthVoList = new ArrayList<>();
        // 1、获取已授权菜单
        List<AdminSmAuthTreeVo> authMenuData = authTreeService.getUserAuthMenuData(authObjId,dataTenantId);
        // 2、获取已授权的数据模板
        List<String> contrIdList = authContrData.stream().map(AdminSmAuthTreeVo::getAuthresId).collect(Collectors.toList());
        List<AdminTmplAndRecoVo> tmplAndRecoVoList = adminSmDataAuthTmplService.getTmplAndRecoVoList(contrIdList, authObjId, dataTenantId);
        log.info("2、获取已授权的数据模板 : {}", tmplAndRecoVoList);
        // 3、拼装返回数据
        authContrData.forEach(resContr -> {
            AdminSmResContrAuthVo resContrAuthVo = new AdminSmResContrAuthVo();
            resContrAuthVo.setResContrId(resContr.getAuthresId());
            resContrAuthVo.setUpMenuId(resContr.getUpTreeId());
            List<String> pathList = new ArrayList<>();
            pathList.add(resContr.getNodeName());

            // 拼装 AdminSmResContrAuthVo 返回对象
            assemblyResContr(resContrAuthVo, authMenuData, pathList);

            // 4、拼装 authTmplVoList 数据
            if (tmplAndRecoVoList != null && !tmplAndRecoVoList.isEmpty()) {
                for (AdminTmplAndRecoVo vo : tmplAndRecoVoList) {
                    log.info("vo.getContrId() : {}", vo.getContrId());
                    log.info("resContrAuthVo.getResContrId() : {}", resContrAuthVo.getResContrId());
                    if (vo.getContrId().equals(resContrAuthVo.getResContrId())) {
                        resContrAuthVo.setTmplAndRecoVo(vo);
                    }
                }
            }
            resContrAuthVoList.add(resContrAuthVo);
        });

        log.info("7、组装数据模板授权页面的数据 :" );
        return resContrAuthVoList;
    }


    private void assemblyResContr(AdminSmResContrAuthVo resContrAuthVo, List<AdminSmAuthTreeVo> authMenuData, List<String> pathList) {
        Map<String, AdminSmAuthTreeVo> menuDataMap = new HashMap<>(8);
        for (AdminSmAuthTreeVo adminSmAuthTreeVo:authMenuData) {
            String authoressId=adminSmAuthTreeVo.getAuthresId();
            if(menuDataMap.containsKey(authoressId)){
                continue;
            }else{
                menuDataMap.put(authoressId,adminSmAuthTreeVo);
            }
        }
        AdminSmAuthTreeVo authTreeVo;
        String errorMsg = "控制点相关菜单已被删除或未授权！\n({})";

        while(!TOP_MENU_ID.equals(resContrAuthVo.getUpMenuId())) {
            if (!menuDataMap.containsKey(resContrAuthVo.getUpMenuId())) {
                String replace = errorMsg.replace("{}", "【控制点id=" + resContrAuthVo.getResContrId() + "，菜单id=" + resContrAuthVo.getUpMenuId() + "】");
                resContrAuthVo.setMenuPath(replace);
                break ;
            } else {
                authTreeVo = menuDataMap.get(resContrAuthVo.getUpMenuId());
                resContrAuthVo.setUpMenuId(authTreeVo.getUpTreeId());
                pathList.add(authTreeVo.getNodeName() + " / ");
            }
            if (TOP_MENU_ID.equals(resContrAuthVo.getUpMenuId())) {
                Collections.reverse(pathList);
                StringBuilder stringBuffer = new StringBuilder();

                for(int i=0 ; i<pathList.size()-1 ; i++){
                    stringBuffer.append(pathList.get(i));
                }
                stringBuffer.deleteCharAt(stringBuffer.length()-1);
                resContrAuthVo.setMenuPath(stringBuffer.toString());
                resContrAuthVo.setCornName(pathList.get(pathList.size()-1));
                log.info("方法：assemblyResContr 中拼装的结果：{}", resContrAuthVo);
            }
        }
    }

    private AdminSmAuthRecoEntity getAdminSmAuthRecoEntity(AdminSmAuthTreeVo vo,AdminSmAuthUpdateForm form) {
        AdminSmAuthRecoEntity entity = new AdminSmAuthRecoEntity();
        if (vo.getState() == 1) {
            entity.setAuthRecoId(StringUtils.getUUID());
        } else {
            entity.setAuthRecoId(vo.getAuthRecoId());
        }
        String sysId = OcaCommonInfoUtils.getUserSysId();
        entity.setSysId(sysId);
        entity.setLastChgUsr(SessionUtils.getUserId());
        entity.setAuthresType(vo.getAuthresType());
        entity.setAuthresId(vo.getAuthresId());
        entity.setAuthobjType(form.getAuthObjType());
        entity.setAuthobjId(form.getAuthObjId());
        entity.setMenuId(vo.getUpTreeId());
        return entity;
    }

    @Override
    public List<AdminSmAuthTreeVo> getTreeQuery(AdminSmAuthTreeForm adminSmAuthTreeForm) {
        String userId = SessionUtils.getUserId();
        String authObjId = adminSmAuthTreeForm.getAuthObjId();
        String userRoleId = adminSmAuthTreeForm.getUserRoleId();

        // 获取授权人能够授权的所有数据
        List<List<AdminSmAuthTreeVo>> userData = getTreeData(null,userId, userRoleId);
        if(CollectionUtils.isEmpty(userData)){
            return null;
        }

        // 获取被授权人已经授权的对象
        List<List<AdminSmAuthTreeVo>> authorizedData = getTreeData(adminSmAuthTreeForm.getDataTenantId(),authObjId);

        // 组装树结构
        return this.composeTreeData(userData, authorizedData);
    }



    /**
     * 树结构组装前期准备
     * @param userData 授权人能够授权的所有数据
     * @param authData 被授权人已经授权的数据
     * @return 组装返回的树结构
     */
    private List<AdminSmAuthTreeVo> composeTreeData(List<List<AdminSmAuthTreeVo>> userData, List<List<AdminSmAuthTreeVo>> authData) {

        List<AdminSmAuthTreeVo> treeVoList = new ArrayList<>();

        // 整合授权人数据和被授权人数据
        for (int i = 0,len=userData.size(); i < len; i++) {
            List<AdminSmAuthTreeVo> userList = userData.get(i);
            List<AdminSmAuthTreeVo> authList = authData.get(i);
            for (AdminSmAuthTreeVo vo : userList) {
                // 分组时parentId不能为null.
                if (StringUtils.isEmpty(vo.getUpTreeId())) {
                    vo.setUpTreeId("*");
                }

                // 如果已有该权限，将state设置为 1，否则默认为 0
                if (authList != null && authList.contains(vo)) {
                    vo.setState(1);

                    // 修改当前节点的记录id为被授权人的记录id
                    vo.setAuthRecoId(authList.get(authList.indexOf(vo)).getAuthRecoId());
                }
            }
            treeVoList.addAll(userList);
        }

        // 将所有的树结构数据按照 upTreeId 进行分组
        Map<String, List<AdminSmAuthTreeVo>> userGroup = treeVoList.stream()
                .collect(Collectors.groupingBy(AdminSmAuthTreeVo::getUpTreeId, Collectors.toList()));

        // 设置虚构顶级树节点，该树节点 nodeId 为 "0"
        AdminSmAuthTreeVo topVo = new AdminSmAuthTreeVo();
        topVo.setAuthresId("0");

        // 使用递归进行数据的组装
        this.composeMenuTreeData(userGroup, topVo);

        // 树结构排序
        return treeSort(topVo.getChildren());
    }


    private List<AdminSmAuthTreeVo> treeSort(List<AdminSmAuthTreeVo> treeList) {
        if (treeList == null || treeList.isEmpty()) {
            return null;
        }
        treeList.forEach(vo -> {
            if (vo.getChildren() != null) {
                // 树结构排序方法
                List<AdminSmAuthTreeVo> childrenList = treeSort(vo.getChildren());
                vo.setChildren(childrenList);
            }
        });
        if (MENU_CODE.equals(treeList.get(0).getAuthresType())) {
            treeList.sort(Comparator.comparing(AdminSmAuthTreeVo::getOrderColumn));
        } else {
            treeList.sort(Comparator.comparing(AdminSmAuthTreeVo::getAuthresId));
        }
        return treeList;
    }


    private void composeMenuTreeData(Map<String, List<AdminSmAuthTreeVo>> menuGroup, AdminSmAuthTreeVo topVo) {

        // 如果该节点拥有子节点，遍历所有子节点，设置子节点的 children 属性
        // 如果没有子节点，就返回该节点自己
        if (menuGroup.containsKey(topVo.getAuthresId())) {
            List<AdminSmAuthTreeVo> treeVoList = menuGroup.get(topVo.getAuthresId());
            treeVoList.forEach(vo -> composeMenuTreeData(menuGroup, vo));
            topVo.setChildren(treeVoList);
        }
    }

    private List<List<AdminSmAuthTreeVo>> getTreeData(String dataTenantId, String... ids) {
        if (ids.length <= 0) {
            return new ArrayList<>();
        }

        List<List<AdminSmAuthTreeVo>> list = new ArrayList<>();
        List<AdminSmAuthTreeVo> menuVoList = new ArrayList<>();
        List<AdminSmAuthTreeVo> contrVoList = new ArrayList<>();

        for (String id : ids) {
            menuVoList.addAll(authTreeService.getUserAuthMenuData(id,dataTenantId));
        }

        //查询出不能授权的页面
        List<String> excludeMenuId = adminSmMenuDao.getExcludeMenuId();

        menuVoList = menuVoList.stream().filter(menu -> !excludeMenuId.contains(menu.getAuthresId())).collect(collectingAndThen(toCollection(() ->
                new TreeSet<>(Comparator.comparing(AdminSmAuthTreeVo::getAuthresId))), ArrayList::new));

        list.add(menuVoList);

        for (String id : ids) {
            contrVoList.addAll(authTreeService.getUserAuthContrData(id,dataTenantId));
        }
        contrVoList = contrVoList.stream().collect(collectingAndThen(toCollection(() ->
                new TreeSet<>(Comparator.comparing(AdminSmAuthTreeVo::getAuthresId))), ArrayList::new));
        list.add(contrVoList);

        return list;
    }
}
