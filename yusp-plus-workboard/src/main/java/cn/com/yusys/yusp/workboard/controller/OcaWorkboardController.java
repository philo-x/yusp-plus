package cn.com.yusys.yusp.workboard.controller;

import java.util.Arrays;
import java.util.List;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.yusys.yusp.workboard.domain.vo.OcaWorkboardQueryVo;

import cn.com.yusys.yusp.workboard.domain.entity.OcaWorkboardEntity;
import cn.com.yusys.yusp.workboard.service.OcaWorkboardService;

/**
 *
 *
 * @author wx20220514
 * @date 2022-07-29 15:07:33
 */
@RestController
@RequestMapping("/api/ocaworkboard")
public class OcaWorkboardController {
    @Autowired
    private OcaWorkboardService ocaWorkboardService;

    /**
     * 列表查询
     */
    @PostMapping("/list")
    public JClientRspEntity<List<OcaWorkboardEntity>> list(@RequestBody JClientReqEntity<OcaWorkboardQueryVo> reqEntity){
        IPage<OcaWorkboardEntity> page = ocaWorkboardService.queryPage(reqEntity.getBody());

        return JClientRspEntity.buildSuccess(page.getRecords());
    }
    /**
     * 详细
     */
    @PostMapping("/info")
    public JClientRspEntity<?> info(@RequestBody JClientReqEntity<String> reqEntity){
        OcaWorkboardEntity ocaWorkboard = ocaWorkboardService.getById(reqEntity.getBody());
        if (ocaWorkboard == null){
            return JClientRspEntity.buildFail("暂无数据");
        }else{
            return JClientRspEntity.buildSuccess(ocaWorkboard);
        }
    }
    /**
     * 保存
     */
    @PostMapping("/save")
    public JClientRspEntity<Boolean> save(@RequestBody JClientReqEntity<OcaWorkboardEntity> reqEntity){
        Boolean result = ocaWorkboardService.save(reqEntity.getBody());

        return JClientRspEntity.buildSuccess(result);
    }
    /**
     * 修改
     */
    @PostMapping("/update")
    public JClientRspEntity<Boolean> update(@RequestBody JClientReqEntity<OcaWorkboardEntity> reqEntity){
        Boolean result = ocaWorkboardService.updateById(reqEntity.getBody());

        return JClientRspEntity.buildSuccess(result);
    }
    /**
     * 删除
     */
    @PostMapping("/delete")
    public JClientRspEntity<Boolean> delete(@RequestBody JClientReqEntity<String> reqEntity){
        Boolean result = ocaWorkboardService.removeByIds(Arrays.asList(reqEntity.getBody().split(",")));

        return JClientRspEntity.buildSuccess(result);
    }

}
