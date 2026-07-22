package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPasswordLogEntity;
import cn.com.yusys.yusp.oca.service.AdminSmPasswordLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;



/**
 * 密码修改记录表
 *
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-02 15:07:44
 */
@RestController
@RequestMapping("/api/adminsmpasswordlog")
public class AdminSmPasswordLogController {
    @Autowired
    private AdminSmPasswordLogService adminSmPasswordLogService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public JClientRspEntity<PageUtils> list(@RequestBody JClientReqEntity<Map<String,Object>> req){
        Map<String,Object> params = req.getBody();
        PageUtils page = adminSmPasswordLogService.queryPage(params);
        return JClientRspEntity.buildSuccess(page);
    }


    /**
     * 信息
     */
    @PostMapping("/info/{logId}")
    public JClientRspEntity<AdminSmPasswordLogEntity> info(@PathVariable String logId){
		AdminSmPasswordLogEntity adminSmPasswordLog = adminSmPasswordLogService.getById(logId);
        return JClientRspEntity.buildSuccess(adminSmPasswordLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public JClientRspEntity<String> save(@RequestBody JClientReqEntity<AdminSmPasswordLogEntity> req){
        AdminSmPasswordLogEntity adminSmPasswordLog = req.getBody();
        adminSmPasswordLog.setUserId(SessionUtils.getUserId());
        adminSmPasswordLog.setLastChgDt(new Date());
        adminSmPasswordLog.setLastChgUsr(SessionUtils.getUserId());
		adminSmPasswordLogService.save(adminSmPasswordLog);
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@RequestBody JClientReqEntity<AdminSmPasswordLogEntity> adminSmPasswordLog){
		adminSmPasswordLogService.updateById(adminSmPasswordLog.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> logIds){
		adminSmPasswordLogService.removeByIds(Arrays.asList(logIds.getBody()));
        return JClientRspEntity.buildSuccess("成功");
    }
}
