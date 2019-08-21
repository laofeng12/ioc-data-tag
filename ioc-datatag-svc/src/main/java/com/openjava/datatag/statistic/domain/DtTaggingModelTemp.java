package com.openjava.datatag.statistic.domain;

import lombok.Data;

/**
 * 实体,上个月使用数据集个数
 * @author maliang
 *
 */

@Data
public class DtTaggingModelTemp {
    private Long growth_rate;
    public DtTaggingModelTemp(Long lastMonthDataSetCount){
        this.growth_rate=lastMonthDataSetCount;
    }
}
