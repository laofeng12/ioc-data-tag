package com.openjava.datatag.statistic.domain;

import lombok.Data;

/**
 * 实体，上个月唯一标签增长数量
 * @author maliang
 *
 */

@Data
public class DtTagTemp {
    private int tagsum;
    public DtTagTemp(int tagsum){
        this.tagsum=tagsum;
    }
}
