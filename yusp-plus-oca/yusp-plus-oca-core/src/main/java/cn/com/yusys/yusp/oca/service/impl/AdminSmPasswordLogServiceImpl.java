package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.oca.domain.dto.PasswordLogDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.common.utils.Query;

import cn.com.yusys.yusp.oca.dao.AdminSmPasswordLogDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPasswordLogEntity;
import cn.com.yusys.yusp.oca.service.AdminSmPasswordLogService;

/**
 * 密码日志业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service("adminSmPasswordLogService")
public class AdminSmPasswordLogServiceImpl extends ServiceImpl<AdminSmPasswordLogDao, AdminSmPasswordLogEntity> implements AdminSmPasswordLogService {
    /**
     * 分页查询密码日志
     * @param params
     * @return 返回信息
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminSmPasswordLogEntity> page = this.page(
                new Query<AdminSmPasswordLogEntity>().getPage(params),
                new QueryWrapper<AdminSmPasswordLogEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 更新密码日志
     * @param passwordLogDto
     */
    @Override
    public void updatePwdLog(PasswordLogDto passwordLogDto) {
        //更新密码日志
        AdminSmPasswordLogEntity adminSmPasswordLogEntity = new AdminSmPasswordLogEntity();
        BeanUtils.beanCopy(passwordLogDto,adminSmPasswordLogEntity);
        adminSmPasswordLogEntity.setLastChgDt(new Date());
        adminSmPasswordLogEntity.setLastChgUsr(passwordLogDto.getUpdateUser());
        adminSmPasswordLogEntity.setPwdUpTime(new Date());
        this.save(adminSmPasswordLogEntity);
    }

    /**
     * 获取最后修改密码日志
     * @param userId
     * @return 密码日志
     */
    @Override
    public AdminSmPasswordLogEntity getLastChangeLog(String userId) {
        return getBaseMapper().getLastChangeLog(userId);
    }
}