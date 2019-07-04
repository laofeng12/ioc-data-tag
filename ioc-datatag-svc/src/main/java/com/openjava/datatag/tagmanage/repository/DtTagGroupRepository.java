package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * DT_TAG_GROUP数据库访问层
 * @author lch
 *
 */
public interface DtTagGroupRepository extends DynamicJpaRepository<DtTagGroup, Long>, DtTagGroupRepositoryCustom{
	
}
