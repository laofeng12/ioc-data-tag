package com.openjava.datatag.tagmanage.dto;

import lombok.Data;

@Data
public class ShareTopDTO {
    private Long heat;//使用热度
    private String name;//表签组名称
    private Long id;//表签组id
    public ShareTopDTO(){

    }
    public ShareTopDTO(Long heat, String name,Long id){
        this.heat=heat;
        this.name=name;
        this.id= id;
    }
}
