package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmMessageDao;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmMessageQuery;
import cn.com.yusys.yusp.oca.service.AdminSmMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * 系统提示消息业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service("adminSmMessageService")
public class AdminSmMessageServiceImpl extends ServiceImpl<AdminSmMessageDao, AdminSmMessageEntity> implements AdminSmMessageService {

    /**
     * 分页获取系统提示消息
     * @param adminSmMessageQuery 查询条件
     * @return 分页获取分页信息
     */
    @Override
    public Page<AdminSmMessageEntity> queryMessagePage(AdminSmMessageQuery adminSmMessageQuery) {
        //获取分页参数
        Page<AdminSmMessageEntity> page = adminSmMessageQuery.getIPage();
        //创建查询wrapper
        LambdaQueryWrapper<AdminSmMessageEntity> wrapper = Wrappers.lambdaQuery();
        //添加查询条件
        Optional.ofNullable(adminSmMessageQuery.getKeyWord()).ifPresent((param) -> wrapper.like(AdminSmMessageEntity::getCode, param).or().like(AdminSmMessageEntity::getMessage, param));
        Optional.ofNullable(adminSmMessageQuery.getCode()).ifPresent((param) -> wrapper.like(AdminSmMessageEntity::getCode, param));
        Optional.ofNullable(adminSmMessageQuery.getMessage()).ifPresent((param) -> wrapper.like(AdminSmMessageEntity::getMessage, param));
        wrapper.orderByDesc(AdminSmMessageEntity::getLastChgDt);
        //分页查询
        return getBaseMapper().selectPage(page, wrapper);
    }

    /**
     * 保存系统提示消息参数
     * @param adminSmMessage 要保存的系统提示消息实体
     */
    @Override
    public void saveMessage(AdminSmMessageSaveBo adminSmMessage) {
        //校验新增系统消息提示码是否已经存在
        List<AdminSmMessageEntity> adminSmMessageEntities = this.list(new QueryWrapper<AdminSmMessageEntity>().eq("CODE", adminSmMessage.getCode()));
        if (CollectionUtils.nonEmpty(adminSmMessageEntities)) {
            throw BizException.error(null, "50600002",
                    null, "系统提示消息信息码:" + adminSmMessage.getCode() + "已存在！");
        }
        //创建新增对象
        AdminSmMessageEntity adminSmMessageEntity = new AdminSmMessageEntity();
        //新增对象添加数据
        cn.com.yusys.yusp.commons.util.BeanUtils.beanCopy(adminSmMessage, adminSmMessageEntity);
        adminSmMessageEntity.setLastChgUsr(SessionUtils.getUserId());
        adminSmMessageEntity.setLastChgDt(new Date());
        //新增
        this.save(adminSmMessageEntity);
    }

    /**
     * 更新系统提示消息
     * @param adminSmMessage 要修改的系统提示消息实体
     */
    @Override
    public void updateMessageById(AdminSmMessageEditBo adminSmMessage) {
        //校验新增系统消息提示码是否已经存在
        List<AdminSmMessageEntity> adminSmMessageEntities = this.list(new QueryWrapper<AdminSmMessageEntity>().eq("CODE", adminSmMessage.getCode()).ne("MESSAGE_ID", adminSmMessage.getMessageId()));
        if (CollectionUtils.nonEmpty(adminSmMessageEntities)) {
            throw BizException.error(null, "50600002",
                    null, "系统提示消息信息码:" + adminSmMessage.getCode() + "已存在！");
        }
        //创建修改对象
        AdminSmMessageEntity adminSmMessageEntity = new AdminSmMessageEntity();
        //修改对象添加数据
        BeanUtils.beanCopy(adminSmMessage, adminSmMessageEntity);
        adminSmMessageEntity.setLastChgUsr(SessionUtils.getUserId());
        adminSmMessageEntity.setLastChgDt(new Date());
        //修改对象
        this.updateById(adminSmMessageEntity);
    }
}