package cn.com.yusys.yusp.oca.passwordstrategy;

import cn.com.yusys.yusp.oca.domain.constants.Constants;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 连续密码策略
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Component
public class ContinuousHandler implements Handler {
    @Override
    public List<Rule> getPasswordStrategy(String name, String detail) {
        ArrayList<Rule> passwordStratety = new ArrayList<>();
        passwordStratety.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, Integer.parseInt(detail), false));
        return passwordStratety;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        PasswordFactory.register(Constants.SystemUserConstance.PASSWD_SEQUNNUMBER_RULE, this);
        PasswordFactory.registerErrorCode("ILLEGAL_ALPHABETICAL_SEQUENCE",PasswordStrategyConstant.PASSWD_CONTINUOUS_ERROR);

    }
}
