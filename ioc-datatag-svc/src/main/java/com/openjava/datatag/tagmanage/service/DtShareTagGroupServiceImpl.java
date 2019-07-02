package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.repository.DtShareTagGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class DtShareTagGroupServiceImpl implements DtShareTagGroupService{
    @Resource
    private DtShareTagGroupRepository dtShareTagGroupRepository;
    public Page<DtShareTagGroup> findList(String searchKey, Pageable pageable){
        return dtShareTagGroupRepository.findList("%" + searchKey+ "%", pageable);
    }
}
