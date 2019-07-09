package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.JpaMultiDynamicQueryDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DtSetColRepositoryImpl implements DtSetColRepositoryCustom {
	private EntityManager em;
	private JpaMultiDynamicQueryDAO dao;//动态参数多表关联查询

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
		this.dao = new JpaMultiDynamicQueryDAO(em);
	}
	/**
	 * 检查打标条件是否合理
	 */
	public void check(String sql) throws Exception{
		try	{
			dao.query("SELECT COUNT (tagConditionId) FROM DtTagCondition WHERE "+sql);
		}catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
