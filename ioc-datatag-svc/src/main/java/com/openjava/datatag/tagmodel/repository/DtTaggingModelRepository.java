package com.openjava.datatag.tagmodel.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 标签模型数据库访问层
 * @author zmk
 *
 */
public interface DtTaggingModelRepository extends DynamicJpaRepository<DtTaggingModel, Long>, DtTaggingModelRepositoryCustom{

}
