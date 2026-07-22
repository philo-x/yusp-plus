package cn.com.yusys.yusp.uaa.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * servlet 全局异常处理
 * @author zhangyt12
 * @date 2021/8/13 10:26
 */
@RestController
public class ServletErrorController extends BasicErrorController {

    private static final Logger log = LoggerFactory.getLogger(ServletErrorController.class);

    private ServerProperties serverProperties;

    /**
     * Create a new {@link BasicErrorController} instance.
     * @param errorAttributes the error attributes
     * @param serverProperties configuration properties
     */
    public ServletErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError(), null);
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        HttpStatus status = getStatus(request);

        log.info("ErrorController extends BasicErrorController - 当前全局异常状态码 status：{}", status);

        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            String message = (String) body.get("message");
            if (message.startsWith("http://")) {
                log.info("ErrorController extends BasicErrorController - 将 302 改为 402");
                // 将 302 改为 402
                return new ResponseEntity<>(body, HttpStatus.PAYMENT_REQUIRED);
            }
        }
        return new ResponseEntity<>(body, status);
    }
}
