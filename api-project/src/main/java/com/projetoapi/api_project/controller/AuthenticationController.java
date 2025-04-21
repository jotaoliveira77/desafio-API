package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.LoginDTO;
import com.projetoapi.api_project.model.LoginResponseDTO;
import com.projetoapi.api_project.model.RegisterDTO;
import com.projetoapi.api_project.model.Users;
import com.projetoapi.api_project.repository.UserRepository;
import com.projetoapi.api_project.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
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

    return ResponseEntity.ok("Usuário registrado");
}

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Users)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));

    }


}