package com.mesh.task.dto;

import lombok.Data;

@Data
public class EmailDto {

    private Long id;

    private String email;

    public Long userId;

}
