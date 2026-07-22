package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.user.UserIdentity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.notice.constant.NoticeConstant;
import cn.com.yusys.yusp.notice.dao.AdminSmNoticeDao;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeEntity;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReciveEntity;
import cn.com.yusys.yusp.notice.entity.AdminSmRicheditInfoEntity;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeCondition;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeForm;
import cn.com.yusys.yusp.notice.form.AdminSmRicheditFileInfoForm;
import cn.com.yusys.yusp.notice.service.*;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeReciveVo;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeVo;
import cn.com.yusys.yusp.notice.vo.AdminSmReciveVo;
import cn.com.yusys.yusp.notice.vo.NoticeHomePageVo;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统公告表对应 service 层实现类
 * @author zhangyt12
 * @date 2021/6/25 17:39
 */
@Service("adminSmNoticeService")
public class AdminSmNoticeServiceImpl extends ServiceImpl<AdminSmNoticeDao, AdminSmNoticeEntity> implements AdminSmNoticeService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmNoticeServiceImpl.class);
    @Autowired
    private AdminSmRicheditInfoService adminSmRicheditInfoService;
    @Autowired
    private AdminSmNoticeReciveService adminSmNoticeReciveService;
    @Autowired
    private AdminSmUserService adminSmUserService;
    @Autowired
    private AdminSmNoticeReadService adminSmNoticeReadService;
    @Autowired
    private AdminSmRicheditFileInfoService adminSmRicheditFileInfoService;

    @Override
    public IPage<NoticeHomePageVo> getViewList(AdminSmNoticeCondition condition) {
        QueryWrapper<NoticeHomePageVo> wrapper = new QueryWrapper<>();

        // 获取当前用户可查看的公告 id 集合
        List<String> noticeIds = this.getReciveNoticeIds();

        // 1、查询必要条件
        wrapper.ge("n.active_date", DateUtils.formatDateByDef());
        wrapper.eq("n.pub_sts", "O");
        wrapper.in(noticeIds.size() > 0,"n.notice_id", noticeIds);

        // 2、多条件查询
        if (!StringUtils.isEmpty(condition.getKeyWord())) {
            wrapper.like("n.notice_title", condition.getKeyWord());
        } else {
            wrapper.like(!StringUtils.isEmpty(condition.getNoticeTitle()), "n.notice_title", condition.getNoticeTitle());
            wrapper.eq(!StringUtils.isEmpty(condition.getNoticeLevel()), "n.notice_level", condition.getNoticeLevel());
            wrapper.isNotNull(condition.getReadSts() != null && "1".equals(condition.getReadSts()), "r.read_id");
            wrapper.isNull(condition.getReadSts() != null && "0".equals(condition.getReadSts()), "r.read_id");
        }

        // 3、排序
        wrapper.orderByAsc("READ_STS", "n.IS_TOP");
        wrapper.orderByDesc("n.PUB_TIME");
        IPage<NoticeHomePageVo> iPage = new Page<>(condition.getPage(), condition.getSize());
        iPage = getBaseMapper().findListByCondition(iPage, wrapper, SessionUtils.getUserId());
        log.info("List<NoticeHomePageVo> pageVoList : " + iPage.toString());
        return iPage;
    }

    @Override
    public List<AdminSmNoticeEntity> getUnreadList() {
        List<String> noticeIds = getReciveNoticeIds();
        QueryWrapper<AdminSmNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("PUB_STS", "O");
        wrapper.ge("ACTIVE_DATE", DateUtils.formatDateByDef());
        wrapper.in(!noticeIds.isEmpty(),"NOTICE_ID", noticeIds);
        wrapper.notExists("select \n" +
                "  `READ_ID`,\n" +
                "  `NOTICE_ID`,\n" +
                "  `USER_ID`,\n" +
                "  `READ_TIME`,\n" +
                "  `DATA_TENANT_ID`,\n" +
                "  `LAST_CHG_USR`,\n" +
                "  `LAST_CHG_DT` from admin_sm_notice_read where NOTICE_ID = admin_sm_notice.NOTICE_ID and user_id = '" + SessionUtils.getUserId() + "'");
        wrapper.orderByAsc("NOTICE_LEVEL");
        wrapper.orderByDesc("PUB_TIME");

        // 提示框中的公告数量不能超过 5 个
        IPage<AdminSmNoticeEntity> iPage = new Page<>(1, 5);
        iPage = this.page(iPage, wrapper);
        return iPage.getRecords();
    }

    @Override
    public IPage<AdminSmNoticeEntity> getControlList(AdminSmNoticeCondition condition) {
        QueryWrapper<AdminSmNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("CREATOR_ID", SessionUtils.getUserId());

        // 判断是 关键字查询 或者 多条件查询
        if (!StringUtils.isEmpty(condition.getKeyWord())) {
            wrapper.like("NOTICE_TITLE", condition.getKeyWord());
        } else {
            wrapper.like(!StringUtils.isEmpty(condition.getNoticeTitle()), "NOTICE_TITLE", condition.getNoticeTitle());
            wrapper.eq(!StringUtils.isEmpty(condition.getNoticeLevel()), "notice_level", condition.getNoticeLevel());
            wrapper.eq(!StringUtils.isEmpty(condition.getPubSts()), "pub_sts", condition.getPubSts());
        }
        wrapper.orderByAsc( "pub_sts", "notice_level");
        wrapper.orderByDesc("pub_time" ,"creator_time");
        IPage<AdminSmNoticeEntity> iPage = new Page<>(condition.getPage(), condition.getSize());
        iPage = this.page(iPage, wrapper);
        log.info("List<AdminSmNoticeEntity> pageVoList : " + iPage.toString());
        return iPage;
    }

    private List<String> getReciveNoticeIds() {
        String userId = SessionUtils.getUserId();
        log.info("查看公告权限，SessionUtils.getUserId(): " + SessionUtils.getUserId());

        // 1、查询当前用户的 roleId 和 orgId
        List<AdminSmReciveVo> reciveVoList = adminSmUserService.selectRoleAndObj(userId);
        List<String> roleIdList = reciveVoList.stream().map(AdminSmReciveVo::getRoleId).collect(Collectors.toList());
        List<String> orgIdList = reciveVoList.stream().map(AdminSmReciveVo::getOrgId).collect(Collectors.toList());
        log.info("查看公告权限，getReciveNoticeIds: " + reciveVoList);

        // 2、获取有权限看的公告
        List<AdminSmNoticeReciveEntity> reciveEntityList = adminSmNoticeReciveService.findListByCondition(roleIdList, orgIdList);
        List<String> noticeIds = reciveEntityList.stream()
                .map(AdminSmNoticeReciveEntity::getNoticeId)
                .collect(Collectors.toList());
        log.info("该用户可以产看的公告id，List<String> noticeIds : " + noticeIds.toString());
        return noticeIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotice(AdminSmNoticeForm form) {
        log.info("新增公告前端发来的 form 数据： " + form.toString());
        // 生成富文本表主键 id
        String richeditId = StringUtils.getUUID();
        // 从 form 中获取表对应实体类对象：noticeEntity，插入notice表
        AdminSmNoticeEntity noticeEntity = this.getNoticeEntity(form);
        noticeEntity.setRicheditId(richeditId);
        log.info(noticeEntity.toString());
        this.save(noticeEntity);

        AdminSmRicheditInfoEntity richeditInfoEntity = new AdminSmRicheditInfoEntity();
        richeditInfoEntity.setRicheditId(richeditId);
        richeditInfoEntity.setContent(form.getContext());
        richeditInfoEntity.setRelId(noticeEntity.getNoticeId());
        richeditInfoEntity.setRelMod(NoticeConstant.RICHEDIT_REL_MOD);
        adminSmRicheditInfoService.save(richeditInfoEntity);

        // 获取公告权限对象，插入访问权限控制表
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity(
                noticeEntity.getNoticeId(),
                (StringUtils.isBlank(form.getReciveOrgId()) ? "NA" : form.getReciveOrgId()),
                (StringUtils.isBlank(form.getReciveRoleId()) ? "NA" : form.getReciveRoleId()));
        log.info(reciveEntity.toString());
        this.adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, NoticeConstant.RECIVE_CREATE);

        // 保存公告附件
        form.setNoticeId(noticeEntity.getNoticeId());
        this.relationFileInfo(form, false);
    }

    private void relationFileInfo(AdminSmNoticeForm form, boolean b) {
        if (b) {
            adminSmRicheditFileInfoService.deleteByBusNo(form.getNoticeId());
        }
        if (form.getFileInfoFormList() != null && form.getFileInfoFormList().size() > 0) {
            List<AdminSmRicheditFileInfoForm> fileInfoFormList = form.getFileInfoFormList();
            log.info("富文本中的附件信息 - fileInfoFormList： " + fileInfoFormList);
            if (fileInfoFormList != null && fileInfoFormList.size() > 0) {
                fileInfoFormList.forEach((fileInfoForm) -> {
                    fileInfoForm.setBusNo(form.getNoticeId());
                    fileInfoForm.setExtName(fileInfoForm.getExtName());
                    fileInfoForm.setFilePath(fileInfoForm.getFilePath());
                });
                this.adminSmRicheditFileInfoService.addFileInfo(fileInfoFormList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteNotice(List<String> noticeIds) {
        log.info("删除公告的id：{}", noticeIds);

        if (noticeIds == null || noticeIds.size() == 0) {
            throw BizException.error(null,"52000001","请选择您想要删除的公告！");
        }

        // 首先判断公告是否已经发布，如果已经发布不能删除
        List<AdminSmNoticeEntity> entityList = listByIds(noticeIds);
        for (int i = 0; i < noticeIds.size(); i++) {
            if (NoticeConstant.PUB_STS_O.equals(entityList.get(i).getPubSts())) {
                throw BizException.error(null,"52000002","该公告已发布，不能删除!");
            }
        }

        getBaseMapper().deleteBatchIds(noticeIds);

        // 删除recive表
        adminSmNoticeReciveService.deleteRecive(noticeIds);

        // 删除富文本表
        adminSmRicheditInfoService.deleteRicheditInfo(noticeIds);

        // 删除公告文件
        adminSmRicheditFileInfoService.deleteByBusNo(new String[noticeIds.size()]);

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateNotice(AdminSmNoticeForm form) {

        // 已发布的公告不能修改
        AdminSmNoticeEntity entity = getById(form.getNoticeId());
        if (NoticeConstant.PUB_STS_O.equals(entity.getPubSts())) {
            throw BizException.error(null, "52000003","该公告已发布，不能修改!");
        }

        // 1、更新富文本表
        if (form.getContext() != null && form.getContext().length() > 0) {
            AdminSmRicheditInfoEntity richeditInfoEntity = new AdminSmRicheditInfoEntity();
            richeditInfoEntity.setRelId(form.getNoticeId());
            richeditInfoEntity.setContent(form.getContext());
            if (StringUtils.isEmpty(form.getRicheditId())) {
                String richeditId = StringUtils.getUUID();
                richeditInfoEntity.setRicheditId(richeditId);
                richeditInfoEntity.setRelMod(NoticeConstant.RICHEDIT_REL_MOD);
                form.setRicheditId(richeditId);
                adminSmRicheditInfoService.save(richeditInfoEntity);
            }
            adminSmRicheditInfoService.updateByRelId(richeditInfoEntity);
        }

        // 2、删除该公告之前关联recive表中的权限数据，并存储新数据
        AdminSmNoticeReciveEntity reciveEntity = new AdminSmNoticeReciveEntity();
        reciveEntity.setNoticeId(form.getNoticeId());
        reciveEntity.setReciveRoleId(form.getReciveRoleId());
        reciveEntity.setReciveOgjId(form.getReciveOrgId());
        adminSmNoticeReciveService.saveByAdminSmNoticeAllEntity(reciveEntity, NoticeConstant.RECIVE_UPDATE);
        log.info("修改公告查看权限逻辑完成：" + reciveEntity.toString());

        // 3、更新notice主表
        AdminSmNoticeEntity noticeEntity = getNoticeEntity(form);
        log.info("修改公告时的数据：" + noticeEntity.toString());
        getBaseMapper().updateById(noticeEntity);

        // 4、更新富文本附件
        this.relationFileInfo(form, true);

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pubNotices(List<String> noticeIds) {
        // 需要进行判断: 如果已经发布过了，就不能再发布了
        if (noticeIds != null && !noticeIds.isEmpty()) {
            List<AdminSmNoticeEntity> entityList = listByIds(noticeIds);

            // 获取发布人name和发布机构name
            User userInfo = SessionUtils.getUserInformation();
            UserIdentity org = userInfo.getOrg();
            // 遍历当前需要发布的公告，如果未发布，就进行发布
            List<AdminSmNoticeEntity> releaseEntityList = new ArrayList<>();
            for (AdminSmNoticeEntity noticeEntity : entityList) {
                if (!NoticeConstant.PUB_STS_O.equals(noticeEntity.getPubSts())) {
                    noticeEntity.setPubSts(NoticeConstant.PUB_STS_O);
                    noticeEntity.setPubUserId(SessionUtils.getUserId());
                    noticeEntity.setPubUserName(userInfo.getUserName());
                    noticeEntity.setPubOrgId(org.getId());
                    noticeEntity.setPubOrgName(org.getName());
                    noticeEntity.setPubTime(DateUtils.formatDateTimeByDef());
                    releaseEntityList.add(noticeEntity);
                }
            }
            log.info("发布的公告：" + releaseEntityList);
            this.updateBatchById(releaseEntityList);
        }
    }

    private AdminSmNoticeEntity getNoticeEntity(AdminSmNoticeForm form) {
        String userName = adminSmUserService.getUserNameById(SessionUtils.getUserId());
        AdminSmNoticeEntity noticeEntity = BeanUtils.beanCopy(form, AdminSmNoticeEntity.class);
        if (StringUtils.isEmpty(noticeEntity.getNoticeId())) {
            noticeEntity.setNoticeId(StringUtils.getUUID());
        }
        if (StringUtils.isEmpty(form.getPubSts())) {
            noticeEntity.setPubSts(NoticeConstant.PUB_STS_C);
        }
        noticeEntity.setCreatorId(SessionUtils.getUserId());
        noticeEntity.setCreatorName(userName);
        noticeEntity.setCreatorTime(DateUtils.formatDateTimeByDef());
        if (StringUtils.isEmpty(form.getTopActiveDate())) {
            noticeEntity.setTopActiveDate(null);
        }
        return noticeEntity;
    }

    @Override
    public AdminSmNoticeVo getInfo(String noticeId) {
        AdminSmNoticeEntity noticeEntity = this.getById(noticeId);
        AdminSmNoticeVo noticeVo = BeanUtils.beanCopy(noticeEntity, AdminSmNoticeVo.class);
        // 将查看过详情的公告标为已读
        if ( NoticeConstant.PUB_STS_O.equals(noticeEntity.getPubSts())) {
            List<String> noticeList = new ArrayList<>();
            noticeList.add(noticeId);
            adminSmNoticeReadService.recordRead(noticeList);
        }

        // 获取富文本信息
        AdminSmRicheditInfoEntity richeditInfoEntity = adminSmRicheditInfoService.getById(noticeVo.getRicheditId());
        log.info("info 查询富文本表 context : " + richeditInfoEntity);
        if (richeditInfoEntity != null) {
            noticeVo.setContext(richeditInfoEntity.getContent());
        }

        // 查询公告权限的相关信息
        this.getReciveIdAndName(noticeVo);

        // 获取公告相关的附件信息
        noticeVo.setFileInfoFormList(adminSmRicheditFileInfoService.getFileByBusNo(noticeId));

        log.info(noticeVo.toString());
        return noticeVo;
    }

    private void getReciveIdAndName(AdminSmNoticeVo noticeVo) {
        noticeVo.setReciveOrgMap(new HashMap<>(16));
        noticeVo.setReciveRoleMap(new HashMap<>(16));
        List<AdminSmNoticeReciveVo> reciveVoList = adminSmNoticeReciveService.selectReciveIdAndName(noticeVo.getNoticeId());
        for (AdminSmNoticeReciveVo vo : reciveVoList) {
            if (NoticeConstant.RECIVE_TYPE_ORG.equals(vo.getReciveType())) {
                noticeVo.getReciveOrgMap().put(vo.getReciveOgjId(), NoticeConstant.RECIVE_OGJ_ID_NA.equals(vo.getReciveOgjId()) ? null : vo.getOrgName());
            } else {
                noticeVo.getReciveRoleMap().put(vo.getReciveOgjId(), NoticeConstant.RECIVE_OGJ_ID_NA.equals(vo.getReciveOgjId()) ? null : vo.getRoleName());
            }
        }
    }
}

