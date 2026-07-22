package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmMessageQuery;
import cn.com.yusys.yusp.oca.service.AdminSmMessageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * 系统提示消息controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@RestController
@RequestMapping("/api/adminsmmessage")
public class AdminSmMessageController {
    @Autowired
    private AdminSmMessageService adminSmMessageService;


    /**
     * 系统提示信息分页查询
     * @param adminSmMessageQuery 查询条件
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 15:21
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmMessageEntity>> queryMessagePage(@RequestBody JClientReqEntity<AdminSmMessageQuery> adminSmMessageQuery) {
        Page<AdminSmMessageEntity> page = adminSmMessageService.queryMessagePage(adminSmMessageQuery.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 保存系统提示消息
     * @param adminSmMessage 要保存的消息实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 15:23
     */
    @PostMapping("/save")
    public JClientRspEntity<String> save(@Valid @RequestBody JClientReqEntity<AdminSmMessageSaveBo> adminSmMessage) {
        adminSmMessageService.saveMessage(adminSmMessage.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 修改系统提示消息
     * @param adminSmMessage 要修改的消息实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 15:23
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Valid @RequestBody JClientReqEntity<AdminSmMessageEditBo> adminSmMessage) {
        adminSmMessageService.updateMessageById(adminSmMessage.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 批量删除系统提示消息
     * @param ids 系统提示消息ID
     * @return 返回删除结果
     * @author zhanyq
     * @date 2021-06-25 15:24
     */
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmMessageService.removeByIds(Arrays.asList(ids.getBody()));
        return JClientRspEntity.buildSuccess("成功");
    }
}
