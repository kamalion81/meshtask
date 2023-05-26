package com.mesh.task.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {

    private Long id;

    private BigDecimal balance;

    public Long userId;


}
