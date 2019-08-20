package com.openjava.datatag.user.repository;

import com.openjava.datatag.user.domain.SysUser;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 数据库访问层
 * @author 黄文鑫
 *
 */
public interface SysUserRepository extends DynamicJpaRepository<SysUser, Long>, SysUserRepositoryCustom {
	
	SysUser findByAccount(String account);

	SysUser findByMobile(String mobile);

	SysUser findByOarelationid(String uid);

	SysUser findByFullname(String fullname);
}
