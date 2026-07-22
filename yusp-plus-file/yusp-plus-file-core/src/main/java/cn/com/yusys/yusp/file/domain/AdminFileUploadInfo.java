package cn.com.yusys.yusp.file.domain;

import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

/**
 * 文件上传基本信息实体类
 *
 * @author wangyf10
 */
public class AdminFileUploadInfo {

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

    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AdminFileUploadInfo other = (AdminFileUploadInfo) that;
        return (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
                && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
                && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
                && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
                && (this.getExtName() == null ? other.getExtName() == null : this.getExtName().equals(other.getExtName()))
                && (this.getParentFolder() == null ? other.getParentFolder() == null : this.getParentFolder().equals(other.getParentFolder()))
                && (this.getBusNo() == null ? other.getBusNo() == null : this.getBusNo().equals(other.getBusNo()))
                && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
                && (this.getFileRemark() == null ? other.getFileRemark() == null : this.getFileRemark().equals(other.getFileRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getExtName() == null) ? 0 : getExtName().hashCode());
        result = prime * result + ((getParentFolder() == null) ? 0 : getParentFolder().hashCode());
        result = prime * result + ((getBusNo() == null) ? 0 : getBusNo().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getFileRemark() == null) ? 0 : getFileRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileId=").append(fileId);
        sb.append(", fileName=").append(fileName);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", extName=").append(extName);
        sb.append(", parentFolder=").append(parentFolder);
        sb.append(", busNo=").append(busNo);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", fileRemark=").append(fileRemark);
        sb.append("]");
        return sb.toString();
    }

}