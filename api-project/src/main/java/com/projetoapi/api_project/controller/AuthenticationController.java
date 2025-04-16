package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.LoginDTO;
import com.projetoapi.api_project.model.Users;
import com.projetoapi.api_project.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;
@PostMapping("/signup")
public ResponseEntity<?> register(@RequestBody Users user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity.badRequest().body("Nome de usuário já está em uso");
    }
    if (userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body("E-mail já está em uso");
    }

    Users usuario = new Users();
    usuario.setUsername(user.getUsername());
    usuario.setPassword(user.getPassword());
    usuario.setEmail(user.getEmail());

    userRepository.save(user);

    return ResponseEntity.ok("Usuário registrado");
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO data) {
        var userOptional = userRepository.findByUsername(data.username());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            if (user.getPassword().equals(data.password())) {
                return ResponseEntity.ok("Login realizado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Nome de usuário ou senha inválidos");
            }
        } else {
            return ResponseEntity.badRequest().body("Nome de usuário ou senha inválidos");
        }
    }
}