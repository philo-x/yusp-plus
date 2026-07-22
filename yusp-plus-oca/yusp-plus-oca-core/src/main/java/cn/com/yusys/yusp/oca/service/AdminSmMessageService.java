package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageEditBo;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmMessageSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmMessageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 提示信息管理表
 *
 * @author wujp4
 * @date 2020-11-16 11:19:42
 */
public interface AdminSmMessageService extends IService<AdminSmMessageEntity> {


    /**
     * 查询提示信息分页
     *
     * @param adminSmMessageQuery 查询体哦阿健
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-06-25 15:28
     */
    Page<AdminSmMessageEntity> queryMessagePage(AdminSmMessageQuery adminSmMessageQuery);


    /**
     * 新增提示信息
     *
     * @param adminSmMessage 要保存的系统提示消息实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 15:28
     */
    void saveMessage(AdminSmMessageSaveBo adminSmMessage);


    /**
     * 修改提示信息
     *
     * @param adminSmMessage 要修改的系统提示消息实体
     * @return void
     * @author zhanyq
     * @date 2021-06-25 15:29
     */
    void updateMessageById(AdminSmMessageEditBo adminSmMessage);
}

