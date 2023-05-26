package com.mesh.task.service;

import com.mesh.task.dao.EmailRepository;
import com.mesh.task.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository repository;

    public boolean checkEmailEquals(List<String> emails, User user){
        return !repository.findAllByEmailInAndUserIsNot(emails, user).isEmpty();
    }
}
