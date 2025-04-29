package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.usersPackage.*;
import com.projetoapi.api_project.repository.UserRepository;
import com.projetoapi.api_project.repository.VerificadoroUsuarioRepository;
import com.projetoapi.api_project.security.TokenService;
import com.projetoapi.api_project.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    VerificadoroUsuarioRepository verificadoroUsuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;
@PostMapping("/signup")
public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO user) {

    if (this.userRepository.findByUsername(user.username()) !=  null) {
        return ResponseEntity.badRequest().body("Nome de usuário já está em uso");
    }
    if (this.userRepository.existsByEmail(user.email())) {
        return ResponseEntity.badRequest().body("E-mail já está em uso");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
    Users newUser = new Users(user.username(), user.email(), user.role(), encryptedPassword);

    this.userRepository.save(newUser);

    UUID uuid = UUID.randomUUID();
    Instant expDate = Instant.now().plusSeconds(60 * 60 * 24); // 24 horas para expirar
    VerificadorUsuario verificador = new VerificadorUsuario(null, uuid, expDate, newUser);
    verificadoroUsuarioRepository.save(verificador);

    String verificationLink = "http://localhost:8080/auth/verify?token=" + uuid.toString();
    emailService.sendEmail(newUser.getEmail(), "Confirme seu e-mail",
            "Clique no link para ativar sua conta: " + verificationLink);

    return ResponseEntity.ok("Usuário registrado. Verifique seu e-mail!");

}

        @PostMapping("/login")
        public ResponseEntity login(@RequestBody @Valid LoginDTO data) {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Users)auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @PatchMapping("/update")
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid UpdatePasswordDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }

            String username = authentication.getName();
            Users user = userRepository.existsByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!encoder.matches(dto.password(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Senha atual incorreta");
            }

            if (encoder.matches(dto.newPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("A nova senha não pode ser igual à senha atual");
            }

            if (dto.newPassword().length() < 8) {
                return ResponseEntity.badRequest().body("A nova senha deve ter pelo menos 8 caracteres");
            }

            user.setPassword(encoder.encode(dto.newPassword()));
            userRepository.save(user);

            return ResponseEntity.ok("Senha atualizada com sucesso");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar a solicitação");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        Optional<VerificadorUsuario> verificador = verificadoroUsuarioRepository.findByUuid(UUID.fromString(token));

        if (verificador.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }

        VerificadorUsuario usuarioVerificado = verificador.get();
        Users user = usuarioVerificado.getUser();

        if (usuarioVerificado.getExp_date().isBefore(Instant.now())) {
            return ResponseEntity.badRequest().body("O token expirou.");
        }

        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok("E-mail verificado com sucesso!");
    }


}