package com.openjava.datatag.tagmanage.repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtShareTagGroupRepositoryImpl implements DtShareTagGroupRepositoryCustorm {
    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
