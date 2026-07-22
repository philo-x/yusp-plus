package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthEntity;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 数据权限表
 * @author zhangyt12
 * @date 2021/6/28 10:16
 */
@RestController
@RequestMapping("/api/adminsmdataauth")
public class AdminSmDataAuthController {

    @Autowired
    private AdminSmDataAuthService adminSmDataAuthService;

    /**
     * 删除数据权限信息,同时删除其授权信息
     * @param ids 数据权限关联控制点数据表 id
     * @return 成功返回 "删除权限信息成功"，失败返回 code 500
     */
    @PostMapping("/deletedataauth")
    public JClientRspEntity<Object> deleteDataAuth(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDataAuthService.deleteDataAuth(ids.getBody());
        return JClientRspEntity.buildSuccess("删除权限信息成功");
    }

    /**
     * 根据控制点id分页查询非当前持有的数据权限模板
     * @param params
     * @return
     */
    @PostMapping("/authtmplquery")
    public JClientRspEntity<PageUtils> pageAuthTmplByContrId(@RequestBody JClientReqEntity<Map<String, String>> params) {
        PageUtils page = adminSmDataAuthService.pageAuthTmplByContrId(params.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 分页查询控制点下的数据权限模板列表
     * @param params
     * @return
     */
    @PostMapping("/getdataauth")
    public JClientRspEntity<PageUtils> pageResControlDataAuthTmpl(@RequestBody JClientReqEntity<Map<String, String>> params) {
        PageUtils page = adminSmDataAuthService.pageResControlDataAuthTmpl(params.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 添加控制点与数据库之间的交互
     * @param adminSmDataAuth
     * @return
     */
    @PostMapping("/createdataauth")
    public JClientRspEntity<String> createDataAuth(@RequestBody JClientReqEntity<AdminSmDataAuthEntity> adminSmDataAuth) {
        adminSmDataAuthService.createDataAuth(adminSmDataAuth.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 获取数据权限管理树，由1级：模块列表 2级：业务功能列表 3级：控制点列表 组成
     * @param nodeId 写死0？
     * @return
     */
    @PostMapping("/treequery")
    public JClientRspEntity<PageUtils> getDataAuthTree(@RequestBody JClientReqEntity<String> nodeId) {
        PageUtils page = adminSmDataAuthService.getDataAuthTree(nodeId.getBody());
        return JClientRspEntity.buildSuccess(page);
    }
}
