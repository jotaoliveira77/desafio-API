package com.projetoapi.api_project.repository;

import com.projetoapi.api_project.model.usersPackage.VerificadorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificadoroUsuarioRepository extends JpaRepository<VerificadorUsuario, Long> {
    Optional<VerificadorUsuario> findByUuid(UUID uuid);
}
