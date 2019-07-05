package com.openjava.datatag.statistic.domain;


import lombok.Data;

/**
 * 实体，上个月每个数据集对应的标签数量
 * @author maliang
 *
 */

@Data
public class DtTagThanDataSet {
   private double numbers;
    public DtTagThanDataSet(double numbers){
        this.numbers=numbers;
    }

}
