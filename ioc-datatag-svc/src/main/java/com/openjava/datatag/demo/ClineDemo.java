package com.openjava.datatag.demo;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.demo.dto.BaseResp;
import com.openjava.datatag.demo.dto.TopDepartmentReqReqDTO;
import com.openjava.datatag.demo.dto.TopDepartmentRespDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;

public class ClineDemo {
    public void doGet() throws Exception{
        LjdpHttpClient client = new LjdpHttpClient();
        client.setHeader("authority-token","klynRtd2upCTo1EOywc4davW5lBwrNF5Y9CDP4dZb-Q");
        client.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
//        HttpResponse resp = client.postJSON("", JSONObject.toJSON());

        HttpResponse resp = client.get("http://183.6.55.26:31075/admin/org/sysOrg/doUserTopDepartment?id="+"392846190550001");
        System.out.println(resp.getStatusLine().getStatusCode());
        String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
        System.out.println(jsontext);
        TopDepartmentRespDTO result = JSONObject.parseObject(jsontext, TopDepartmentRespDTO.class);
        if (resp.getStatusLine().getStatusCode() == 200) {
            System.out.println(result.getOrgname());
        }
    }
    public void doPost() throws Exception{
        LjdpHttpClient client = new LjdpHttpClient();
        client.setHeader("authority-token","klynRtd2upCTo1EOywc4davW5lBwrNF5Y9CDP4dZb-Q");
        client.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        TopDepartmentReqReqDTO req = new TopDepartmentReqReqDTO();
        req.setModelName("233");
        req.setTaggingModelId(123L);
        HttpResponse resp = client.postJSON("http://localhost:8080//datatag/demo/cline/doPost", JSONObject.toJSONString(req));
        String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
        System.out.println(jsontext);
        BaseResp result = JSONObject.parseObject(jsontext, BaseResp.class);
        System.out.println(resp.getStatusLine().getStatusCode());
        if (resp.getStatusLine().getStatusCode()==200) {
            System.out.println( result.getMessage());
        }else {
            System.out.println(result.getMessage());
        }

    }
    public static void main(String[] args) throws Exception{
        ClineDemo cline = new ClineDemo();
        cline.doGet();
        cline.doPost();
    }
}
