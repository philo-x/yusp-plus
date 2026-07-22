package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.bo.DragMenuBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMenuEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmBusiFuncQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * 系统菜单表
 *
 * @author wujp4
 * @date 2020-11-24 11:02:41
 */
@RestController
@RequestMapping("/api/adminsmmenu")
public class AdminSmMenuController {

    private static final String SYS_ID = "sysId";

    @Autowired
    private AdminSmMenuService adminSmMenuService;
    @Autowired
    private AdminSmBusiFuncService adminSmBusiFuncService;


    /**
     * 递归查询菜单树列表
     *
     * @param req    逻辑系统ID
     * @return 菜单树过节列表
     * @author zhanyq
     * @date 2021-06-24 15:09
     */
    @PostMapping("/tree")
    public JClientRspEntity<List<MenuVo>> treeList(@RequestBody JClientReqEntity<Map<String,String>> req){
        Map<String,String> params = req.getBody();
        List<MenuVo> menuVos = adminSmMenuService.queryTreeMenuList(params.get(SYS_ID), params.get("upMenuId"), params.get("treeDeep"));
        return JClientRspEntity.buildSuccess(menuVos);
    }


    /**
     * 搜索菜单树
     *
     * @param req
     * @return 搜索结果数据
     * @author zhanyq
     * @date 2021-06-24 15:11
     */
    @PostMapping("/searchtree")
    public JClientRspEntity<List<MenuVo>> treeSearchList(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        List<MenuVo> menuVos = adminSmMenuService.querySearchTree(params.get(SYS_ID), params.get("menuName"));
        return JClientRspEntity.buildSuccess(menuVos);
    }

    /**
     * 菜单列表分页查询
     *
     * @param params 查询参数
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-24 15:12
     */
    @PostMapping("/list")
    public JClientRspEntity<PageUtils> list(@RequestBody JClientReqEntity<Map<String, Object>> params) {
        PageUtils page = adminSmMenuService.queryPage(params.getBody());
        return JClientRspEntity.buildSuccess(page);
    }


    /**
     * 菜单树初始化查询
     *
     * @param req
     * @return 菜单树查询结果数据
     * @author zhanyq
     * @date 2021-06-24 15:14
     */
    @PostMapping("/menutreequery")
    public JClientRspEntity<List<MenuVo>> getMenuTree(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        List<MenuVo> list = adminSmMenuService.getMenuTree(params.get(SYS_ID), Boolean.parseBoolean(params.get("lazy")), params.get("menuId"));
        return JClientRspEntity.buildSuccess(list);
    }


    /**
     * 菜单详情查询
     *
     * @param menuId 菜单ID
     * @return 菜单详情数据
     * @author zhanyq
     * @date 2021-06-24 15:16
     */
    @PostMapping("/info/{menuId}")
    public JClientRspEntity<AdminSmMenuEntity> info(@PathVariable String menuId) {

        return JClientRspEntity.buildSuccess(adminSmMenuService.getById(menuId));
    }

    /**
     * 菜单数据保存
     *
     * @param req 要保存的菜单数据
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:18
     */
    @PostMapping("/createmenu")
    public JClientRspEntity<MenuVo> save(@Validated @RequestBody JClientReqEntity<AdminSmMenuEntity> req) {
        AdminSmMenuEntity adminSmMenu = req.getBody();
        adminSmMenu.setMenuId(StringUtils.getUUID());
        return JClientRspEntity.buildSuccess(adminSmMenuService.saveAdminSmMenu(adminSmMenu));
    }

    /**
     * 修改菜单
     *
     * @param adminSmMenu 修改菜单的form数据
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:19
     */
    @PostMapping("/editmenu")
    public JClientRspEntity<String> update(@Validated @RequestBody JClientReqEntity<AdminSmMenuEntity> adminSmMenu) {

        adminSmMenuService.updateMenuById(adminSmMenu.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 修改菜单全树，菜单树可拖动
     *
     * @param menuVoList 前端传menu tree
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:20
     */
    @PostMapping("/update/menutree")
    public JClientRspEntity<Object> updateAllMenuTree(@RequestBody JClientReqEntity<List<MenuVo>> menuVoList) {

        int batchIds = adminSmMenuService.updateAllMenuTree(menuVoList.getBody());
        if (batchIds > 0) {
            return JClientRspEntity.buildSuccess("修改菜单tree 成功!");
        }
        return JClientRspEntity.buildFail("修改菜单tree 失败!");
    }


    /**
     * 更新菜单指定位置，菜单树可拖动
     *
     * @param dragMenuBo 前端传指定 拖动的menuId 参照的menuId 相对于参照点的前后顺序
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:21
     */
    @PostMapping("/update/specificmenutree")
    public JClientRspEntity<Object> updateMenuTree(@RequestBody JClientReqEntity<DragMenuBo> dragMenuBo) {

        int batchIds = adminSmMenuService.updateMenuTreeByDragMenu(dragMenuBo.getBody());
        if (batchIds > 0) {
            return JClientRspEntity.buildSuccess("修改菜单tree 成功!");
        }
        return JClientRspEntity.buildFail("修改菜单tree 失败!");
    }

    /**
     * 批量删除菜单
     *
     * @param ids 要删除的菜单ID
     * @return void
     * @author zhanyq
     * @date 2021-06-24 15:22
     * -----------------------------
     * 更新删除逻辑，废弃原来的removeMenuByIds，改用 deleteMenuByIds
     * @author zhanyq
     * @date 2021-07-13
     */
    @PostMapping("/deletemenu")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmMenuService.deleteMenuByIds(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 业务功能列表分业查询
     *
     * @param adminSmBusiFuncQuery 查询条件
     * @return 业务功能数据分页列表
     * @author zhanyq
     * @date 2021-06-24 15:22
     */
    @PostMapping("/funclistquery")
    public JClientRspEntity<Page<AdminSmBusiFuncVo>> getFuncInfo(@RequestBody JClientReqEntity<AdminSmBusiFuncQuery> adminSmBusiFuncQuery) {
        return JClientRspEntity.buildSuccess(adminSmBusiFuncService.getFuncInfoWithCondition(adminSmBusiFuncQuery.getBody()));
    }


    /**
     * 菜单节点信息查询
     *
     * @param req 菜单ID
     * @return 菜单节点信息数据
     * @author zhanyq
     * @date 2021-06-24 15:23
     */
    @PostMapping("/menuinfoquery")
    public JClientRspEntity<Object> getMenuInfo(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        return JClientRspEntity.buildSuccess(adminSmMenuService.getMenuInfo(params.get("menuId")));
    }

}
