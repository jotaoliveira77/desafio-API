package com.projetoapi.api_project.model.usersPackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name ="verificacao_usuario")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificadorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private Instant exp_date;

    @OneToOne
    @JoinColumn(nullable = false)
    private Users user;



}
