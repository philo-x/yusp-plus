package cn.com.yusys.yusp.file.web.rest;

import cn.com.yusys.yusp.commons.file.util.FileUtils;
import cn.com.yusys.yusp.commons.module.adapter.query.QueryModel;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.ResultDto;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.ArrayUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.MimeMappingUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.file.domain.AdminFileUploadInfo;
import cn.com.yusys.yusp.file.domain.AdminSmRicheditFileInfo;
import cn.com.yusys.yusp.file.dto.AdminFileUploadInfoDTO;
import cn.com.yusys.yusp.file.dto.AdminSmRicheditFileInfoDTO;
import cn.com.yusys.yusp.file.service.impl.FileProviderServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yusys
 * @version 1.0
 * @date 2020/7/15 10:07
 */
@RestController
@RequestMapping("/api/file/provider")
public class FileProviderServiceResource {

    private static final Logger logger = LoggerFactory.getLogger(FileProviderServiceResource.class);

    @Autowired
    private FileProviderServiceImpl fileProviderService;

    @Value(value = "${yusp.file.service.default-template-name}")
    private String defaultTemplateName;

    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return fileId 包含文件信息的fileId
     */
    @PostMapping(value = "/uploadfile")
    public JClientRspEntity<String> uploadFile(@RequestPart("file") MultipartFile file) throws Exception {
        AdminFileUploadInfo adminFileUploadInfo = new AdminFileUploadInfo();
        String fileName = "";
        // 当未单独传递文件名称时，取文件name，可能中文乱码
        if (StringUtils.isEmpty(fileName)) {
            fileName = file.getOriginalFilename();
        }
        fileName = URLDecoder.decode(fileName, "UTF-8");
        // 现在 common 中的文件组件通过 fastDfs 上传同名文件不会覆盖, 其他两种上传同名文件会覆盖, 这里在上传时重命名文件, 添加时间戳来区分同名文件.
        // admin_file_upload_info 中还是保存原始文件名
        String tempFileName =
                fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
        String fileId = FileUtils.upload(defaultTemplateName, file.getBytes(), file.getSize(), tempFileName);
        logger.debug("File path->{}", fileId);
        adminFileUploadInfo.setFileName(fileName);
        long size = file.getSize() / 1024;
        adminFileUploadInfo.setFileSize(new BigDecimal(size));
        adminFileUploadInfo.setExtName(fileName.substring(fileName.lastIndexOf(".") + 1));
        adminFileUploadInfo.setUploadTime(DateUtils.formatDateTimeByDef());
        adminFileUploadInfo.setFilePath(fileId);
        //解决上传未带业务流水号的bug
        if (StringUtils.isEmpty(adminFileUploadInfo.getBusNo())) {
            //使用uuid,
            adminFileUploadInfo.setBusNo(StringUtils.getUUID());
        }
        adminFileUploadInfo.setUserId(SessionUtils.getLoginCode() != null ? SessionUtils.getLoginCode() : "SYSTEM");
        fileProviderService.insertSelective(adminFileUploadInfo);
        return JClientRspEntity.buildSuccess(fileId);
    }

