package cn.com.yusys.yusp.workboard.controller;

import cn.cscb.uadp.tc.httpclient.core.async.AsyncHttpClient;
import cn.cscb.uadp.tc.httpclient.core.core.HttpClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Http Client 公共组件调用示例
 *
 * @author 19814
 * @version 1.0
 * @since 2025/11/21 15:44
 */
@Slf4j
@RestController
@RequestMapping("/api/tc/httpclient")
public class HttpClientController {

    @Autowired
    @Qualifier("defaultHttpClientTemplate")
    private HttpClientTemplate httpClientTemplate;

    @Autowired
    @Qualifier("defaultAsyncHttpClient")
    private AsyncHttpClient defaultAsyncHttpClient;

    private static final String URL = "http://api.example.com/users";

    @PostMapping("/send")
    public String send() throws IOException {
        // 发送 GET 请求
        String response = httpClientTemplate.get("http://api.example.com/users/1");

        // 带请求头的 GET 请求
        Map<String, String> headers = new HashMap<>(8);
        headers.put("Authorization", "Bearer token");
        response = httpClientTemplate.get(URL, headers);

        // 带参数的 GET 请求
        Map<String, String> params = new HashMap<>(8);
        params.put("page", "1");
        params.put("size", "10");
        response = httpClientTemplate.get(URL, headers, params);

        // POST JSON 请求
        String jsonBody = "{\"name\":\"张三\",\"age\":25}";
        response = httpClientTemplate.postJson(URL, null, jsonBody);

        // POST 表单请求
        Map<String, String> formData = new HashMap<>(8);
        formData.put("username", "zhangsan");
        formData.put("password", "123456");
        response = httpClientTemplate.postForm("http://api.example.com/login", null, formData);
        return "成功";
    }

    @PostMapping("/async")
    public String async() throws IOException {
        // 异步 GET 请求
        CompletableFuture<String> future = defaultAsyncHttpClient.getAsync(URL);
        future.thenAccept(response-> {
            log.info("异步请求完成："+response);
        });

        // 异步 POST JSON 请求
        String jsonBody="{\"name\":\"张三\",\"age\":25}";
        future = defaultAsyncHttpClient.postJsonAsync(
                URL,
                null,
                jsonBody
        );
        return "成功";
    }



}
