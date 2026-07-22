package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDptEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDptQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDptVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import cn.com.yusys.yusp.oca.service.AdminSmDptService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


/**
 * 部门管理
 *
 * @author terry
 * @date 2020-11-12 10:47:26
 */
@RestController
@RequestMapping("/api/adminsmdpt")
public class AdminSmDptController {
    @Autowired
    private AdminSmDptService adminSmDptService;

    /**
     * 部门管理主页列表查询（分页）
     *
     * @param query 费用查询条件
     * @return 部门列表
     */
    @PostMapping("/page")
    public JClientRspEntity<Page<AdminSmDptVo>> page(@RequestBody JClientReqEntity<AdminSmDptQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmDptService.queryPage(query.getBody()));
    }

    /**
     * 查出指定机构下所有部门以及子部门，以树形结构组装起来
     *
     * @param query 分页查询条件
     * @return 机构下所有部门以及子部门树形结构
     */
    @PostMapping("/tree")
    public JClientRspEntity<?> dptTree(@RequestBody JClientReqEntity<AdminSmDptQuery> query) {
        if (!StringUtils.isEmpty(query.getBody().getOrgId())) {
            return JClientRspEntity.buildSuccess("成功");
        }
        List<AdminSmDptEntity> dptTreeData = adminSmDptService.dptTree(query.getBody());
        return JClientRspEntity.buildSuccess(dptTreeData);
    }

    /**
     * 新增部门
     *
     * @param entity 部门实体类
     * @return 成功返回 code = 0000，message"新增成功"
     */
    @PostMapping("/add")
    public JClientRspEntity<String> add(@Validated({Insert.class}) @RequestBody JClientReqEntity<AdminSmDptEntity> entity) {
        adminSmDptService.save(entity.getBody());
        //跟新部门缓存
        adminSmDptService.updateDepCache(entity.getBody());
        return JClientRspEntity.buildSuccess("新增成功");
    }

    /**
     * 修改部门
     *
     * @param entity 部门实体类
     * @return 成功返回 code = 0000，message"修改成功"
     */
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Validated({Update.class}) @RequestBody JClientReqEntity<AdminSmDptEntity> entity) {
        adminSmDptService.updateById(entity.getBody());
        //跟新部门缓存
        adminSmDptService.updateDepCache(entity.getBody());
        return JClientRspEntity.buildSuccess("修改成功");
    }

    /**
     * 批量启用部门
     *
     * @param ids 部门id数组
     * @return 成功返回 code = 0000，message"启用成功"
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<String> batchEnable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDptService.batchEnable(ids.getBody());
        return JClientRspEntity.buildSuccess("启用成功");
    }

    /**
     * 批量停用部门
     *
     * @param ids 部门id数组
     * @return 成功返回 code = 0000，message"停用成功"
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<String> batchDisable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDptService.batchDisable(ids.getBody());
        return JClientRspEntity.buildSuccess("停用成功");
    }

    /**
     * 批量删除
     *
     * @param ids 部门id数组
     * @return 成功返回 code = 0000，message"删除成功"
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<String> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmDptService.batchDelete(ids.getBody());
        //删除部门名称缓存的部分数据
        adminSmDptService.deletePartDepCache(ids.getBody());
        return JClientRspEntity.buildSuccess("删除成功");
    }

    /**
     * 部门用户
     *
     * @param query 分页查询实体类
     * @return 部门用户列表
     */
    @PostMapping("/memberlist")
    public JClientRspEntity<Page<AdminSmUserVo>> memberList(@RequestBody JClientReqEntity<AdminSmUserQuery> query) {
        Page<AdminSmUserVo> dptUserVoList = adminSmDptService.memberPage(query.getBody());
        return JClientRspEntity.buildSuccess(dptUserVoList);
    }

}
