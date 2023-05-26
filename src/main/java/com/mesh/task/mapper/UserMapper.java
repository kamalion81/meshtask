package com.mesh.task.mapper;

import com.mesh.task.dao.UserRepository;
import com.mesh.task.dto.*;
import com.mesh.task.entity.Account;
import com.mesh.task.entity.EmailData;
import com.mesh.task.entity.PhoneData;
import com.mesh.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected UserRepository userRepository;

    public abstract UserDtoResponse userToResponseDto(User user);

    public abstract User dtoToUser(UserDtoResponse dto);

    public abstract UserDtoResponse dtoRequestToResponse(UserDtoRequest request);

    @Mapping(target = "userId", source = "user.id")
    public abstract PhoneDto phoneToDto(PhoneData phoneData);

    @Mapping(target = "user", expression = "java(userRepository.findById(dto.userId).orElse(null))")
    public abstract PhoneData dtoToPhone(PhoneDto dto);

    @Mapping(target = "userId", source = "user.id")
    public abstract EmailDto emailToDto(EmailData email);

    @Mapping(target = "user", expression = "java(userRepository.findById(dto.userId).orElse(null))")
    public abstract EmailData dtoToEmail(EmailDto dto);

    @Mapping(target = "userId", source = "user.id")
    public abstract AccountDto accountToDto(Account account);

    @Mapping(target = "user", expression = "java(userRepository.findById(dto.userId).orElse(null))")
    public abstract Account dtoToAccount(AccountDto dto);


}
