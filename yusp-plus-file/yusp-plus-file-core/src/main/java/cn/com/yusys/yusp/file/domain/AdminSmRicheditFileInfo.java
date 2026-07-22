package cn.com.yusys.yusp.file.domain;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 富文本文件上传基本信息实体类
 *
 * @author liaoxd
 */
public class AdminSmRicheditFileInfo implements Serializable {

    @TableId
    private String fileId;

    private String fileName;

    private String filePath;

    private BigDecimal fileSize;

    private String extName;

    private String parentFolder;

    private String busNo;

    private String uploadTime;

    private String fileRemark;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @return FILE_ID
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    /**
     * @return FILE_NAME
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * @return FILE_PATH
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * @return FILE_SIZE
     */
    public BigDecimal getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize
     */
    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return EXT_NAME
     */
    public String getExtName() {
        return extName;
    }

    /**
     * @param extName
     */
    public void setExtName(String extName) {
        this.extName = extName == null ? null : extName.trim();
    }

    /**
     * @return PARENT_FOLDER
     */
    public String getParentFolder() {
        return parentFolder;
    }

    /**
     * @param parentFolder
     */
    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder == null ? null : parentFolder.trim();
    }

    /**
     * @return BUS_NO
     */
    public String getBusNo() {
        return busNo;
    }

    /**
     * @param busNo
     */
    public void setBusNo(String busNo) {
        this.busNo = busNo == null ? null : busNo.trim();
    }

    /**
     * @return UPLOAD_TIME
     */
    public String getUploadTime() {
        return uploadTime;
    }

    /**
     * @param uploadTime
     */
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime == null ? null : uploadTime.trim();
    }

    /**
     * @return FILE_REMARK
     */
    public String getFileRemark() {
        return fileRemark;
    }

    /**
     * @param fileRemark
     */
    public void setFileRemark(String fileRemark) {
        this.fileRemark = fileRemark == null ? null : fileRemark.trim();
    }

}