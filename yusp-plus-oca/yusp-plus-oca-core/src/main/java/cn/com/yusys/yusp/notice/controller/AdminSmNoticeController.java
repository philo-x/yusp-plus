package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.entity.AdminSmNoticeEntity;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeCondition;
import cn.com.yusys.yusp.notice.form.AdminSmNoticeForm;
import cn.com.yusys.yusp.notice.service.AdminSmNoticeService;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeVo;
import cn.com.yusys.yusp.notice.vo.NoticeHomePageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统公告表
 * @author zhangyt12
 * @date 2021/6/24 14:07
 */
@RestController
@RequestMapping("/api/adminsmnotice")
public class AdminSmNoticeController {

    @Autowired
    private AdminSmNoticeService adminSmNoticeService;

    /**
     * 查询有权看的已发布的公告
     * @param adminSmNoticeCondition 自定义实体类，用于接收可能前端传递的参数
     * @return 返回分页查询对象
     */
    @PostMapping("/view/list")
    public JClientRspEntity<IPage<NoticeHomePageVo>> getViewList(@RequestBody JClientReqEntity<AdminSmNoticeCondition> adminSmNoticeCondition) {
        IPage<NoticeHomePageVo> iPage = adminSmNoticeService.getViewList(adminSmNoticeCondition.getBody());
        return JClientRspEntity.buildSuccess(iPage);
    }

    /**
     * 查询有权限看的未读的公告
     * @return 返回最多 5 条公告记录
     */
    @PostMapping("/unread/list")
    public JClientRspEntity<List<AdminSmNoticeEntity>> getUnreadList() {
        List<AdminSmNoticeEntity> list = adminSmNoticeService.getUnreadList();
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 查询自己编写的公告
     * @param adminSmNoticeCondition 自定义实体类，用于接收可能前端传递的参数
     * @return 返回分页查询对象
     */
    @PostMapping("/control/list")
    public JClientRspEntity<IPage<AdminSmNoticeEntity>> getControlList(@RequestBody JClientReqEntity<AdminSmNoticeCondition> adminSmNoticeCondition) {
        IPage<AdminSmNoticeEntity> iPage = adminSmNoticeService.getControlList(adminSmNoticeCondition.getBody());
        return JClientRspEntity.buildSuccess(iPage);
    }

    /**
     * 新增公告
     * @param adminSmNoticeForm 自定义实体类，用于接收新增公告的参数
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/add")
    public JClientRspEntity<Object> createNotice(@RequestBody JClientReqEntity<AdminSmNoticeForm> adminSmNoticeForm) {
        adminSmNoticeService.createNotice(adminSmNoticeForm.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 删除公告
     * @param noticeIds 批量删除公告，接收多个公告 id
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/delete")
    public JClientRspEntity<Object> deleteNotice(@RequestBody JClientReqEntity<List<String>> noticeIds) {
        String message = adminSmNoticeService.deleteNotice(noticeIds.getBody());
        if (message == null) {
            return JClientRspEntity.buildSuccess("成功");
        } else {
            return JClientRspEntity.buildFail(message);
        }
    }

    /**
     * 修改公告
     * @param adminSmNoticeForm 自定义实体类，用于接收修改公告的参数
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/update")
    public JClientRspEntity<Object> updateNotice(@RequestBody JClientReqEntity<AdminSmNoticeForm> adminSmNoticeForm) {
        String message = adminSmNoticeService.updateNotice(adminSmNoticeForm.getBody());
        if (message != null) {
            return JClientRspEntity.buildFail(message);
        }
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 发布公告(批量)
     * @param noticeIds 批量发布公告，接收多个公告 id
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/pub")
    public JClientRspEntity<Object> pubNotices(@RequestBody JClientReqEntity<List<String>> noticeIds) {
        adminSmNoticeService.pubNotices(noticeIds.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 公告信息查询
     * @param noticeId 查询公告详情，接收公告 id
     * @return 返回公告详情自定义实体类
     */
    @PostMapping("/info/{noticeId}")
    public JClientRspEntity<AdminSmNoticeVo> getInfo(@PathVariable String noticeId) {
        AdminSmNoticeVo noticeVo = adminSmNoticeService.getInfo(noticeId);
        return JClientRspEntity.buildSuccess(noticeVo);
    }
}
