package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmPropDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmPropSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPropEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPropQuery;
import cn.com.yusys.yusp.oca.service.AdminSmPropService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 系统信息业务处理类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service("adminSmPropService")
public class AdminSmPropServiceImpl extends ServiceImpl<AdminSmPropDao, AdminSmPropEntity> implements AdminSmPropService {

    /**
     * 分页查询系统信息
     * @param adminSmPropQuery 查询条件
     * @return 返回信息
     */
    @Override
    public Page<AdminSmPropEntity> queryPropPage(AdminSmPropQuery adminSmPropQuery) {
        //获取分页参数
        Page<AdminSmPropEntity> page = adminSmPropQuery.getIPage();
        //创建查询wrapper
        LambdaQueryWrapper<AdminSmPropEntity> wrapper = Wrappers.lambdaQuery();
        //添加查询条件
        Optional.ofNullable(adminSmPropQuery.getKeyWord()).ifPresent((param) -> wrapper.like(AdminSmPropEntity::getPropName, param).or().like(AdminSmPropEntity::getPropDesc, param));
        Optional.ofNullable(adminSmPropQuery.getPropName()).ifPresent((param) -> wrapper.like(AdminSmPropEntity::getPropName, param));
        Optional.ofNullable(adminSmPropQuery.getPropDesc()).ifPresent((param) -> wrapper.like(AdminSmPropEntity::getPropDesc, param));
        wrapper.orderByDesc(AdminSmPropEntity::getLastChgDt);
        //分页查询
        return getBaseMapper().selectPage(page, wrapper);
    }

    /**
     * 保存系统信息
     * @param adminSmProp 系统信息参数
     */
    @Override
    public void saveProp(AdminSmPropSaveBo adminSmProp) {
        //校验新增系统参数名是否已经存在
        List<AdminSmPropEntity> adminSmPropEntities = this.list(new QueryWrapper<AdminSmPropEntity>().eq("PROP_NAME", adminSmProp.getPropName()));
        if (CollectionUtils.nonEmpty(adminSmPropEntities)) {
            throw BizException.error(null, "50600003",
                    null, "系统参数名:" + adminSmProp.getPropName() + "已存在！");
        }
        //创建新增对象
        AdminSmPropEntity adminSmPropEntity = new AdminSmPropEntity();
        //新增对象添加数据
        BeanUtils.beanCopy(adminSmProp, adminSmPropEntity);
        adminSmPropEntity.setInstuId(SessionUtils.getUserInformation().getInstuOrg().getId());
        adminSmPropEntity.setLastChgUsr(SessionUtils.getUserId());
        adminSmPropEntity.setLastChgDt(new Date());
        //新增
        this.save(adminSmPropEntity);
    }

    /**
     * 更新系统信息
     * @param adminSmProp 系统信息
     */
    @Override
    public void updatePropById(AdminSmPropEditBo adminSmProp) {
        //校验新增系统参数名是否已经存在
        List<AdminSmPropEntity> adminSmPropEntities = this.list(new QueryWrapper<AdminSmPropEntity>().eq("PROP_NAME", adminSmProp.getPropName()).ne("PROP_ID", adminSmProp.getPropId()));
        if (CollectionUtils.nonEmpty(adminSmPropEntities)) {
            throw BizException.error(null, "50600003",
                    null, "系统参数名:" + adminSmProp.getPropName() + "已存在！");
        }
        //创建修改对象
        AdminSmPropEntity adminSmPropEntity = new AdminSmPropEntity();
        //修改对象添加数据
        BeanUtils.beanCopy(adminSmProp, adminSmPropEntity);
        adminSmPropEntity.setInstuId(SessionUtils.getUserInformation().getInstuOrg().getId());
        adminSmPropEntity.setLastChgUsr(SessionUtils.getUserId());
        adminSmPropEntity.setLastChgDt(new Date());
        //修改对象
        this.updateById(adminSmPropEntity);
    }
}