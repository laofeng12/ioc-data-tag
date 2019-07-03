package com.openjava.datatag.utils;

import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.vo.UserVO;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 实体类工具
 * @author zmk
 *
 */
public class EntityClassUtil {

	/**
	 * 处理创建时的数据
	 * @param entity
	 * @param user
	 */
	public static void dealCreateInfo(Object entity, BaseUserInfo user) {
		if (entity == null || user == null) {
			return;
		}
//		Field[] fields = entity.getClass().getDeclaredFields();
		try {
			Field createUser = entity.getClass().getDeclaredField("createUser");
			Field createTime = entity.getClass().getDeclaredField("createTime");
			Field modifyUser = entity.getClass().getDeclaredField("modifyUser");
			Field modifyTime = entity.getClass().getDeclaredField("modifyTime");
			Date now = new Date();
			Long userId = Long.valueOf(user.getUserId());
			if (createUser != null) {
				createUser.setAccessible(true);
				createUser.set(entity, userId);
			}
			if (createTime != null) {
				createTime.setAccessible(true);
				createTime.set(entity, now);
			}
			if (modifyUser != null) {
				modifyUser.setAccessible(true);
				modifyUser.set(entity, userId);
			}
			if (modifyTime != null) {
				modifyTime.setAccessible(true);
				modifyTime.set(entity, now);
			}

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 处理更新时的数据
	 * @param entity
	 * @param user
	 */
	public static void dealModifyInfo(Object entity, BaseUserInfo user) {
		if (entity == null || user == null) {
			return;
		}
		try {
			Field modifyUser = entity.getClass().getDeclaredField("modifyUser");
			Field modifyTime = entity.getClass().getDeclaredField("modifyTime");
			Date now = new Date();
			Long userId = Long.valueOf(user.getUserId());
			if (modifyUser != null) {
				modifyUser.setAccessible(true);
				modifyUser.set(entity, userId);
			}
			if (modifyTime != null) {
				modifyTime.setAccessible(true);
				modifyTime.set(entity, now);
			}


		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
}
