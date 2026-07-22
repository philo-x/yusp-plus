package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.collection.CollectionUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmDataAuthTmplEntity;
import cn.com.yusys.yusp.oca.domain.form.AdminSmDataAuthTmplForm;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataAuthTmplVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataTmplVo;
import cn.com.yusys.yusp.oca.service.AdminSmDataAuthTmplService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据权限模板表
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
@RestController
@RequestMapping("/api/adminsmdataauthtmpl")
public class AdminSmDataAuthTmplController {

    @Autowired
    private AdminSmDataAuthTmplService adminSmDataAuthTmplService;

    /**
     * 批量删除
     * @param ids 数据权限模板记录编号数组
     * @return 统一返回结果实体类
     */
    @PostMapping("/deletes")
    public JClientRspEntity<Object> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        List<AdminSmDataTmplVo> tmplVoList = adminSmDataAuthTmplService.deleteTmpl(ids.getBody());
        if (CollectionUtils.isEmpty(tmplVoList)) {
            return JClientRspEntity.buildSuccess("删除数据权限模板成功");
        } else {
            throw BizException.error(null,"50600007","数据模板关联了控制点，无法删除！");
        }
    }

    /**
     * 修改模板
     * @param adminSmDataAuthTmplEntity 数据权限模板表实体类
     * @return 统一返回结果实体类
     */
    @PostMapping("/updates")
    public JClientRspEntity<String> updateTmpl(@RequestBody JClientReqEntity<AdminSmDataAuthTmplEntity> adminSmDataAuthTmplEntity) {
        adminSmDataAuthTmplService.updateTmpl(adminSmDataAuthTmplEntity.getBody());
        return JClientRspEntity.buildSuccess("数据权限模板修改");
    }

    /**
     * 新增模板
     * @param adminSmDataAuthTmplEntity 数据权限模板表实体类
     * @return 统一返回结果实体类
     */
    @PostMapping("/add")
    public JClientRspEntity<AdminSmDataAuthTmplEntity> addAuthTemplate(@RequestBody JClientReqEntity<AdminSmDataAuthTmplEntity> adminSmDataAuthTmplEntity) {
        AdminSmDataAuthTmplEntity entity = adminSmDataAuthTmplService.addAuthTemplate(adminSmDataAuthTmplEntity.getBody());
        return JClientRspEntity.buildSuccess(entity);
    }

    /**
     * 查询列表
     * @param adminSmDataAuthTmplForm 数据模板条件查询
     * @return 统一返回结果实体类
     */
    @PostMapping("/list")
    public JClientRspEntity<Page<AdminSmDataAuthTmplVo>> list(@RequestBody JClientReqEntity<AdminSmDataAuthTmplForm> adminSmDataAuthTmplForm){
        Page<AdminSmDataAuthTmplVo> page = adminSmDataAuthTmplService.queryPage(adminSmDataAuthTmplForm.getBody());
        return JClientRspEntity.buildSuccess(page);
    }

    /**
     * 修改之前 通过模板主键查询模板信息
     * @param authTmplId 记录编号
     * @return 统一返回结果实体类
     */
    @PostMapping("/info/{authTmplId}")
    public JClientRspEntity<AdminSmDataAuthTmplEntity> getInfo(@PathVariable String authTmplId){
		AdminSmDataAuthTmplEntity entity = adminSmDataAuthTmplService.getInfo(authTmplId);
        return JClientRspEntity.buildSuccess(entity);
    }

    /**
     * 获取已关联的数据模板
     * @param contrId 控制点记录编号
     * @return 统一返回结果实体类
     */
    @PostMapping("/associated/{contrId}")
    public JClientRspEntity<List<AdminSmDataAuthTmplEntity>> getByContrId(@PathVariable String contrId, String dataTenantId) {
        List<AdminSmDataAuthTmplEntity> list=adminSmDataAuthTmplService.getByContrId(contrId,dataTenantId);
        return JClientRspEntity.buildSuccess(list);
    }
}
