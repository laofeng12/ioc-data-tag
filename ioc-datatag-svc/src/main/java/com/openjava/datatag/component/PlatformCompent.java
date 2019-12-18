package com.openjava.datatag.component;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.tagcol.dto.SpUnifyWorkformDTO;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "platform")
public class PlatformCompent {
    private String baseUrl;
    private String spUnifyWorkform;
    private String finish;
    private String cancel;
    private String spUnifyMsgNotice;
    private String jobPath;

    @Resource
    private TokenGenerator tokenGenerator;//
    /**
     * 提交工单
     * @param businessId
     * @param userId 协作用户
     * @param userAccount
     * @param
     */
    public void spUnifyWorkform(String businessId,String userId,String userAccount){
        String url = spUnifyWorkform;//提交工单url
        try {
            LjdpHttpClient client = new LjdpHttpClient();
            client = initcClient(client,Long.valueOf(userId));
            SpUnifyWorkformDTO spUnifyWorkformDTO = new SpUnifyWorkformDTO();
            spUnifyWorkformDTO.setJobPath(jobPath);
            spUnifyWorkformDTO.setBusinessId(businessId);
            spUnifyWorkformDTO.setUserId(userId);
            spUnifyWorkformDTO.setUserAccount(userAccount);
            HttpResponse resp = client.postJSON(url, JSONObject.toJSONString(spUnifyWorkformDTO));
            String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
            System.out.println(jsontext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成工单
     * @param businessId
     * @param userId
     */
    public void finish(String businessId,String userId){
        String url = String.format(finish,1,businessId,userId);//完成工单url
        try {
            LjdpHttpClient client = new LjdpHttpClient();
            client = initcClient(client,Long.valueOf(userId));
            HttpResponse resp = client.postJSON(url, "");
            String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
            System.out.println(jsontext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消工单
     * @param businessId
     * @param userId
     */
    public void cancel(String businessId,String userId){
        String url = String.format(cancel,1,businessId,userId);//取消工单url
        try {
            LjdpHttpClient client = new LjdpHttpClient();
            client = initcClient(client,Long.valueOf(userId));
            HttpResponse resp = client.postJSON(url,"");
            String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
            System.out.println(jsontext);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 告警
     */
    public void spUnifyMsgNotice(){
        String url = spUnifyMsgNotice;//告警url
        try {
            LjdpHttpClient client = new LjdpHttpClient();
//            client = initcClient(client,Long.valueOf(userId));
            HttpResponse resp = client.postJSON(url, JSONObject.toJSONString(""));
            String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
            System.out.println(jsontext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public LjdpHttpClient initcClient(LjdpHttpClient client,Long userid){
        String  token= tokenGenerator.createToken(userid);//模型创建者才有权限调用数据集接口
        client.setHeader("authority-token",token);//
        client.setHeader("User-Agent","platform-schedule-job");//
        return client;
    }
    public static void main(String[] args) {
        String kkk = "/category/workform/spUnifyWorkform/cancel/%s/%S-%s";
        kkk = String.format(kkk,1,2,3);
        System.out.println(kkk);
    }
}
