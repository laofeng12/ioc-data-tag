package com.openjava.datatag.tagmanage.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShareTopListDTO {
    List<ShareTopDTO> shareTopDTOList = new ArrayList<>();//签组共享榜单
}
