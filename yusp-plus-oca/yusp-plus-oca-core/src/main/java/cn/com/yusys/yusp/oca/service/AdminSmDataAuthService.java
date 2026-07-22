package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataTmplVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 数据权限表
 *
 * @author wujp4
 * @date 2020-12-01 09:52:42
 */
public interface AdminSmDataAuthService extends IService<AdminSmDataAuthEntity> {
    /**
     *  查询的数据权限模板数据
     * @param params 查询条件
     * @return  分页查询的数据权限模板数据
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取数据权限管理树，由1级：模块列表 2级：业务功能列表 3级：控制点列表 组成
     * @param nodeId
     * @return
     */
    PageUtils getDataAuthTree(String nodeId);

    /**
     * 分页查询控制点下的数据权限模板列表
     * @param params
     * @return
     */
    PageUtils pageResControlDataAuthTmpl(Map<String, String> params);

    /**
     * 根据控制点id分页查询非当前持有的数据权限模板
     * @param params
     * @return
     */
    PageUtils pageAuthTmplByContrId(Map<String, String> params);

    /**
     * 删除数据权限信息,同时删除其授权信息
     * @param ids
     */
    void deleteDataAuth(String[] ids);

    /**
     * 删除控制点时删除该表中与数据权限相关联的记录
     *
     * @param contrIds 控制点记录编号数组
     */
    void deleteByContrIds(String[] contrIds);

    /**
     * createDataAuth
     * @param adminSmDataAuth
     */
    void createDataAuth(AdminSmDataAuthEntity adminSmDataAuth);

    /**
     * 删除控制点之前关联的模板
     * @param contrId
     */
    void deleteTmplWhithContr(String contrId);

    /**
     * 使用contrId获取dataTmplId
     * @param contrId
     * @return
     */
    List<AdminSmDataAuthEntity> getByContrId(String contrId);

    /**
     * 使用 contrId 获取 dataTmplId 列表
     *
     * @param idList 控制点的记录编号数组
     * @return 已经关联的数据权限模板列表
     */
    List<AdminSmDataAuthEntity> getListByContrIds(List<String> idList);

    /**
     * 使用数据模板 id，查询与控制点的关联数据
     *
     * @param idList  数据权限模板表的组件数组
     * @return 数据权限模板表的模板id和模板名称组成对象的集合
     */
    List<AdminSmDataTmplVo> selectWithTmplIds(List<String> idList);

}

