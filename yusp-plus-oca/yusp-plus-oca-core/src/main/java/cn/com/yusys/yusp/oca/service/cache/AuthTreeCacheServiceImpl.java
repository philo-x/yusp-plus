package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.common.utils.TableConstant;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthTmplService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmResContrService;
import cn.cscb.uadp.tc.cachedependency.constant.CacheLevelEnum;
import cn.cscb.uadp.tc.cachedependency.service.CacheDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询授权数据的 service 层实现类
 * <p></p>
 * note：目前 ocaplus 实际使用的是 {@link AuthTreeNoCacheServiceImpl}
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@Service("authTreeCacheService")
public class AuthTreeCacheServiceImpl implements AuthTreeService {

    @Autowired
    private AdminSmMenuService adminSmMenuService;
    @Autowired
    private AdminSmResContrService adminSmResContrService;
    @Autowired
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;
    @Autowired
    private CacheDependencyService cacheDependencyService;

    private static final String CACHE_KEY = "#root.methodName + ':' + #p1 + '-' + #p0";

    private static final String CACHE_PREFIX = "AuthTreeCache:";

    @Override
    @Cacheable(value = "AuthTreeCache@5M", key = CACHE_KEY)
    public List<AdminSmAuthTreeVo> getUserAuthMenuData(String id, String dataTenantId) {
        cacheDependencyService.recordDependencies(CACHE_PREFIX + "getUserAuthMenuData:" + dataTenantId + "-" + id, CacheLevelEnum.SECONDS.getLevel(), TableConstant.ADMIN_SM_MENU, TableConstant.ADMIN_SM_AUTH_RECO);
        return adminSmMenuService.selectAuthTree(id, dataTenantId);
    }

    @Override
    @Cacheable(value = "AuthTreeCache@5M", key = CACHE_KEY)
    public List<AdminSmAuthTreeVo> getUserAuthDataTmplData(String id, String dataTenantId) {
        cacheDependencyService.recordDependencies(CACHE_PREFIX + "getUserAuthDataTmplData:" + dataTenantId + "-" + id, CacheLevelEnum.SECONDS.getLevel(), TableConstant.ADMIN_SM_AUTH_RECO, TableConstant.ADMIN_SM_DATA_AUTH_TMPL, TableConstant.ADMIN_SM_DATA_AUTH);
        return adminSmDataAuthTmplService.selectAuthTree(id, dataTenantId);
    }

    @Override
    @Cacheable(value = "AuthTreeCache@5M", key = CACHE_KEY)
    public List<AdminSmAuthTreeVo> getUserAuthContrData(String id, String dataTenantId) {
        cacheDependencyService.recordDependencies(CACHE_PREFIX  + "getUserAuthContrData:" + dataTenantId + "-" + id, CacheLevelEnum.SECONDS.getLevel(),
                TableConstant.ADMIN_SM_RES_CONTR, TableConstant.ADMIN_SM_AUTH_RECO);
        return adminSmResContrService.selectAuthTree(id, dataTenantId);
    }

}
