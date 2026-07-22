package cn.com.yusys.yusp.notice.dao;

import cn.com.yusys.yusp.notice.entity.AdminSmNoticeEntity;
import cn.com.yusys.yusp.notice.vo.NoticeHomePageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 系统公告表对应 mapper 接口类
 * @author zhangyt12
 * @date 2021/6/24 15:27
 */
public interface AdminSmNoticeDao extends BaseMapper<AdminSmNoticeEntity> {

    /**
     * 查询自己能查看的已发布的公告
     * @param iPage 分页对象
     * @param query 查询条件对象
     * @param userId 用户 id
     * @return 返回分页查询结果 IPage 对象
     */
    IPage<NoticeHomePageVo> findListByCondition(
            IPage<NoticeHomePageVo> iPage,
            @Param(Constants.WRAPPER) QueryWrapper<NoticeHomePageVo> query,
            @Param("userId") String userId);
}
