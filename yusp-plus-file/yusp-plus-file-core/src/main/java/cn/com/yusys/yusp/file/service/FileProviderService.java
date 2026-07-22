package cn.com.yusys.yusp.file.service;

import cn.com.yusys.yusp.commons.module.adapter.query.QueryModel;
import cn.com.yusys.yusp.file.domain.AdminFileUploadInfo;
import cn.com.yusys.yusp.file.domain.AdminSmRicheditFileInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;


/**
 * @author yusys
 * @version 1.0
 * @date 2021/1/26 10:04
 */
public interface FileProviderService {

    /**
     * selectFileUploadByModel
     * @param param param
     * @return List<AdminFileUploadInfo>
     */
    List<AdminFileUploadInfo> selectFileUploadByModel(Map<String, Object> param);

    /**
     * deleteFileRecords
     * @param paths paths
     */
    void deleteFileRecords(String paths);

    /**
     * deleteFile
     * @param paths paths
     */
    void deleteFile(String paths);

    /**
     * selectRicheditFileByModel
     * @param param param
     * @return List<AdminSmRicheditFileInfo>
     */
    List<AdminSmRicheditFileInfo> selectRicheditFileByModel(Map<String, Object> param);

    /**
     * uploadRicheditFile
     * @param adminSmRicheditFileInfo adminSmRicheditFileInfo
     * @return int
     */
    int uploadRicheditFile(AdminSmRicheditFileInfo adminSmRicheditFileInfo);

    /**
     * deleteRicheidtFileRecords
     * @param paths paths
     */
    void deleteRicheidtFileRecords(String paths);

    /**
     * insertSelective
     * @param adminFileUploadInfo  adminFileUploadInfo
     * @return int
     */
    int insertSelective(AdminFileUploadInfo adminFileUploadInfo);

    /**
     * deleteByIds
     * @param ids ids
     * @return int
     */
    int deleteByIds(String ids);

    /**
     * deleteByPrimaryKey
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(String id);

    /**
     * update
     * @param t t
     * @return int
     */
    int update(AdminFileUploadInfo t);

    /**
     * insert
     * @param t t
     * @return int
     */
    int insert(AdminFileUploadInfo t);

    /**
     * selectByPrimaryKey
     * @param id id
     * @return AdminFileUploadInfo
     */
    AdminFileUploadInfo selectByPrimaryKey(String id);

    /**
     * selectByModel
     * @param queryModel queryModel
     * @return IPage<AdminFileUploadInfo>
     */
    IPage<AdminFileUploadInfo> selectByModel(QueryModel queryModel);
}
