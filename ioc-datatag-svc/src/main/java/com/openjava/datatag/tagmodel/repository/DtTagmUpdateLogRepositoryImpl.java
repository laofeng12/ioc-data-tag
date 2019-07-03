package com.openjava.datatag.tagmodel.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtTagmUpdateLogRepositoryImpl implements DtTagmUpdateLogRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
