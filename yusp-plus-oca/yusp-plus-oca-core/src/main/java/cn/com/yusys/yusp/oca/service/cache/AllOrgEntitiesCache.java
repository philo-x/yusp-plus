package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmOrgDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yusys
 */
@Component
public class AllOrgEntitiesCache {

    private static final String ORG_REDIS_VALUE = "Organizations";

    @Autowired
    private AdminSmOrgDao adminSmOrgDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<AdminSmOrgEntity> getAllOrgEntities() {
        BoundHashOperations<String, String, AdminSmOrgEntity> hashOps = redisTemplate.opsForHash().getOperations().boundHashOps(ORG_REDIS_VALUE);
        //目前强制走数据库
        int cacheSize = hashOps.size() == 0 ? 0 : 0;
        //没缓存就刷新缓存
        if (cacheSize == 0) {
            LambdaQueryWrapper<AdminSmOrgEntity> wrapper = Wrappers.lambdaQuery();
            //todo 机构树直接按更新时间排序,有需求再加
            wrapper.orderByDesc(AdminSmOrgEntity::getLastChgDt);
            List<AdminSmOrgEntity> list = this.adminSmOrgDao.selectList(wrapper);
            Map<String, AdminSmOrgEntity> collect = list.stream().collect(Collectors.toMap(AdminSmOrgEntity::getOrgId, Function.identity()));
            hashOps.putAll(collect);
            //全表查
            return list;
        } else {
            //有缓存直接返回
            Map<String, AdminSmOrgEntity> map = hashOps.entries();
            return new ArrayList<>(map.values());
        }
    }

    public void addOrUpdateCache(AdminSmOrgEntity entity) {
        if (null != entity && StringUtils.nonEmpty(entity.getOrgId())) {
            BoundHashOperations<String, String, AdminSmOrgEntity> hashOps = redisTemplate.opsForHash().getOperations().boundHashOps(ORG_REDIS_VALUE);
            hashOps.put(entity.getOrgId(), entity);
        }
    }

    public void clearCache(Object[] orgIds) {
        if (orgIds.length > 0) {
            BoundHashOperations<String, String, AdminSmOrgEntity> hashOps = redisTemplate.opsForHash().getOperations().boundHashOps(ORG_REDIS_VALUE);
            hashOps.delete(orgIds);
        }
    }

}
