package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 更新机构表ORG_SEQ字段
 * @author: Zhan YongQiang
 * @date: 2021/8/16 16:07
 */
@Component("orgSeqUpdateTask")
public class OrgSeqUpdateTask implements ITask {
private static final Logger log = LoggerFactory.getLogger(OrgSeqUpdateTask.class);

    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Override
    public void run(String params) {
        log.info("org_seq字段更新任务【orgSeqUpdateTask】开始");
        if(StringUtils.isEmpty(params)){
            log.error("机构表ORG_SEQ更新任务，需要传入参数:\"{deep:integer,flag:boolean}\"");
            throw new IllegalArgumentException("机构表ORG_SEQ更新任务，非法参数");
        }
        JSONObject paramJson = JSON.parseObject(params);
        int deep = paramJson.getIntValue("deep");
        boolean flag = paramJson.getBoolean("flag");
        for (int i = 1; i <= deep; i++) {
            adminSmOrgService.updateOrgSeqForTask(i,flag);
        }
        log.info("org_seq字段更新任务【orgSeqUpdateTask】结束");
    }
}
