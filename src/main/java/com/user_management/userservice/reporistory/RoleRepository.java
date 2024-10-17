package com.user_management.userservice.reporistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_management.userservice.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByRoleName(String roleName);
}
