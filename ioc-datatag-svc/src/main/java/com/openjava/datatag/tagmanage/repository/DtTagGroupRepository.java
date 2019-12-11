package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.dto.ShareTopDTO;
import com.openjava.datatag.tagmanage.dto.ShareTopListDTO;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * DT_TAG_GROUP数据库访问层
 * @author lch
 *
 */
public interface DtTagGroupRepository extends DynamicJpaRepository<DtTagGroup, Long>, DtTagGroupRepositoryCustom{
    /**
     *
     * @param createUser
     * @param groupId
     * @return
     */
    @Query(value = "from DtTagGroup t where t.isDeleted=0 and  t.createUser=:createUser and  t.id=:groupId")
    List<DtTagGroup> getMyTagGroupByGroupId(@Param("createUser") Long createUser,@Param("groupId")Long groupId);

    /**
     *
     * @param createUser
     * @return
     */
    @Query(value = "from DtTagGroup t where t.isDeleted=0 and  t.createUser=:createUser")
    List<DtTagGroup> getMyTagGroup(@Param("createUser") Long createUser);

    /**
     *
     * @param tagsId
     * @return
     */
    @Query(value = "select popularity_level from (\n" +
            "  select id,POPULARITY,ROUND(((RANK() over (order by POPULARITY))-1)/(COUNT(*) over ())*4) popularity_level,IS_SHARE\n" +
            "  from DT_TAG_GROUP where IS_DELETED = 0 and (id = :tagsId) or IS_SHARE=1) e\n" +
            "where e.ID = :tagsId"
            ,nativeQuery = true
    )
    Long  findPopuLvlByTagsId(Long tagsId);

    /**
     *
     * @param createUser
     * @param isDeleted
     * @return
     */
    @Query(value = "select max (t.popularity) from DtTagGroup t where t.createUser=:createUser and t.isDeleted=:isDeleted")
    Long findMaxPopularityBytagsIdAAndIsDeleted(Long createUser,Long isDeleted);

    /**
     *
     * @param isDeleted
     * @param isShare
     * @return
     */
    @Query(value = "select max (t.popularity) from DtTagGroup t  where t.isDeleted=:isDeleted and t.isShare=:isShare")
    Long findMaxPopularityBytagsIdAAndIsDeletedAAndIsShare(Long isDeleted,Long isShare);

    /**
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT t.POPULARITY,t.TAGS_NAME,t.id FROM DT_TAG_GROUP t WHERE t.IS_DELETED !=1 order by t.POPULARITY desc nulls last",
            countQuery = "SELECT count(1) from DT_TAG_GROUP t WHERE t.IS_DELETED !=1",
            nativeQuery = true)
    Page<Object[]> getShareTopList(Pageable pageable);
}
