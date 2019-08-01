package com.openjava.datatag.component;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datatag.user.domain.SysUser;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.framework.sys.service.LmMemberTokenService;
import org.ljdp.common.json.JacksonTools;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenGenerator {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private LmMemberTokenService lmMemberTokenService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String createToken(Long userId){
        String token = "";
        try {
            if(userId==null){
                return "";
            }
            SysUser u = sysUserService.get(userId);
            if(u==null){
                return "";
            }

            OaUserVO vo = new OaUserVO();
            vo.setUserId(u.getUserid().toString());
            vo.setUserAccount(u.getAccount());
            vo.setUserName(u.getFullname());
            vo.setUserMobileno(u.getMobile());
            vo.setHeadImg(u.getPicture());
            vo.setUserType(u.getAccounttype());
            vo.setLoginTime(new Date());
            vo.setRefreshTime(new Date());
            vo.setUserAgent("platform-schedule-job");

            //OA返回用户信息
            vo.setOrgcode(u.getOrgcode());
            vo.setDeptcode(u.getDeptcode());
            vo.setDeptid(u.getDeptid());
            vo.setOarelationid(u.getOarelationid());
            vo.setOrgname(u.getOrgname());
            vo.setLevel0(u.getLevel0());
            vo.setLevel1(u.getLevel1());

            //会话保持时间
            int loginTimeout = 1;
            vo.setExpireInMin(loginTimeout);

            String userJson = JacksonTools.getObjectMapper().writeValueAsString(vo);
            token = lmMemberTokenService.createToken(userId, vo.getUserAgent());
            System.out.println("【生成token： 】"+token);
            vo.setTokenId(token);

            //集群版,默认用redis
            redisTemplate.boundValueOps(token).set(vo, loginTimeout, TimeUnit.MINUTES);
            redisTemplate.boundValueOps(token+"-JSON").set(userJson, loginTimeout, TimeUnit.HOURS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

}
