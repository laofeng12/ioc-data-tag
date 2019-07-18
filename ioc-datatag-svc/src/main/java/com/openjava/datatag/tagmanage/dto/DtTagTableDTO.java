package com.openjava.datatag.tagmanage.dto;

import com.openjava.datatag.tagmanage.domain.DtTag;
import lombok.Data;

import java.util.List;

@Data
public class DtTagTableDTO {
    private DtTag parentTag;
    private List<DtTag> ChildrenTag;
}
