package com.mesh.task.dao;

import com.mesh.task.entity.User;
import org.springframework.cache.annotation.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("select u from User u inner join u.phones p inner join u.emails e where p.phone = ?1 or e.email = ?2")
    @Cacheable(value = "userCache")
    Optional<User> findByPhonesContainsOrEmailsContains(String phone, String email);

}
