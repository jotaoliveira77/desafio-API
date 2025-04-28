package com.projetoapi.api_project.service;

import com.projetoapi.api_project.model.usersPackage.Users;
import com.projetoapi.api_project.model.usersPackage.VerificadorUsuario;
import com.projetoapi.api_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);

    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Users> existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public String getEmailByUsername(String username) {
        UserDetails user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

}
