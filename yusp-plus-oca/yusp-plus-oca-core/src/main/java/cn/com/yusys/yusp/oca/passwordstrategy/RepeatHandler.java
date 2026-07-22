package cn.com.yusys.yusp.oca.passwordstrategy;

import cn.com.yusys.yusp.oca.domain.constants.Constants;
import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码重复策略
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Component
public class RepeatHandler implements Handler {
    @Override
    public List<Rule> getPasswordStrategy(String name, String detail) {
        ArrayList<Rule> passwordStratety = new ArrayList<>();
        passwordStratety.add(new CharacterOccurrencesRule(Integer.parseInt(detail)));
        return passwordStratety;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        PasswordFactory.register(Constants.SystemUserConstance.PASSWD_REPETNUMBER_RULE,this);
        PasswordFactory.registerErrorCode("TOO_MANY_OCCURRENCES",PasswordStrategyConstant.PASSWD_REPEAT_ERROR);

    }
}
