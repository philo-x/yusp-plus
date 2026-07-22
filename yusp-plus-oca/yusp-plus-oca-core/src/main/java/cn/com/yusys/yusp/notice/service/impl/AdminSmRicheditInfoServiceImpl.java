package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.notice.dao.AdminSmRicheditInfoDao;
import cn.com.yusys.yusp.notice.entity.AdminSmRicheditInfoEntity;
import cn.com.yusys.yusp.notice.service.AdminSmRicheditInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 富文本信息表对应 service 层实现类
 * @author zhangyt12
 * @date 2021/6/28 9:56
 */
@Service("adminSmRicheditInfoService")
public class AdminSmRicheditInfoServiceImpl extends ServiceImpl<AdminSmRicheditInfoDao, AdminSmRicheditInfoEntity> implements AdminSmRicheditInfoService {

    @Override
    public void deleteRicheditInfo(List<String> noticeIds) {
        QueryWrapper<AdminSmRicheditInfoEntity> wrapper = Wrappers.<AdminSmRicheditInfoEntity>query().in(
                noticeIds != null && noticeIds.size() > 0,
                "REL_ID",
                noticeIds);
        this.remove(wrapper);
    }

    @Override
    public void updateByRelId(AdminSmRicheditInfoEntity richeditInfoEntity) {
        UpdateWrapper<AdminSmRicheditInfoEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(!StringUtils.isEmpty(
                richeditInfoEntity.getRelId()),
                "rel_id",
                richeditInfoEntity.getRelId());
        AdminSmRicheditInfoEntity updateEntity = new AdminSmRicheditInfoEntity();
        updateEntity.setContent(richeditInfoEntity.getContent());
        getBaseMapper().update(updateEntity, wrapper);
    }
}