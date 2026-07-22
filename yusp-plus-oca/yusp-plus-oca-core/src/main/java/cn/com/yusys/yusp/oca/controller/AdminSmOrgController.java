package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.progress.model.ProgressDto;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.oca.domain.bo.AdminSmOrgTreeNodeBo;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmOrgEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgExtQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmOrgTreeQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgVo;
import cn.com.yusys.yusp.oca.service.AdminSmOrgService;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * 系统机构表
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
@RestController
@RequestMapping("/api/adminsmorg")
public class AdminSmOrgController {

    private static final String ORG_ID = "orgId";

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmOrgController.class);
    @Autowired
    private AdminSmOrgService adminSmOrgService;

    /**
     * 分页查询
     *
     * @param query 分页查询条件
     * @return 机构列表
     */
    @PostMapping("/querypage")
    public JClientRspEntity<Page<AdminSmOrgVo>> querypage(@RequestBody JClientReqEntity<AdminSmOrgExtQuery> query) {
        return JClientRspEntity.buildSuccess(adminSmOrgService.queryPage(query.getBody()));
    }

    /**
     * 详情
     *
     * @param orgId 记录编号
     * @return 机构详情
     */
    @PostMapping("/info/{orgId}")
    public JClientRspEntity<AdminSmOrgDetailVo> querypage(@PathVariable String orgId) {
        return JClientRspEntity.buildSuccess(adminSmOrgService.getDetailById(orgId));
    }

    /**
     * 机构树查询:从数据库中查出全量机构数据，通过代码去组织树形数据结构，从输入orgId算起最多返回5级（）
     *
     * @param req - orgId：默认值是当前用户所在机构
     *              - orgSts：为null或空字串时查询所有状态的机构，有值时只返回对应状态的机构树
     *              - sort：默认值是last_chg_dt desc
     * @return 机构树
     */
    @PostMapping("/orgtreequery")
    public JClientRspEntity<List<AdminSmOrgTreeNodeBo>> orgTreeQuery(@RequestBody JClientReqEntity<AdminSmOrgTreeQuery> req) {
        AdminSmOrgTreeQuery query = req.getBody();
        log.info("Query organization tree");
        query.setOrgSts(Optional.ofNullable(query.getOrgSts()).orElse(AvailableStateEnum.ENABLED));
        List<AdminSmOrgTreeNodeBo> list = adminSmOrgService.getOrgTree(query);
        return JClientRspEntity.buildSuccess(list);
    }


    /**
     * 带权限机构查询，当前登录人只能看到他所属的机构以及下级机构
     *
     * @return 查询到的机构数据
     * @author zhanyq
     * @date 2021-06-28 10:54
     */
    @PostMapping("/treequeryauth")
    public JClientRspEntity<List<AdminSmOrgTreeNodeBo>> orgTreeQueryAuth() {
        log.info("Query organization tree");
        String orgId = SessionUtils.getUserOrganizationId();
        List<String> orgIds = new ArrayList<>();
        orgIds.add(orgId);
        List<AdminSmOrgTreeNodeBo> orgTrees = adminSmOrgService.getOrgTrees(orgIds, AvailableStateEnum.ENABLED);
        return JClientRspEntity.buildSuccess(orgTrees);
    }

    /**
     * 新增
     *
     * @param adminSmOrg 机构实体
     * @return 成功返回 code = 0000，message"新增成功"
     */
    @PostMapping("/save")
    public JClientRspEntity<String> save(@Validated({Insert.class}) @RequestBody JClientReqEntity<AdminSmOrgEntity> adminSmOrg) {
        adminSmOrgService.save(adminSmOrg.getBody());
        adminSmOrgService.updateOrgCache(adminSmOrg.getBody());
        return JClientRspEntity.buildSuccess("新增成功");
    }

    /**
     * 修改
     *
     * @param adminSmOrg 机构实体
     * @return 成功返回 code = 0000，message"修改成功"
     */
    @PostMapping("/update")
    public JClientRspEntity<Object> update(@Validated({Update.class}) @RequestBody JClientReqEntity<AdminSmOrgEntity> adminSmOrg) {
        adminSmOrgService.updateBy(adminSmOrg.getBody());
        adminSmOrgService.updateOrgCache(adminSmOrg.getBody());
        return JClientRspEntity.buildSuccess("修改成功");
    }


    /**
     * 批量启用
     *
     * @param ids 记录编号数组
     * @return 成功返回 code = 0000，message"启用成功"
     */
    @PostMapping("/batchenable")
    public JClientRspEntity<Object> batchEnable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmOrgService.batchEnable(ids.getBody());
        return JClientRspEntity.buildSuccess("启用成功");
    }


    /**
     * 批量启用
     *
     * @param ids 记录编号数组
     * @return 成功返回 code = 0000，message"停用成功"
     */
    @PostMapping("/batchdisable")
    public JClientRspEntity<Object> batchDisable(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmOrgService.batchDisable(ids.getBody());
        return JClientRspEntity.buildSuccess("停用成功");
    }


    /**
     * 批量删除
     *
     * @param ids 记录编号数组
     * @return 成功返回 code = 0000，message"删除成功"
     */
    @PostMapping("/batchdelete")
    public JClientRspEntity<Object> batchDelete(@RequestBody JClientReqEntity<String[]> ids) {
        adminSmOrgService.batchDelete(ids.getBody());
        adminSmOrgService.deletePartOrgCache(ids.getBody());
        return JClientRspEntity.buildSuccess("删除成功");
    }

    /**
     * 获取指定机构所有先辈机构详情，默认取当前用户所在机构上级机构
     *
     * @param req 记录编号
     * @return 所有上级机构
     */
    @PostMapping("/getallancestryorgs")
    public JClientRspEntity<Set<AdminSmOrgTreeNodeBo>> getAllAncestryOrgs(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        log.info("Query parent organization");
        Set<AdminSmOrgTreeNodeBo> allAncestryOrgs = adminSmOrgService.getAllAncestryOrgs(params.get(ORG_ID));
        return JClientRspEntity.buildSuccess(allAncestryOrgs);
    }

    /**
     * 获取指定机构父机构详情，默认取当前用户所在机构上级机构
     *
     * @param req 记录编号
     * @return 上级机构
     */
    @PostMapping("/getparentorg")
    public JClientRspEntity<AdminSmOrgEntity> getParentOrg(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        log.info("Query parent organization");
        AdminSmOrgEntity parent = adminSmOrgService.getParentOrg(params.get(ORG_ID));
        return JClientRspEntity.buildSuccess(parent);
    }

    /**
     * 获取指定机构兄弟机构详情，默认取当前用户所在机构上级机构
     *
     * @param req 记录编号
     * @return 兄弟机构
     */
    @PostMapping("/getsiblingorgs")
    public JClientRspEntity<Set<AdminSmOrgEntity>> getSiblingOrgs(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        log.info("Query sibling organization");
        Set<AdminSmOrgEntity> siblings = adminSmOrgService.getSiblingOrgs(params.get(ORG_ID));
        return JClientRspEntity.buildSuccess(siblings);
    }

    /**
     * 导出模板
     */
    @PostMapping("/exporttemplate")
    public JClientRspEntity<ProgressDto> asyncExportTemplate() {
        ProgressDto progressDto = adminSmOrgService.asyncExportTemplate();
        return JClientRspEntity.buildSuccess(progressDto);
    }

    /**
     * 导出查询数据
     */
    @PostMapping("/excelexport")
    public JClientRspEntity<ProgressDto> excelExport(@RequestBody JClientReqEntity<AdminSmOrgExtQuery> query) {
        ProgressDto progressDto = adminSmOrgService.asyncExportData(query.getBody());
        return JClientRspEntity.buildSuccess(progressDto);
    }

    /**
     * Excel数据导入
     *
     * @param fileId Excel文件信息ID
     * @return
     * @throws IOException
     */
    @PostMapping("/excelimport")
    public JClientRspEntity<ProgressDto> asyncImportDataBatch(String fileId) throws IOException {
        ProgressDto progressDto = adminSmOrgService.asyncImportData(fileId);
        return JClientRspEntity.buildSuccess(progressDto);
    }

}
