package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.GenericBuilder;
import cn.com.yusys.yusp.oca.dao.AdminSmAuthInfoDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthInfoEntity;
import cn.com.yusys.yusp.oca.service.AdminSmAuthInfoService;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author yusys
 */
@Service("adminSmAuthInfoService")
public class AdminSmAuthInfoServiceImpl extends ServiceImpl<AdminSmAuthInfoDao, AdminSmAuthInfoEntity> implements AdminSmAuthInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminSmAuthInfoEntity> page = this.page(
                new Query<AdminSmAuthInfoEntity>().getPage(params),
                new QueryWrapper<AdminSmAuthInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AdminSmAuthInfoVo> getAuthKeyValue() {
        List<AdminSmAuthInfoVo> authInfoVoList = new ArrayList<>();
        List<AdminSmAuthInfoEntity> authInfoList = this.list();
        authInfoList.forEach(adminSmAuthInfoEntity -> authInfoVoList.add(
                GenericBuilder.of(AdminSmAuthInfoVo::new)
                        .with(AdminSmAuthInfoVo::setKey, adminSmAuthInfoEntity.getAuthId())
                        .with(AdminSmAuthInfoVo::setValue, adminSmAuthInfoEntity.getAuthName())
                        .build()));

        return authInfoVoList;
    }

}