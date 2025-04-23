package com.projetoapi.api_project.model.usersPackage;

public enum UserRole {
    ADMIN(1),
    USER(2);

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public static UserRole fromInt(int i) {
        switch (i) {
            case 1: return ADMIN;
            case 2: return USER;
            default: throw new IllegalArgumentException("Invalid role: " + i);
        }
    }
}
