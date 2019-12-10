package com.openjava.datatag.statistic.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DtMonthsTagNumber {
    private List<Object> MonthTagNumber =new ArrayList<>();//
    public void getMonthTagNumber(List<Object> MonthTagNumber){this.MonthTagNumber=MonthTagNumber;}

}
