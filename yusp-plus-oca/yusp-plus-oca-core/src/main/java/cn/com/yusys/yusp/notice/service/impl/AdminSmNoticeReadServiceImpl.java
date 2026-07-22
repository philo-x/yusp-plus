package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.notice.dao.AdminSmNoticeReadDao;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReadEntity;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeReadService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统公告用户查阅历史表对应 service 实现类
 *
 * @author zhangyt12
 * @date 2020-12-15 13:05:27
 */
@Service("adminSmNoticeReadService")
public class AdminSmNoticeReadServiceImpl extends ServiceImpl<AdminSmNoticeReadDao, AdminSmNoticeReadEntity> implements AdminSmNoticeReadService {

    private static final Logger log = LoggerFactory.getLogger(AdminSmNoticeReadServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordRead(List<String> noticeIds) {
        if (noticeIds != null && !noticeIds.isEmpty()) {
            String userId = SessionUtils.getUserId();
            // 已阅的公告，不能再次阅读
            List<AdminSmNoticeReadEntity> alreadyEntityList = getByNoticeIds(noticeIds, userId);
            List<String> alreadyNoticeIds = alreadyEntityList.stream().map(AdminSmNoticeReadEntity::getNoticeId).toList();
            log.info("已阅公告：" + alreadyNoticeIds.toString());
            noticeIds.removeAll(alreadyNoticeIds);

            List<AdminSmNoticeReadEntity> readEntityList = new ArrayList<>();

            if (!noticeIds.isEmpty()) {
                for (String noticeId : noticeIds) {
                    AdminSmNoticeReadEntity entity = new AdminSmNoticeReadEntity();
                    entity.setReadId(StringUtils.getUUID());
                    entity.setNoticeId(noticeId);
                    entity.setUserId(SessionUtils.getUserId());
                    entity.setReadTime(DateUtils.formatDateTimeByDef());
                    readEntityList.add(entity);
                }
                this.saveBatch(readEntityList);
            }
        }
    }

    /**
     * 使用noticeId和阅读用户id，查询阅读记录
     * @param noticeIds 不为 null 且不为空
     * @return
     */
    public List<AdminSmNoticeReadEntity> getByNoticeIds(List<String> noticeIds, String userId) {
        QueryWrapper<AdminSmNoticeReadEntity> wrapper = new QueryWrapper<>();
        wrapper.in("notice_id", noticeIds);
        wrapper.eq("user_id", userId);
        return this.list(wrapper);
    }
}