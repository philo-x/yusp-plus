package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleUserRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.UserRelationshipVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户角色关联表
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
public interface AdminSmUserRoleRelService extends IService<AdminSmUserRoleRelEntity> {
    /**
     * 角色关联用户弹框中的查询条件：机构树
     * 范围是角色所在机构及下级所有机构
     *
     * @param roleId 角色id
     * @return 角色所在机构及下级所有机构
     */
    List<AdminSmOrgTreeNodeBo> getOrgTree(String roleId);

    /**
     * 查询指定角色下所有关联用户信息及关联状态
     *
     * @param query 查询条款
     * @return [指定角色所在机构及所有下级机构下]或者[指定机构及所有下级机构下]的所有用户，其中已关联的checked为true，否则为false
     */
    Page<UserRelationshipVo> memberPage(AdminSmRoleUserRelQuery query);

    /**
     * 给角色批量新增关联用户
     * @param entityList
     * @return true 新增成功
     */
    boolean save(List<AdminSmUserRoleRelEntity> entityList);

    /**
     * 给角色批量移除关联用户
     *
     * @param entityList 用户角色关联表列表
     * @return true 移除成功
     */
    boolean remove(List<AdminSmUserRoleRelEntity> entityList);


    /**
     * 只查询与用户状态对应的关联信息
     *
     * @param user 用户实体类
     * @return 用户关联信息
     */
    List<String>  findUserRoleRelsByUser(AdminSmUserEntity user);

    /**
     * 用户切换角色
     * @param roleId 角色id
     * @return 切换角色的响应信息
     */
    JClientRspEntity<String> switchRole(String roleId);
}

