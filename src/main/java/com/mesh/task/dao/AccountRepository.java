package com.mesh.task.dao;

import com.mesh.task.entity.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findAccountByUser(User user);

}
