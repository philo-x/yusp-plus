package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.ObjectUtils;
import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.dao.AdminSmDptDao;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDptEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDptQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDptVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import cn.com.yusys.yusp.oca.service.AdminSmDptService;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmUserMgrOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统部门表
 *
 * @author terry
 * @date 2020-12-12 10:47:26
 */
@Service("adminSmDptService")
public class AdminSmDptServiceImpl extends ServiceImpl<AdminSmDptDao, AdminSmDptEntity> implements AdminSmDptService {

    private static final Logger logger = LoggerFactory.getLogger(AdminSmDptServiceImpl.class);

    private static final String T1_ORG_SEQ = "T1.ORG_SEQ";

    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private AdminSmUserMgrOrgService adminSmUserMgrOrgService;

    @Override
    public Page<AdminSmDptVo> queryPage(AdminSmDptQuery query) {
        Page<AdminSmDptVo> page = query.getIPage();
        QueryWrapper<AdminSmDptVo> queryWrapper = new QueryWrapper<AdminSmDptVo>()
                // 部门编码模糊匹配
                .like(!StringUtils.isEmpty(query.getDptCode()), "T0.DPT_CODE", query.getDptCode())
                // 部门名称模糊匹配
                .like(!StringUtils.isEmpty(query.getDptName()), "T0.DPT_NAME", query.getDptName())
                // 部门状态精确匹配
                .eq(ObjectUtils.nonNull(query.getDptSts()), "T0.DPT_STS", query.getDptSts());
        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like("T0.DPT_CODE", query.getKeyWord())//关键字模糊匹配部门编码
                    .or()
                    .like("T0.DPT_NAME", query.getKeyWord())//关键字模糊匹配部门名称
            );
        }
        queryWrapper.orderByDesc("T0.LAST_CHG_DT");
        AdminSmOrgEntity adminSmOrgEntity = null;
        //指定机构时，获取登录用户对应机构及下级所有机构和登录人被授权的机构下的部门）
        if (StringUtils.isEmpty(query.getOrgId())) {
            //查询机构的信息
            adminSmOrgEntity = adminSmOrgService.getById(SessionUtils.getUserOrganizationId());
            final String orgSeq = adminSmOrgEntity.getOrgSeq();
            List<String> relUserOrgIds = adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());
            if (CollectionUtils.nonEmpty(relUserOrgIds)) {
                queryWrapper.and(wrapper -> wrapper
                        .in("T0.ORG_ID", relUserOrgIds)//关键字模糊匹配部门编码
                        .or()
                        .likeRight(T1_ORG_SEQ, orgSeq)//关键字模糊匹配部门名称
                );
            } else {
                queryWrapper.likeRight(T1_ORG_SEQ, orgSeq);
            }
        } else {
            //指定机构时，获取指定机构及下级所有机构的部门
            adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
            queryWrapper.likeRight(T1_ORG_SEQ, adminSmOrgEntity.getOrgSeq());
        }
        return getBaseMapper().selectOrgAccessible(queryWrapper, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmDptEntity dptEntity = getBaseMapper().selectById(id);
                dptEntity.setDptId(id);
                dptEntity.setDptSts(AvailableStateEnum.ENABLED);
                getBaseMapper().updateById(dptEntity);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                if (haveEffectiveSon(id)) {
                    throw BizException.error(null, "50100003", "该部门存在生效的下级部门，请停用对应的下级部门后操作");
                } else {
                    AdminSmDptEntity entity = getBaseMapper().selectById(id);
                    entity.setDptId(id);
                    entity.setDptSts(AvailableStateEnum.DISABLED);
                    getBaseMapper().updateById(entity);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                if (checkBlocked(id)) {
                    throw BizException.error(null, "50100001", "该部门绑定有用户或存在下级部门，请删除关联信息后操作");
                } else {
                    getBaseMapper().deleteById(id);
                }
            });
        }
    }

    /**
     * 检查部门是否已关联其他信息
     *
     * @param dptId 部门id
     * @return 部门关联数量
     */
    private boolean checkBlocked(String dptId) {
        LambdaQueryWrapper<AdminSmDptEntity> wrapper = new QueryWrapper<AdminSmDptEntity>().lambda();
        wrapper.eq(AdminSmDptEntity::getUpDptId, dptId);
        // 是否有下级部门
        Long countSon = getBaseMapper().selectCount(wrapper);
        LambdaQueryWrapper<AdminSmUserEntity> userWrapper = new QueryWrapper<AdminSmUserEntity>().lambda();
        userWrapper.eq(AdminSmUserEntity::getDptId, dptId).eq(AdminSmUserEntity::getUserSts, AvailableStateEnum.ENABLED.getCode());
        // 是否有关联用户
        long countUser = this.adminSmUserService.count(userWrapper);
        return countSon > 0 || countUser > 0;
    }


    /**
     * 检查部门是否生效的子机构
     *
     * @param dptId 部门id
     * @return
     */
    private boolean haveEffectiveSon(String dptId) {
        LambdaQueryWrapper<AdminSmDptEntity> wrapper = new QueryWrapper<AdminSmDptEntity>().lambda();
        wrapper.eq(AdminSmDptEntity::getUpDptId, dptId).eq(AdminSmDptEntity::getDptSts, AvailableStateEnum.ENABLED);
        // 是否有生效的下级部门
        Long countSon = getBaseMapper().selectCount(wrapper);
        return countSon >0;
    }

    @Override
    public boolean save(AdminSmDptEntity entity) {
        LambdaQueryWrapper<AdminSmDptEntity> codeWrapper = new QueryWrapper<AdminSmDptEntity>().lambda();
        codeWrapper.eq(AdminSmDptEntity::getDptCode, entity.getDptCode());
        AdminSmDptEntity check = getBaseMapper().selectOne(codeWrapper);
        if (Objects.nonNull(check)) {
            throw BizException.error(null, "50100002", "部门代码："+entity.getDptCode()+"重复");
        }
        AdminSmDptEntity parent = getById(entity.getUpDptId());
        if (ObjectUtils.nonNull(parent)) {
            entity.setOrgId(parent.getOrgId());
        }
        // 新增的数据默认是待启用的
        entity.setDptSts(Optional.ofNullable(entity.getDptSts()).orElse(AvailableStateEnum.UNENABLED));
        logger.info("New department data: [new department: {}] ", entity.getDptName());
        return getBaseMapper().insert(entity) > 0;
    }

    @Override
    public Page<AdminSmUserVo> memberPage(AdminSmUserQuery query) {
        if (!StringUtils.isBlank(query.getDptId())) {
            return adminSmUserService.queryPage(query);
        }
        return null;
    }

    @Override
    public boolean updateById(AdminSmDptEntity entity) {
        // 改为停用时要判断是否关联其他信息
        if (AvailableStateEnum.DISABLED.equals(entity.getDptSts()) && haveEffectiveSon(entity.getDptId())) {
            throw BizException.error(null, "50100003", "该部门存在生效的下级部门，请停用对应的下级部门后操作");
        }
        entity.setDptSts(Optional.ofNullable(entity.getDptSts()).orElse(AvailableStateEnum.UNENABLED));
        return super.updateById(entity);
    }

    @Override
    public List<AdminSmDptEntity> dptTree(AdminSmDptQuery query) {
        query.setOrgId(StringUtils.isEmpty(query.getOrgId()) ? SessionUtils.getUserOrganizationId() : query.getOrgId());
        LambdaQueryWrapper<AdminSmDptEntity> queryWrapper = new QueryWrapper<AdminSmDptEntity>().lambda();
        queryWrapper.eq(AdminSmDptEntity::getOrgId, query.getOrgId());
        queryWrapper.eq(ObjectUtils.nonNull(query.getDptSts()), AdminSmDptEntity::getDptSts, query.getDptSts());

        // 1. 查出所有部门
        List<AdminSmDptEntity> dptEntityList = getBaseMapper().selectList(queryWrapper);

        // 2 组装成父子的树形结构
        // 2.1 找到所有的一级部门 目前一级部门的部门id写死为字符串0
        List<AdminSmDptEntity> level1Dpt = dptEntityList.stream().filter((dptEntity -> AdminSmDptEntity.DEFAULT_UP_DPT_ID.equals(dptEntity.getUpDptId())))
                .peek((dpt) -> {
                    // 2.2 递归下探当前部门的子部门树组装
                    dpt.setChildren(getChildren(dpt, dptEntityList));
                }).collect(Collectors.toList());
        return level1Dpt;
    }

    @Override
    public Page<AdminSmDptVo> getDeptsForWf(AdminSmDptQuery query) {
        Page<AdminSmDptVo> page = query.getIPage();
        QueryWrapper<AdminSmDptVo> wrapper = new QueryWrapper<>();
        wrapper.eq("T1.DPT_STS", "A");
        creatWrapper(wrapper, "T1.ORG_ID", query.getOrgId());
        creatWrapper(wrapper, "T1.DPT_CODE", query.getDptCode());
        creatWrapper(wrapper, "T1.DPT_NAME", query.getDptName());
        return getBaseMapper().getDeptsForWf(page, wrapper);
    }

    private void creatWrapper(QueryWrapper<AdminSmDptVo> wrapper, String column, String value) {
        if (StringUtils.nonEmpty(value)) {
            boolean condition = value.startsWith("%") || value.endsWith("%");
            wrapper.like(condition, column, value);
            wrapper.eq(!condition, column, value);
        }
    }

    /**
     * 递归查找传入部门的子部门，拼装成子部门树结构
     *
     * @param root 传入的部门实体
     * @param all  全量部门列表
     * @return 子部门树
     */
    private List<AdminSmDptEntity> getChildren(AdminSmDptEntity root, List<AdminSmDptEntity> all) {
        return all.stream()
                .filter(dptEntity -> dptEntity.getUpDptId().equals(root.getDptId()))
                .peek(dptEntity -> {
                    // 1. 递归查找子部门
                    dptEntity.setChildren(getChildren(dptEntity, all));
                }).collect(Collectors.toList());
    }

    /**
     * 部门名称的缓存跟新
     *
     * @param entity 新增或者需要跟新的部门
     */
    @Override
    public void updateDepCache(AdminSmDptEntity entity) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        String dataTenantId=String.valueOf(SessionUtils.getUserInformation().getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY));

        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        Map<String, String> map = new HashMap<>(8);
        map.put(entity.getDptId(), entity.getDptName());
        cacheService.hashPut(name,dataTenantId, redisKey, map, 24 * 15 * 3600);
    }

    /**
     * 删除部分部门缓存
     *
     * @param ids 需要被删除缓存的部门id
     */
    @Override
    public void deletePartDepCache(String[] ids) {
        String name = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        for (String id : ids) {
            cacheService.hashKeyDelete(name, redisKey, id);
        }
    }

}