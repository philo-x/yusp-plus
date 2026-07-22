package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户角色关联表
 *
 * @author terry
 * @date 2020-12-01 21:55:19
 */
public interface AdminSmUserDutyRelService extends IService<AdminSmUserDutyRelEntity> {


    /**
     * 用户已关联岗位
     *
     * @param user 用户实体类
     * @return 用户岗位id集合
     */
    List<String> findUserDutyRelsByUser(AdminSmUserEntity user);

    /**
     * 新增用户角色关联信息
     *
     * @param entityList 用户角色关联列表
     * @return true 新增成功
     */
    boolean save(List<AdminSmUserDutyRelEntity> entityList);

}

