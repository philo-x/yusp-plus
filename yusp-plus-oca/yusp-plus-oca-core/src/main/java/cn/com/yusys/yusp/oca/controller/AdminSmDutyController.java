package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDutyEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDutyVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDutyRelVo;
import cn.com.yusys.yusp.oca.service.AdminSmDutyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;


/**
 * 系统岗位表
 *
 * @author terry
 * @date 2020-12-11 10:47:26
 */
@RestController
@RequestMapping("/api/adminsmduty")
public class AdminSmDutyController {
    @Autowired
    private AdminSmDutyService adminSmDutyService;

    /**
     * 分页列表
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmDutyVo>>page(@RequestBody JClientReqEntity<AdminSmDutyQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmDutyService.queryPage(query.getBody()));
    }

    /**
     * 信息
     *
     * @param dutyId 岗位id
     * @return 岗位信息
     */
    @PostMapping("/info/{dutyId}")
    public JClientRspEntity<AdminSmDutyEntity> info(@PathVariable String dutyId) {
        AdminSmDutyEntity adminSmDuty = adminSmDutyService.getById(dutyId);
        return JClientRspEntity.buildSuccess(adminSmDuty);
    }

    /**
     * 新增
     *
     * @param entity 岗位实体类
     * @return 成功返回 code = 0000，message"保存成功"
     */
    @PostMapping("/add")
    public JClientRspEntity<String> add(@Validated({Insert.class}) @RequestBody JClientReqEntity<AdminSmDutyEntity> entity) {
        adminSmDutyService.save(entity.getBody());
        return JClientRspEntity.buildSuccess("保存成功");

    }

    /**
     * 更新
     *
     * @param entity 岗位实体类
     * @return 成功返回 code = 0000，message"更新成功"
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Validated({Update.class}) @RequestBody JClientReqEntity<AdminSmDutyEntity> entity) {
        adminSmDutyService.updateById(entity.getBody());
        return JClientRspEntity.buildSuccess("更新成功");
    }

    /**
     * 批量启用岗位
     *
     * @param ids 岗位id数组
     * @return 成功返回 code = 0000，message"启用成功"
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<String> batchEnable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDutyService.batchEnable(ids.getBody());
        return JClientRspEntity.buildSuccess("启用成功");
    }

    /**
     * 批量停用岗位
     *
     * @param ids 岗位id数组
     * @return 成功返回 code = 0000，message"停用成功"
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<String> batchDisable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDutyService.batchDisable(ids.getBody());

        return JClientRspEntity.buildSuccess("停用成功");
    }

    /**
     * 批量删除
     *
     * @param ids 岗位id数组
     * @return 成功返回 code = 0000，message"删除成功"
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<String> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDutyService.batchDelete(ids.getBody());
        return JClientRspEntity.buildSuccess("删除成功");
    }

    /**
     * 查询岗位下的用户
     *
     * @param query 查询条件
     * @return 用户列表
     */
    @PostMapping("/userlist")
    public JClientRspEntity<Page<AdminSmUserDutyRelVo>> userList(@RequestBody JClientReqEntity<AdminSmDutyUserQuery> query) {
        Page<AdminSmUserDutyRelVo> dutyUserVoList = adminSmDutyService.memberPage(query.getBody());
        return JClientRspEntity.buildSuccess(dutyUserVoList);
    }
}
