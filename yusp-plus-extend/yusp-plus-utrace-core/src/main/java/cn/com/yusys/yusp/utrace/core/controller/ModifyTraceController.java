package cn.com.yusys.yusp.utrace.core.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.utrace.core.domain.entity.ModifyTraceEntity;
import cn.com.yusys.yusp.utrace.core.domain.vo.TraceQueryVo;
import cn.com.yusys.yusp.utrace.core.service.ModifyTraceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 小U留痕记录表
 *
 * @author zhanyq
 * @date 2021-05-17 15:13:18
 */
@Tag(name = "小U留痕记录表接口")
@RestController
@RequestMapping("/api/utrace")
public class ModifyTraceController {
    @Autowired
    private ModifyTraceService modifyTraceService;

    /**
     * 小U留痕列表分页查询
     *
     * @param query 查询参数
     * @return 前端统一响应实体
     * @description: 查询修改记录列表信息，分页
     * @date 2021/6/24 13:42
     * @author zhanyq
     */
    @Operation(summary = "列表")
    @GetMapping("/listPage")
    public JClientRspEntity<Page<ModifyTraceEntity>> list(TraceQueryVo query) {
        Page<ModifyTraceEntity> page = modifyTraceService.queryPage(query);
        return JClientRspEntity.buildSuccess(page);
    }


    /**
     * 根据 数据主键 mPkV查询是否有修改记录
     *
     * @param query 查询参数
     * @return 前端统一响应实体
     * @date 2021/6/24 13:42
     * @author zhanyq
     */
    @Operation(summary = "信息")
    @GetMapping("/listByPk")
    public JClientRspEntity<List<ModifyTraceEntity>> listAll(TraceQueryVo query) {
        List<ModifyTraceEntity> sModifyTrace = modifyTraceService.queryTraceByPk(query);
        return JClientRspEntity.buildSuccess(sModifyTrace);
    }


    /**
     * 保存小U留痕数据
     *
     * @param t 要保存的小U数据实体
     * @return 前端统一响应实体
     * @date 2021/6/24 13:42
     * @author zhanyq
     */
    @Operation(summary = "保存")
    @PostMapping("/save")
    public JClientRspEntity<String> save(@RequestBody List<ModifyTraceEntity> t) {
        modifyTraceService.addTrace(t);
        return JClientRspEntity.buildSuccess("成功");
    }


}
