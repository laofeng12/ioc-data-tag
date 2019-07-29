package com.openjava.datatag.user.service;


import com.openjava.datatag.user.domain.SysUser;
import com.openjava.datatag.user.repository.SysUserRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * 业务层
 * @author 黄文鑫
 *
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
	
	@Resource
	private SysUserRepository sysUserRepository;
	@Resource
	private SysCodeService sysCodeService;

	public SysUser get(Long id) {
		Optional<SysUser> o = sysUserRepository.findById(id);
		if(!o.isPresent()) {
			return null;
		}
		SysUser m = o.get();
		if(m.getAccounttype() != null) {
			Map<String, SysCode> sysaccounttype = sysCodeService.getCodeMap("sys.account.type");
			SysCode c = sysaccounttype.get(m.getAccounttype().toString());
			if(c != null) {
				m.setAccounttypeName(c.getCodename());
			}
		}
		if(m.getIsexpired() != null) {
			Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
			SysCode c = publicYN.get(m.getIsexpired().toString());
			if(c != null) {
				m.setIsexpiredName(c.getCodename());
			}
		}
		if(m.getIslock() != null) {
			Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
			SysCode c = publicYN.get(m.getIslock().toString());
			if(c != null) {
				m.setIslockName(c.getCodename());
			}
		}
		if(m.getStatus() != null) {
			Map<String, SysCode> sysuserstate = sysCodeService.getCodeMap("sys.user.state");
			SysCode c = sysuserstate.get(m.getStatus().toString());
			if(c != null) {				
				m.setStatusName(c.getCodename());
			}
		}
		if(m.getFromtype() != null) {
			Map<String, SysCode> sysuserfromtype = sysCodeService.getCodeMap("sys.user.fromtype");
			SysCode c = sysuserfromtype.get(m.getFromtype().toString());
			if(c != null) {
				m.setFromtypeName(c.getCodename());
			}
		}
		return m;
	}

}
