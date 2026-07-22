package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.form.AdminSmRicheditFileInfoForm;
import cn.com.yusys.yusp.notice.service.AdminSmRicheditFileInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 富文本附件表相关 controller
 * @author zhangyt12
 * @date 2021/6/24 14:48
 */
@Tag(name = "接口")
@RestController
@RequestMapping("/api/adminsmricheditfileinfo")
public class AdminSmRicheditFileInfoController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmRicheditFileInfoController.class);

    @Autowired
    private AdminSmRicheditFileInfoService adminSmRicheditFileInfoService;

    /**
     * 新增文件数据
     * @param fileInfoFormList 自定义表单类，接收多个富文本附件相关参数
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @Operation(summary = "保存")
    @PostMapping("/add")
    public JClientRspEntity<Object> addFileInfo(@RequestBody JClientReqEntity<List<AdminSmRicheditFileInfoForm>> fileInfoFormList){
        log.info("保存新增文件数据： " + fileInfoFormList.getBody());
		adminSmRicheditFileInfoService.addFileInfo(fileInfoFormList.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 删除数据库数据及oss中的文件
     * @param fileInfoFormList 自定义表单类，接收删除接口的相关参数
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @Operation(summary = "删除")
    @PostMapping("/delete")
    public JClientRspEntity<Object> delete(@RequestBody JClientReqEntity<List<AdminSmRicheditFileInfoForm>> fileInfoFormList){
        adminSmRicheditFileInfoService.deleteFileInfo(fileInfoFormList.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 整合文件服务提供的附件删除功能
     * @param fileId 文件id
     * @return 成功返回code = 0000，失败返回异常信息
     */
    @Operation(summary = "删除")
    @PostMapping("/del")
    public JClientRspEntity<Object> deleteWithFileServer(@RequestBody JClientReqEntity<String> fileId){
        adminSmRicheditFileInfoService.deleteFileInfoWithFileServer(fileId.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }
}
