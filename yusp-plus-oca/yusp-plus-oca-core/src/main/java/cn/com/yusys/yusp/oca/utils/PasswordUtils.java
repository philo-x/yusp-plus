package cn.com.yusys.yusp.oca.utils;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.req.crypto.replay.RequestReplaySeq;
import cn.com.yusys.yusp.commons.util.encrypt.BCRSAUtils;
import cn.com.yusys.yusp.oca.domain.constants.MessageEnums;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * @program: yusp-app-framework
 * @description: 密码工具类
 * @author: wujiangpeng
 * @email: wujp4@yusys.com.cn
 * @create: 2020-11-04 09:18
 */
@Component
public class PasswordUtils {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PasswordUtils.class);
    @Autowired
    AdminSmUserService adminSmUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    I18nMessageByCode i18nMessageByCode;

    @Autowired(required = false)
    private RequestReplaySeq seqService;
    /**
     * 检查密码是否正确
     *
     * @param rawPassword
     * @param loginCode
     * @return
     */
    public boolean matchUserPassword(String rawPassword, String loginCode, String seq) {

        AdminSmUserEntity userEntity = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));
        if (userEntity != null) {
            String passwordDb = userEntity.getUserPassword();
            return checkSecret(passwordDb, rawPassword, seq);
        } else {
            log.error("用户不存在");
            throw BizException.error(null,ResponseAndMessageEnum.NON_USER.getCode(),i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.NON_USER.getCode()));
        }
    }

    /**
     * 校验密码
     *
     * @param dbPassword
     * @param rawPassword
     * @return
     */
    public boolean checkSecret(String dbPassword, String rawPassword, String seq) {
        //密码校验
        try {
            //密码已密钥加密，先解密
            rawPassword = dePassword(rawPassword, true, seq);
            //如果密码防重返回为空 则直接返回 不再判断密码
            if (rawPassword==null){
                return false;
            }else{
                //密码匹配
                return passwordEncoder.matches(rawPassword, dbPassword);
            }
        } catch (Exception e) {
            log.error("密码校验失败");
            return false;
        }
    }

    /**
     * 密文解密
     *
     * @param password
     * @return
     */
    public String dePassword(String password) {
        return dePassword(password, false, null);
    }
    public String dePassword(String password, boolean check, String seq) {
        try {
            password = BCRSAUtils.decryptByPrivate(password);
            if(seqService != null){
                int pindex = password.indexOf("-");
                if(pindex < 0){
                    log.error("密码未进行防重处理");
                    throw BizException.error(null,MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode(),i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode()));
                }else if(check){
                    String num = password.substring(0, pindex);
                    if (!seqService.checkReplaySeq(seq, num)) {
                        log.error("密码防重验证失败");
                        return null;
                    } else {
                        password = password.substring(pindex + 1);
                    }
                }else{
                    password = password.substring(pindex + 1);
                }
            }
            return password;
        } catch (BizException e1){
            throw e1;
        }catch (Exception e) {
            log.error("密码解密失败");
            throw BizException.error(null,MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode(),i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode()));
        }
    }

    /**
     * 密文加密
     *
     * @param password
     * @return
     */
    public String enPassword(String password) {
        try {
            String cipherText = dePassword(password);
            password = new BCryptPasswordEncoder().encode(cipherText);
            return password;
        } catch (Exception e) {
            log.error("密码加密失败");
            throw BizException.error(null,MessageEnums.PASSWORD_ENCODE_FAILED.getCode(),i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_ENCODE_FAILED.getCode()));
        }
    }

    public String enPasswordNormal(String password) {
        try {
            String cipherText = BCRSAUtils.decryptByPrivate(password);
            password = new BCryptPasswordEncoder().encode(cipherText);
            return password;
        }catch (Exception e) {
            log.error("密码解密失败");
            throw BizException.error(null,MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode(),i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode()));
        }
    }
}
