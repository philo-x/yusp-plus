package cn.com.yusys.yusp.oca.config;


import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证策略配置
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */

@Deprecated
@Component
public class CrelStraConfig {
    private List<AdminSmCrelStraEntity> strategy;

    public List<AdminSmCrelStraEntity> getStrategy() {
        return strategy;
    }

    public void setStrategy(List<AdminSmCrelStraEntity> strategy) {
        this.strategy = strategy;
    }
}
