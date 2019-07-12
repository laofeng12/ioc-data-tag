package com.openjava.datatag.log.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtTagUpdateLogRepositoryImpl implements DtTagUpdateLogRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
