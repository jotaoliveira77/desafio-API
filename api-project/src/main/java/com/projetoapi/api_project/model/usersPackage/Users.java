package com.projetoapi.api_project.model.usersPackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    private boolean enabled = false;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role;

    public Users(String username, String email, UserRole role, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN)return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
