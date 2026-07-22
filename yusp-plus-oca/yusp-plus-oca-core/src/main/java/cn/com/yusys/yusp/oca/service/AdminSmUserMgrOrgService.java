package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserMgrOrgEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户授权管理机构表
 *
 * @author terry
 * @date 2020-12-02 16:29:50
 */
public interface AdminSmUserMgrOrgService extends IService<AdminSmUserMgrOrgEntity> {

    /**
     * 查询用户机构列表
     *
     * @param userId 用户id
     * @return 用户关联的机构id集合
     */
    List<String> findOrgRelsByUser(String userId);
}

