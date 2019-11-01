package com.openjava.datatag.tagmanage.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@ApiModel("新版标签树节点")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Data
public class DtTagDTO2 {
    private String value;
    private String label;
}
