package com.mesh.task.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDtoResponse {

    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private List<PhoneDto> phones;

    private List<EmailDto> emails;

    private AccountDto account;

}
