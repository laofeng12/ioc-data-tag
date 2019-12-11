package com.openjava.datatag.log.repository;

import com.openjava.datatag.log.domain.DtTaggChooseLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * DT_TAGG_CHOOSE_LOG数据库访问层
 * @author lch
 *
 */
public interface DtTaggChooseLogRepository extends DynamicJpaRepository<DtTaggChooseLog, Long>, DtTaggChooseLogRepositoryCustom{
    /**
     *
     * @param userId
     * @param copiedTaggId
     * @return
     */
    @Query(value = "SELECT count(id) " +
            "from DT_TAGG_CHOOSE_LOG " +
            "where COPIED_TAGG = :copiedTaggId " +
            "and CHOOSE_USER = :userId " +
            "and to_char(CHOOSE_TIME,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')"
            ,nativeQuery = true)
    Long countChooseToday(Long userId,Long copiedTaggId);

    /**
     *
     * @param userId
     * @param copiedTaggId
     * @return
     */
    @Query(value = "SELECT count(id) " +
            "from DT_TAGG_CHOOSE_LOG " +
            "where COPIED_TAGG = :copiedTaggId " +
            "and CHOOSE_USER = :userId "
            ,nativeQuery = true)
	Long countChoose(Long userId,Long copiedTaggId);
}
