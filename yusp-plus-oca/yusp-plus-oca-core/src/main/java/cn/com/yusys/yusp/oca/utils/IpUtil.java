package cn.com.yusys.yusp.oca.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description: 类说明
 * @author: zhangsong
 * @date: 2021/3/29
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    public static String getIpAddr(ServerHttpRequest request) {

        HttpHeaders headers = request.getHeaders();
        String ipAddress = getIpAddressFromHeader(headers);
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getIpAddressFromRequest(request);
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }

        return ipAddress;
    }

    private static String getIpAddressFromRequest(ServerHttpRequest request) {
        String ipAddress;
        ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
        //IP4和IP6的本机IP
        if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
            // 根据网卡取本机配置的IP
            try {
                InetAddress inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            } catch (UnknownHostException e) {
                // 获取本机配置的IP异常
                ipAddress = "";
            }
        }
        return ipAddress;
    }

    private static String getIpAddressFromHeader(HttpHeaders headers) {
        String ipAddress = headers.getFirst("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        return ipAddress;
    }
}
