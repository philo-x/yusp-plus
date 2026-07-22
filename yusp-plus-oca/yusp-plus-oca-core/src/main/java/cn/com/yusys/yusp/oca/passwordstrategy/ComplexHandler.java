package cn.com.yusys.yusp.oca.passwordstrategy;

import cn.com.yusys.yusp.oca.domain.constants.Constants;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 密码复杂策略
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Component
public class ComplexHandler implements Handler {
    @Override
    public List<Rule> getPasswordStrategy(String name, String detail) {
        ArrayList<Rule> passwordComplexs = new ArrayList<>();
        //从参数中获取密码复杂度组成
        String regex = ",";
        List<String> strings = Arrays.asList(detail.split(regex));
        //组装密码复杂度规则
        strings.stream().forEach(item -> {
            if (PasswordStrategyConstant.NUMBER.equals(item)) {
                passwordComplexs.add(new CharacterRule(EnglishCharacterData.Digit, PasswordStrategyConstant.CHAR_NUMBER));
            }
            if (PasswordStrategyConstant.UPPERCASE.equals(item)) {
                passwordComplexs.add(new CharacterRule(EnglishCharacterData.UpperCase, PasswordStrategyConstant.CHAR_NUMBER));
            }
            if (PasswordStrategyConstant.LOWERCASE.equals(item)) {
                passwordComplexs.add(new CharacterRule(EnglishCharacterData.LowerCase, PasswordStrategyConstant.CHAR_NUMBER));
            }
            if (PasswordStrategyConstant.SPECIAL.equals(item)) {
                passwordComplexs.add(new CharacterRule(EnglishCharacterData.Special, PasswordStrategyConstant.CHAR_NUMBER));
            }
        });
        return passwordComplexs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        PasswordFactory.register(Constants.SystemUserConstance.PASSWD_COMPLEX_RULE, this);
        PasswordFactory.registerErrorCode("INSUFFICIENT_DIGIT",PasswordStrategyConstant.PASSWD_DIGIT_ERROR);
        PasswordFactory.registerErrorCode("INSUFFICIENT_UPPERCASE",PasswordStrategyConstant.PASSWD_UPPERCASE_ERROR);
        PasswordFactory.registerErrorCode("INSUFFICIENT_LOWERCASE",PasswordStrategyConstant.PASSWD_LOWERCASE_ERROR);
        PasswordFactory.registerErrorCode("INSUFFICIENT_SPECIAL",PasswordStrategyConstant.PASSWD_SPECIAL_ERROR);

    }
}
