package com.projetoapi.api_project.model.usersPackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name ="verificacao_usuario")
@Getter
@Setter
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
