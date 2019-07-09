package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * DT_TAG_GROUP数据库访问层
 * @author lch
 *
 */
public interface DtTagGroupRepository extends DynamicJpaRepository<DtTagGroup, Long>, DtTagGroupRepositoryCustom{

    @Query(value = "from DtTagGroup t where t.isDeleted=0 and  t.createUser=:createUser")
    List<DtTagGroup> getMyTagGroup(@Param("createUser") Long createUser);
}
