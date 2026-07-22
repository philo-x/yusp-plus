package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmResContrEntity;
import cn.com.yusys.yusp.oca.domain.form.*;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthTmplService;
import cn.com.yusys.yusp.oca.service.AdminSmResContrService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统功能控制点表
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
@RestController
@RequestMapping("/api/adminsmrescontr")
public class AdminSmResContrController {

    @Autowired
    private AdminSmResContrService adminSmResContrService;
    @Autowired
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;


    /**
     * 控制点页面新增、修改时所需的菜单列表
     * @param adminSmMenuConditionForm 菜单列表查询条件的封装
     * @return 统一返回结果实体类
     */
    @PostMapping("/getmenulist")
    public JClientRspEntity<Page<AdminSmMenuVo>> getMenuList(@RequestBody JClientReqEntity<AdminSmMenuConditionForm> adminSmMenuConditionForm) {
        Page<AdminSmMenuVo> page = adminSmResContrService.getMenuList(adminSmMenuConditionForm.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 查询控制点列表
     * @param adminSmResContrForm 系统功能控制点查询列表封装条件
     * @return 统一返回结果实体类
     */
    @PostMapping("/getcontrinfo")
    public JClientRspEntity<Page<AdminSmResContrVo>> getContrInfo(@RequestBody JClientReqEntity<AdminSmResContrForm> adminSmResContrForm) {
        Page<AdminSmResContrVo> page = adminSmResContrService.queryPageWithCondition(adminSmResContrForm.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 根据控制点记录编号获取控制点信息
     *
     * @param contrId 系统功能控制点记录编号
     * @return 统一返回结果实体类
     */
    @PostMapping("/info/{contrId}")
    public JClientRspEntity<AdminSmResContrEntity> info(@PathVariable String contrId) {
        AdminSmResContrEntity adminSmResContr = adminSmResContrService.getById(contrId);
        return JClientRspEntity.buildSuccess(adminSmResContr);
    }

    /**
     * 验证contrCode是否已经存在
     *
     * 如果返回大于0：表示已经存在
     * 如果返回0：表示可以使用该contrCode
     * 如果返回-1：表示contrCode为空，需要填写该值
     *
     * @param adminSmResContrForm 系统功能控制点查询封装条件
     * @return 统一返回结果实体类
     */
    @PostMapping("/checkcode")
    public JClientRspEntity<Long> checkCode(@RequestBody AdminSmResContrForm adminSmResContrForm) {
        long count = adminSmResContrService.checkCode(adminSmResContrForm);
        return JClientRspEntity.buildSuccess(count);
    }

    /**
     * 新增控制点
     *
     * @param adminSmResContrSaveForm   系统功能控制点表新建/修改表单数据
     * @return 统一返回结果实体类
     */
    @PostMapping("/createcontr")
    public JClientRspEntity<Object> createContr(@RequestBody JClientReqEntity<AdminSmResContrSaveForm> adminSmResContrSaveForm) {
        adminSmResContrService.createContr(adminSmResContrSaveForm.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 修改控制点
     *
     * 修改的时候还要验证 contrCode，需要调用一次checkCode接口
     *
     * @param adminSmResContrSaveForm 系统功能控制点表新建/修改表单数据
     * @return 统一返回结果实体类
     */
    @PostMapping("/editcontr")
    public JClientRspEntity<Object> updateContr(@RequestBody JClientReqEntity<AdminSmResContrSaveForm> adminSmResContrSaveForm) {
        adminSmResContrService.updateContr(adminSmResContrSaveForm.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 删除控制点
     * @param ids  控制点记录编号数组
     * @return 统一返回结果实体类
     */
    @PostMapping("/deletecontr")
    public JClientRspEntity<Object> deleteContr(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmResContrService.deleteContr(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 获取业务功能树
     *
     * @return 统一返回结果实体类
     */
    @PostMapping("/treequery")
    public JClientRspEntity<List<AdminSmContrTreeVo>> getFuncTree() {
        List<AdminSmContrTreeVo> treeList = adminSmResContrService.getFuncTree();
        return JClientRspEntity.buildSuccess(treeList);
    }

    /**
     * 查询控制点可授权的数据权限模板列表
     * @param form 查询控制点可授权的数据权限模板列表条件封装类
     * @return 统一返回结果实体类
     */
    @PostMapping("/datatmpllist")
    public JClientRspEntity<IPage<AdminSmDataTmplListVo>> getDataTmplList(@RequestBody JClientReqEntity<AdminSmDataTmplConditionForm> form) {
        IPage<AdminSmDataTmplListVo> pageForContr = adminSmDataAuthTmplService.getListForContr(form.getBody());
        return JClientRspEntity.buildSuccess(pageForContr);
    }

    /**
     * 控制点关联数据权限模板操作
     * @param adminSmResContrSaveForm 系统功能控制点表新建/修改表单数据
     * @return 统一返回结果实体类
     */
    @PostMapping("/relationtmpl")
    public JClientRspEntity<Object> relationTmpl(@RequestBody JClientReqEntity<AdminSmResContrSaveForm> adminSmResContrSaveForm) {
        String result = adminSmResContrService.relationTmpl(adminSmResContrSaveForm.getBody(), true);
        if ("修改关联成功！".equals(result)) {
            return JClientRspEntity.buildSuccess(result);
        } else {
            return JClientRspEntity.buildFail(result);
        }
    }

    /**
     * 控制点信息的查询
     * @param adminSmDataAuthForm 系统功能控制点查询信息的封装
     * @return 统一返回结果实体类
     */
    @PostMapping("/associatedtmpl")
    public JClientRspEntity<List<AdminDataAuthVo>> associatedTmpl(@RequestBody JClientReqEntity<AdminSmDataAuthForm> adminSmDataAuthForm) {
        return JClientRspEntity.buildSuccess(adminSmDataAuthTmplService.associatedTmpl(adminSmDataAuthForm.getBody().getContrId()));
    }

    /**
     * 查询已授权的控制点
     * @param adminSmResContrForm 查询条件
     * @return 分页返回结果
     * @author zhanyq
     * @date 2021-09-29 10:02
     */
    @PostMapping("/getAuthedContrList")
    public JClientRspEntity<Page<AdminSmResContrVo>> getAuthedContrList(@RequestBody JClientReqEntity<AdminSmResContrForm> adminSmResContrForm) {
        Page<AdminSmResContrVo> page = adminSmResContrService.getAuthedContrPage(adminSmResContrForm.getBody());
        return JClientRspEntity.buildSuccess(page);
    }
}
