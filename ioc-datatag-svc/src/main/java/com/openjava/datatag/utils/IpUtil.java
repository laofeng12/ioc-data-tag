package com.openjava.datatag.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class IpUtil {
    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");//x-forwarded-for
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)){// 多次反向代理后会有多个ip值，第一个ip才是真实ipif( ip.indexOf(",")!=-1 )
             ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");System.out.println("Proxy-Client-IP ip: " + ip);//Proxy-Client-IP ip:
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");System.out.println("WL-Proxy-Client-IP ip: " + ip);//WL-Proxy-Client-IP
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");System.out.println("HTTP_CLIENT_IP ip: " + ip);//HTTP_CLIENT_IP
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);//HTTP_X_FORWARDED_FOR
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");System.out.println("X-Real-IP ip: " + ip);//X-Real-IP
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();System.out.println("getRemoteAddr ip: " + ip);//getRemoteAddr
        }
        return ip;
    }
}