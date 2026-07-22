package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPropEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPropQuery;
import cn.com.yusys.yusp.oca.service.AdminSmPropService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * 系统消息controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@RestController
@RequestMapping("/api/adminsmprop")
public class AdminSmPropController {

    @Autowired
    private AdminSmPropService adminSmPropService;

    /**
     * 分页查询系统信息
     * @param adminSmPropQuery 查询条件
     * @return 分页信息及数据
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmPropEntity>> list(@RequestBody JClientReqEntity<AdminSmPropQuery> adminSmPropQuery) {
        Page<AdminSmPropEntity> page = adminSmPropService.queryPropPage(adminSmPropQuery.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 保存系统信息
     * @param adminSmProp 保存参数
     * @return 保存是否成功
     */
    @PostMapping("/save")
    public JClientRspEntity<String> save(@Valid @RequestBody JClientReqEntity<AdminSmPropSaveBo> adminSmProp) {
        adminSmPropService.saveProp(adminSmProp.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 系统信息更新
     * @param adminSmProp 更新参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Valid @RequestBody JClientReqEntity<AdminSmPropEditBo> adminSmProp) {
        adminSmPropService.updatePropById(adminSmProp.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 系统信息删除
     * @param ids 删除id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmPropService.removeByIds(Arrays.asList(ids.getBody()));
        return JClientRspEntity.buildSuccess("成功");
    }
}
