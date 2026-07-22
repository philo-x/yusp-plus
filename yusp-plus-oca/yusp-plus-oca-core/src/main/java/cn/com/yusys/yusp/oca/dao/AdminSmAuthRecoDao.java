package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminDataTmplControlVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminRecoWithTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源对象授权记录表(含菜单、控制点、数据权限)
 *
 * @author wujp4
 * @date 2020-11-19 17:43:42
 */
public interface AdminSmAuthRecoDao extends BaseMapper<AdminSmAuthRecoEntity> {

    /**
     * 菜单查询 M
     * @param sysId 系统 id
     * @return 获取菜单授权 vo
     */
    List<MenuVo> findMenuTreeList(String sysId);

    /**
     * 控制点查询 C
     * @param sysId 系统 id
     * @return 获取控制点授权 vo
     */
    List<MenuVo> findContrTreeList(String sysId);

    /**
     * 数据权限查询 D
     * @param sysId 系统 id
     * @return 获取数据模板授权 vo
     */
    List<MenuVo> findDataAuthTreeList(String sysId);

    /**
     * 使用 menuId 关联数据模板查询授权记录
     * @param wrapper 查询条件对象
     * @return 获取数据模板相关权限列表
     */
    List<AdminRecoWithTmplVo> getByMenuIdWithTmpl(@Param(Constants.WRAPPER) QueryWrapper<AdminRecoWithTmplVo> wrapper);

    /**
     * 查询角色列表授权的数据模板列表
     * @param wrapper 查询条件对象
     * @return 获取控制点相关权限列表
     */
    List<AdminDataTmplControlVo> getDataTmplControl(@Param(Constants.WRAPPER) QueryWrapper<AdminDataTmplControlVo> wrapper);

    /**
     * 根据菜单id查询已被授权的控制点id
     * @param queryWrapper 查询条件对象
     * @return 获取控制点相关权限列表
     */
    List<String> selectResContrIdByMenuIds(@Param(Constants.WRAPPER) QueryWrapper<String> queryWrapper);
}
