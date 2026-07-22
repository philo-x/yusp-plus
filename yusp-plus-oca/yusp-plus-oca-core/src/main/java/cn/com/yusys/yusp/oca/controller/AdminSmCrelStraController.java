package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmCrelStraDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;


/**
 * 认证策略参数表
 *
 * @author wujp4
 * @date 2021-03-30 11:27:32
 */
@Tag(name = "认证策略参数表接口")
@RestController
@RequestMapping("/api/adminsmcrelstra")
public class AdminSmCrelStraController {

    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;

    /**
     * 批量修改认证策略
     *
     * @param adminSmCrelStraDto 被修改的数据
     * @return void
     * @author zhanyq
     * @date 2021-06-24 17:44
     */
    @Operation(summary = "修改")
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Valid @RequestBody JClientReqEntity<AdminSmCrelStraDto> adminSmCrelStraDto) {

        adminSmCrelStraService.updateById(adminSmCrelStraDto.getBody());

        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 查询所有认证策略
     *
     * @return 认证策略集合
     * @author zhanyq
     * @date 2021-06-24 17:46
     */
    @Operation(summary = "列表")
    @PostMapping("/getall")
    public JClientRspEntity<List<AdminSmCrelStraEntity>> getall() {

        List<AdminSmCrelStraEntity> list = adminSmCrelStraService.list();
        return JClientRspEntity.buildSuccess(list);
    }
}
