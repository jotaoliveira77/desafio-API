package com.projetoapi.api_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoapi.api_project.model.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
Optional<Users> findByUsername(String username);
Optional<Users> existsByEmail(String email);
}
