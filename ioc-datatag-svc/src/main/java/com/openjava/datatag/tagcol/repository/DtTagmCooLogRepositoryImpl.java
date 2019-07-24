package com.openjava.datatag.tagcol.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtTagmCooLogRepositoryImpl implements DtTagmCooLogRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
