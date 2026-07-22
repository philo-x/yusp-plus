package cn.com.yusys.yusp.notice.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 富文本附件表对应实体类
 * @author zhangyt12
 * @date 2021/6/24 15:43
 */
@TableName("admin_sm_richedit_file_info")
public class AdminSmRicheditFileInfoEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 唯一主键
     */
    @TableId
    private String fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件存储路径
     */
    private String filePath;
    /**
     * 文件大小
     */
    private BigDecimal fileSize;
    /**
     * 文件扩展名
     */
    private String extName;
    /**
     * 文件虚拟文件夹
     */
    private String parentFolder;
    /**
     * 业务流水号
     */
    private String busNo;
    /**
     * 上传时间
     */
    private String uploadTime;
    /**
     * 备注
     */
    private String fileRemark;

    public AdminSmRicheditFileInfoEntity() {
    }

    public String getFileId() {
        return this.fileId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public BigDecimal getFileSize() {
        return this.fileSize;
    }

    public String getExtName() {
        return this.extName;
    }

    public String getParentFolder() {
        return this.parentFolder;
    }

    public String getBusNo() {
        return this.busNo;
    }

    public String getUploadTime() {
        return this.uploadTime;
    }

    public String getFileRemark() {
        return this.fileRemark;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setFileRemark(String fileRemark) {
        this.fileRemark = fileRemark;
    }
}
