package com.projetoapi.api_project.model.usersPackage;

public record RegisterDTO(String username, String email, String password, UserRole role) {
}
