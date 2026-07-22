package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmBusiFuncDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmBusiFuncBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmResContrEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmBusiFuncQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmResContrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务功能管理service实现类
 *
 * @author zhanyq
 * @date 2021-06-24 14:53
 */
@Service("adminSmBusiFuncService")
public class AdminSmBusiFuncServiceImpl extends ServiceImpl<AdminSmBusiFuncDao, AdminSmBusiFuncEntity> implements AdminSmBusiFuncService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AdminSmResContrService adminSmResContrService;
    @Autowired
    AdminSmMenuService adminSmMenuService;



    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int saveBusiFuncByBo(AdminSmBusiFuncBo adminSmBusiFuncBo) {
        //校验同模块业务功能名称是否已经存在
        List<AdminSmBusiFuncEntity> adminSmBusiFuncEntities = this.list(new QueryWrapper<AdminSmBusiFuncEntity>().eq("FUNC_NAME", adminSmBusiFuncBo.getFuncName()).eq("MOD_ID", adminSmBusiFuncBo.getModId()));
        if (CollectionUtils.nonEmpty(adminSmBusiFuncEntities)) {
            throw BizException.error(null, "50600005",
                    null, "同模块下业务功能名称:" + adminSmBusiFuncBo.getFuncName() + "已存在！");
        }
        // bean copy
        AdminSmBusiFuncEntity adminSmBusiFuncEntity =
                BeanUtils.beanCopy(adminSmBusiFuncBo, AdminSmBusiFuncEntity.class);
        adminSmBusiFuncEntity.setFuncOrder(new AtomicInteger(1).intValue());
        adminSmBusiFuncEntity.setLastChgUsr(SessionUtils.getUserId());
        adminSmBusiFuncEntity.setFuncId(StringUtils.getUUID());
        return getBaseMapper().insert(adminSmBusiFuncEntity);
    }

    @Override
    public void updateBusiFunc(AdminSmBusiFuncBo adminSmBusiFuncBo) {
        //校验同模块业务功能名称是否已经存在
        List<AdminSmBusiFuncEntity> adminSmBusiFuncEntities = this.list(new QueryWrapper<AdminSmBusiFuncEntity>().eq("FUNC_NAME", adminSmBusiFuncBo.getFuncName()).eq("MOD_ID", adminSmBusiFuncBo.getModId()).ne("FUNC_ID", adminSmBusiFuncBo.getFuncId()));
        if (CollectionUtils.nonEmpty(adminSmBusiFuncEntities)) {
            throw BizException.error(null, "50600005",
                    null, "同模块下业务功能名称:" + adminSmBusiFuncBo.getFuncName() + "已存在！");
        }
        AdminSmBusiFuncEntity adminSmBusiFuncEntity = new AdminSmBusiFuncEntity();
        BeanUtils.beanCopy(adminSmBusiFuncBo, adminSmBusiFuncEntity);
        this.updateById(adminSmBusiFuncEntity);
    }


    @Override
    public Page<AdminSmBusiFuncVo> queryPageWithCondition(AdminSmBusiFuncQuery adminSmBusiFuncQuery) {

        Page<AdminSmBusiFuncVo> iPage = adminSmBusiFuncQuery.getIPage();
        String modId = adminSmBusiFuncQuery.getModId();
        String keyWord = adminSmBusiFuncQuery.getKeyWord();

//
        QueryWrapper<AdminSmBusiFuncVo> busiFuncQuery = new QueryWrapper<>();
        //非空条件查
        busiFuncQuery.eq(!StringUtils.isEmpty(modId), "f.mod_id", modId);
        //模糊查
        if (StringUtils.nonEmpty(keyWord)) {
            busiFuncQuery.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(keyWord), "f.func_name", keyWord)
                    .or()
                    .like(StringUtils.nonEmpty(keyWord), "f.func_url", keyWord)
            );
        }
        busiFuncQuery.orderByDesc("f.last_chg_dt");
        return getBaseMapper().queryPageWithCondition(iPage, busiFuncQuery);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void removeFuncByIds(String[] funcIds) {
        //判断是否和控制点有关联
        List<AdminSmResContrEntity> resContrList = adminSmResContrService.list(new QueryWrapper<AdminSmResContrEntity>()
                .in("func_id", Arrays.asList(funcIds)));
        if (!resContrList.isEmpty()) {
            throw BizException.error(null, "50300001", "业务功能已关联控制点，不能删除！");
        }
        getBaseMapper().deleteByIds(Arrays.asList(funcIds));
    }

    @Override
    public Page<AdminSmBusiFuncVo> getFuncInfoWithCondition(AdminSmBusiFuncQuery adminSmBusiFuncQuery) {

        Page<AdminSmBusiFuncVo> iPage = adminSmBusiFuncQuery.getIPage();
        String queryKey = adminSmBusiFuncQuery.getQueryKey();

        QueryWrapper<AdminSmBusiFuncVo> busiFuncQuery = new QueryWrapper<>();
        //模糊查
        if (StringUtils.nonEmpty(queryKey)) {

            busiFuncQuery.and(wrapper -> wrapper
                    .like(!StringUtils.isEmpty(queryKey), "m.mod_name", queryKey)
                    .or()
                    .like(!StringUtils.isEmpty(queryKey), "f.func_name", queryKey)
                    .or()
                    .like(!StringUtils.isEmpty(queryKey), "f.func_url", queryKey)
            );
        }
        busiFuncQuery.orderByAsc("m.mod_name", "f.func_order");

        return getBaseMapper().getFuncInfoWithConditionDao(iPage, busiFuncQuery);
    }

}