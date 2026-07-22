package cn.com.yusys.yusp.notice.service;

import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReadEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统公告用户查阅历史表
 *
 * @author danyb1
 * @date 2020-12-15 13:05:27
 */
public interface AdminSmNoticeReadService extends IService<AdminSmNoticeReadEntity> {

    /**
     * 批量保存查看公告记录
     * @param noticeIds 公告 id 集合
     */
    void recordRead(List<String> noticeIds);
}

