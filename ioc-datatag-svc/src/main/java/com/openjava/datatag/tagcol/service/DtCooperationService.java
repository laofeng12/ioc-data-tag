package com.openjava.datatag.tagcol.service;

import java.util.List;
import java.util.Map;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.dto.*;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import org.ljdp.ui.bootstrap.TablePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagcol.domain.DtCooperation;
import com.openjava.datatag.tagcol.query.DtCooperationDBParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * tagcol协作打标业务层接口
 *
 * @author hyq
 */
public interface DtCooperationService {
    /**
     *
     * @param params
     * @param pageable
     * @return
     */
    Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable);

    /**
     *
     * @param params
     * @param pageable
     * @return
     */
    List<DtCooperation> queryDataOnly(DtCooperationDBParam params, Pageable pageable);

    /**
     *
     * @param userId
     * @return
     */
    List<DtCooperationModelDTO> findUserModelByUserId(Long userId);

    /**
     *
     * @param prodPrams
     * @param itemParams
     * @param pageable
     * @return
     */
    Page<?> queryShopItemAndProd(DtCooperationDBParam prodPrams, DtTaggingModelDBParam itemParams, Pageable pageable);

    /**
     *
     * @param item
     * @param pageable
     * @return
     */
    Page<?> findPageUserModelByUserId(DtCooperationDBParam item, Pageable pageable);

    /**
     *
     * @param userId
     * @param modelId
     * @return
     */
    List<DtCooperationSetCol> findUserModelCooField(Long userId, Long modelId);

    /**
     *
     * @param modelId
     * @param colField
     * @return
     */
    List<DtTagGroup> findCurrentUserTagGroup(Long modelId, Long colField);

    /**
     *
     * @param id
     * @return
     */
    DtCooperation get(Long id);

    /**
     *
     * @param userId
     * @param tagGroupId
     * @return
     */
    Long findCooUserTagGroup(Long userId, Long tagGroupId);

    /**
     *
     * @param m
     * @return
     */
    DtCooperation doSave(DtCooperation m);

    /**
     *
     * @param m
     * @throws Exception
     */
    void doCoolSave(DtCooperationDTO m) throws Exception;

    /**
     *
     * @param list
     * @throws Exception
     */
    void doCoolListSave(List<DtCooperationListDTO> list) throws Exception;

    /**
     *
     * @param id
     */
    void doDelete(Long id);

    /**
     *
     * @param ids
     */
    void doRemove(String ids);

    /**
     *
     * @param params
     * @param pageable
     * @return
     * @throws Exception
     */
    TablePage<DtCooperationDTO> doSearch(DtCooperationDBParam params, Pageable pageable) throws Exception;

    /**
     *
     * @param id
     * @return
     */
    ColListDTO getColList(Long id);
}
