package cn.com.yusys.yusp.notice.form;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 富文本附件相关 api 接收前端请求参数自定义实体类
 * @author zhangyt12
 * @date 2021/6/24 16:00
 */
public class AdminSmRicheditFileInfoForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 文件 id
     */
    private String fileId;
    /**
     * 文件大小
     */
    private BigDecimal fileSize;
    /**
     * oss 保存完整路径
     */
    @Value(value = "/")
    private String filePath;
    /**
     * 文件后缀
     */
    private String extName;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 公告 id
     */
    private String busNo;
    /**
     * 文件虚拟路径
     */
    private String parentFolder;

    public AdminSmRicheditFileInfoForm() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }
}
