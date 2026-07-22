package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthTmplService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmResContrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询授权数据的 service 层实现类
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@Service("authTreeNoCacheService")
public class AuthTreeNoCacheServiceImpl implements AuthTreeService {

    @Autowired
    private AdminSmMenuService adminSmMenuService;
    @Autowired
    private AdminSmResContrService adminSmResContrService;
    @Autowired
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;

    @Override
    public List<AdminSmAuthTreeVo> getUserAuthMenuData(String id, String dataTenantId) {
        return  adminSmMenuService.selectAuthTree(id, dataTenantId);
    }

    @Override
    public List<AdminSmAuthTreeVo> getUserAuthDataTmplData(String id, String dataTenantId) {
        return  adminSmDataAuthTmplService.selectAuthTree(id, dataTenantId);
    }

    @Override
    public List<AdminSmAuthTreeVo> getUserAuthContrData(String id, String dataTenantId) {
        return  adminSmResContrService.selectAuthTree(id,dataTenantId);
    }
}
