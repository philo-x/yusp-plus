package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.dao.AdminSmOrgDao;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 机构导入
 *
 * @author: zhangsong
 * @date: 2021/3/31
 */
@Component("orgLoadTask")
public class OrgLoadTask implements ITask {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrgLoadTask.class);
    @Autowired
    private AdminSmOrgService adminSmOrgService;
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private AdminSmOrgDao adminSmOrgDao;
    @Autowired
    @Qualifier("asyncLogThreadPoolExecutor")
    private ThreadPoolExecutor executor;

    private static final int PAGE_NUM = 1000;

    @Override
    public void run(String params) {
        log.info("orgLoadTask定时任务正在执行，参数为：{}", params);
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        List<Map<String, Object>> list = adminSmOrgDao.groupDataTenantCount();
        for(Map<String, Object> map : list) {
            String dataTenantId = (String) map.get("tenantid");
            customCacheService.clear(name, dataTenantId, redisKey);
            Object tanantCount=map.get("tenantcount");
            if (ObjectUtils.isEmpty(tanantCount)) {
                return;
            }
            int orgCount = Integer.parseInt(tanantCount.toString());
            int orgTotalPage = getTotalPage(orgCount);
            List<Integer> orgPageNumbers = getPageNumbers(orgTotalPage);
            QueryWrapper<AdminSmOrgEntity> eq = new QueryWrapper<AdminSmOrgEntity>().eq("DATA_TENANT_ID", dataTenantId);
            orgPageNumbers.stream().forEach(pageNumber -> CompletableFuture.runAsync(() -> {
                IPage<AdminSmOrgEntity> page = new Page<>(pageNumber, PAGE_NUM);
                IPage<AdminSmOrgEntity> adminSmOrgEntityIPage = adminSmOrgDao.selectPage(page, eq);
                Map<String, String> orgMap = adminSmOrgEntityIPage.getRecords().stream().collect(Collectors.toMap(AdminSmOrgEntity::getOrgId, AdminSmOrgEntity::getOrgName));
                customCacheService.hashPut(name,dataTenantId, redisKey, orgMap, 24 * 15 * 3600);
            }, executor));
        }
    }

    /**
     * 处理总页数
     *
     * @param totalSize
     * @return 总页数
     */
    private int getTotalPage(int totalSize) {
        return (totalSize % PAGE_NUM == 0)
                ? (totalSize / PAGE_NUM)
                : (totalSize / PAGE_NUM + 1);
    }

    /**
     * 获取页数
     *
     * @param totalPage
     * @return 页数列表
     */
    private List<Integer> getPageNumbers(int totalPage) {
        int pageNumber = 1;
        List<Integer> pageNumbers = new ArrayList<>();
        while (pageNumber <= totalPage) {
            pageNumbers.add(pageNumber++);
        }
        return pageNumbers;
    }
}
