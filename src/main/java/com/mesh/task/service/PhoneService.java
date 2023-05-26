package com.mesh.task.service;

import com.mesh.task.dao.PhoneRepository;
import com.mesh.task.entity.PhoneData;
import com.mesh.task.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository repository;

    public PhoneData save(PhoneData phone){
        return repository.save(phone);
    }

    public Page<PhoneData> findAll(Pageable pageable){
        return repository.findAll(pageable);

    }

    public boolean checkPhoneEquals(List<String> phones, User user){
        return !repository.findAllByPhoneInAndUserIsNot(phones, user).isEmpty();
    }
}
