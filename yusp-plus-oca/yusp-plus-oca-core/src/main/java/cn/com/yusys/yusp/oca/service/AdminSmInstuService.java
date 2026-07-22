package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmInstuEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmInstuQuery;
import cn.com.yusys.yusp.oca.domain.vo.InstuExtVo;
import cn.com.yusys.yusp.oca.domain.vo.UserSessionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 金融机构表
 *
 * @author terry
 * @email tanrui1@yusys.com.cn
 * @date 2020-11-19 14:30:22
 */
public interface AdminSmInstuService extends IService<AdminSmInstuEntity> {

    /**
     * 分页查询
     * @param params
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-30 17:55
     */
    Page<InstuExtVo> queryPage(AdminSmInstuQuery params);

    /**
     * 保存
     * @param entity 金融机构实体
     * @param userSessionVo 用户session
     * @return boolean
     * @author zhanyq
     * @date 2021-06-30 17:56
     */
    boolean save(AdminSmInstuEntity entity, UserSessionVo userSessionVo);

    /**
     * 批量启用
     * @param ids 金融机构ID
     * @return boolean
     * @author zhanyq
     * @date 2021-06-30 17:57
     */
    boolean batchEnable(String ids);

    /**
     * ids
     * @param ids 金融机构ID
     * @return boolean
     * @author zhanyq
     * @date 2021-06-30 17:57
     */
    boolean batchDisable(String ids);

    /**
     * 批量删除
     * @param ids 金融机构ID
     * @return
     * @author zhanyq
     * @date 2021-06-30 17:58
     */
    boolean batchDelete(String ids);

    /**
     * 修改
     * @param entity 要修改的数据
     * @return boolean
     * @author zhanyq
     * @date 2021-06-30 17:59
     */
    @Override
    boolean updateById(AdminSmInstuEntity entity);
}

