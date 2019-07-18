package com.openjava.datatag.utils;

import org.ljdp.component.user.BaseUserInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

	public static void getHtmlOfEntity(Object entity){
		if (entity == null) {
			return;
		}
		Class cls = entity.getClass();
		Field[] fields = cls.getDeclaredFields();
			for (Field field : fields){
				try{
					String type = field.getGenericType().toString();
					if (type.equals("class java.lang.String")){
						String name = field.getName();
						name = name.substring(0, 1).toUpperCase() + name.substring(1);
						Method mGet = entity.getClass().getMethod("get" + name);
						// 调用getter方法获取属性值
						String value = (String) mGet.invoke(entity);
						String htmlValue = StringUtil.stringToHtmlEntity(value);
						Method mSet = entity.getClass().getMethod("set" + name,String.class);
						mSet.invoke(entity,htmlValue);
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}


	}
}
