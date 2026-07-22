package cn.com.yusys.yusp.notice.service;

import java.util.List;

/**
 * oss 存储文件对应 service 接口层
 * @author zhangyt12
 * @date 2021/6/24 16:48
 */
public interface ThirdPartOssService {
    /**
     * 批量删除 oss 中存储的文件
     * @param filePathList 被删除文件在 oss 中的路径集合
     */
    void delete(List<String> filePathList);
}