    /**
     * 文件下载
     *
     * @param fileId   文件id，多个用,隔开,多个文件统一压缩打包成一个zip文件
     * @param response 写文件的响应流
     */
    @GetMapping("/download")
    public void download(@RequestParam String fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (Objects.isNull(fileId)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "illegal file id.");
            return;
        }
        logger.debug("Request parameter: {}", fileId);
        try {
            String[] fileIds = fileId.split(",");
            byte[] downloadFile = batchDownloadFile(fileIds);

            String fileName;
            if (fileIds.length > 1) {
                fileName = System.currentTimeMillis() + ".zip";
            } else {
                fileName = fileProviderService.getOriginalFileName(fileIds[0]);
            }
            writeToResponse(MimeMappingUtils.getEncodeFileName(request, fileName), MimeMappingUtils.getMimeType(fileName),
                    downloadFile, response);
        } catch (Exception e) {
            logger.error("file download failed. file ids:{}", fileId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "file download failed.");
        }
    }

    /**
     * 输出文件流
     *
     * @param fileName 文件名称
     * @param mimeType 文件类型
     * @param fileByte 文件字节
     * @param response
     * @throws IOException
     */
    private void writeToResponse(String fileName, String mimeType, byte[] fileByte, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType(mimeType + ";charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
    }

    /**
     * 批量下载文件，多个文件统一压缩打包成zip文件
     *
     * @param fileIds 文件id，多个用,隔开
     * @return
     * @throws Exception
     */
    private byte[] batchDownloadFile(String[] fileIds) throws Exception {
        //单文件直接下载返回
        if (fileIds.length == 1) {
            return FileUtils.readToByte(defaultTemplateName, fileIds[0]);
        }
        // 多个文件打成zip包
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zos = new ZipOutputStream(bos)) {
            Map<String, Integer> fileNameMap = new HashMap<>(8);
            for (String fileId : fileIds) {
                // 下载文件
                byte[] bytes = FileUtils.readToByte(defaultTemplateName, fileId);
                String fileName = fileProviderService.getOriginalFileName(fileId);
                if (ArrayUtils.nonEmpty(bytes)) {
                    if (fileNameMap.containsKey(fileName)) {
                        // 防止压缩时存在同名会报错
                        int num = fileNameMap.get(fileName);
                        fileNameMap.put(fileName, num + 1);
                        int pointIndex = fileName.lastIndexOf(".");
                        pointIndex = pointIndex < 0 ? fileName.length() : pointIndex;
                        fileName = fileName.substring(0, pointIndex) + "(" + num + ")" + fileName.substring(pointIndex);
                    } else {
                        fileNameMap.put(fileName, 1);
                    }
                    zos.putNextEntry(new ZipEntry(fileName));
                    zos.write(bytes);
                    zos.closeEntry();
                }
            }
            zos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new Exception("Download exception：", e);
        }
    }

    /**
     * 文件上传信息获取
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/fileUpload")
    public JClientRspEntity<List<AdminFileUploadInfoDTO>> selectFileUploadByModel(@RequestBody JClientReqEntity<Map<String, Object>> param) {
        List<AdminFileUploadInfo> infoList = this.fileProviderService.selectFileUploadByModel(param.getBody());
        return JClientRspEntity.buildSuccess((List<AdminFileUploadInfoDTO>) BeanUtils.beansCopy(infoList, AdminFileUploadInfoDTO.class));
    }

    /**
     * 删除文件，多个文件以逗号分隔
     *
     * @param fileId
     */
    @PostMapping("/deleteFile")
    public JClientRspEntity<?> deleteFileRecords(String fileId) throws IOException {
        if (StringUtils.isBlank(fileId)) {
            return JClientRspEntity.buildFail("500", "illegal parameter");
        }
        try {
            this.fileProviderService.deleteFileRecords(fileId);
            return JClientRspEntity.buildSuccess("");
        } catch (Exception e) {
            return JClientRspEntity.buildFail("500", "file delete failed");
        }
    }

    /**
     * 富文本文件信息查询
     *
     * @param param
     * @return
     */
    @PostMapping("/richFile")
    @SuppressWarnings("unchecked")
    public JClientRspEntity<List<AdminSmRicheditFileInfoDTO>> selectRicheditFileByModel(@RequestBody JClientReqEntity<Map<String, Object>> param) {
        List<AdminSmRicheditFileInfo> infos = this.fileProviderService.selectRicheditFileByModel(param.getBody());
        return JClientRspEntity.buildSuccess((List<AdminSmRicheditFileInfoDTO>) BeanUtils.beansCopy(infos, AdminSmRicheditFileInfoDTO.class));
    }

    /**
     * 单个富文本文件上传
     *
     * @param file
     * @param busNo-必须指定其中的busNo
     * @return
     */
    @PostMapping(value = "/richedituploadfile")
    @ResponseBody
    public JClientRspEntity<?> uploadRicheditFile(@RequestPart("file") MultipartFile file, @RequestParam(required=false) String busNo) {
        AdminSmRicheditFileInfo adminSmRicheditFileInfo = new AdminSmRicheditFileInfo();
        try {
            String fileName = file.getOriginalFilename();
            String fileNameExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            String fileId = FileUtils.upload(defaultTemplateName, file.getBytes(), file.getSize(), fileName);
            logger.debug("File path->{}", fileId);
            adminSmRicheditFileInfo.setFileName(fileName);
            long size = file.getSize() / 1024;
            adminSmRicheditFileInfo.setFileSize(new BigDecimal(size));
            adminSmRicheditFileInfo.setExtName(fileNameExt);
            // 最近跟新时间
            adminSmRicheditFileInfo.setUploadTime(DateUtils.formatDateTimeByDef());
            adminSmRicheditFileInfo.setFilePath(fileId);
            if(StringUtils.isEmpty(busNo) || busNo.length() > 32) {
                busNo = StringUtils.getUUID();
                adminSmRicheditFileInfo.setBusNo(busNo);
            }
            fileProviderService.uploadRicheditFile(adminSmRicheditFileInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JClientRspEntity.buildFail("500", "上传失败");
        }
        return JClientRspEntity.buildSuccess(adminSmRicheditFileInfo);
    }

    /**
     * 删除富文本文件
     *
     * @param adminFileUploadInfo-根据PATH删除富文本附件
     * @return
     */
    @PostMapping(value = "/deletericheditfile")
    public void deleteRicheditFiles(@RequestBody JClientReqEntity<AdminSmRicheditFileInfo> adminFileUploadInfo) {
        this.fileProviderService.deleteRicheidtFileRecords(adminFileUploadInfo.getBody().getFilePath());
    }

    /**
     * 删除富文本文件
     *
     * @param fileId
     */
    @PostMapping("/deleteRichFile")
    public void deleteRicheidtFileRecords(String fileId) {
        this.fileProviderService.deleteRicheidtFileRecords(fileId);
    }

    @GetMapping("/")
    protected JClientRspEntity<IPage<AdminFileUploadInfo>> index(@RequestBody JClientReqEntity<QueryModel> queryModel) {
        logger.debug("REST request to query index : {}", queryModel);
        return JClientRspEntity.buildSuccess(fileProviderService.selectByModel(queryModel.getBody()));
    }

    /**
     * 查询单个对象，公共API接口.
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    protected JClientRspEntity<AdminFileUploadInfo> show(@PathVariable String id) {
        logger.debug("REST request to get Object : {}", id);
        return JClientRspEntity.buildSuccess(fileProviderService.selectByPrimaryKey(id));
    }

    /**
     * 实体类创建，公共API接口.
     *
     * @param t
     * @return
     */
    @PostMapping("/")
    protected JClientRspEntity<Object> create(@RequestBody JClientReqEntity<AdminFileUploadInfo> t) {
        logger.debug("REST request to save Object : {}", t.getBody());
        fileProviderService.insert(t.getBody());
        return JClientRspEntity.buildSuccess(t);
    }

    /**
     * 对象修改，公共API接口.
     *
     * @param t
     * @return
     */
    @PostMapping("/update")
    protected JClientRspEntity<?> update(@RequestBody JClientReqEntity<AdminFileUploadInfo> t) {
        logger.debug("REST request to update Object : {}", t.getBody());
        try {
            int result = fileProviderService.update(t.getBody());
            return JClientRspEntity.buildSuccess(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JClientRspEntity.buildFail("500", e.getMessage());
        }
    }

    /**
     * 单个对象删除，公共API接口.
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    protected JClientRspEntity<?> delete(@PathVariable String id) {
        logger.debug("REST request to delete Object : {}", id);
        try {
            int result = fileProviderService.deleteByPrimaryKey(id);
            return JClientRspEntity.buildSuccess(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JClientRspEntity.buildFail("500", e.getMessage());
        }
    }

    /**
     * 批量对象删除，公共API接口.
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchdelete/{ids}")
    protected JClientRspEntity<Integer> deletes(@PathVariable String ids) {
        logger.debug("REST request to delete Object : {}", ids);
        return JClientRspEntity.buildSuccess(fileProviderService.deleteByIds(ids));
    }

}