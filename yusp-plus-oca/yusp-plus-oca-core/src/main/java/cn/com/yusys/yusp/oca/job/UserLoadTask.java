package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.dao.AdminSmUserDao;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 用户信息初始化到缓存
 *
 * @author: zhangsong
 * @date: 2021/3/31
 */
@Component("userLoadTask")
public class UserLoadTask implements ITask {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserLoadTask.class);
    @Autowired
    private AdminSmUserService adminSmUserService;
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private AdminSmUserDao adminSmUserDao;
    @Autowired
    @Qualifier("asyncLogThreadPoolExecutor")
    private ThreadPoolExecutor executor;


    private static int PAGE_NUM = 1000;

    @Override
    public void run(String params) {
        log.info("userLoadTask定时任务正在执行，参数为：{}", params);

        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        //用户按tenantid分组
        List<Map<String, Object>> list = adminSmUserDao.groupDataTenantCount();
        for(Map<String, Object> map : list) {
            String dataTenantId = (String) map.get("tenantid");
            //删除用户信息
            customCacheService.clear(name, dataTenantId, redisKey);
            Object tanantCount=map.get("tenantcount");
            if (ObjectUtils.isEmpty(tanantCount)) {
                return;
            }
            int userCount = Integer.parseInt(tanantCount.toString());
            if(userCount == 0){
                return;
            }
            int userTotalPage = getTotalPage(userCount);
            List<Integer> userPageNumbers = getPageNumbers(userTotalPage);
            QueryWrapper<AdminSmUserEntity> eq = new QueryWrapper<AdminSmUserEntity>().eq("DATA_TENANT_ID", dataTenantId);
            for (int i = 0; i < userPageNumbers.size(); i++) {
                IPage<AdminSmUserEntity> page = new Page<>(userPageNumbers.get(i), PAGE_NUM);
                IPage<AdminSmUserEntity> adminSmUserEntityIPage = adminSmUserDao.selectPage(page, eq);
                Map<String, String> userMap = adminSmUserEntityIPage.getRecords().stream().collect(Collectors.toMap(AdminSmUserEntity::getUserId, AdminSmUserEntity::getUserName));
                //添加admin用户，因为admin创建了租户，所以最新更新人那里要有admin
                if(i == 0){
                    userMap.put("40","system administrator");
                }
                customCacheService.hashPut(name,dataTenantId, redisKey, userMap, 24 * 15 * 3600);
            }
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
