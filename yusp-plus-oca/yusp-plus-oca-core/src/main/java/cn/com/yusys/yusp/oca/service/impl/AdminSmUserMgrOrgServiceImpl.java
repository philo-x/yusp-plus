package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.oca.dao.AdminSmUserMgrOrgDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserMgrOrgEntity;
import cn.com.yusys.yusp.oca.service.AdminSmUserMgrOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 用户授权管理机构表
 *
 * @author terry
 * @date 2020-12-11 16:29:50
 */
@Service("adminSmUserMgrOrgService")
public class AdminSmUserMgrOrgServiceImpl extends ServiceImpl<AdminSmUserMgrOrgDao, AdminSmUserMgrOrgEntity> implements AdminSmUserMgrOrgService {
    @Override
    public List<String> findOrgRelsByUser(String userId) {
        Objects.requireNonNull(userId);
        return getBaseMapper().findOrgRelsByUser(userId);
    }

}