package cn.com.yusys.yusp.oca.passwordstrategy;

import cn.com.yusys.yusp.oca.domain.constants.Constants;
import org.passay.LengthRule;
import org.passay.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码长度策略
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Component
public class LengthHandler implements Handler {
    @Override
    public List<Rule> getPasswordStrategy(String name, String detail) {
        ArrayList<Rule> passwordStrategy = new ArrayList<>();
        //从参数中获取密码复杂度组成
        passwordStrategy.add(new LengthRule(PasswordStrategyConstant.PASSWD_MIN_LENGTH,PasswordStrategyConstant.PASSWD_MAX_LENGTH));
        return passwordStrategy;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        PasswordFactory.register(Constants.SystemUserConstance.PASSWD_LENGTH_RULE,this);
        PasswordFactory.registerErrorCode("TOO_SHORT",PasswordStrategyConstant.PASSWD_LENGTH_ERROR);

    }
}
