package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class DtTagConditionDTO {
    @ApiModelProperty("条件设置主键")
    private Long tagConditionId;
    @ApiModelProperty("字段表主键")
    private Long colId;
    @ApiModelProperty("标签id")
    private Long tagId;
    @ApiModelProperty("标签名")
    private String tagName;
    @ApiModelProperty("标签组id")
    private Long tagsId;
    @ApiModelProperty("标签id路径")
    private String idPath;
    @ApiModelProperty("是否手动设置")
    private Long isHandle;
    @ApiModelProperty("逻辑语句")
    private String filterExpression;
    @ApiModelProperty("源字段名")
    private String sourceCol;
    @ApiModelProperty("显示字段名")
    private String showCol;
    @ApiModelProperty("条件设置")
    private List<SaveConditionDtFilterExpressionDTO> conditionSetting = new ArrayList<>();
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
//        if (getClass() != obj.getClass()){
//            return false;
//        }
        DtTagConditionDTO condition = (DtTagConditionDTO) obj;
        if (condition == null) {
            return false;
        }
        if (this.getTagConditionId() == null) {
            if (condition != null){
                return false;
            }
        } else if (!this.getTagConditionId().equals(condition.getTagConditionId())){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<DtTagConditionDTO> saveconditions = new ArrayList<>();
        DtTagConditionDTO d = new DtTagConditionDTO();
        DtTagConditionDTO d2 = new DtTagConditionDTO();
        d.setTagConditionId(1L);
        d2.setTagConditionId(1l);
        saveconditions.add(d);
        System.out.println(saveconditions.contains(d2));
    }
}
