package cn.com.yusys.yusp.uaa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author lty
 * @description 验证码生成接口
 * @date 2020/12/29
 */
@RestController
@RequestMapping("/api")
public class CodeGenerateController {

    private final Random rand = new SecureRandom();
    private final Logger log = LoggerFactory.getLogger(CodeGenerateController.class);
    /**
     * 设置redis的缓存key值
     */
    public String imageCodeRedisKey = "IMAGE_CODE_REDIS_KEY";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/codeimage/{uuId}")
    public void genCodeImage(@PathVariable String uuId,
                             HttpServletResponse response) throws Exception {
        log.debug("Generate corresponding graphic verification code according to client Id...");
        int imgWidth = 64;
        int imgHeight = 32;

        Color textColor = new Color(68, 115, 145);
        Color backColor = new Color(255, 255, 255);

        String random;
        int length = 4;
        try {
            random = String.valueOf(rand.nextInt());
            random = random.substring(random.length() - length);
        } catch (Exception err) {
            double r = ThreadLocalRandom.current().nextDouble();
            while ((length--) > 0) {
                r = r * 10;
            }
            random = String.valueOf((int) r);
        }
        log.debug("Graphic verification code:" + random);

        // 在内存中创建图象
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(backColor);
        g.clearRect(0, 0, imgWidth, imgHeight);
        g.setColor(textColor);
        g.setFont(new Font(null, Font.ITALIC, 20));
        g.drawString(random, 5, 23);

        // 随机产生几条干扰线
        // 随机数
        for (int i = 0; i < 6; i++) {
            int x1 = rand.nextInt(imgWidth);
            int y1 = rand.nextInt(imgHeight);
            int x2 = rand.nextInt(20);
            int y2 = rand.nextInt(10);
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(x1, y1, x1 + x2, y1 + y2);
        }
        try {
            // 将生成的图形验证码存储到 redis 缓存中，默认超时时间是1分钟
            // 向redis里存入数据和设置缓存时间
            stringRedisTemplate.opsForValue().set(imageCodeRedisKey + "-" + uuId, random, 60,
                    TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("image code generate failed", e);
            return;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", outputStream);
            log.debug("The graphics verification code corresponding to the client Id is generated successfully!");
        } catch (IOException ex) {
            log.error("image code generate failed", ex);
            return;
        }

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        os.write(outputStream.toByteArray());
        os.flush();
        os.close();
        outputStream.close();
    }
}
