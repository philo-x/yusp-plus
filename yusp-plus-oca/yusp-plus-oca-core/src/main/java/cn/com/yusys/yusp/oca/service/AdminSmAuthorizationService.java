package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthTreeForm;
import cn.com.yusys.yusp.oca.domain.form.AdminSmAuthUpdateForm;
import cn.com.yusys.yusp.oca.domain.form.AdminTmplAuthForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmResContrAuthVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
/**
 * 功能授权
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public interface AdminSmAuthorizationService {

    /**
     * 获取权限树
     *
     * @param adminSmAuthTreeForm 包含 userID|roleId，两个只能有一个
     * @return 权限树列表
     */
    List<AdminSmAuthTreeVo> getTreeQuery(AdminSmAuthTreeForm adminSmAuthTreeForm);

    /**
     * 修改被授权人权限
     *
     * @param adminSmAuthUpdateForm 修改被授权人权限所需参数
     */
    void saveAuth(AdminSmAuthUpdateForm adminSmAuthUpdateForm);

    /**
     * 复制拷贝权限
     * @param adminSmAuthUpdateForm 修改被授权人权限所需参数
     */
    void copyAuth(AdminSmAuthUpdateForm adminSmAuthUpdateForm);

    /**
     * 获取数据权限模板授权页面
     * @param adminSmAuthTreeForm 授权树实体类
     * @return 返回控制点+数据模板vo的分页对象
     */
    IPage<AdminSmResContrAuthVo> getAuthTmplList(AdminSmAuthTreeForm adminSmAuthTreeForm);

    /**
     * 给被授权对象修改控制点的数据模板
     * @param form  数据模板授权
     */
    void saveTmplAuth(AdminTmplAuthForm form);

    /**
     * 获取已经授权的菜单和控制点
     * @param adminSmAuthTreeForm
     * @return
     */
    List<AdminSmAuthTreeVo> getAuthedMenuTree(AdminSmAuthTreeForm adminSmAuthTreeForm);

}
