package cn.com.yusys.yusp.notice.service;

import cn.com.yusys.yusp.notice.entity.AdminSmNoticeReciveEntity;
import cn.com.yusys.yusp.notice.vo.AdminSmNoticeReciveVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 公告发布对象表
 *
 * @author danyb1
 * @date 2020-12-15 13:05:33
 */
public interface AdminSmNoticeReciveService extends IService<AdminSmNoticeReciveEntity> {

    /**
     * 新增公告：保存公告权限
     * @param reciveEntity 公告发布实体类
     * @param type 新增公告 或者 修改公告
     */
    void saveByAdminSmNoticeAllEntity(AdminSmNoticeReciveEntity reciveEntity, String type);

    /**
     * noticeId 批量删除公告权限
     * @param noticeIds 公告 id 集合
     */
    void deleteRecive(List<String> noticeIds);

    /**
     * 按照条件查询数据
     * @param roleIdList 发布对象的角色 id 集合
     * @param orgIdList 发布对象的机构 id 集合
     * @return 查询公告发布数据的集合
     */
    List<AdminSmNoticeReciveEntity> findListByCondition(List<String> roleIdList, List<String> orgIdList);

    /**
     * 查询公告发布的对象
     * @param noticeId 公告 id
     * @return 返回公告发布数据的自定义实体类集合
     */
    List<AdminSmNoticeReciveVo> selectReciveIdAndName(String noticeId);
}

