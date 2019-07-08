package com.openjava.datatag.statistic.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DtHotTags {
    private List<Object> HotTags =new ArrayList<>();
    public void getHotTags(List<Object> AllYearMonth){this.HotTags=HotTags;}
}
