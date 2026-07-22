package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.MenuAndControlAuthRecoSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminTmplAuthForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminDataTmplControlVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminRecoWithTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthRecoVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashSet;
import java.util.List;

/**
 * 资源对象授权记录表(含菜单、控制点、数据权限)
 *
 * @author wujp4
 * @date 2020-11-19 17:43:42
 */
public interface AdminSmAuthRecoService extends IService<AdminSmAuthRecoEntity> {

    /**
     * 数据授权 查询菜单树（包含数据权限模版节点）
     * 替换sql中的union 函数
     *
     * @param sysId 系统id
     * @return list  数据授权 查询菜单树（包含数据权限模版节点）
     */
    List<MenuVo> selectDataPowerTree(String sysId);


    /**
     * 查询对象资源关系数据
     *
     * @param objectType 授权对象的类型
     * @param resType 授权资源类型
     * @param objectId 授权对象的id
     * @param sysId 系统id
     * @return
     */
    List<AdminSmAuthRecoVo> queryRecoInfo(String objectType, String resType, String objectId, String sysId);

    /**
     * 保存数据授权
     *
     * @param authRecoList 资源对象授权记录
     * @return 保存的授权对象记录数
     */
    int adminSmAuthRecoService(List<AdminSmAuthRecoEntity> authRecoList);

    /**
     * 保存角色菜单权限
     * @param authRecoSaveBo
     * @return
     */
    void saveMenuAndControlAuthReco(MenuAndControlAuthRecoSaveBo authRecoSaveBo);

    /**
     * 批量删除授权记录
     *
     * @param authresIdList 授权记录编号集合
     */
    void deleteByList(List<String> authresIdList);

    /**
     * 批量删除被拷贝角色或用户原有的权限
     * @param ids 授权的记录编号
     */
    void deleteByAuthObjIds(String[] ids);

    /**
     * 删除控制点授权，同时删除控制点关联的数据权限模板授权
     * @param contrIdList 控制点id集合
     * @param dataAuthIdList 控制点关联的数据模板的id集合
     */
    void deleteByResContrList(List<String> contrIdList, List<String> dataAuthIdList);

    /**
     * 使用authObjId获取该用户的所有权限
     * @param authObjId
     * @return
     */
    List<AdminSmAuthRecoEntity> getByAuthObjId(String authObjId);

    /**
     * 使用authresId获取一个授权记录
     * @param authresId
     * @return
     */
    AdminSmAuthRecoEntity getByAuthresId(String authresId);

    /**
     * 使用授权类型获取授权记录
     * @param d
     * @return
     */
    List<AdminSmAuthRecoEntity> getByAuthresType(String d);

    /**
     * 使用授权对象id和授权资源对象id删除记录
     * @param form 数据模板授权
     */
    void removeByMenuIdAndAuthobjId(AdminTmplAuthForm form);

    /**
     * 查询多个数据模板的授权记录
     * @param tmplIdList
     * @return
     */
    List<AdminSmAuthRecoEntity> getByAuthresIds(List<String> tmplIdList);

    /**
     * 使用 menuId 查询授权记录
     * @param contrId 控制点的id
     * @param dataTenantId dataTenantId
     * @return 控制点关联的模板集合
     */
    List<AdminRecoWithTmplVo> getByMenuIdWithTmpl(String contrId,String dataTenantId);

    /**
     * 使用 userId 查询该用户的数据模板授权记录
     * @param userId
     * @return
     */
    List<AdminDataTmplControlVo> getDataTmplControl(String userId);

    /**
     * 批量删除菜单授权记录
     * @param menuIds
     * @return 删除的记录数
     */
    int deleteMenuAuthData(HashSet<String> menuIds);

    /**
     * 批量删除租户的菜单授权记录
     * @param authresIds 菜单id
     * @param tanentId 租户id
     */
    void deleteByAuthresAndTenant(List<String> authresIds, String tanentId);
}

