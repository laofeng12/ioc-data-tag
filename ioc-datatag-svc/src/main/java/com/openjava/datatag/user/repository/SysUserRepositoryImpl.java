package com.openjava.datatag.user.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
public class SysUserRepositoryImpl implements SysUserRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
