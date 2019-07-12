package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.utils.IpUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseRequestDTO {

    private HttpServletRequest httpServletRequest;

    public HttpServletRequest getRequest() {
        return httpServletRequest;
    }


    public void setRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getIp(){
        return IpUtil.getRealIP(httpServletRequest);
    }
}
