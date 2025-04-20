package com.projetoapi.api_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.projetoapi.api_project.model.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
UserDetails findByUsername(String username);

boolean existsByUsername(String username);

boolean existsByEmail(String email);
}
