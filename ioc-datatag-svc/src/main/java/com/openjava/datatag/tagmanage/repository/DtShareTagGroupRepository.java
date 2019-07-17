package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DtShareTagGroupRepository extends DynamicJpaRepository<DtShareTagGroup,Long>,DtShareTagGroupRepositoryCustorm {

    @Query(value ="select e.* ,ROUND(((RANK() over (order by POPULARITY))-1)/(COUNT(*) over ())*4) popularity_level from\n" +
            "       (select tg.ID tags_id,tg.TAGS_NAME, u.USERID SHARE_USER_ID, u.FULLNAME SHARE_USER_Name,\n" +
            "            tg.MODIFY_TIME,tg.SYNOPSIS,tg.POPULARITY,u.LEVEL1,u.FULLNAME\n" +
            "        from SYS_USER u inner join DT_TAG_GROUP tg on tg.CREATE_USER = u.USERID where tg.IS_SHARE = 1 and tg.IS_DELETED = 0) e\n" +
            "where e.LEVEL1 like :searchKey or e.TAGS_NAME like :searchKey or e.SYNOPSIS like :searchKey or e.FULLNAME like :searchKey " +
            "order by e.POPULARITY desc "
            ,nativeQuery = true
            ,countQuery = "select count(*) from\n" +
            "       (select tg.ID tags_id,tg.TAGS_NAME, u.USERID SHARE_USER_ID, u.FULLNAME SHARE_USER_Name,\n" +
            "            tg.MODIFY_TIME,tg.SYNOPSIS,tg.POPULARITY,u.LEVEL1,u.FULLNAME\n" +
            "        from SYS_USER u inner join DT_TAG_GROUP tg on tg.CREATE_USER = u.USERID where tg.IS_SHARE = 1 and tg.IS_DELETED = 0) e\n" +
            "where e.LEVEL1 like :searchKey or e.TAGS_NAME like :searchKey or e.SYNOPSIS like :searchKey or e.FULLNAME like :searchKey")
    Page<DtShareTagGroup> findList(@Param("searchKey")String searchKey, Pageable pageable);


}

