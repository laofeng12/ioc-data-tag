package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.utils.IpUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class BaseIpDTO {
    private String Ip;//

    public void setIp(String ip) {
        Ip = ip;
    }
    public void setIp(HttpServletRequest request){
        Ip = IpUtil.getRealIP(request);
    }
    public String getIp(){
        return Ip;
    }
}
