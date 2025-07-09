package com.example.demo.repository;

import com.example.demo.dto.UserFilterRequest;
import com.example.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> filterUsers(UserFilterRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Join<Object, Object> schoolJoin = user.join("school", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getId() != null) {
            predicates.add(cb.equal(user.get("id"), request.getId()));
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("lastName")), "%" + request.getName().toLowerCase() + "%"));
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("email")), "%" + request.getEmail().toLowerCase() + "%"));
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            predicates.add(cb.equal(user.get("status"), request.getStatus()));
        }

        if (request.getSchoolId() != null) {
            predicates.add(cb.equal(schoolJoin.get("id"), request.getSchoolId()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}