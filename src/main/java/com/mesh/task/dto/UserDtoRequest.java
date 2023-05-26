package com.mesh.task.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UserDtoRequest {

    private String name;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    private List<PhoneDto> phones;

    private List<EmailDto> emails;

}
