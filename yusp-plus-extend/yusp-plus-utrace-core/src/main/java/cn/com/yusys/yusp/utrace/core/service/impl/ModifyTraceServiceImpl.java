package cn.com.yusys.yusp.utrace.core.service.impl;


import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.utrace.core.dao.ModifyTraceDao;
import cn.com.yusys.yusp.utrace.core.domain.entity.ModifyTraceEntity;
import cn.com.yusys.yusp.utrace.core.domain.vo.TraceQueryVo;
import cn.com.yusys.yusp.utrace.core.service.ModifyTraceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小U留痕业务实现类
 *
 * @author zhanyq
 * @date 2021-06-24 13:58
 */
@Service("sModifyTraceService")
public class ModifyTraceServiceImpl extends ServiceImpl<ModifyTraceDao, ModifyTraceEntity> implements ModifyTraceService {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String M_DATETIME = "s.m_datetime";

    @Override
    public Page<ModifyTraceEntity> queryPage(TraceQueryVo params) {
        Page<ModifyTraceEntity> iPage = params.getIPage();
        QueryWrapper<ModifyTraceEntity> wrapper = new QueryWrapper<ModifyTraceEntity>().eq(StringUtils.nonEmpty(params.getMFieldId()), "s.m_field_id", params.getMFieldId())
                .in(StringUtils.nonEmpty(params.getMPkV()), "s.m_pk_v", Arrays.asList(params.getMPkV().split(",")))
                .likeRight(StringUtils.nonEmpty(params.getMonth()), M_DATETIME, params.getMonth())
                .orderByDesc(M_DATETIME);
        return getBaseMapper().selectByPage(iPage, wrapper);
    }

    @Override
    public List<ModifyTraceEntity> queryTraceByPk(TraceQueryVo params) {
        QueryWrapper<ModifyTraceEntity> queryWrapper = new QueryWrapper<ModifyTraceEntity>().eq(StringUtils.nonEmpty(params.getMFieldId()), "s.m_field_id", params.getMFieldId())
                .in(StringUtils.nonEmpty(params.getMPkV()), "s.m_pk_v", Arrays.asList(params.getMPkV().split(",")))
                .orderByDesc(M_DATETIME);
        // 查出当前修改项最后一次修改记录
        return getBaseMapper().selectListByPk(queryWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTrace(List<ModifyTraceEntity> modifyTraceEntityList) {
        if (!CollectionUtils.isEmpty(modifyTraceEntityList)) {
            List<ModifyTraceEntity> collect = modifyTraceEntityList.stream().filter(sModifyTraceEntity -> !sModifyTraceEntity.getmOldV().equals(sModifyTraceEntity.getmNewV())).collect(Collectors.toList());
            collect.forEach(sModifyTraceEntity -> {
                LocalDateTime localDateTime = LocalDateTime.now();
                sModifyTraceEntity.setmDatetime(localDateTime.format(DF));
            });
            this.saveBatch(collect);
        }
    }

}