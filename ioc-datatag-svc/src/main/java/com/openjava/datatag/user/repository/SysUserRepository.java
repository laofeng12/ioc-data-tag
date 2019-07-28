package com.openjava.datatag.user.repository;

import com.openjava.datatag.user.domain.SysUser;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 数据库访问层
 * @author 黄文鑫
 *
 */
public interface SysUserRepository extends DynamicJpaRepository<SysUser, Long>, SysUserRepositoryCustom {
	
	public SysUser findByAccount(String account);

	public SysUser findByMobile(String mobile);

	public SysUser findByOarelationid(String uid);
}
