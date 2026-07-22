package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmBusiFuncBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmBusiFuncQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;


/**
 * 系统业务功能表
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@RestController
@RequestMapping("/api/adminsmbusifunc")
public class AdminSmBusiFuncController {

    @Autowired
    private AdminSmBusiFuncService adminSmBusiFuncService;


    /**
     * 业务功能分页查询
     *
     * @param adminSmBusiFuncQuery 分页查询条件
     * @return 业务功能分页查询结果
     * @author zhanyq
     * @date 2021-06-24 14:30
     */
    @PostMapping("/queryfunc")
    public JClientRspEntity<Page<AdminSmBusiFuncVo>> list(@RequestBody JClientReqEntity<AdminSmBusiFuncQuery> adminSmBusiFuncQuery) {

        return JClientRspEntity.buildSuccess(adminSmBusiFuncService.queryPageWithCondition(adminSmBusiFuncQuery.getBody()));
    }


    /**
     * 业务功能详情查询，此接口暂时未使用
     *
     * @param funcId 业务功能ID
     * @return 业务功能详情数据
     * @author zhanyq
     * @date 2021-06-24 14:33
     */
    @PostMapping("/info/{funcId}")
    public JClientRspEntity<AdminSmBusiFuncEntity> info(@PathVariable String funcId) {

        AdminSmBusiFuncEntity adminSmBusiFunc = adminSmBusiFuncService.getById(funcId);
        return JClientRspEntity.buildSuccess(adminSmBusiFunc);
    }


    /**
     * 新增业务功能信息
     *
     * @param adminSmBusiFuncBo 要保存的业务功能数据
     * @return 成功或者失败的信息
     * @author zhanyq
     * @date 2021-06-24 14:35
     */
    @PostMapping("/createfunc")
    public JClientRspEntity<Object> save(@RequestBody JClientReqEntity<AdminSmBusiFuncBo> adminSmBusiFuncBo) {
        int resultInt = adminSmBusiFuncService.saveBusiFuncByBo(adminSmBusiFuncBo.getBody());
        if (resultInt >= 1) {
            return JClientRspEntity.buildSuccess("新增成功!");
        }
        return JClientRspEntity.buildFail("新增失败!");
    }


    /**
     * 修改业务功能信息
     *
     * @param adminSmBusiFuncBo 业务功能修改form数据
     * @return 修改成功或者失败的信息
     * @author zhanyq
     * @date 2021-06-24 14:36
     */
    @PostMapping("/editfunc")
    public JClientRspEntity<Object> update(@RequestBody JClientReqEntity<AdminSmBusiFuncBo> adminSmBusiFuncBo) {
        // bean copy
        adminSmBusiFuncService.updateBusiFunc(adminSmBusiFuncBo.getBody());
        return JClientRspEntity.buildSuccess("修改模块成功!");

    }


    /**
     * 删除业务功能
     *
     * @param ids 删除的业务功能的ID
     * @return 无
     * @author zhanyq
     * @date 2021-06-24 14:38
     */
    @PostMapping("/deletefunc")
    public JClientRspEntity<Object> delete(@RequestBody JClientReqEntity<String[]> ids) {

        adminSmBusiFuncService.removeFuncByIds(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

}
