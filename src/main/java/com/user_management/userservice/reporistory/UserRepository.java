package com.user_management.userservice.reporistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_management.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    User findByEmail(String email);
}
