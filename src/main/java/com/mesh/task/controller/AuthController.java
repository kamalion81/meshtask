package com.mesh.task.controller;

import com.mesh.task.autorization.*;
import com.mesh.task.dao.*;
import com.mesh.task.entity.User;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Tag(name = "Авторизация")
public class AuthController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping
    @Operation(summary = "Получить JWT токен для авторизации")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userRepository.findByPhonesContainsOrEmailsContains(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username/email not found"));
        String token = StringUtils.join("Bearer ", jwtUtil.generateToken(user));
        return ResponseEntity.ok(token);
    }
}
