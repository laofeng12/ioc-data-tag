package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DtShareTagGroupRepository extends DynamicJpaRepository<DtShareTagGroup,Long>,DtShareTagGroupRepositoryCustorm {
    @Query(value =
            "select tg.ID tags_id,tg.TAGS_NAME, u.USERID SHARE_USER_ID, u.FULLNAME SHARE_USER_Name,\n" +
            "tg.MODIFY_TIME,tg.SYNOPSIS,tg.POPULARITY,u.LEVEL1\n" +
            "from SYS_USER u inner join DT_TAG_GROUP tg on tg.CREATE_USER = u.USERID\n" +
            "where tg.IS_SHARE = 1 and tg.IS_DELETED = 0 and " +
                    "(u.LEVEL1 like :searchKey or tg.TAGS_NAME like :searchKey or tg.SYNOPSIS like :searchKey)"
            ,nativeQuery = true
            ,countQuery = "select count(*) from SYS_USER u inner join DT_TAG_GROUP tg on tg.CREATE_USER = u.USERID\n" +
            "where tg.IS_SHARE = 1 and tg.IS_DELETED = 0 \n" +
            "  and (u.LEVEL1 like :searchKey or tg.TAGS_NAME like :searchKey or tg.SYNOPSIS like :searchKey)")
    Page<DtShareTagGroup> findList(@Param("searchKey")String searchKey, Pageable pageable);

}

