package com.mesh.task.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UserDtoRequest {

    private String name;

    private LocalDate dateOfBirth;

    private List<PhoneDto> phones;

    private List<EmailDto> emails;

}
