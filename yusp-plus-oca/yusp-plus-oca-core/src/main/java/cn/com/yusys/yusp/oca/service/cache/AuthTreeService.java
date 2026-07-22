package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;

import java.util.List;
/**
 * 查询授权数据的 service 层接口
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public interface AuthTreeService {
    /**
     * 获取菜单授权数据
     * @param id 授权对象id
     * @param dataTenantId 租户ID
     * @return 返回自定菜单授权数据封装类
     */
    List<AdminSmAuthTreeVo> getUserAuthMenuData(String id, String dataTenantId);

    /**
     * 获取数据模板授权数据
     * @param id 授权对象id
     * @param dataTenantId 租户ID
     * @return 返回自定数据模板授权数据封装类
     */
    List<AdminSmAuthTreeVo> getUserAuthDataTmplData(String id, String dataTenantId);

    /**
     * 获取控制点授权数据
     * @param id 授权对象id
     * @param dataTenantId 租户ID
     * @return 返回自定控制点授权数据封装类
     */
    List<AdminSmAuthTreeVo> getUserAuthContrData(String id, String dataTenantId);
}
