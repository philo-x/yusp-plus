package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.oca.dao.AdminSmUserDutyRelDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmUserDutyRelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 用户角色关联表
 *
 * @author terry
 * @date 2020-12-01 21:55:19
 */
@Service("adminSmUserDutyRelService")
public class AdminSmUserDutyRelServiceImpl extends ServiceImpl<AdminSmUserDutyRelDao, AdminSmUserDutyRelEntity> implements AdminSmUserDutyRelService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmUserDutyRelServiceImpl.class);


    @Override
    public List<String> findUserDutyRelsByUser(AdminSmUserEntity user) {
        Objects.requireNonNull(user.getUserId());
        return getBaseMapper().findUserDutyRelsByUser(user.getUserId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean save(List<AdminSmUserDutyRelEntity> entityList) {
        Integer count = 0;
        for (AdminSmUserDutyRelEntity entity : entityList) {
            LambdaQueryWrapper<AdminSmUserDutyRelEntity> checkWrapper = new QueryWrapper<AdminSmUserDutyRelEntity>().lambda();
            checkWrapper.eq(AdminSmUserDutyRelEntity::getDutyId, entity.getDutyId());
            checkWrapper.eq(AdminSmUserDutyRelEntity::getUserId, entity.getUserId());
            AdminSmUserDutyRelEntity check = getBaseMapper().selectOne(checkWrapper);
            if (Objects.nonNull(check)) {
                throw BizException.error("exist", "50900001", "dutyId:" + entity.getDutyId() + ",userId:" + entity.getUserId());
            }
            log.info("New userDutyRel data: [userId: {},dutyId={}] ", entity.getUserId(), entity.getDutyId());
            count += getBaseMapper().insert(entity);
        }
        return count.equals(entityList.size());
    }

}