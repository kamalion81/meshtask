package com.mesh.task.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PhoneDto {

    private Long id;

    public String phone;

    public Long userId;


}
