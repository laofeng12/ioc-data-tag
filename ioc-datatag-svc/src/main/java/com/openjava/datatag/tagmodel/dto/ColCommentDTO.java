package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

/**
 * 前端要求的数据结构（字段信息）
 */
@Data
public class ColCommentDTO {
    String definition;//字段名
    String comment;//中文名称
}
