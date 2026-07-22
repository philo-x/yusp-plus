package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmInstuEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmInstuQuery;
import cn.com.yusys.yusp.oca.domain.vo.InstuExtVo;
import cn.com.yusys.yusp.oca.domain.vo.UserSessionVo;
import cn.com.yusys.yusp.oca.service.AdminSmInstuService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 金融机构表
 *
 * @author terry
 * @email tanrui1@yusys.com.cn
 * @date 2020-11-19 14:30:22
 */
@RestController
@RequestMapping("/api/adminsminstu")
public class AdminSmInstuController {


    @Autowired
    private AdminSmInstuService adminSmInstuService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public JClientRspEntity list(){
        QueryWrapper<AdminSmInstuEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("INSTU_STS","A");
        List<AdminSmInstuEntity> list = adminSmInstuService.list(queryWrapper);
        return JClientRspEntity.buildSuccess(list);
    }

    @PostMapping("/page")
    public JClientRspEntity<Page<InstuExtVo>> page(@RequestBody JClientReqEntity<AdminSmInstuQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmInstuService.queryPage(query.getBody()));
    }


    /**
     * 信息
     */
    @PostMapping("/info/{instuId}")
    public JClientRspEntity<AdminSmInstuEntity> info(@PathVariable String instuId){
		AdminSmInstuEntity adminSmInstu = adminSmInstuService.getById(instuId);
        return JClientRspEntity.buildSuccess(adminSmInstu);
    }

    /**
     * 保存新增
     */
    @PostMapping("/save")
    public JClientRspEntity<Map<String,Object>> save(@RequestBody JClientReqEntity<AdminSmInstuEntity> req){
        AdminSmInstuEntity adminSmInstu = req.getBody();
        UserSessionVo userSessionVo = adminSmUserService.getUserByAuthorization(SessionUtils.getUserId(),SessionUtils.getClientId());
        Map<String,Object> data=new HashMap<>(4);
        if(adminSmInstuService.save(adminSmInstu,userSessionVo)){
            data.put("code","0");
            data.put("message","金融机构代码为："+adminSmInstu.getInstuCde()+",金融机构名称为:"+adminSmInstu.getInstuName()+"的数据保存成功。");
            return JClientRspEntity.buildSuccess(data);
        }else{
            data.put("code","2");
            data.put("message","金融机构代码为："+adminSmInstu.getInstuCde()+",金融机构名称为:"+adminSmInstu.getInstuName()+"的数据已经存在。");
            return JClientRspEntity.buildSuccess(data);
        }
    }

    /**
     * 批量启用
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<String> batchEnable(@RequestBody JClientReqEntity<String> ids){
        String message="启用失败";
        if(adminSmInstuService.batchEnable(ids.getBody())){
            message="启用成功";
        }
        return JClientRspEntity.buildSuccess(message);
    }

    /**
     * 批量停用
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<String> batchDisable(@RequestBody JClientReqEntity<String> ids){
        String message="停用失败";
        if(adminSmInstuService.batchDisable(ids.getBody())){
            message="停用成功";
        }
        return JClientRspEntity.buildSuccess(message);
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<String> batchDelete(@RequestBody JClientReqEntity<String> ids){
        String message="删除失败";
        if(adminSmInstuService.batchDelete(ids.getBody())){
            message="删除成功";
        }
        return JClientRspEntity.buildSuccess(message);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public JClientRspEntity<Boolean> update(@RequestBody JClientReqEntity<AdminSmInstuEntity> adminSmInstu){
        return JClientRspEntity.buildSuccess(adminSmInstuService.updateById(adminSmInstu.getBody()));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> instuIds){
		adminSmInstuService.removeByIds(Arrays.asList(instuIds.getBody()));
        return JClientRspEntity.buildSuccess("成功");
    }
}
