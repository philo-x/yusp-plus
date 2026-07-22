package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.req.crypto.replay.RequestReplaySeq;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgTreeQuery;
import cn.com.yusys.yusp.oca.service.AdminSmDptService;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author danyu
 */

@RestController
@RequestMapping("/api/util")
public class SystemUtilController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SystemUtilController.class);

    private static final String ORG_ID = "orgId";
    @Autowired
    AdminSmDptService adminSmDptService;

    @Autowired
    AdminSmOrgService adminSmOrgService;

    @Autowired(required = false)
    RequestReplaySeq seqService;

    @PostMapping("/getorgById")
    public JClientRspEntity<List<AdminSmOrgEntity>> getorgById(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        String orgId = params.get(ORG_ID);
        List<AdminSmOrgEntity> orgList = adminSmOrgService
                .list(new QueryWrapper<AdminSmOrgEntity>()
                        .eq(!StringUtils.isEmpty(orgId), "ORG_ID", orgId));
        return JClientRspEntity.buildSuccess(orgList);
    }

    /**
     * 不太完美的接口
     */
    @PostMapping("/getorgtree")
    public JClientRspEntity<List<AdminSmOrgTreeNodeBo>> getOrgtreeByParam(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        AdminSmOrgTreeQuery query = new AdminSmOrgTreeQuery();
        query.setOrgId(params.get(ORG_ID));
        query.setOrgSts(AvailableStateEnum.ENABLED);
        List<AdminSmOrgTreeNodeBo> orgTree = adminSmOrgService.getOrgTree(query);
        return JClientRspEntity.buildSuccess(orgTree);
    }

    /**
     * 不太完美的接口
     */
    @PostMapping("/getorg")
    public JClientRspEntity<List<AdminSmOrgTreeNodeBo>> getOrgByParam(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        AdminSmOrgTreeQuery query = new AdminSmOrgTreeQuery();
        query.setOrgId(params.get(ORG_ID));
        query.setOrgSts(AvailableStateEnum.ENABLED);
        List<AdminSmOrgTreeNodeBo> orgTree = adminSmOrgService.getOrgTree(query);
        return JClientRspEntity.buildSuccess(orgTree);
    }
    @PostMapping("/getReplaySeq")
    public JClientRspEntity<?> getReplaySeq(@RequestBody JClientReqEntity<String> req) {
        if(seqService != null) {
            return JClientRspEntity.buildSuccess(seqService.getReplaySeq());
        }else{
            return JClientRspEntity.buildFail("500", "功能未开启");
        }
    }
}
