package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.notice.dao.AdminSmRicheditFileInfoDao;
import cn.com.yusys.yusp.notice.entity.AdminSmRicheditFileInfoEntity;
import cn.com.yusys.yusp.notice.form.AdminSmRicheditFileInfoForm;
import cn.com.yusys.yusp.notice.service.AdminSmRicheditFileInfoService;
import cn.com.yusys.yusp.notice.service.ThirdPartOssService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 富文本附件表对应 service 层实现类
 *
 * @author zhangyt12
 * @date 2021/6/28 9:54
 */
@Service("adminSmRicheditFileInfoService")
public class AdminSmRicheditFileInfoServiceImpl extends ServiceImpl<AdminSmRicheditFileInfoDao, AdminSmRicheditFileInfoEntity> implements AdminSmRicheditFileInfoService {

    @Autowired
    private ThirdPartOssService thirdPartOssService;

    @Override
    public void addFileInfo(List<AdminSmRicheditFileInfoForm> fileInfoFormList) {
        if (fileInfoFormList == null || fileInfoFormList.size() == 0) {
            return;
        }
        List<AdminSmRicheditFileInfoEntity> infoEntityList = new ArrayList<>();
        fileInfoFormList.forEach((fileInfoForm -> {
            AdminSmRicheditFileInfoEntity entity = BeanUtils.beanCopy(fileInfoForm, AdminSmRicheditFileInfoEntity.class);
            entity.setFileId(StringUtils.getUUID());
            entity.setFilePath(fileInfoForm.getFileId());
            entity.setUploadTime(DateUtils.formatDateTimeByDef());
            infoEntityList.add(entity);
        }));
        this.saveBatch(infoEntityList);
    }

    @Override
    public List<AdminSmRicheditFileInfoEntity> getFileByBusNo(String noticeId) {
        QueryWrapper<AdminSmRicheditFileInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("bus_no", noticeId);
        return this.list(wrapper);
    }

    @Override
    public void deleteByBusNo(String... noticeIds) {
        QueryWrapper<AdminSmRicheditFileInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.in("bus_no", noticeIds);
        this.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFileInfo(List<AdminSmRicheditFileInfoForm> fileInfoFormList) {
        List<String> fileIdList = fileInfoFormList.stream().map(AdminSmRicheditFileInfoForm::getFileId).collect(Collectors.toList());
        List<String> filePathList = fileInfoFormList.stream().map(AdminSmRicheditFileInfoForm::getFilePath).collect(Collectors.toList());
        // 1、删除数据库中的fileInfo数据
        removeByIds(fileIdList);
        // 2、删除oss中的文件
        thirdPartOssService.delete(filePathList);
    }

    @Override
    public void deleteFileInfoWithFileServer(String fileId) {
        // fileProviderService.deleteByPrimaryKey(fileId);
    }
}