package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthTreeForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthUpdateForm;
import cn.com.yusys.yusp.oca.domain.form.AdminTmplAuthForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrAuthVo;
import cn.com.yusys.yusp.oca.service.AdminSmAuthorizationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能授权
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@RestController
@RequestMapping("/api/authorization")
public class AdminSmAuthorizationController {

    @Autowired
    private AdminSmAuthorizationService adminSmAuthorizationService;

    /**
     * 获取数据权限模板授权页面
     *
     * @param form：包含 userID|roleId，两个只能有一个
     *                userID：授权对象的主键 userId
     *                roleId：授权角色的主键 roleId
     * @return 数据权限模板授权列表
     */
    @PostMapping("/authList")
    public JClientRspEntity<IPage<AdminSmResContrAuthVo>> getAuthTmplList(@RequestBody JClientReqEntity<AdminSmAuthTreeForm> form) {
        IPage<AdminSmResContrAuthVo> result = adminSmAuthorizationService.getAuthTmplList(form.getBody());
        return JClientRspEntity.buildSuccess(result);
    }

    /**
     * 获取权限树
     *
     * @param adminSmAuthTreeForm：包含 userID|roleId，两个只能有一个
     *                               userID：授权对象的主键 userId
     *                               roleId：授权角色的主键 roleId
     * @return 权限树列表
     */
    @PostMapping("/treeQuery")
    public JClientRspEntity<List<AdminSmAuthTreeVo>> treeQuery(@RequestBody JClientReqEntity<AdminSmAuthTreeForm> adminSmAuthTreeForm) {
        List<AdminSmAuthTreeVo> voList = adminSmAuthorizationService.getTreeQuery(adminSmAuthTreeForm.getBody());
        return JClientRspEntity.buildSuccess(voList);
    }


    @PostMapping("/getAuthedMenuTree")
    public JClientRspEntity<List<AdminSmAuthTreeVo>> getAuthedMenuTree(@RequestBody JClientReqEntity<AdminSmAuthTreeForm> adminSmAuthTreeForm) {
        List<AdminSmAuthTreeVo> voList = adminSmAuthorizationService.getAuthedMenuTree(adminSmAuthTreeForm.getBody());
        return JClientRspEntity.buildSuccess(voList);
    }


    /**
     * 修改被授权人权限
     *
     * @param adminSmAuthUpdateForm 修改被授权人权限所需参数
     * @return 统一返回实体类
     */
    @PostMapping("/saveAuth")
    public JClientRspEntity<Object> saveAuth(@RequestBody JClientReqEntity<AdminSmAuthUpdateForm> adminSmAuthUpdateForm) {
        adminSmAuthorizationService.saveAuth(adminSmAuthUpdateForm.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 复制拷贝权限
     *
     * @param adminSmAuthUpdateForm 修改被授权人权限所需参数
     * @return 统一返回实体类
     */
    @PostMapping("/copyAuth")
    public JClientRspEntity<Object> copyAuth(@RequestBody JClientReqEntity<AdminSmAuthUpdateForm> adminSmAuthUpdateForm) {
        adminSmAuthorizationService.copyAuth(adminSmAuthUpdateForm.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 给被授权对象修改控制点的数据模板
     *
     * @param form 数据模板授权
     * @return 统一返回实体类
     */
    @PostMapping("/saveTmplAuth")
    public JClientRspEntity<Object> saveTmplAuth(@RequestBody JClientReqEntity<AdminTmplAuthForm> form) {
        adminSmAuthorizationService.saveTmplAuth(form.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }
}
