package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.profilePackage.ProfileDTO;
import com.projetoapi.api_project.model.usersPackage.Users;
import com.projetoapi.api_project.repository.UserRepository;
import com.projetoapi.api_project.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/update-username")
    public ResponseEntity updateProfile(@RequestBody ProfileDTO profile, Principal principal) {
        String currentUsername = principal.getName();
        Users user = authorizationService.findByUsername(currentUsername);

        user.setUsername(profile.username());
        userRepository.save(user);

        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }

}
