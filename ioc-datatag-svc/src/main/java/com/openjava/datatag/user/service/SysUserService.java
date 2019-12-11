package com.openjava.datatag.user.service;


import com.openjava.datatag.user.domain.SysUser;

/**
 * 业务层接口
 * @author 黄文鑫
 *
 */
public interface SysUserService {
	/**
	 *
	 * @param id
	 * @return
	 */
	SysUser get(Long id);

	/**
	 *
	 * @param fullname
	 * @return
	 */
	SysUser findByFullname(String fullname);

}
