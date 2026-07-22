package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import cn.com.yusys.yusp.oca.passwordstrategy.Handler;
import cn.com.yusys.yusp.oca.passwordstrategy.PasswordFactory;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.OcaCommonInfoUtils;
import cn.com.yusys.yusp.oca.utils.PasswordUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码策略校验
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class PasswordStrategyCheck implements ConstraintValidator<PasswordStrategy, String> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PasswordStrategyCheck.class);
    @Autowired
    AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    PasswordUtils passwordUtils;
    @Autowired
    I18nMessageByCode i18nMessageByCode;


    @Override
    public void initialize(PasswordStrategy passwordStrategy) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        //密码解密
        String dePassord = passwordUtils.dePassword(password);
        String sysId = OcaCommonInfoUtils.getUserSysId();
        //查询密码策略名称和明细
        List<AdminSmCrelStraEntity> adminSmCrelStraEntities = adminSmCrelStraService.list(new QueryWrapper<AdminSmCrelStraEntity>()
                .eq("SYS_ID", sysId)
                .eq("ENABLE_FLAG", String.valueOf(1))
                .like("CREL_KEY", "PASSWD")
                .ne("CREL_KEY", "PASSWORD_COMPEL_CHANGE"));

        if (CollectionUtils.isEmpty(adminSmCrelStraEntities)) {
            return true;
        }

        //获取校验规则
        List<List<Rule>> collect = adminSmCrelStraEntities.stream().map(item -> {
            Handler invokeStrategy = PasswordFactory.getInvokeStrategy(item.getCrelKey());
            if (Constants.SystemUserConstance.PASSWD_SEQUNNUMBER_RULE.equals(item.getCrelKey()) && Integer.parseInt(item.getCrelDetail()) < 3) {
                log.error("不重复字符长度必须大于等于3个");
                throw new RuntimeException("不重复字符长度必须大于等于3个");
            }
            return invokeStrategy.getPasswordStrategy(item.getCrelKey(), item.getCrelDetail());
        }).collect(Collectors.toList());
        ArrayList<Rule> rules = new ArrayList<>();
        if (CollectionUtils.nonEmpty(collect)) {
            collect.forEach(rules::addAll);
        }
        //密码校验
        PasswordValidator passwordValidator = new PasswordValidator(rules);
        RuleResult validate = passwordValidator.validate(new PasswordData(dePassord));

        //如果校验通过，直接返回
        if (validate.isValid()) {
            return true;
        }

        //验证失败
        //获取自定义错误码
        String errorCode = PasswordFactory.getErrorCode(validate.getDetails().get(0).getErrorCode());

        //根据当前语言获取错误信息
        String message = i18nMessageByCode.getMessageByCode(errorCode);

        if (StringUtils.nonEmpty(message)) {
            Object[] values = validate.getDetails().get(0).getValues();
            if ("10101001".equals(errorCode)) {
                List<AdminSmCrelStraEntity> collect1 = adminSmCrelStraEntities.stream().filter(item -> {
                    return Constants.SystemUserConstance.PASSWD_SEQUNNUMBER_RULE.equals(item.getCrelKey());
                }).toList();
                message = MessageFormat.format(message, collect1.get(0).getCrelDetail());
            }
            message = MessageFormat.format(message, values);
            //组合返回错误信息
            message = errorCode + "&" + message;
        } else {
            message = String.join(",", passwordValidator.getMessages(validate));
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
