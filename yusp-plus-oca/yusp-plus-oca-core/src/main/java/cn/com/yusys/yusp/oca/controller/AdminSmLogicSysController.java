package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmLogicSysBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogicSysEntity;
import cn.com.yusys.yusp.oca.service.AdminSmLogicSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 系统逻辑系统表
 *
 * @author wujp4
 * @date 2020-11-19 14:30:22
 */
@RestController
@RequestMapping("/api/adminsmlogicsys")
public class AdminSmLogicSysController {

    @Autowired
    private AdminSmLogicSysService adminSmLogicSysService;


    /**
     * 逻辑系统-列表查询
     * @param params 查询参数
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 10:17
     */
    @PostMapping("/")
    public JClientRspEntity<PageUtils> list(@RequestBody JClientReqEntity<Map<String, Object>> params) {
        PageUtils page = adminSmLogicSysService.getAdminSmLogicSys(params.getBody());
        return JClientRspEntity.buildSuccess(page);
    }


    /**
     * 详情查询
     * @param sysId 逻辑系统ID
     * @return 逻辑系统详情
     * @author zhanyq
     * @date 2021-06-25 10:18
     */
    @PostMapping("/info/{sysId}")
    public JClientRspEntity<AdminSmLogicSysEntity> info(@PathVariable String sysId) {
        AdminSmLogicSysEntity adminSmLogicSys = adminSmLogicSysService.getById(sysId);
        return JClientRspEntity.buildSuccess(adminSmLogicSys);
    }


    /**
     * 保存 初始化逻辑系统
     * @param adminSmLogicSys 要保存的逻辑系统ID
     * @return void
     * @author zhanyq
     * @date 2021-06-25 10:19
     */
    @PostMapping("/copy")
    public JClientRspEntity<AdminSmLogicSysEntity> initLogicSys(@RequestBody JClientReqEntity<AdminSmLogicSysBo> adminSmLogicSys) {

        AdminSmLogicSysEntity logicSysEntity = adminSmLogicSysService.insertAndCopy(adminSmLogicSys.getBody());

        return JClientRspEntity.buildSuccess(logicSysEntity);
    }


    /**
     * 逻辑系统修改-并设置逻辑系统认证策略
     * @param adminSmLogicSysBo 要修改的逻辑系统信息
     * @param funcId 业务功能ID
     * @return void
     * @author zhanyq
     * @date 2021-06-25 10:20
     */
    @PostMapping("/update/{funcId}")
    public JClientRspEntity<String> update(@RequestBody JClientReqEntity<AdminSmLogicSysBo> adminSmLogicSysBo, @PathVariable String funcId) {

        int isLogicSysVoBoolean = adminSmLogicSysService.updateAdminSmLogic(adminSmLogicSysBo.getBody(), funcId);
        if (isLogicSysVoBoolean > 0) {
            return JClientRspEntity.buildSuccess("成功");
        }
        return JClientRspEntity.buildFail("300002", "修改逻辑系统失败!");
    }

    /**
     * 逻辑系统 删除
     * @param id 逻辑系统ID
     * @return void
     * @author zhanyq
     * @date 2021-06-25 10:25
     */
    @PostMapping("/delete")
    public JClientRspEntity<?> delete(@RequestParam String id) {

        int delSysIdInt = adminSmLogicSysService.deleteLogicAndCrelInfo(id);
        if (delSysIdInt == -1) {
            return JClientRspEntity.buildFail("逻辑系统中状态为生效不能删除");
        }
        return JClientRspEntity.buildSuccess(delSysIdInt);
    }

    /**
     * 逻辑系统状态修改
     * @param adminSmLogicSysBo 要修改的逻辑系统数据
     * @return void
     * @author zhanyq
     * @date 2021-06-25 10:26
     */
    @PostMapping("/updatestatu")
    protected JClientRspEntity<String> updateStatu(@RequestBody JClientReqEntity<AdminSmLogicSysBo> adminSmLogicSysBo) {
        this.adminSmLogicSysService.updateAdminSmLogicStat(adminSmLogicSysBo.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }
}
