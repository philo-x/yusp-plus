package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.commons.session.user.Control;
import cn.com.yusys.yusp.commons.session.user.impl.ControlImpl;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmResContrEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmMenuConditionForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmResContrForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmResContrSaveForm;
import cn.com.yusys.yusp.oca.domain.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统功能控制点表
 *
 * @author wujp4
 * @date 2020-11-19 16:38:48
 */
public interface AdminSmResContrService extends IService<AdminSmResContrEntity> {
    /**
     * 获取控制点的数据
     *
     * @param userId 用户id
     * @return 控制点数据
     */
    List<ControlImpl> getAdminSmContr(String userId);

    /**
     * 获取分页查询
     *
     * @param params 查询参数
     * @return 分页工具
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询控制点列表
     * @param adminSmResContrForm 系统功能控制点查询列表封装条件
     * @return 分页查询的控制点列表
     */
    Page<AdminSmResContrVo> queryPageWithCondition(AdminSmResContrForm adminSmResContrForm);

    /**
     * 获取业务功能树
     * @return list
     */
    List<AdminSmContrTreeVo> getFuncTree();

    /**
     * 批量删除 控制点
     * 删除控制点时，需要同时删除与数据权限相关联的admin_sm_data_auth表记录但是权限模板是不删除的
     *
     * @param ids 控制点记录编号数组
     */
    void deleteContr(String[] ids);

    /**
     * 验证contrCode是否重复
     * @param adminSmResContrForm 系统功能控制点查询封装条件
     * @return 整数
     */
    long checkCode(AdminSmResContrForm adminSmResContrForm);

    /**
     * 新增控制点
     *
     * @param adminSmResContrSaveForm 系统功能控制点表新建/修改表单数据
     */
    void createContr(AdminSmResContrSaveForm adminSmResContrSaveForm);

    /**
     * 修改控制点
     * @param adminSmResContrSaveForm 系统功能控制点表新建/修改表单数据
     */
    void updateContr(AdminSmResContrSaveForm adminSmResContrSaveForm);

    /**
     * 查询权限授权树
     * @param id 授权对象记录编号
     * @param dataTenantId 租户ID
     * @return 权限授权树
     */
    List<AdminSmAuthTreeVo> selectAuthTree(String id, String dataTenantId);


    /**
     * 分页查询权限表中的控制点数据
     *
     * @param id 被授权对象 id
     * @param keyWord 查询关键字
     * @param dataTenantId 租户id
     * @param page 当前页面
     * @param size 当前页条数
     * @return 分页对象
     */
    IPage<AdminSmAuthTreeVo> selectAuthTreePage(String id, String keyWord, String dataTenantId, int page, int size);

    /**
     * 控制点关联数据权限模板操作
     *
     * @param adminSmResContrSaveForm 系统功能控制点表新建/修改表单数据
     * @param createOrUpdate 是否是更新操作，如果是true，反之是false
     * @return string
     */
    String relationTmpl(AdminSmResContrSaveForm adminSmResContrSaveForm, boolean createOrUpdate);

    /**
     * 查询控制点关联菜单列表
     * @param adminSmMenuConditionForm 控制点查询条件表单
     * @return 分页查询统一返回实体类
     */
    Page<AdminSmMenuVo> getMenuList(AdminSmMenuConditionForm adminSmMenuConditionForm);

    /**
     * 缓存中添加 userId 现有的控制点
     * @return list
     */
    List<? extends Control> selectControlImplList();


    /**
     * 查询已授权的控制点
     * @param form 查询条件
     * @return 分页查询结果
     * @author zhanyq
     * @date 2021-09-29 9:57
     */
    Page<AdminSmResContrVo> getAuthedContrPage(AdminSmResContrForm form);

    /**
     * 查询交易请求特殊控制器
     * @param type 控制类型
     * @return
     */
    Set<String> getRequestCheckList(String type);
}

