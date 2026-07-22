package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.service.AdminSmTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjing
 */
public abstract class AbstractCustomCacheService implements CustomCacheService {

    @Autowired
    private AdminSmTenantService adminSmTenantService;
    /**
     * 所有租户共享
     */
    private static final Map<String, String> SHARED_FIELD_MAP = new HashMap<>(8);

    static {
        SHARED_FIELD_MAP.put("dptName", Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME);
        SHARED_FIELD_MAP.put("orgName", Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME);
        SHARED_FIELD_MAP.put("userName", Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME);
    }


    protected String getResetName(String name) {
        if (SHARED_FIELD_MAP.containsKey(name)) {
            String tanantId = adminSmTenantService.getTanentId();
            if (!ObjectUtils.isEmpty(tanantId)) {
                return name + ":" + tanantId;
            }
        }
        return name;
    }

}
