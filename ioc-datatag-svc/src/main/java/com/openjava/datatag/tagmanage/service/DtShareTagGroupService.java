package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.utils.tree.TagTreeNode;
import org.ljdp.component.exception.APIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DtShareTagGroupService {
    Page<DtShareTagGroup> findList(String searchKey, Pageable pageable);

     void choose(Long id, Long userId,String ip)throws APIException;
}
