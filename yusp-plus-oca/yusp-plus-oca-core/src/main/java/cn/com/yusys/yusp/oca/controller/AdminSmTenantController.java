package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmTenantQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmTenantVo;
import cn.com.yusys.yusp.oca.service.AdminSmTenantService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * 租户管理表
 *
 * @author zhanyq
 * @date 2021-09-17 18:29:55
 */
@Tag(name = "租户管理表接口")
@RestController
@RequestMapping("/api/adminsmtenant")
public class AdminSmTenantController {
    @Autowired
    private AdminSmTenantService adminSmTenantService;

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmTenantEntity>>page(@RequestBody JClientReqEntity<AdminSmTenantQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmTenantService.queryPage(query.getBody()));
    }


    /**
     * 信息
     */
    @Operation(summary = "信息")
    @PostMapping("/info/{tenantId}")
    public JClientRspEntity<AdminSmTenantVo> info(@PathVariable String tenantId) {
        AdminSmTenantVo baseDetail = adminSmTenantService.getBaseDetail(tenantId);
        return JClientRspEntity.buildSuccess(baseDetail);
    }

    /**
     * 保存
     */
    @Operation(summary = "保存租户基本信息")
    @PostMapping("/saveBaseInfo")
    public JClientRspEntity<AdminSmTenantVo> saveBaseInfo(@RequestBody JClientReqEntity<AdminSmTenantVo> req) {
        AdminSmTenantVo adminSmTenantVo = req.getBody();
        adminSmTenantVo = adminSmTenantService.saveBaseInfo(adminSmTenantVo);
        return JClientRspEntity.buildSuccess(adminSmTenantVo);
    }


    /**
     * 修改
     */
    @Operation(summary = "修改")
    @PostMapping("/update")
    public JClientRspEntity<String> update(@RequestBody JClientReqEntity<AdminSmTenantVo> adminSmTenantVo) {
        adminSmTenantService.updateBaseInfo(adminSmTenantVo.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }


    @Operation(summary = "批量注销")
    @PostMapping("/disEnable")
    public JClientRspEntity<String> disEnable(@RequestBody JClientReqEntity<String[]> tenantIds){
        adminSmTenantService.updateTenantSts(tenantIds.getBody(), Constants.TenantConst.TENANT_DISENABLE);
        return JClientRspEntity.buildSuccess("成功");
    }


    @Operation(summary = "批量注销启用")
    @PostMapping("/enable")
    public JClientRspEntity<String> enable(@RequestBody JClientReqEntity<String[]> tenantIds){
        adminSmTenantService.updateTenantSts(tenantIds.getBody(), Constants.TenantConst.TENANT_ENABLE);
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> tenantIds) {
        adminSmTenantService.removeByIds(Arrays.asList(tenantIds.getBody()));
        return JClientRspEntity.buildSuccess("成功");
    }


}
