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
    Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable);

    List<DtCooperation> queryDataOnly(DtCooperationDBParam params, Pageable pageable);

    List<DtCooperationModelDTO> findUserModelByUserId(Long userId);

    Page<?> queryShopItemAndProd(DtCooperationDBParam prodPrams, DtTaggingModelDBParam itemParams, Pageable pageable);

    Page<?> findPageUserModelByUserId(DtCooperationDBParam item, Pageable pageable);

    List<DtCooperationSetCol> findUserModelCooField(Long userId, Long modelId);

    List<DtTagGroup> findCurrentUserTagGroup(Long modelId, Long colField);

    DtCooperation get(Long id);

    Long findCooUserTagGroup(Long userId, Long tagGroupId);

    DtCooperation doSave(DtCooperation m);

    void doCoolSave(DtCooperationDTO m) throws Exception;

    void doCoolListSave(List<DtCooperationListDTO> list) throws Exception;

    void doDelete(Long id);

    void doRemove(String ids);
    TablePage<DtCooperationDTO> doSearch(DtCooperationDBParam params, Pageable pageable) throws Exception;

    ColListDTO getColList(Long id);
}
