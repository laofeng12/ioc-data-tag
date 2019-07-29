package com.openjava.datatag.portrayal.api;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.user.service.SysUserService;
import io.swagger.annotations.*;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * api接口
 *
 * @author hyq
 */
@Api(tags = "画像相关接口管理")
@RestController
@RequestMapping("/datatag/portrayal")
public class PortrayalAction {

    @Resource
    private SysUserService sysUserService;

    
}
