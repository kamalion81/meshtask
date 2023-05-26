package com.mesh.task.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDtoResponse {

    private Long id;

    private String name;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    private List<PhoneDto> phones;

    private List<EmailDto> emails;

    private AccountDto account;

}
