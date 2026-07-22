package cn.com.yusys.yusp.notice.service.impl;

import cn.com.yusys.yusp.notice.service.ThirdPartOssService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * oss 存储文件对应 service 层实现类
 * @author zhangyt12
 * @date 2021/6/28 9:57
 */
@Service("thirdPartOssService")
public class ThirdPartOssServiceImpl implements ThirdPartOssService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ThirdPartOssServiceImpl.class);

    @Override
    public void delete(List<String> filePathList) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-chengdu.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "";
        String accessKeySecret = "";
        String bucketName = "yusp-oss";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件，key 等同于 ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如 abc/efg/123.jpg。
        for (String filePath : filePathList) {
            if (filePath.contains(endpoint)) {
                filePath = filePath.substring(endpoint.length() + 1);
            }
        }
        List<String> keys = new ArrayList<>(filePathList);
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        log.info("删除 oss 后的返回值： " + deletedObjects);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
