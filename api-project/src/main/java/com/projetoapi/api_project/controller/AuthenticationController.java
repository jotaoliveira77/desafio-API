package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.Users;
import com.projetoapi.api_project.repository.UserRepository;
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
    if (userRepository.findByUsername(user.getUsername()) != null) {
        return ResponseEntity.badRequest().body("Nome de usuário já está em uso");
    }
    if (userRepository.existsByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity.badRequest().body("E-mail já está em uso");
    }

    Users usuario = new Users();
    usuario.setUsername(user.getUsername());
    usuario.setPassword(user.getPassword());
    usuario.setEmail(user.getEmail());

    userRepository.save(usuario);

    return ResponseEntity.ok("Usuário registrado");
}

}