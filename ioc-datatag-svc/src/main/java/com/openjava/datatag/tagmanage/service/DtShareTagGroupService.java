package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DtShareTagGroupService {
    Page<DtShareTagGroup> findList(String searchKey, Pageable pageable);

    List<DtTag> choose(Long id);
}
