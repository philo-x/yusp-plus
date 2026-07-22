package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.dao.AdminSmDptDao;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDptEntity;
import cn.com.yusys.yusp.oca.service.AdminSmDptService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yusys
 */
@Component("dptLoadTask")
public class DptLoadTask implements ITask {

    @Autowired
    private AdminSmDptService adminSmDptService;
    @Autowired
    private CustomCacheService customCacheService;

    @Autowired
    private AdminSmDptDao adminSmDptDao;

    @Override
    public void run(String params) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        List<Map<String, Object>> list = adminSmDptDao.groupDataTenantCount();
        for(Map<String, Object> map : list) {
            String dataTenantId = (String) map.get("tenantid");
            //删除部门信息
            customCacheService.clear(name,dataTenantId, redisKey);
            QueryWrapper<AdminSmDptEntity> eq = new QueryWrapper<AdminSmDptEntity>().eq("DATA_TENANT_ID", dataTenantId);
            //初始化部门信息
            List<AdminSmDptEntity> dptList = adminSmDptService.list(eq);
            Map<String, String> dptMap = dptList.stream().collect(Collectors.toMap(AdminSmDptEntity::getDptId, AdminSmDptEntity::getDptName));
            customCacheService.hashPut(name,dataTenantId, redisKey, dptMap, 24 * 15 * 3600);

        }
    }
}
