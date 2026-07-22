package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmFuncModDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmFuncModBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmFuncModEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmFuncModQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;
import cn.com.yusys.yusp.oca.service.AdminSmBusiFuncService;
import cn.com.yusys.yusp.oca.service.AdminSmFuncModService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统模块管理实现类
 *
 * @author zhanyq
 * @date 2021-06-25 10:03
 */
@Service("adminSmFuncModService")
public class AdminSmFuncModServiceImpl extends ServiceImpl<AdminSmFuncModDao, AdminSmFuncModEntity> implements AdminSmFuncModService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AdminSmBusiFuncService adminSmBusiFuncService;

    private static final String MOD_ID = "mod_id";

    @Override
    public Page<AdminSmFuncModVo> queryPageWithCondition(AdminSmFuncModQuery adminSmFuncModQuery) {

        Page<AdminSmFuncModVo> iPage = adminSmFuncModQuery.getIPage();


        QueryWrapper<AdminSmFuncModVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.nonEmpty(adminSmFuncModQuery.getModelName()), "m.mod_name", adminSmFuncModQuery.getModelName());

        queryWrapper.orderByDesc("m.last_chg_dt");

        return getBaseMapper().queryPageWithCondition(iPage, queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int removeByModId(String[] modIdStr) {
        // 查询是否有关联业务功能
        List<AdminSmBusiFuncEntity> busiFuncList = adminSmBusiFuncService.list(new QueryWrapper<AdminSmBusiFuncEntity>()
                .in(MOD_ID, modIdStr));
        if (busiFuncList.size() > 0) {
            return -1;
        }
        return getBaseMapper().deleteBatchIds(Arrays.asList(modIdStr));
    }


    @Override
    public List<String> checkName(String modName, String modId) {
        List<String> funcModIdList = (List<String>) (List) getBaseMapper().selectObjs(new QueryWrapper<AdminSmFuncModEntity>()
                .select(MOD_ID).eq(!StringUtils.isEmpty(modName), "mod_name", modName)
                .ne(!StringUtils.isEmpty(modId), MOD_ID, modId)
        );
        return funcModIdList;
    }


    @Override
    public int saveFuncMod(AdminSmFuncModBo adminSmFuncBo) {

        // 校验 模块名是否已存在
        List<String> strings = this.checkName(adminSmFuncBo.getModName(), adminSmFuncBo.getModId());
        if (strings.size() > 0) {
            return -1;
        }
        AdminSmFuncModEntity adminSmFuncModEntity =
                BeanUtils.beanCopy(adminSmFuncBo, AdminSmFuncModEntity.class);
        adminSmFuncModEntity.setLastChgUsr(SessionUtils.getUserId());
        return getBaseMapper().insert(adminSmFuncModEntity);
    }


    @Override
    public int updateFuncMod(AdminSmFuncModBo adminSmFuncBo) {

        // 校验 模块名是否已存在
        List<String> strings = this.checkName(adminSmFuncBo.getModName(), adminSmFuncBo.getModId());
        if (strings.size() > 0) {
            return -1;
        }
        AdminSmFuncModEntity adminSmFuncModEntity =
                BeanUtils.beanCopy(adminSmFuncBo, AdminSmFuncModEntity.class);
        adminSmFuncModEntity.setLastChgUsr(SessionUtils.getUserId());
        return getBaseMapper().updateById(adminSmFuncModEntity);
    }
}