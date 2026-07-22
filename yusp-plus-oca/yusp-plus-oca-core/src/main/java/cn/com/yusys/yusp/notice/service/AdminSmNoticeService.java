package cn.com.yusys.yusp.notice.service;

import cn.com.yusys.yusp.notice.entity.AdminSmNoticeEntity;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeCondition;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeForm;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeVo;
import cn.com.yusys.yusp.notice.vo.NoticeHomePageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统公告表
 *
 * @author zhangyt12
 * @date 2020-12-15 13:04:49
 */
public interface AdminSmNoticeService extends IService<AdminSmNoticeEntity> {
    /**
     * 查询自己能查看的已发布的公告
     * @param adminSmNoticeCondition 公告查询条件
     * @return 分页查询结果
     */
    IPage<NoticeHomePageVo> getViewList(AdminSmNoticeCondition adminSmNoticeCondition);

    /**
     * 查询自己有权限看的未读的公告
     * @return 公告表查询结果集合，最多返回 5 条
     */
    List<AdminSmNoticeEntity> getUnreadList();

    /**
     * 查询自己编写的公告
     * @param adminSmNoticeCondition 公告查询条件
     * @return 分页查询结果
     */
    IPage<AdminSmNoticeEntity> getControlList(AdminSmNoticeCondition adminSmNoticeCondition);

    /**
     * 新增公告
     * @param adminSmNoticeForm 自定义实体类，接收新增公告的参数
     */
    void createNotice(AdminSmNoticeForm adminSmNoticeForm);

    /**
     * 批量删除公告
     * @param noticeIds 被删除公告的 id 集合
     * @return 删除成功返回 null，删除失败返回失败信息
     */
    String deleteNotice(List<String> noticeIds);

    /**
     * 修改公告
     * @param adminSmNoticeForm 自定义实体类，接收修改公告的参数
     * @return 修改成功返回 null，修改失败返回失败信息
     */
    String updateNotice(AdminSmNoticeForm adminSmNoticeForm);

    /**
     * 批量发布公告
     * @param noticeIds 批量发布公告的 id 集合
     */
    void pubNotices(List<String> noticeIds);

    /**
     * 获取公告信息
     * @param noticeId 公告 id
     * @return 公告详细信息自定义实体类
     */
    AdminSmNoticeVo getInfo(String noticeId);
}

