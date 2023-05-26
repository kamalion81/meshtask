package com.mesh.task.dao;

import com.mesh.task.entity.EmailData;
import com.mesh.task.entity.PhoneData;
import com.mesh.task.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<EmailData, Long> {

    public List<EmailData> findAllByEmailInAndUserIsNot(List<String> emails, User user);
}
