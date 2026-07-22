package cn.com.yusys.yusp.file.service.impl;

import cn.com.yusys.yusp.commons.file.FileInfo;
import cn.com.yusys.yusp.commons.file.util.FileInfoUtils;
import cn.com.yusys.yusp.commons.file.util.FileUtils;
import cn.com.yusys.yusp.commons.module.adapter.query.QueryModel;
import cn.com.yusys.yusp.commons.mybatisplus.util.MybatisPlusUtils;
import cn.com.yusys.yusp.commons.util.ArrayUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.collection.Entry;
import cn.com.yusys.yusp.commons.util.collection.MapUtils;
import cn.com.yusys.yusp.file.domain.AdminFileUploadInfo;
import cn.com.yusys.yusp.file.domain.AdminSmRicheditFileInfo;
import cn.com.yusys.yusp.file.repository.mapper.AdminFileUploadInfoMapper;
import cn.com.yusys.yusp.file.repository.mapper.AdminSmRicheditFileInfoMapper;
import cn.com.yusys.yusp.file.service.FileProviderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yusys
 * @version 1.0
 * @date 2020/7/15 10:07
 * @description 文件上传服务实现类
 */
@Service
public class FileProviderServiceImpl implements FileProviderService {

    private static final Logger log = LoggerFactory.getLogger(FileProviderServiceImpl.class);

    @Autowired
    private AdminFileUploadInfoMapper adminFileUploadInfoMapper;
    @Autowired
    private AdminSmRicheditFileInfoMapper adminSmRicheditFileInfoMapper;

    @Value(value = "${yusp.file.service.default-template-name}")
    private String defaultTemplateName;

    @Override
    public List<AdminFileUploadInfo> selectFileUploadByModel(Map<String, Object> param) {
        return this.adminFileUploadInfoMapper.selectList(MybatisPlusUtils.lambdaQuery(param));
    }

    @Override
    public void deleteFileRecords(String paths) {
        String[] fileIds = paths.split(",");
        for (String fileId : fileIds) {
            AdminFileUploadInfo adminFileUploadInfo = new AdminFileUploadInfo();
            adminFileUploadInfo.setFilePath(fileId);
            this.adminFileUploadInfoMapper.deleteByMap(MapUtils.ofHashMap(new Entry<>("file_path", fileId)));
        }
        deleteFile(paths);
    }

    @Override
    public void deleteFile(String paths) {
        String[] fileIds = paths.split(",");
        if (ArrayUtils.nonEmpty(fileIds)) {
            Arrays.stream(fileIds).filter(StringUtils::nonBlank)
                    .forEach(fileId -> {
                        try {
                            if (!FileUtils.delete(defaultTemplateName, fileId)) {
                                log.warn("file [{}] delete failed.", fileId);
                            }
                        } catch (Exception e) {
                            log.error("file [{}] delete failed.", fileId, e);
                        }
                    });
        }
    }

    @Override
    public List<AdminSmRicheditFileInfo> selectRicheditFileByModel(Map<String, Object> param) {
        QueryModel model = new QueryModel();
        model.getCondition().putAll(param);
        return this.adminSmRicheditFileInfoMapper.selectByModel(model);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int uploadRicheditFile(AdminSmRicheditFileInfo adminSmRicheditFileInfo) {
        int count = adminSmRicheditFileInfoMapper.insertSelective(adminSmRicheditFileInfo);
        return count;
    }

    /**
     * @方法名称: deleteRicheditFile
     * @方法描述: 删除富文本附件记录
     * @参数与返回说明:
     * @算法描述:
     */
    @Override
    public void deleteRicheidtFileRecords(String paths) {

        String[] fileIds = paths.split(",");
        if (fileIds != null) {
            for (int i = 0; i < fileIds.length; i++) {
                AdminSmRicheditFileInfo adminSmRicheditFileInfo = new AdminSmRicheditFileInfo();
                adminSmRicheditFileInfo.setFilePath(fileIds[i]);
                this.adminSmRicheditFileInfoMapper.delete(adminSmRicheditFileInfo);
            }
        }
        deleteFile(paths);
    }

    @Override
    public int insertSelective(AdminFileUploadInfo adminFileUploadInfo) {
        if (StringUtils.isEmpty(adminFileUploadInfo.getFileId())) {
            adminFileUploadInfo.setFileId(StringUtils.getUUID());
        }
        return this.adminFileUploadInfoMapper.insert(adminFileUploadInfo);
    }

    @Override
    public int deleteByIds(String ids) {
        return this.adminFileUploadInfoMapper.deleteBatchIds(ids);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return this.adminFileUploadInfoMapper.deleteById(id);
    }

    @Override
    public int update(AdminFileUploadInfo t) {
        return this.adminFileUploadInfoMapper.updateById(t);
    }

    @Override
    public int insert(AdminFileUploadInfo t) {
        if (StringUtils.isEmpty(t.getFileId())) {
            t.setFileId(StringUtils.getUUID());
        }
        return this.adminFileUploadInfoMapper.insert(t);
    }

    @Override
    public AdminFileUploadInfo selectByPrimaryKey(String id) {
        return this.adminFileUploadInfoMapper.selectById(id);
    }

    @Override
    public IPage<AdminFileUploadInfo> selectByModel(QueryModel queryModel) {
        return this.adminFileUploadInfoMapper.selectByModel(queryModel);
    }

    /**
     * 根据 file_path 查询文件原始名字
     *
     * @param fileId 文件路径 file_path
     * @return 文件名
     */
    public String getOriginalFileName(String fileId) {
        String fileName;
        QueryModel queryModel = new QueryModel();
        queryModel.addCondition("filePath", fileId);
        List<AdminFileUploadInfo> adminFileUploadInfos = adminFileUploadInfoMapper.selectByModel(queryModel).getRecords();
        if (adminFileUploadInfos.size() == 1) {
            fileName = adminFileUploadInfos.get(0).getFileName();
        } else {
            if (fileId.contains("/")) {
                fileName = fileId.substring(fileId.lastIndexOf("/") + 1);
            } else {
                FileInfo fileInfo = FileInfoUtils.fromIdentity(fileId);
                fileName = fileInfo.getFileName();
            }
        }
        return fileName;
    }

}
