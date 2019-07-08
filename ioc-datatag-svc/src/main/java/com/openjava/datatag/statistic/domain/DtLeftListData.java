package com.openjava.datatag.statistic.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DtLeftListData {
    private List<Object> AllYearMonth =new ArrayList<>();
    public void getAllYearMonth(List<Object> AllYearMonth){this.AllYearMonth=AllYearMonth;}



}
