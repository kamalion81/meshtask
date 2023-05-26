package com.mesh.task.controller;

import com.mesh.task.dto.UserDtoRequest;
import com.mesh.task.dto.UserDtoResponse;
import com.mesh.task.entity.User;
import com.mesh.task.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.*;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService service;

    @Hidden
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody User user) {
        return service.save(user);

    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping
    public void deleteUser(Long userId){
        service.delete(userId);
    }


    @Operation(summary = "Список пользователей")
    @GetMapping
    public ResponseEntity<Page<UserDtoResponse>> getAllUsers(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "phone", required = false) String phone,
        @RequestParam(name = "email", required = false) String email,
        @RequestParam(name = "date_of_birth", required = false) LocalDate dateOfBirth,
        Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable, name, phone, email, dateOfBirth));


    }

    @Operation(summary = "Редактирование пользователя")
    @PutMapping(path = "/{userId}", consumes = "application/json")
    public UserDtoResponse updateUser(@PathVariable("userId") Long userId,
                                      @RequestBody UserDtoRequest user) {
        return service.update(userId, user);
    }

    @Operation(summary = "Получение пользователя")
    @GetMapping(path = "/{userId}")
    public UserDtoResponse getUser(@PathVariable("userId") Long userId){
        return service.getUserById(userId);
    }

    @Operation(summary = "Перевод денег со счета на счет")
    @GetMapping(path = "/transfer")
    @ResponseStatus
    public void transferMoney(
        @RequestHeader(value = "Authorization", required = false) String token,
        @RequestParam(name = "toUser") Long toUserId,
        @RequestParam(name = "amount") BigDecimal amount){

        service.transferMoney(token, toUserId, amount);
    }





}
