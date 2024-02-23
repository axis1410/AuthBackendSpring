package com.axis.authbackend.repository;


import com.axis.authbackend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);
}
