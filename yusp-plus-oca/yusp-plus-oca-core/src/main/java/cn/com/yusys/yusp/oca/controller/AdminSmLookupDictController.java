package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmLookupDictBo;
import cn.com.yusys.yusp.oca.domain.query.AdminSmLookupDictQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLookupDictVo;
import cn.com.yusys.yusp.oca.service.AdminSmLookupDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;


/**
 * 数据字典内容表，新数据字典接口
 *
 * @author danyb1
 * @date 2021-01-15 16:44:33
 */
@Tag(name = "数据字典内容表接口")
@RestController
@RequestMapping("/api/adminsmlookupdict")
public class AdminSmLookupDictController {
    @Autowired
    private AdminSmLookupDictService adminSmLookupDictService;


    /**
     * 列表，包括 模糊条件分页查询
     *
     * @param adminSmLookupDictQuery 查询条件
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 14:17
     */
    @Operation(summary = "列表")
    @PostMapping("/list")
    public JClientRspEntity<Page<AdminSmLookupDictVo>> list(@RequestBody JClientReqEntity<AdminSmLookupDictQuery> adminSmLookupDictQuery) {

        return JClientRspEntity.buildSuccess(adminSmLookupDictService.queryLookupDictWithCondition(adminSmLookupDictQuery.getBody()));
    }


    /**
     * 详情信息
     *
     * @param lookupItemId 数据字典ID
     * @return 数据字典详情
     * @author zhanyq
     * @date 2021-06-25 14:18
     */
    @Operation(summary = "详情信息")
    @PostMapping("/info/{lookupItemId}")
    public JClientRspEntity<List<AdminSmLookupDictVo>> info(@PathVariable String lookupItemId) {
        return JClientRspEntity.buildSuccess(adminSmLookupDictService.queryLookupDictInfoById(lookupItemId));
    }

    /**
     * 查询初始化字典分类
     *
     * @return 数据字典分类数据
     * @author zhanyq
     * @date 2021-06-25 14:18
     */
    @Operation(summary = "查询初始化字典分类")
    @PostMapping("/queryinitdict")
    public JClientRspEntity<List<AdminSmLookupDictVo>> info() {
        return JClientRspEntity.buildSuccess(adminSmLookupDictService.queryInitLookupDict());
    }

    /**
     * 保存数据自带你
     *
     * @param adminSmLookupDictBo 要保存的数据
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:19
     */
    @Operation(summary = "保存")
    @PostMapping("/save")
    public JClientRspEntity<String> save(@Validated @RequestBody JClientReqEntity<AdminSmLookupDictBo> adminSmLookupDictBo) {

        adminSmLookupDictService.saveLookupDictByDictBo(adminSmLookupDictBo.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 插入字典项
     *
     * @param adminSmLookupDictBo 要插入的数据字典项实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:21
     */
    @Operation(summary = "插入字典项")
    @PostMapping("/insertdictitem")
    public JClientRspEntity<String> insertDictItem(@Validated @RequestBody JClientReqEntity<AdminSmLookupDictBo> adminSmLookupDictBo) {

        adminSmLookupDictService.insertLookupDictByDictBo(adminSmLookupDictBo.getBody());
        return JClientRspEntity.buildSuccess("成功");

    }


    /**
     * 修改数据字典项
     *
     * @param adminSmLookupDictBo 要修改的数据字典实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:23
     */
    @Operation(summary = "修改")
    @PostMapping("/update")
    public JClientRspEntity<String> update(@Validated @RequestBody JClientReqEntity<AdminSmLookupDictBo> adminSmLookupDictBo) {

        adminSmLookupDictService.updateLookupDictByDictBo(adminSmLookupDictBo.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 批量删除数据字典项
     *
     * @param ids 数据字典项ID
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:24
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public JClientRspEntity<String> delete(@RequestBody JClientReqEntity<String[]> ids) {

        adminSmLookupDictService.removeLookupDictByIds(ids.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 刷新数据字典项缓存
     *
     * @return void
     * @author zhanyq
     * @date 2021-06-25 14:25
     */
    @PostMapping("/refreshdict")
    public JClientRspEntity<String> refreshItemCache() {
        adminSmLookupDictService.refreshLookupDict();
        return JClientRspEntity.buildSuccess("数据字典缓存更新成功");
    }


    /**
     * 查询数据字典
     *
     * @param req 数据字典Code编码，可以查多个，中间使用英文","分割
     * @return 查询到的数据字典项 Map<String, List<Map<String, Object>>> 格式
     * @author zhanyq
     * @date 2021-06-25 14:26
     */
    @PostMapping("/querylookupcode")
    public JClientRspEntity<Map<String, List<Map<String, Object>>>> queryLookupCode(@RequestBody JClientReqEntity<Map<String,String>> req) {
        String lookupCodes = req.getBody().get("lookupCodes");
        return JClientRspEntity.buildSuccess(adminSmLookupDictService.queryLookupCode(lookupCodes));
    }


}
