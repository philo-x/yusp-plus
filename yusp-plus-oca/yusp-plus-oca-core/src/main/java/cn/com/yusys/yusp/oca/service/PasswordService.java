package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.PasswordCheckBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordModifyBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordResetBo;

/**
 * 密码服务接口
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public interface PasswordService {
    /**
     * 校验用户密码
     *
     * @param passwordCheckBo 密码参数
     * @return 校验结果
     */
    JClientRspEntity<String> checkPasswrod(PasswordCheckBo passwordCheckBo);

    /**
     * 密码修改
     *
     * @param passwordModifyBo 修改密码参数
     * @return 修改结果
     */
    JClientRspEntity<String> passwordModification(PasswordModifyBo passwordModifyBo);

    /**
     * 密码重置
     *
     * @param passwordResetBo 重置密码参数
     * @return 密码重置结果
     */
    JClientRspEntity<String> resetPassword(PasswordResetBo passwordResetBo);
}
