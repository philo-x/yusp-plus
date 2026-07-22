package cn.com.yusys.yusp.notice.service;


import cn.com.yusys.yusp.notice.entity.AdminSmRicheditFileInfoEntity;
import cn.com.yusys.yusp.notice.form.AdminSmRicheditFileInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 富文本附件表对应 service 接口层
 *
 * @author zhangyt12
 * @date 2021-03-31 11:00:50
 */
public interface AdminSmRicheditFileInfoService extends IService<AdminSmRicheditFileInfoEntity> {

    /**
     * 新增富文本附件
     * @param fileInfoFormList 附件表对应接收参数自定义实体类的集合
     */
    void addFileInfo(List<AdminSmRicheditFileInfoForm> fileInfoFormList);

    /**
     * 根据业务编号获取附件信息
     * @param noticeId 公告 id
     * @return 返回附件信息集合
     */
    List<AdminSmRicheditFileInfoEntity> getFileByBusNo(String noticeId);

    /**
     * 删除文件信息
     * @param noticeId 公告 id 集合
     */
    void deleteByBusNo(String ... noticeId);

    /**
     * 批量删除富文本文件数据及 oss 中的文件
     * @param fileInfoFormList 附件表对应接收参数自定义实体类的集合
     */
    void deleteFileInfo(List<AdminSmRicheditFileInfoForm> fileInfoFormList);

    /**
     * 删除文件表及删除富文本附件表
     * @param fileId 文件id
     */
    void deleteFileInfoWithFileServer(String fileId);
}

