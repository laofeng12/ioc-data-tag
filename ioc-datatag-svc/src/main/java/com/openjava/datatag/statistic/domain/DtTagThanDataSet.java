package com.openjava.datatag.statistic.domain;


import lombok.Data;

/**
 * 实体，上个月每个数据集对应的标签数量
 * @author maliang
 *
 */

@Data
public class DtTagThanDataSet {
   private String numbers;//上个月每个数据集对应的标签数量
    public DtTagThanDataSet(String numbers){
        this.numbers=numbers;
    }

}
