package com.openjava.datatag.statistic.domain;

import lombok.Data;

/**
 * 实体,上个月使用数据集个数
 * @author maliang
 *
 */

@Data
public class DtTaggingModelTemp {
    private Long lastMonthDataSetCount;
    public DtTaggingModelTemp(Long lastMonthDataSetCount){
        this.lastMonthDataSetCount=lastMonthDataSetCount;
    }
}
