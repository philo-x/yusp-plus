package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 认证信息表
 *
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-11 14:11:01
 */
public interface AdminSmAuthInfoService extends IService<AdminSmAuthInfoEntity> {

    /**
     * queryPage
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * getAuthKeyValue
     * @return
     */
    List<AdminSmAuthInfoVo> getAuthKeyValue();
}

