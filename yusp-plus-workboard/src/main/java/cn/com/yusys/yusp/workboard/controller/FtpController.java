package cn.com.yusys.yusp.workboard.controller;

import cn.cscb.uadp.tc.ftp.springboot3.ThreadSafeFtpService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * FTP 公共组件调用示例
 *
 * @author 19814
 * @version 1.0
 * @since 2025/11/21 15:51
 */
@Slf4j
@RestController
@RequestMapping("/api/tc/ftp")
public class FtpController {

    @Autowired
    private ThreadSafeFtpService ftpService;

    @PostMapping("/upload")
    public void uploadFile(File file, String remotePath) {
        // 无需手动连接/断开，连接池自动管理
        if (ftpService.uploadFile(file, remotePath)) {
            log.info("上传成功");
        }
    }

    /**
     * 多线程并发操作示例
     * @param files 文件列表
     * @param remoteDir remoteDir
     */
    @PostMapping("/batchUpload")
    public void batchUpload(List<File> files, String remoteDir) {
        // 替换为自定义线程池
        ExecutorService executor = new ThreadPoolExecutor(5, 5, 0L, java.util.concurrent.TimeUnit.MILLISECONDS,
                new java.util.concurrent.LinkedBlockingQueue<Runnable>(), new ThreadFactoryBuilder()
                .setNameFormat("ftp-demo-pool-%d").build());
        CountDownLatch latch = new CountDownLatch(files.size());

        for (File file : files) {
            executor.submit(() -> {
                try {
                    String remotePath = remoteDir + "/" + file.getName();
                    ftpService.uploadFile(file, remotePath);
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
            log.info("批量上传完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }
}
