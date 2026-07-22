package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmFuncModBo;
import cn.com.yusys.yusp.oca.domain.query.AdminSmFuncModQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;
import cn.com.yusys.yusp.oca.service.AdminSmFuncModService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 系统功能模块表
 *
 * @author wujp4
 * @date 2020-11-26 10:50:57
 */
@RestController
@RequestMapping("/api/adminsmfuncmod")
public class AdminSmFuncModController {

    @Autowired
    private AdminSmFuncModService adminSmFuncModService;

    /**
     * 系统模块分页信息
     *
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 9:46
     */
    @PostMapping("/querymod")
    public JClientRspEntity<Page<AdminSmFuncModVo>> list(@RequestBody JClientReqEntity<AdminSmFuncModQuery> adminSmFuncModQuery) {

        return JClientRspEntity.buildSuccess(adminSmFuncModService.queryPageWithCondition(adminSmFuncModQuery.getBody()));
    }


    /**
     * 详情查询
     *
     * @param modId 模块ID
     * @return 详情数据
     * @author zhanyq
     * @date 2021-06-25 9:47
     */
    @PostMapping("/info/{modId}")
    public JClientRspEntity<Object> info(@PathVariable String modId) {

        return JClientRspEntity.buildSuccess(adminSmFuncModService.getById(modId));
    }

    /**
     * 保存 新增模块信息
     *
     * @param adminSmFuncBo 要保存的信息
     * @return void
     * @author zhanyq
     * @date 2021-06-25 9:49
     */
    @PostMapping("/createmod")
    public JClientRspEntity<Object> save(@Validated @RequestBody JClientReqEntity<AdminSmFuncModBo> adminSmFuncBo) {

        int resultInt = adminSmFuncModService.saveFuncMod(adminSmFuncBo.getBody());
        if (resultInt > 0) {
            return JClientRspEntity.buildSuccess("新增模块成功!");
        }
        if (resultInt == -1) {
            throw BizException.error("exist","-1",adminSmFuncBo.getBody().getModName());
        }
        return JClientRspEntity.buildFail("新增模块失败!");
    }


    /**
     * 修改模块信息
     *
     * @param adminSmFuncBo 要修改的form数据
     * @return void
     * @author zhanyq
     * @date 2021-06-25 9:50
     */
    @PostMapping("/editmod")
    public JClientRspEntity<Object> update(@Validated @RequestBody JClientReqEntity<AdminSmFuncModBo> adminSmFuncBo) {

        int updateId = adminSmFuncModService.updateFuncMod(adminSmFuncBo.getBody());
        if (updateId > 0) {
            return JClientRspEntity.buildSuccess("修改模块成功!");
        }
        if (updateId == -1) {
            return JClientRspEntity.buildFail("模块已存在!");
        }
        return JClientRspEntity.buildFail("修改模块失败!");
    }

    /**
     * 删除模块信息
     *
     * @param modId 模块ID
     * @return void
     * @author zhanyq
     * @date 2021-06-25 9:50
     */
    @PostMapping("/deletemod")
    public JClientRspEntity<Object> delete(@RequestBody JClientReqEntity<String[]> modId) {

        int modResult = adminSmFuncModService.removeByModId(modId.getBody());
        if (modResult == -1) {
            throw BizException.error(null, "50600006");
        }
        if (modResult > 0) {
            return JClientRspEntity.buildSuccess("删除成功");
        }
        return JClientRspEntity.buildFail("删除失败!");
    }


    /**
     * 保存数据前查询模块名称是否已经存在
     *
     * @param req 模块名称
     * @return 模块数据条数
     * @author zhanyq
     * @date 2021-06-25 9:51
     */
    @PostMapping("/checkname")
    public JClientRspEntity<Object> checkName(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        List<String> funcModIdList = adminSmFuncModService.checkName(params.get("modName"), params.get("modId"));
        return JClientRspEntity.buildSuccess(funcModIdList);
    }

}
