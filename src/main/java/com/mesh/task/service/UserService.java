package com.mesh.task.service;

import com.mesh.task.autorization.*;
import com.mesh.task.dao.UserRepository;
import com.mesh.task.dto.EmailDto;
import com.mesh.task.dto.PhoneDto;
import com.mesh.task.dto.UserDtoRequest;
import com.mesh.task.dto.UserDtoResponse;
import com.mesh.task.entity.*;
import com.mesh.task.mapper.UserMapper;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.math.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper mapper;

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public User save(User user){
        return userRepository.save(user);
    }

    public void delete(Long userId){
        userRepository.deleteById(userId);
    }

    public UserDtoResponse update(Long userId, UserDtoRequest dto){
        try{
            User user = getUser(userId);
            List<PhoneDto> phones = dto.getPhones();
            List<EmailDto> emails = dto.getEmails();
            List<String> phoneNumbers = phones.stream().map(phoneDto -> phoneDto.phone).collect(Collectors.toList());
            List<String> emailsString = emails.stream().map(emailDto -> emailDto.getEmail()).collect(Collectors.toList());
            boolean existPhoneDoubles = phoneService.checkPhoneEquals(phoneNumbers, user);
            boolean existEmailDoubles = emailService.checkEmailEquals(emailsString, user);
            if (existEmailDoubles || existPhoneDoubles) throw new Exception("Exist doubles in db");
            log.error("Exist doubles in db");
            UserDtoResponse responseDto = mapper.dtoRequestToResponse(dto);

            phones.stream().forEach(phone -> phone.setUserId(userId));
            emails.stream().forEach(email -> email.setUserId(userId));
            responseDto.setId(userId);

            responseDto.setAccount(mapper.accountToDto(user.getAccount()));
            User saved = save(mapper.dtoToUser(responseDto));
            return mapper.userToResponseDto(saved);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }

    }

    public Page<UserDtoResponse> findAll(Pageable pageable, String name, String phone, String email, LocalDate dateOfBirth){
        Specification<User> spec = Specification.where(null);
        if (name != null) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        if(dateOfBirth != null) {
            spec = spec.and((root, query, builder) -> builder.greaterThan(root.get("dateOfBirth"), dateOfBirth));
        }
        if (phone != null) {
            spec = spec.and((root, query, builder) -> {
                Join<User, PhoneData> phoneDataJoin = root.join("phones", JoinType.INNER);
                return builder.equal(phoneDataJoin.get("phone"), phone);
            });
        }
        if (email != null) {
            spec = spec.and((root, query, builder) -> {
                Join<User, EmailData> emailDataJoin = root.join("emails", JoinType.INNER);
                return builder.equal(emailDataJoin.get("email"), email);
            });
        }
        return userRepository.findAll(spec, pageable).map(mapper::userToResponseDto);
    }

    public UserDtoResponse getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(mapper::userToResponseDto).orElse(null);
    }

    public User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Transactional
    public synchronized void transferMoney(String token, Long toUserId, BigDecimal amount) {
        Long fromUserId = jwtUtil.getUserIdFromToken(token);
        // Fetch users
        User fromUser = userRepository.findById(fromUserId)
            .orElseThrow(() -> new IllegalArgumentException("From user not found"));
        User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new IllegalArgumentException("To user not found"));

        //Fetch accounts
        Account fromUserAccount = accountService.findByUser(fromUser);
        Account toUserAccount = accountService.findByUser(toUser);

        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Transfer amount should be positive");
            throw new IllegalArgumentException("Transfer amount should be positive");
        }

        // Validate from user's balance
        if (fromUserAccount.getBalance().compareTo(amount) < 0) {
            log.error("Insufficient balance");
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Update user balances
        fromUserAccount.setBalance(fromUserAccount.getBalance().subtract(amount));
        toUserAccount.setBalance(toUserAccount.getBalance().add(amount));

        // Save updated users
        accountService.save(fromUserAccount);
        accountService.save(toUserAccount);

    }


}
