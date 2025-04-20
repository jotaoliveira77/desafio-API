package com.projetoapi.api_project.model;

public record RegisterDTO(String username, String email, String password, UserRole role) {
}
