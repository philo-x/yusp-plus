package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPropEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPropQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统信息业务接口
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public interface AdminSmPropService extends IService<AdminSmPropEntity> {

    /**
     * 查询系统参数列表
     * @param adminSmPropQuery
     * @return 系统分页数据
     */
    Page<AdminSmPropEntity> queryPropPage(AdminSmPropQuery adminSmPropQuery);

    /**
     * 新增系统参数
     * @param adminSmProp
     */
    void saveProp(AdminSmPropSaveBo adminSmProp);

    /**
     * 修改系统参数
     * @param adminSmProp
     */
    void updatePropById(AdminSmPropEditBo adminSmProp);
}

