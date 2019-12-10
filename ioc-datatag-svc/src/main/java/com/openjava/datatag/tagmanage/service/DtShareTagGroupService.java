package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import org.ljdp.component.exception.APIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DtShareTagGroupService {
    /**
     *
     * @param searchKey
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<DtShareTagGroup> findList(String searchKey, Pageable pageable)throws Exception;

    /**
     *
     * @param id
     * @param userId
     * @param ip
     * @throws Exception
     */
     void choose(Long id, Long userId,String ip)throws Exception;
}
