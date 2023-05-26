package com.mesh.task.dao;

import com.mesh.task.entity.PhoneData;
import com.mesh.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneData, Long> {

    public List<PhoneData> findAllByPhoneInAndUserIsNot(List<String> phones, User user);
}
