package com.mesh.task.service;

import com.mesh.task.dao.*;
import com.mesh.task.entity.User;
import jakarta.transaction.*;
import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhonesContainsOrEmailsContains(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username/email not found"));
        return user;
        // return userRepository.findByPhonesContainsOrEmailsContains(username, username)
        //     .orElseThrow(() -> new UsernameNotFoundException("User with username/email not found"));

    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }
}
