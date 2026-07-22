package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeReadService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统公告用户查阅历史表
 * @author zhangyt12
 * @date 2021/6/24 14:45
 */
@RestController
@RequestMapping("/api/notice/adminsmnoticeread")
public class AdminSmNoticeReadController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmNoticeReadController.class);

    @Autowired
    private AdminSmNoticeReadService adminSmNoticeReadService;

    /**
     * 保存查看公告记录
     * @param noticeIds 批量保存，接收多个公告 id
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/save")
    public JClientRspEntity<Object> recordRead(@RequestBody JClientReqEntity<List<String>> noticeIds){
        log.info("已读参数-------------------------：" + noticeIds.getBody());
		adminSmNoticeReadService.recordRead(noticeIds.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }
}
