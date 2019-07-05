package com.openjava.datatag.statistic.domain;

import lombok.Data;

/**
 * 实体,上个与数据集增长百分比
 * @author maliang
 *
 */

@Data
public class DtTaggingModelTemp {
    private String growth_rate;
    public DtTaggingModelTemp(String growth_rate){
        this.growth_rate=growth_rate;
    }
}
