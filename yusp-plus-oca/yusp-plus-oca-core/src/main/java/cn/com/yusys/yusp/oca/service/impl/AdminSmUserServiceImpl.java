package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.notice.vo.AdminSmReciveVo;
import cn.com.yusys.yusp.oca.constants.OcaCommonConstants;
import cn.com.yusys.yusp.oca.dao.AdminSmUserDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordResetBo;
import cn.com.yusys.yusp.oca.domain.constants.*;
import cn.com.yusys.yusp.oca.domain.dto.PasswordLogDto;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserDutyRelQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserRoleRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.*;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.PasswordUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service("adminSmUserService")
public class AdminSmUserServiceImpl extends ServiceImpl<AdminSmUserDao, AdminSmUserEntity> implements AdminSmUserService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmUserServiceImpl.class);

    private static final String T0_LOGIN_CODE = "T0.LOGIN_CODE";
    private static final String T0_USER_CODE = "T0.USER_CODE";
    private static final String T0_USER_NAME = "T0.USER_NAME";

    private static final String ERROR_CODE_51100004 = "51100004";

    private static final String ERROR_MESSAGE_51100004 = "用户编号不能为空";
    private static final String USER_ID = "USER_ID";

    @Autowired
    private AdminSmDptService adminSmDptService;

    @Autowired
    private AdminSmUserRoleRelService adminSmUserRoleRelService;

    @Autowired
    private AdminSmUserDutyRelService adminSmUserDutyRelService;

    @Autowired
    private AdminSmRoleService adminSmRoleService;

    @Autowired
    private AdminSmOrgService adminSmOrgService;

    @Autowired
    private AdminSmLogicSysService adminSmLogicSysService;

    @Autowired
    private AdminSmInstuService adminSmInstuService;


    @Autowired
    private AdminSmPasswordLogService passwordLogService;


    @Autowired
    private AdminSmUserMgrOrgService userMgrOrgService;

    @Autowired
    private AdminSmDutyService adminSmDutyService;

    @Autowired
    private PasswordUtils passwordUtils;
    @Autowired
    I18nMessageByCode i18nMessageByCode;

    @Autowired
    private AdminSmUserDao adminSmUserDao;

    @Autowired
    AdminSmUserMgrOrgService adminSmUserMgrOrgService;

    @Autowired
    private AdminSmTenantUserRelService adminSmTenantUserRelService;

    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;

    @Override
    public Page<AdminSmUserVo> queryPage(AdminSmUserQuery query) {
        //获取租户的超级管理用户id
        String tenantUserId = adminSmTenantUserRelService.getTenantUserId();
        query.setExceptedUserId(tenantUserId);

        if (StringUtils.isEmpty(query.getOrgId())) {
            query.setOrgId(SessionUtils.getUserOrganizationId());
        }

        Page<AdminSmUserVo> page = query.getIPage();
        QueryWrapper<AdminSmUserVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.nonEmpty(query.getLoginCode()), T0_LOGIN_CODE, query.getLoginCode());
        queryWrapper.like(StringUtils.nonEmpty(query.getUserCode()), T0_USER_CODE, query.getUserCode());
        queryWrapper.eq(StringUtils.nonEmpty(query.getCertType()), "T0.CERT_TYPE", query.getCertType());
        queryWrapper.like(StringUtils.nonEmpty(query.getCertNo()), "T0.CERT_NO", query.getCertNo());
        queryWrapper.lt(Objects.nonNull(query.getDeadline()), "T0.DEADLINE", query.getDeadline());
        queryWrapper.eq(StringUtils.nonEmpty(query.getUserSex()), "T0.USER_SEX", query.getUserSex());
        queryWrapper.eq(StringUtils.nonEmpty(query.getDptId()), "T0.DPT_ID", query.getDptId());
        queryWrapper.like(StringUtils.nonEmpty(query.getUserName()), T0_USER_NAME, query.getUserName());

        queryWrapper.eq(Objects.nonNull(query.getUserSts()), "T0.USER_STS", query.getUserSts());
        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_LOGIN_CODE, query.getKeyWord())//关键字模糊匹配账号
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_USER_CODE, query.getKeyWord())//关键字模糊匹配工号
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_USER_NAME, query.getKeyWord())//关键字模糊匹配用户名
            );
        }
        queryWrapper.orderByDesc("T0.LAST_CHG_DT");
        //排除的用户id
        queryWrapper.notIn(StringUtils.nonEmpty(query.getExceptedUserId()),"T0.USER_ID",query.getExceptedUserId());

        //查询orgId及下级所有用户
        AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(query.getOrgId());
        queryWrapper.likeRight("T1.ORG_SEQ",adminSmOrgEntity.getOrgSeq());
        //查询OrgId对应机构及下级机构所有用户：
        return getBaseMapper().selectOrgAccessibleUser(page,queryWrapper);
    }

    @Override
    public AdminSmUserDetailVo getDetailById(String userId) {
        return getBaseMapper().getDetailById(userId);
    }

    @Override
    public AdminSmUserDetailVo getDetailByIdAndOrg(String userId) {
        //获取租户的超级管理用户id
        String tenantUserId = adminSmTenantUserRelService.getTenantUserId();
        if (userId.equals(tenantUserId)){
            return null;
        }
        String orgId = SessionUtils.getUserOrganizationId();
        AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(orgId);
        return getBaseMapper().getDetailByIdAndOrg(userId,adminSmOrgEntity.getOrgSeq()+"%");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean save(AdminSmUserEntity entity) {
        //工号和账号不可重复，用户名可重复
        boolean existEntity=this.judgeExistEntity(entity);
        if (existEntity) {
            throw BizException.error("exist", "51100005", "userCode:" + entity.getUserCode() + ",loginCode:" + entity.getLoginCode());
        }
        String defaultPassword = "123456";
        entity.setUserPassword(new BCryptPasswordEncoder().encode(defaultPassword));
        //新增的数据默认是待启用的
        entity.setUserSts(Optional.ofNullable(entity.getUserSts()).orElse(AvailableStateEnum.UNENABLED));

        log.info("New user data: [new user: {}] ", entity.getUserName());
        int count = getBaseMapper().insert(entity);

        LambdaQueryWrapper<AdminSmUserEntity> wrapper = new QueryWrapper<AdminSmUserEntity>().lambda();
        wrapper.eq(AdminSmUserEntity::getUserCode, entity.getUserCode());
        wrapper.eq(StringUtils.nonEmpty(entity.getDataTenantId()),AdminSmUserEntity::getDataTenantId,entity.getDataTenantId());
        AdminSmUserEntity user = getBaseMapper().selectOne(wrapper);

        AdminSmPasswordLogEntity passwordLogEntity = new AdminSmPasswordLogEntity();
        passwordLogEntity.setUserId(user.getUserId());
        passwordLogEntity.setPwdUped(entity.getUserPassword());
        passwordLogEntity.setPwdUpTime(new Date());
        passwordLogEntity.setUpdateUser(SessionUtils.getUserId());
        passwordLogService.save(passwordLogEntity);
        if (StringUtils.nonEmpty(user.getUserMobilephone())) {
            // TODO 发送密码短信，这部分留给框架使用方自定义。可以选择通过消息中心发送短信
        }
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean updateById(AdminSmUserEntity entity) {
        entity.setUserSts(Optional.ofNullable(entity.getUserSts()).orElse(AvailableStateEnum.UNENABLED));
        return super.updateById(entity);
    }

    private boolean checkBlocked(String userId) {
        //是否有关联的角色、岗位、授权机构
        Integer count = getBaseMapper().countRel(userId);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEnable(String[] ids) {
        if (CollectionUtils.nonEmpty(ids)) {
            List<String> idList = Arrays.asList(ids);
            idList.forEach((id) -> {
                AdminSmUserEntity entity = new AdminSmUserEntity();
                entity.setUserId(id);
                entity.setUserSts(AvailableStateEnum.ENABLED);

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
                AdminSmUserEntity entity = new AdminSmUserEntity();
                entity.setUserId(id);
                entity.setUserSts(AvailableStateEnum.DISABLED);
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
                    throw BizException.error(null, "51100002", "该用户存在其它关联信息，请删除关联信息后操作");//这里也可以选择删除用户关联信息
                } else {
                    //删除用户的授权信息
                    adminSmAuthRecoService.deleteByAuthObjIds(ids);
                    getBaseMapper().deleteById(id);
                }
            });
        }
    }

    @Override
    public List<UserRoleRelVo> getUserRoleList(AdminSmUserRoleRelQuery query) {
        String userId = query.getUserId();
        if (userId == null) {
            throw BizException.error(null, ERROR_CODE_51100004, ERROR_MESSAGE_51100004);
        }
        AdminSmUserEntity user = this.getById(userId);
        //获取机构向上继承路径
        List<String> orgIdList = adminSmOrgService.getUpOrgIdList(user.getOrgId());

        LambdaQueryWrapper<AdminSmRoleEntity> queryWrapper = new QueryWrapper<AdminSmRoleEntity>().lambda()
                .eq(AdminSmRoleEntity::getRoleSts, AvailableStateEnum.ENABLED)
                .in(AdminSmRoleEntity::getOrgId, orgIdList);
        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(AdminSmRoleEntity::getRoleCode, query.getKeyWord())
                    .or()
                    .like(AdminSmRoleEntity::getRoleName, query.getKeyWord())
            );
        }
        //所有可选角色，只包含启用状态的
        List<AdminSmRoleEntity> allRoleList = adminSmRoleService.list(queryWrapper);
        //用户已关联角色
        List<String> selectedRoleIds = adminSmUserRoleRelService.findUserRoleRelsByUser(user);
        List<UserRoleRelVo> res = allRoleList.stream().map(entity -> {
            UserRoleRelVo vo = new UserRoleRelVo();
            BeanUtils.beanCopy(entity, vo);
            if (selectedRoleIds.contains(entity.getRoleId())) {
                vo.setChecked(true);
            }
            return vo;
        }).collect(Collectors.toList());
        if (Objects.nonNull(query.getChecked())) {
            res = res.stream().filter(rel -> query.getChecked().equals(rel.getChecked())).collect(Collectors.toList());
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoleList(String userId, List<AdminSmUserRoleRelEntity> list) {
        if (userId == null) {
            throw BizException.error(null, ERROR_CODE_51100004, ERROR_MESSAGE_51100004);
        }
        // 先将当前用户的全部角色关联信息删除，再批量保存最新的关联信息数据
        LambdaQueryWrapper<AdminSmUserRoleRelEntity> deleteWrapper = new QueryWrapper<AdminSmUserRoleRelEntity>().lambda();
        deleteWrapper.eq(AdminSmUserRoleRelEntity::getUserId, userId);
        adminSmUserRoleRelService.remove(deleteWrapper);
        if (!list.isEmpty()) {
            adminSmUserRoleRelService.saveBatch(list);
        }
    }

    @Override
    public List<UserDutyRelVo> getUserDutyList(AdminSmUserDutyRelQuery query) {
        String userId = query.getUserId();
        if (userId == null) {
            throw BizException.error(null, ERROR_CODE_51100004, ERROR_MESSAGE_51100004);
        }
        AdminSmUserEntity user = this.getById(userId);
        //获取机构向上继承路径
        List<String> orgIdList = adminSmOrgService.getUpOrgIdList(user.getOrgId());

        LambdaQueryWrapper<AdminSmDutyEntity> queryWrapper = new QueryWrapper<AdminSmDutyEntity>().lambda()
                .eq(AdminSmDutyEntity::getDutySts, AvailableStateEnum.ENABLED)
                .in(AdminSmDutyEntity::getOrgId, orgIdList);
        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(AdminSmDutyEntity::getDutyCode, query.getKeyWord())
                    .or()
                    .like(AdminSmDutyEntity::getDutyName, query.getKeyWord())
            );
        }
        //所有可选岗位
        List<AdminSmDutyEntity> allDutyList = adminSmDutyService.list(queryWrapper);

        //用户已关联岗位
        List<String> selectedDutyIds = adminSmUserDutyRelService.findUserDutyRelsByUser(user);

        List<UserDutyRelVo> res = allDutyList.stream().map(entity -> {
            UserDutyRelVo vo = new UserDutyRelVo();
            BeanUtils.beanCopy(entity, vo);
            if (selectedDutyIds.contains(entity.getDutyId())) {
                vo.setChecked(true);
            }
            return vo;
        }).collect(Collectors.toList());
        if (Objects.nonNull(query.getChecked())) {
            res = res.stream().filter(rel -> query.getChecked().equals(rel.getChecked())).collect(Collectors.toList());
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserDutyList(String userId, List<AdminSmUserDutyRelEntity> list) {
        if (userId == null) {
            throw BizException.error(null, ERROR_CODE_51100004, ERROR_MESSAGE_51100004);
        }
        LambdaQueryWrapper<AdminSmUserDutyRelEntity> deleteWrapper = new QueryWrapper<AdminSmUserDutyRelEntity>().lambda();
        deleteWrapper.eq(AdminSmUserDutyRelEntity::getUserId, userId);
        adminSmUserDutyRelService.remove(deleteWrapper);
        if (!list.isEmpty()) {
            adminSmUserDutyRelService.saveBatch(list);
        }
    }


    @Override
    public List<UserMgrOrgVo> getUserMgrOrgList(String userId) {
        if (userId == null) {
            throw BizException.error(null, ERROR_CODE_51100004, ERROR_MESSAGE_51100004);
        }
        AdminSmUserEntity user = this.getById(userId);
        //一级机构ids
        List<String> level1orgIds = adminSmOrgService.lambdaQuery()
                .select(AdminSmOrgEntity::getOrgId)
                .eq(AdminSmOrgEntity::getOrgLevel, 1)
                .eq(AdminSmOrgEntity::getOrgSts, AvailableStateEnum.ENABLED)
                .list().stream().map(AdminSmOrgEntity::getOrgId).collect(Collectors.toList());
        level1orgIds.removeIf((id) -> id.equals(user.getOrgId()));
        //全行机构树，排除指定用户所在机构及其所有子节点
        List<AdminSmOrgTreeNodeBo> allTree = adminSmOrgService.getOrgTrees(level1orgIds, AvailableStateEnum.ENABLED, user.getOrgId());
        //用户已关联机构
        List<String> selectedOrgRelsIds = userMgrOrgService.findOrgRelsByUser(userId);
        return allTree.stream().map((orgTree) -> getOrgRelTree(orgTree, selectedOrgRelsIds)).collect(Collectors.toList());
    }

    private UserMgrOrgVo getOrgRelTree(AdminSmOrgTreeNodeBo orgTree, List<String> selectedOrgIds) {
        UserMgrOrgVo rel = new UserMgrOrgVo();
        rel.setOrgId(orgTree.getOrgId());
        rel.setOrgName(orgTree.getOrgName());
        rel.setUpOrgId(orgTree.getUpOrgId());
        if (selectedOrgIds.contains(orgTree.getOrgId())) {
            rel.setChecked(true);
        }
        if (CollectionUtils.nonEmpty(orgTree.getChildren())) {
            rel.setChildren(orgTree.getChildren().stream().map(node -> getOrgRelTree(node, selectedOrgIds)).collect(Collectors.toList()));
        }
        return rel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserMgrOrg(String userId, List<AdminSmUserMgrOrgEntity> list) {
        LambdaQueryWrapper<AdminSmUserMgrOrgEntity> deleteWrapper = new QueryWrapper<AdminSmUserMgrOrgEntity>().lambda();
        deleteWrapper.eq(AdminSmUserMgrOrgEntity::getUserId, userId);
        userMgrOrgService.remove(deleteWrapper);
        if (list.size() > 0) {
            userMgrOrgService.saveBatch(list);
        }
    }

    @Override
    public Page<AdminSmUserVo> queryPageExcept(AdminSmPasteUserQuery query) {
        //你想除去哪个角色
        if (query.getExpectedUserId() == null || query.getExpectedUserId().trim().length() == 0) {
            throw BizException.error(null, "50800003", "用户代码为空");
        }

        Page<AdminSmUserVo> page = query.getIPage();
        QueryWrapper<AdminSmUserVo> queryWrapper = new QueryWrapper<>();

        if (StringUtils.nonEmpty(query.getKeyWord())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_LOGIN_CODE, query.getKeyWord())//关键字模糊匹配账号
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_USER_CODE, query.getKeyWord())//关键字模糊匹配工号
                    .or()
                    .like(StringUtils.nonEmpty(query.getKeyWord()), T0_USER_NAME, query.getKeyWord())//关键字模糊匹配用户名
            );
        }
        queryWrapper.notIn("T0.USER_ID",query.getExpectedUserId());
        //查询登录人机构的信息
        AdminSmOrgEntity adminSmOrgEntity = adminSmOrgService.getById(SessionUtils.getUserOrganizationId());
        final String orgSeq=adminSmOrgEntity.getOrgSeq();
        //获取登录人被授权的机构
        List<String> orgIds =adminSmUserMgrOrgService.findOrgRelsByUser(SessionUtils.getUserId());
        if(CollectionUtils.nonEmpty(orgIds)){
            //登录人存在被授权的机构,获取登录用户对应机构及下级所有机构和登录人被授权的机构下的角色
            queryWrapper.and(r -> r
                    .likeRight("T1.org_seq", orgSeq)//机构序列模糊右匹配
                    .or()
                    .in("T0.org_id", orgIds)//用户已授权的机构
            );
        }else{
            //登录用户没有被授权机构，那就是获取登录用户对应的机构及下级所有机构的角色
            queryWrapper.likeRight("T1.org_seq",orgSeq);
        }

        queryWrapper.orderByDesc("T0.LAST_CHG_DT");

        return getBaseMapper().selectOrgAccessibleUser(page,queryWrapper);
    }

    @Override
    public List<AdminSmUserEntity> selectIdLikeName(String userName) {
        QueryWrapper<AdminSmUserEntity> wrapper = new QueryWrapper<>();
        wrapper.like("user_name", userName);
        return this.list(wrapper);
    }


    @Override
    public Page<AdminSmUserVo> getUsersForWf(AdminSmUserQuery query) {
        Page<AdminSmUserVo> page = query.getIPage();
        QueryWrapper<AdminSmUserVo> wrapper = new QueryWrapper<>();
        wrapper.eq("T0.USER_STS", "A");
        wrapper.eq(StringUtils.nonEmpty(query.getLoginCode()), T0_LOGIN_CODE, query.getLoginCode());
        wrapper.like(StringUtils.nonEmpty(query.getUserName()), T0_USER_NAME, query.getUserName());
        return getBaseMapper().getUsersForWf(page, wrapper);
    }

    @Override
    public List<AdminSmUserVo> getUsersByOrgForWf(String orgId) {
        return getBaseMapper().getUsersByOrgForWf(orgId);
    }

    @Override
    public List<AdminSmUserVo> getUsersByDeptForWf(String deptId) {
        return getBaseMapper().getUsersByDeptForWf(deptId);
    }

    @Override
    public List<AdminSmUserVo> getUsersByDutyForWf(String dutyId) {
        return getBaseMapper().getUsersByDutyForWf(dutyId);
    }

    @Override
    public List<AdminSmUserVo> getUsersByRoleForWf(String roleId) {
        return getBaseMapper().getUsersByRoleForWf(roleId);
    }

    @Override
    public AdminSmUserVo getUserInfoForWf(String userId) {
        return getBaseMapper().getUserInfoForWf(userId);
    }

    @Override
    public String getUserNameById(String userId) {
        QueryWrapper<AdminSmUserEntity> wrapper = new QueryWrapper<>();
        wrapper.select("USER_NAME").eq(USER_ID, userId);
        AdminSmUserEntity userEntity = this.getOne(wrapper);
        return userEntity.getUserName();
    }

    @Override
    public JClientRspEntity<UserEntityVo> getByPhoneNumber(String userMobilephone) {
        //根据手机号查询用户信息
        AdminSmUserEntity userEntity = this.getOne(new QueryWrapper<AdminSmUserEntity>().eq("USER_MOBILEPHONE", userMobilephone));

        //封装返回数据
        if (Objects.nonNull(userEntity)) {
            userEntity.setUserPassword(null);
            UserEntityVo userEntityVoDb = new UserEntityVo();
            org.springframework.beans.BeanUtils.copyProperties(userEntity, userEntityVoDb);
            return JClientRspEntity.buildSuccess(userEntityVoDb);
        } else {
            log.error("手机号不存在！");
            throw BizException.error(null, ResponseAndMessageEnum.NON_PHONENUMBER.getCode(), ResponseAndMessageEnum.NON_PHONENUMBER.getMessage());
        }
    }



    @Override
    public List<AdminSmReciveVo> selectRoleAndObj(String userId) {
        return getBaseMapper().selectRoleAndObj(userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<String> modifyPassword(String password, String loginCode) {
        //密码加密
        if (StringUtils.nonEmpty(password) && StringUtils.nonEmpty(loginCode)) {
            password = passwordUtils.enPassword(password);
            //更新用户密码
            AdminSmUserEntity userEntity = getBaseMapper().selectOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));
            userEntity.setUserPassword(password);
            userEntity.setLastLoginTime(new Date());
            userEntity.setLastEditPassTime(new Date());
            getBaseMapper().updateById(userEntity);

            //新增密码历史记录
            PasswordLogDto passwordLogDto = new PasswordLogDto();
            passwordLogDto.setPwdUped(password);
            passwordLogDto.setUpdateUser(userEntity.getUserId());
            passwordLogDto.setUserId(userEntity.getUserId());

            passwordLogService.updatePwdLog(passwordLogDto);

            return JClientRspEntity.buildSuccess("成功");
        } else {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<String> resetPassword(PasswordResetBo passwordResetBo) {
        //密码加密
        if (StringUtils.nonEmpty(passwordResetBo.getLoginCode())) {
            String password = passwordUtils.enPasswordNormal(PasswordEnum.INITIAL_PASSWORD.getCode());
            //更新用户密码
            AdminSmUserEntity userEntity = getBaseMapper().selectOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", passwordResetBo.getLoginCode()));
            LambdaUpdateWrapper<AdminSmUserEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(AdminSmUserEntity::getLastLoginTime, null);
            updateWrapper.eq(AdminSmUserEntity::getUserId, userEntity.getUserId());
            updateWrapper.set(AdminSmUserEntity::getUserPassword, password);
            updateWrapper.set(AdminSmUserEntity::getLastEditPassTime, new Date());
            updateWrapper.set(AdminSmUserEntity::getLastChgDt, new Date());
            updateWrapper.set(AdminSmUserEntity::getLastChgUsr, SessionUtils.getUserId());
            this.update(updateWrapper);

            //新增密码历史记录
            PasswordLogDto passwordLogDto = new PasswordLogDto();
            passwordLogDto.setPwdUped(password);
            passwordLogDto.setUpdateUser(userEntity.getUserId());
            passwordLogDto.setUserId(userEntity.getUserId());

            passwordLogService.updatePwdLog(passwordLogDto);

            return JClientRspEntity.buildSuccess("成功");
        } else {
            return JClientRspEntity.buildFail(MessageEnums.PASSWORD_RESOLUTION_FAILED.getCode(), MessageEnums.PASSWORD_RESOLUTION_FAILED.getMessage());
        }
    }

    @Override
    public List<UserSubscribeVo> selectUserSubscribeVoList() {
        return getBaseMapper().selectUserSubscribeVoList();
    }

    @Override
    public void updateLoginTime(String userId, Date lastLoginTime) {
        getBaseMapper().updateLoginTime(userId, lastLoginTime);
    }


    @Override
    public UserSessionVo getUserByAuthorization(String userId, String clientId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(clientId)) {
            throw BizException.error(null, "51100003", "用户账号或客户端编号不能为空");
        }

        UserSessionVo userSessionVo = new UserSessionVo();
        //根据jwttoken信息解密出的sysId查询用户逻辑系统信息
        AdminSmLogicSysEntity logicSysEntity = adminSmLogicSysService.getOne(new QueryWrapper<AdminSmLogicSysEntity>().eq("SYS_ID", clientId));
        if (Objects.nonNull(logicSysEntity)) {
            LogicSysSessionVo logicSysSessionVo = new LogicSysSessionVo();
            BeanUtils.beanCopy(logicSysEntity, logicSysSessionVo);
            userSessionVo.setLogicSys(logicSysSessionVo);
        }
        //根据用户ID查询用户岗位信息
        //根据用户ID查询用户岗位关联表
        List<AdminSmUserDutyRelEntity> userDutyRelEntities = adminSmUserDutyRelService.list(new QueryWrapper<AdminSmUserDutyRelEntity>().eq(USER_ID, userId));
        //根据岗位id查询岗位信息
        setDutysIfHasDutyRel(userDutyRelEntities, userSessionVo);

        //根据jwttoken信息解密出的loginCode查询用户详细信息
        fillUserEntityDetail(userId, userSessionVo);
        return userSessionVo;
    }

    private void fillUserEntityDetail(String userId, UserSessionVo userSessionVo) {
        AdminSmUserEntity userEntity = this.getOne(new QueryWrapper<AdminSmUserEntity>()
                .eq(USER_ID, userId));

        if (Objects.nonNull(userEntity)) {
            BeanUtils.beanCopy(userEntity, userSessionVo);
            //根据用户部门id查询部门详细信息
            setDptIfHasDptId(userEntity, userSessionVo);

            //根据用户id查询用户角色
            List<AdminSmUserRoleRelEntity> userRoleList = adminSmUserRoleRelService.list(new QueryWrapper<AdminSmUserRoleRelEntity>()
                    .eq(USER_ID, userEntity.getUserId()));
            //根据用户角色Id查询用户角色详细信息
            setRolesIfHasUserRole(userRoleList, userSessionVo);

            //根据用户机构Id查询机构详细信息
            setOrgInfoIfHasOrgId(userEntity, userSessionVo);
        }
    }

    private void setDptIfHasDptId(AdminSmUserEntity userEntity, UserSessionVo userSessionVo) {
        if (StringUtils.nonEmpty(userEntity.getDptId())) {
            AdminSmDptEntity dptEntity = adminSmDptService.getOne(new QueryWrapper<AdminSmDptEntity>()
                    .eq("DPT_ID", userEntity.getDptId()));

            if (Objects.nonNull(dptEntity)) {
                DptSessionVo dptSessionVo = new DptSessionVo();
                BeanUtils.beanCopy(dptEntity, dptSessionVo);
                userSessionVo.setDpt(dptSessionVo);

                //根据用户部门id查询上级部门部门详细信息
                if (StringUtils.nonEmpty(dptEntity.getUpDptId())) {
                    AdminSmDptEntity upDptEntity = adminSmDptService.getOne(new QueryWrapper<AdminSmDptEntity>()
                            .eq("DPT_ID", dptEntity.getUpDptId()));

                    if (Objects.nonNull(upDptEntity)) {
                        DptSessionVo upDptSessionVo = new DptSessionVo();
                        BeanUtils.beanCopy(upDptEntity, upDptSessionVo);
                        userSessionVo.setUpDpt(upDptSessionVo);
                    }
                }
            }
        }
    }

    private void setOrgInfoIfHasOrgId(AdminSmUserEntity userEntity, UserSessionVo userSessionVo) {
        if (StringUtils.nonEmpty(userEntity.getOrgId())) {
            AdminSmOrgEntity orgEntity = adminSmOrgService.getOne(new QueryWrapper<AdminSmOrgEntity>()
                    .eq("ORG_ID", userEntity.getOrgId()));

            if (Objects.nonNull(orgEntity)) {
                fillUserSessionVo(userSessionVo, orgEntity);
            }
        }
    }

    private void fillUserSessionVo(UserSessionVo userSessionVo, AdminSmOrgEntity orgEntity) {
        OrgSessionVo org = new OrgSessionVo();
        BeanUtils.beanCopy(orgEntity, org);
        userSessionVo.setOrg(org);

        //根据机构id查询上级机构详细信息
        fillUserSessionVoUpOrgField(userSessionVo, orgEntity);

        if (StringUtils.nonEmpty(orgEntity.getOrgLevel().toString())) {
            userSessionVo.setOrgLevel(orgEntity.getOrgLevel().toString());
        }

        //根据金融机构ID查询金融机构信息
        fillUserSessionVoInstuOrgField(userSessionVo, orgEntity);
    }

    private void fillUserSessionVoInstuOrgField(UserSessionVo userSessionVo, AdminSmOrgEntity orgEntity) {
        if (StringUtils.nonEmpty(orgEntity.getInstuId())) {
            AdminSmInstuEntity instuEntity = adminSmInstuService.getOne(new QueryWrapper<AdminSmInstuEntity>()
                    .eq("INSTU_ID", orgEntity.getInstuId()));

            if (Objects.nonNull(instuEntity)) {
                InstuSessionVo instuSessionVo = new InstuSessionVo();
                BeanUtils.beanCopy(instuEntity, instuSessionVo);
                userSessionVo.setInstuOrg(instuSessionVo);
            }
        }
    }

    private void fillUserSessionVoUpOrgField(UserSessionVo userSessionVo, AdminSmOrgEntity orgEntity) {
        if (StringUtils.nonEmpty(orgEntity.getUpOrgId())) {
            AdminSmOrgEntity upOrgEntity = adminSmOrgService.getOne(new QueryWrapper<AdminSmOrgEntity>()
                    .eq("ORG_ID", orgEntity.getUpOrgId()));

            if (Objects.nonNull(upOrgEntity)) {
                OrgSessionVo upOrgSessionVo = new OrgSessionVo();
                BeanUtils.beanCopy(upOrgEntity, upOrgSessionVo);
                userSessionVo.setUpOrg(upOrgSessionVo);
            }
        }
    }

    private void setRolesIfHasUserRole(List<AdminSmUserRoleRelEntity> userRoleList, UserSessionVo userSessionVo) {
        if (CollectionUtils.nonEmpty(userRoleList)) {
            ArrayList<RoleSessionVo> roles = new ArrayList<>();
            for (AdminSmUserRoleRelEntity userRole : userRoleList) {
                if (StringUtils.nonEmpty(userRole.getRoleId())) {
                    AdminSmRoleEntity roleEntity = adminSmRoleService.getOne(new QueryWrapper<AdminSmRoleEntity>()
                            .eq("ROLE_ID", userRole.getRoleId())
                            .eq("ROLE_STS", AvailableStateEnum.ENABLED));
                    if (Objects.nonNull(roleEntity)) {
                        RoleSessionVo role = new RoleSessionVo();
                        org.springframework.beans.BeanUtils.copyProperties(roleEntity, role);
                        roles.add(role);
                    }
                }
            }
            userSessionVo.setRoles(roles);
        }
    }

    private void setDutysIfHasDutyRel(List<AdminSmUserDutyRelEntity> userDutyRelEntities, UserSessionVo userSessionVo) {
        if (CollectionUtils.nonEmpty(userDutyRelEntities)) {
            List<DutySessionVo> dutys = userDutyRelEntities.stream().map(item -> {
                DutySessionVo duty = new DutySessionVo();
                if (StringUtils.nonEmpty(item.getDutyId())) {
                    AdminSmDutyEntity dutyEntity = adminSmDutyService.getOne(new QueryWrapper<AdminSmDutyEntity>()
                            .eq("DUTY_ID", item.getDutyId())
                            .eq("DUTY_STS", AvailableStateEnum.ENABLED));
                    org.springframework.beans.BeanUtils.copyProperties(dutyEntity, duty);
                }
                return duty;
            }).collect(Collectors.toList());
            userSessionVo.setDutys(dutys);
        }
    }

    /**
     * 判断新增用户工号和账号是否已存在
     * @param entity 新增用户信息
     * @return boolean
     */
    @Override
    public boolean judgeExistEntity(AdminSmUserEntity entity) {
        AdminSmUserEntity userEntity=adminSmUserDao.getUserInfoByCode(entity.getUserCode(),entity.getLoginCode());
        return userEntity != null;
    }

    @Override
    public void updateUserCache(AdminSmUserEntity entity) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        String tenantId=String.valueOf(SessionUtils.getUserInformation().getAdditional(OcaCommonConstants.SESSION_DATATENANT_KEY));
        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        Map<String,String> map=new HashMap<>(8);
        map.put(entity.getUserId(),entity.getUserName());
        cacheService.hashPut(name,tenantId, redisKey, map, 24 * 15 * 3600);
    }

    @Override
    public void deletePartUserCache(String[] ids) {
        String name = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
        for (String id : ids
        ) {
            cacheService.hashKeyDelete(name, redisKey, id);
        }
    }

    /**
     * 获取用户对应所有机构和角色
     *
     * @param loginCode
     * @return List<AdminSmOrgRoleVo>
     */
    @Override
    public List<AdminSmOrgRoleVo> listOrgRolesByCode(String loginCode) {
        return getBaseMapper().listOrgRolesByCode(loginCode);
    }
}