package com.openjava.datatag.user.service;


import com.openjava.datatag.user.domain.SysUser;

/**
 * 业务层接口
 * @author 黄文鑫
 *
 */
public interface SysUserService {

	SysUser get(Long id);
	SysUser findByFullname(String fullname);

}
