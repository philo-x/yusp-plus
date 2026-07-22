package cn.com.yusys.yusp.oca.passwordstrategy;

import org.passay.Rule;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * 处理器
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public interface Handler extends InitializingBean {

    /**
     * 获取密码策略
     * @param name 策略名称
     * @param detail 策略参数
     * @return 密码策略
     */
    List<Rule> getPasswordStrategy(String name, String detail);
}
