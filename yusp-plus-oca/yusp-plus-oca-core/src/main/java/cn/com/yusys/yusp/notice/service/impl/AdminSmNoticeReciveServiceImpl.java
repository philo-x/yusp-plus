package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.notice.constant.NoticeConstant;
import cn.com.yusys.yusp.notice.dao.AdminSmNoticeReciveDao;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReciveEntity;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeReciveService;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeReciveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公告发布对象表对应 service 实现类
 * @author zhangyt12
 * @date 2021/6/25 17:27
 */
@Service("adminNoticeReciveService")
public class AdminSmNoticeReciveServiceImpl extends ServiceImpl<AdminSmNoticeReciveDao, AdminSmNoticeReciveEntity> implements AdminSmNoticeReciveService {

    @Override
    public List<AdminSmNoticeReciveEntity> findListByCondition(List<String> roleIdList, List<String> orgIdList) {
        roleIdList.add(NoticeConstant.RECIVE_OGJ_ID_NA);
        orgIdList.add(NoticeConstant.RECIVE_OGJ_ID_NA);
        QueryWrapper<AdminSmNoticeReciveEntity> query = new QueryWrapper<>();
        query.select("DISTINCT NOTICE_ID")
                .eq("a.RECIVE_TYPE", NoticeConstant.RECIVE_TYPE_ROLE)
                .in("a.RECIVE_OGJ_ID", roleIdList)
                .in("b.RECIVE_OGJ_ID", orgIdList)
                .eq("b.RECIVE_TYPE", NoticeConstant.RECIVE_TYPE_ORG);
        return getBaseMapper().selectByCondition(query);
    }

    @Override
    public List<AdminSmNoticeReciveVo> selectReciveIdAndName(String noticeId) {
        return getBaseMapper().selectReciveIdAndName(noticeId);
    }

    @Override
    public void saveByAdminSmNoticeAllEntity(AdminSmNoticeReciveEntity reciveEntity, String type) {
        // 按照部门 id 和角色 id 分配权限
        List<AdminSmNoticeReciveEntity> entityList = getAnnouncementEntity(reciveEntity);
        if (NoticeConstant.RECIVE_UPDATE.equals(type)) {
            // 修改公告、删除公告
            List<String> noticeIds = entityList.stream().map(AdminSmNoticeReciveEntity::getNoticeId).collect(Collectors.toList());
            this.deleteRecive(noticeIds);
        }
        this.saveBatch(entityList);
    }

    @Override
    public void deleteRecive(List<String> noticeIds) {
        QueryWrapper<AdminSmNoticeReciveEntity> wrapper = new QueryWrapper<>();
        wrapper.in("NOTICE_ID", noticeIds);
        this.remove(wrapper);
    }

    List<AdminSmNoticeReciveEntity> getAnnouncementEntity(AdminSmNoticeReciveEntity entity) {
        List<AdminSmNoticeReciveEntity> entityList = new ArrayList<>();
        String noticeId = entity.getNoticeId();
        String reciveOgjId = entity.getReciveOgjId();
        String reciveRoleId = entity.getReciveRoleId();
        // 两种权限为空，存为NA，所有人都能访问
        if (StringUtils.isEmpty(reciveOgjId) && StringUtils.isEmpty(reciveRoleId)) {
            entityList.add(new AdminSmNoticeReciveEntity(
                    StringUtils.getUUID(),
                    noticeId,
                    NoticeConstant.RECIVE_TYPE_ROLE,
                    NoticeConstant.RECIVE_OGJ_ID_NA));
            entityList.add(new AdminSmNoticeReciveEntity(
                    StringUtils.getUUID(),
                    noticeId,
                    NoticeConstant.RECIVE_TYPE_ORG,
                    NoticeConstant.RECIVE_OGJ_ID_NA));
            return entityList;
        }
        // 拆分角色访问权限
        if (StringUtils.nonEmpty(reciveRoleId)) {
            splitRoleAccessPermissions(reciveRoleId, entityList, noticeId);
        }
        // 拆分部门访问权限
        if (StringUtils.nonBlank(reciveOgjId)) {
            splitOrgAccessPermissions(reciveOgjId, entityList, noticeId);
        }
        return entityList;
    }

    private static void splitRoleAccessPermissions(String reciveRoleId, List<AdminSmNoticeReciveEntity> entityList, String noticeId) {
        if (reciveRoleId.contains(NoticeConstant.SEGMENTATION_COMMA)) {
            // 分解roleIds
            String[] roleIds = reciveRoleId.split(NoticeConstant.SEGMENTATION_COMMA);
            for (String roleId : roleIds) {
                entityList.add(new AdminSmNoticeReciveEntity(
                        StringUtils.getUUID(),
                        noticeId,
                        NoticeConstant.RECIVE_TYPE_ROLE,
                        roleId));
            }
        } else {
            entityList.add(new AdminSmNoticeReciveEntity(
                    StringUtils.getUUID(),
                    noticeId,
                    NoticeConstant.RECIVE_TYPE_ROLE,
                    reciveRoleId));
        }
    }

    /**
     * 拆分部门访问权限
     */
    private static void splitOrgAccessPermissions(String reciveOgjId, List<AdminSmNoticeReciveEntity> entityList, String noticeId) {
        if (reciveOgjId.contains(NoticeConstant.SEGMENTATION_COMMA)) {
            String[] orgIds = reciveOgjId.split(NoticeConstant.SEGMENTATION_COMMA);
            for (String orgId : orgIds) {
                entityList.add(new AdminSmNoticeReciveEntity(
                        StringUtils.getUUID(),
                        noticeId,
                        NoticeConstant.RECIVE_TYPE_ORG,
                        orgId));
            }
        } else {
            entityList.add(new AdminSmNoticeReciveEntity(
                    StringUtils.getUUID(),
                    noticeId,
                    NoticeConstant.RECIVE_TYPE_ORG,
                    reciveOgjId));
        }
    }
}