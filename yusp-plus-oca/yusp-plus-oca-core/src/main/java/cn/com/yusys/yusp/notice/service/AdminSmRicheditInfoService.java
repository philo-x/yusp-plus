package cn.com.yusys.yusp.notice.service;

import cn.com.yusys.yusp.notice.entity.AdminSmRicheditInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 富文本信息表对应 service 接口层
 *
 * @author zhangyt12
 * @date 2020-12-15 13:05:43
 */
public interface AdminSmRicheditInfoService extends IService<AdminSmRicheditInfoEntity> {

    /**
     * 删除富文本表
     * @param noticeIds 公告 id 集合
     */
    void deleteRicheditInfo(List<String> noticeIds);

    /**
     * 按照 relId 修改富文本内容
     * @param richeditInfoEntity 接收前端请求的富文本表实体类
     */
    void updateByRelId(AdminSmRicheditInfoEntity richeditInfoEntity);
}

