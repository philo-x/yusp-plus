package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.ObjectUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.dao.AdminSmDutyDao;
import cn.com.yusys.yusp.oca.dao.AdminSmDutyUserDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDutyEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmDutyUserQuery;
import cn.com.yusys.yusp.oca.service.AdminSmDutyService;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.service.AdminSmUserDutyRelService;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDutyVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDutyRelVo;
import cn.com.yusys.yusp.oca.service.AdminSmUserMgrOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 系统岗位实现类
 *
 * @author terry
 */

@Service("adminSmDutyService")
public class AdminSmDutyServiceImpl extends ServiceImpl<AdminSmDutyDao, AdminSmDutyEntity> implements AdminSmDutyService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmDutyServiceImpl.class);
    @Autowired
    private AdminSmDutyUserDao adminSmDutyUserDao;

    @Autowired
    AdminSmOrgService adminSmOrgService;

    @Autowired
    AdminSmUserDutyRelService userDutyRelService;

    @Autowired
    AdminSmUserMgrOrgService adminSmUserMgrOrgService;

    private static final String T1_ORG_SEQ = "T1.ORG_SEQ";

    @Override
    public Page<AdminSmDutyVo> queryPage(AdminSmDutyQuery query) {
        Page<AdminSmDutyVo> page = query.getIPage();
        QueryWrapper<AdminSmDutyVo> queryWrapper = new QueryWrapper<AdminSmDutyVo>()
                .like(!StringUtils.isEmpty(query.getDutyCode()), "T0.DUTY_CODE", query.getDutyCode())
                .like(!StringUtils.isEmpty(query.getDutyName()), "T0.DUTY_NAME", query.getDutyName())
                .eq(ObjectUtils.nonNull(query.getDutySts()), "T0.DUTY_STS", query.getDutySts());
        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(query.getKeyWord()), "T0.DUTY_CODE", query.getKeyWord())//关键字模糊匹配岗位编码
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), "T0.DUTY_NAME", query.getKeyWord())//关键字模糊匹配岗位名称
            );
        }

        queryWrapper.orderByDesc("T0.LAST_CHG_DT");



        AdminSmOrgEntity adminSmOrgEntity;
        //未指定机构时，获取登录用户对应机构及下级所有机构和登录人被授权的机构下的岗位
        if(StringUtils.isEmpty(query.getOrgId())){
            //查询机构的信息
            adminSmOrgEntity = adminSmOrgService.getById(SessionUtils.getUserOrganizationId());
            final String orgSeq=adminSmOrgEntity.getOrgSeq();
            //查询登录人被授权的机构
            List<String> relUserOrgIds = adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());
            if(CollectionUtils.nonEmpty(relUserOrgIds)){
                queryWrapper.and(wrapper -> wrapper
                        .in("T0.ORG_ID", relUserOrgIds)
                        .or()
                        .likeRight(T1_ORG_SEQ, orgSeq)
                );
            } else {
                queryWrapper.likeRight(T1_ORG_SEQ, orgSeq);
            }
        }else {
            //指定机构时，获取指定机构及下级所有机构的部门
            adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
            queryWrapper.likeRight(T1_ORG_SEQ,adminSmOrgEntity.getOrgSeq());
        }
        return getBaseMapper().selectOrgAccessibleDuty(page,queryWrapper);
    }

    @Override
    public Page<AdminSmUserDutyRelVo> memberPage(AdminSmDutyUserQuery query) {
        if (query.getDutyId() == null) {
            throw BizException.error(null, "50200003", "岗位代码不能为空");
        }
        // 组装查询条件
        Page<AdminSmUserDutyRelVo> page = query.getIPage();
        QueryWrapper<AdminSmUserDutyRelVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ud.DUTY_ID", query.getDutyId());
        queryWrapper.like(!StringUtils.isEmpty(query.getLoginCode()), "u.LOGIN_CODE", query.getLoginCode());
        queryWrapper.like(!StringUtils.isEmpty(query.getUserName()), "u.USER_NAME", query.getUserName());
        queryWrapper.like(!StringUtils.isEmpty(query.getUserCode()), "u.USER_CODE", query.getUserCode());
        queryWrapper.eq(ObjectUtils.nonNull(query.getUserSts()), "u.USER_STS", query.getUserSts());
        return this.adminSmDutyUserDao.pageDutyUserByDutyId(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmDutyEntity entity = new AdminSmDutyEntity();
                entity.setDutyId(id);
                entity.setDutySts(AvailableStateEnum.ENABLED);
                getBaseMapper().updateById(entity);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDisable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmDutyEntity entity = new AdminSmDutyEntity();
                entity.setDutyId(id);
                entity.setDutySts(AvailableStateEnum.DISABLED);
                getBaseMapper().updateById(entity);
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
                    throw BizException.error(null, "50200002", "该岗位绑定有用户，请删除关联信息后操作");
                } else {
                    getBaseMapper().deleteById(id);
                }
            });
        }
    }

    @Override
    public Page<AdminSmDutyVo> getDutysForWf(AdminSmDutyQuery query) {
        Page<AdminSmDutyVo> page = query.getIPage();
        QueryWrapper<AdminSmDutyVo> wrapper = new QueryWrapper<>();
        wrapper.eq("T1.DUTY_STS", "A");
        creatWrapper(wrapper, "T1.ORG_ID", query.getOrgId());
        creatWrapper(wrapper, "T1.DUTY_CODE", query.getDutyCode());
        creatWrapper(wrapper, "T1.DUTY_NAME", query.getDutyName());
        return getBaseMapper().getDutysForWf(page, wrapper);
    }

    private void creatWrapper(QueryWrapper<AdminSmDutyVo> wrapper, String column, String value) {
        if (StringUtils.nonEmpty(value)) {
            boolean condition = value.startsWith("%") || value.endsWith("%");
            wrapper.like(condition, column, value);
            wrapper.eq(!condition, column, value);
        }
    }

    @Override
    public List<String> getDutysByUserIdForWf(String userId) {
        return getBaseMapper().getDutysByUserIdForWf(userId);
    }

    /**
     * 检查岗位是否已关联其他信息
     *
     * @param dutyId 岗位id
     * @return true 已关联其他信息
     */
    private boolean checkBlocked(String dutyId) {
        LambdaQueryWrapper<AdminSmUserDutyRelVo> userWrapper = new QueryWrapper<AdminSmUserDutyRelVo>().lambda();
        userWrapper.eq(AdminSmUserDutyRelVo::getDutyId, dutyId);
        // 是否有关联用户
        Long countUser = this.adminSmDutyUserDao.selectCount(userWrapper);
        return countUser > 0;
    }

    @Override
    public boolean save(AdminSmDutyEntity entity) {
        LambdaQueryWrapper<AdminSmDutyEntity> codeWrapper = new QueryWrapper<AdminSmDutyEntity>().lambda();
        codeWrapper.eq(AdminSmDutyEntity::getDutyCode, entity.getDutyCode());
        AdminSmDutyEntity check = getBaseMapper().selectOne(codeWrapper);
        if (Objects.nonNull(check)) {
            throw BizException.error("exist", "50200001", entity.getDutyCode());
        }
        //新增的数据默认是待启用的
        entity.setDutySts(Optional.ofNullable(entity.getDutySts()).orElse(AvailableStateEnum.UNENABLED));
        log.info("New duty data: [new duty: {}] ", entity.getDutyName());
        return getBaseMapper().insert(entity) > 0;
    }

    @Override
    public boolean updateById(AdminSmDutyEntity entity) {
        //新增的数据默认是待启用的
        entity.setDutySts(Optional.ofNullable(entity.getDutySts()).orElse(AvailableStateEnum.UNENABLED));
        return super.updateById(entity);
    }
}