package com.mesh.task.service;

import com.mesh.task.dao.AccountRepository;
import com.mesh.task.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public Account findByUser(User user){
        return repository.findAccountByUser(user).orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public Account save(Account account){
        return repository.save(account);
    }

    public List<Account> findAll(){
        return (List<Account>) repository.findAll();
    }
}
