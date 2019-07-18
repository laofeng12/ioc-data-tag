package com.openjava.datatag.tagmanage.dto;

import lombok.Data;
import org.springframework.data.annotation.Transient;


/**
 * author lch
 * 前端需要返回状态的都加上
 *
 */
@Data
public class BaseMessageDTO {
    @Transient
    private Long code;
    @Transient
    private String Message;

}
