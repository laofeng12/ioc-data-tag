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

    @Query(value = "select popularity_level from (\n" +
            "  select id,POPULARITY,ROUND(((RANK() over (order by POPULARITY))-1)/(COUNT(*) over ())*4) popularity_level,IS_SHARE\n" +
            "  from DT_TAG_GROUP where IS_DELETED = 0 and (id = :tagsId) or IS_SHARE=1) e\n" +
            "where e.ID = :tagsId"
            ,nativeQuery = true
    )
    Long  findPopuLvlByTagsId(Long tagsId);
}
