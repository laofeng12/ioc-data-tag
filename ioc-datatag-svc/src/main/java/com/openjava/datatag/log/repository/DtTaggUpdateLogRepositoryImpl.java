package com.openjava.datatag.log.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtTaggUpdateLogRepositoryImpl implements DtTaggUpdateLogRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
