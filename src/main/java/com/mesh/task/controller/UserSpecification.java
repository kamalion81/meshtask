package com.mesh.task.controller;

import com.mesh.task.entity.PhoneData;
import com.mesh.task.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {

    private final String name;

    private final LocalDate dateOfBirth;


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }

        if (dateOfBirth != null) {
            predicates.add(criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
