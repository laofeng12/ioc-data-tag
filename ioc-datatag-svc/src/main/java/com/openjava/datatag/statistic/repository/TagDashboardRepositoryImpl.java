package com.openjava.datatag.statistic.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 */
public class TagDashboardRepositoryImpl implements TagDashboardRepositoryCustom {
    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
