package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.repository.DtShareTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.utils.tree.TagTreeNode;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DtShareTagGroupServiceImpl implements DtShareTagGroupService{
    @Resource
    private DtShareTagGroupRepository dtShareTagGroupRepository;

    @Resource
    private DtTagGroupRepository dtTagGroupRepository;

    @Resource
    private DtTagRepository dtTagRepository;

    public Page<DtShareTagGroup> findList(String searchKey, Pageable pageable){
        return dtShareTagGroupRepository.findList("%" + searchKey+ "%", pageable);
    }

    public DtTagGroup chooseNewTagGroup(Long id,Long userId) throws APIException {
        Optional<DtTagGroup> o = dtTagGroupRepository.findById(id);
        DtTagGroup tgg = null;
        if(o.isPresent()){
            tgg = o.get();
        }else{
            //正常状况这个异常不可能报,因为在控制层做过验证了
            throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "未找到该标签组");
        }
        Long newId = ConcurrentSequence.getInstance().getSequence();
        DtTagGroup newTgg = new DtTagGroup();
        newTgg.setIsNew(true);
        newTgg.setId(newId);
        newTgg.setTagsName(tgg.getTagsName());
        newTgg.setIsShare(0L);
        newTgg.setSynopsis(tgg.getSynopsis());
        newTgg.setPopularity(0L);
        newTgg.setCreateUser(userId);
        Date now = new Date();
        newTgg.setCreateTime(now);
        newTgg.setModifyTime(now);
        newTgg.setIsDeleted(0L);
        return dtTagGroupRepository.saveAndFlush(newTgg);
    }


}
