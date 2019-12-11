package com.openjava.datatag.user.repository;

import com.openjava.datatag.user.domain.SysUser;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 数据库访问层
 * @author 黄文鑫
 *
 */
public interface SysUserRepository extends DynamicJpaRepository<SysUser, Long>, SysUserRepositoryCustom {
	/**
	 *
	 * @param account
	 * @return
	 */
	SysUser findByAccount(String account);

	/**
	 *
	 * @param mobile
	 * @return
	 */
	SysUser findByMobile(String mobile);

	/**
	 *
	 * @param uid
	 * @return
	 */
	SysUser findByOarelationid(String uid);

	/**
	 *
	 * @param fullname
	 * @return
	 */
	SysUser findByFullname(String fullname);
}
