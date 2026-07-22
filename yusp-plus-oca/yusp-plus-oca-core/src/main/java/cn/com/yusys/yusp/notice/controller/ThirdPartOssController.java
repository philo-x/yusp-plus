package cn.com.yusys.yusp.notice.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.service.ThirdPartOssService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * oss 存储文件相关 api
 * @author zhangyt12
 * @date 2021/6/24 15:18
 */
@RestController
@RequestMapping("/api/oss")
public class ThirdPartOssController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ThirdPartOssController.class);

    @Autowired
    private ThirdPartOssService thirdPartOssService;

    /**
     * 获取 oss 上传文件密钥
     * @return 返回 oss 存储所需的 policy 令牌及相关信息
     */
    @PostMapping("/policy")
    public JClientRspEntity<Map<String, String>> policy(){
        String endpoint = "oss-cn-chengdu.aliyuncs.com";
        String accessKeyId = "";
        String accessKeySecret = "";
        String bucket = "yusp-oss";
        String host = "https://" + bucket + "." + endpoint;
        String dir = "RichText/";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<>();
            respMap.put("ossaccessKeyId", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
        } catch (Exception e) {
            log.error("生成 oss policy 失败：" + e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return JClientRspEntity.buildSuccess(respMap);
    }

    /**
     * 删除 oss 中存储的文件
     * @param filePathList 批量删除，被删除文件在 oss 中的路径
     * @return 成功返回 code = 0000，失败抛出异常或返回失败信息
     */
    @PostMapping("/delete")
    public JClientRspEntity<Object> delete(@RequestBody JClientReqEntity<List<String>> filePathList) {
        log.info("测试删除 oss 中的文件： " + filePathList.getBody());
        thirdPartOssService.delete(filePathList.getBody());
        return JClientRspEntity.buildSuccess("成功");
    }
}
