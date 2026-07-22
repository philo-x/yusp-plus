package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.bo.MenuAndControlAuthRecoSaveBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthRecoVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import cn.com.yusys.yusp.oca.domain.vo.MenuVo;
import cn.com.yusys.yusp.oca.service.AdminSmAuthRecoService;
import cn.com.yusys.yusp.oca.service.AdminSmMenuService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 资源对象授权记录表(含菜单、控制点、数据权限)
 * @author zhangyt12
 * @date 2021/6/28 14:23
 */
@Deprecated
@RestController
@RequestMapping("/api/adminsmauthteco")
public class AdminSmAuthRecoController {

    @Autowired
    private AdminSmAuthRecoService adminSmAuthRecoService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private AdminSmMenuService adminSmMenuService;

    /**
     * 保存角色菜单权限
     * @param authRecoSaveBo
     * @return
     */
    @PostMapping("/saveinfo")
    public JClientRspEntity<Object> saveMenuAndControlAuthReco(@RequestBody JClientReqEntity<MenuAndControlAuthRecoSaveBo> authRecoSaveBo) {
        adminSmAuthRecoService.saveMenuAndControlAuthReco(authRecoSaveBo.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 分页查询orgId下所有已激活的用户列表,按最后修改时间逆序，
     * 实际调用 adminSmUserService.queryPageNew() 接口实现
     * @param query
     * @return
     */
    @PostMapping("/querypage")
    public JClientRspEntity<Page<AdminSmUserVo>> page(@RequestBody JClientReqEntity<AdminSmUserQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmUserService.queryPage(query.getBody()));
    }

    /**
     * 菜单树初始化查询,
     * 实际调用adminSmMenuService.getMenuTree() 接口实现,
     * lazy 判断是否支持懒加载
     * @param req
     * @return
     */
    @PostMapping("/menutreequery")
    public JClientRspEntity<List<MenuVo>> getMenuTree(@RequestBody JClientReqEntity<Map<String, String>> req) {
        Map<String, String> params = req.getBody();
        List<MenuVo> list = adminSmMenuService.getMenuTree(params.get("sysId"), Boolean.parseBoolean(params.get("lazy")), params.get("menuId"));
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 数据权限树初始化查询
     * @param sysId
     * @return
     */
    @PostMapping("/datapowertreequery")
    public JClientRspEntity<List<MenuVo>> selectDataPowerTree(@RequestBody JClientReqEntity<String> sysId) {
        List<MenuVo> list = adminSmAuthRecoService.selectDataPowerTree(sysId.getBody());
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 查询对象资源关系数据
     * @param req
     * @return
     */
    @PostMapping("/queryinfo")
    public JClientRspEntity<List<AdminSmAuthRecoVo>> getRecoInfo(@RequestBody JClientReqEntity<Map<String, String>> req){
        Map<String, String> params = req.getBody();
        List<AdminSmAuthRecoVo> list = adminSmAuthRecoService.queryRecoInfo(params.get("objectType"),params.get("resType"),params.get("objectId"),params.get("sysId"));
        return JClientRspEntity.buildSuccess(list);
    }

    /**
     * 批量保存 数据授权
     * @param authRecoList
     * @return
     */
    @PostMapping("/savedatapowerinfo")
    public JClientRspEntity<?> saveDataPowerInfo(@RequestBody JClientReqEntity<ArrayList<AdminSmAuthRecoEntity>> authRecoList) {

        int saveInt = adminSmAuthRecoService.adminSmAuthRecoService(authRecoList.getBody());
        if (saveInt >= 1) {
            return JClientRspEntity.buildSuccess("保存成功!");
        }
        return JClientRspEntity.buildFail("保存失败!");
    }
}
