package com.projetoapi.api_project.controller;

import com.projetoapi.api_project.model.usersPackage.*;
import com.projetoapi.api_project.repository.UserRepository;
import com.projetoapi.api_project.repository.VerificadoroUsuarioRepository;
import com.projetoapi.api_project.security.TokenService;
import com.projetoapi.api_project.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificadoroUsuarioRepository verificadoroUsuarioRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDTO dto = new RegisterDTO("testuser", "test@example.com", "password123", UserRole.USER);

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        ResponseEntity<?> response = controller.register(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuário registrado. Verifique seu e-mail!", response.getBody());
        verify(userRepository).save(any(Users.class));
        verify(emailService).sendEmail(eq("test@example.com"), contains("Confirme"), contains("Clique"));
    }

    @Test
    void testRegister_UsernameExists() {
        RegisterDTO dto = new RegisterDTO("existingUser", "new@example.com", "password123", UserRole.USER);
        when(userRepository.findByUsername("existingUser")).thenReturn(new Users());

        ResponseEntity<?> response = controller.register(dto);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Nome de usuário já está em uso", response.getBody());
    }

    @Test
    void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO("user", "password");
        Authentication auth = mock(Authentication.class);
        Users user = new Users("user", "email", UserRole.USER, "encodedPass");

        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenService.generateToken(user)).thenReturn("mocked-jwt");

        ResponseEntity<?> response = controller.login(loginDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof LoginResponseDTO);
        assertEquals("mocked-jwt", ((LoginResponseDTO) response.getBody()).token());
    }

    @Test
    void testVerifyEmail_Success() {
        UUID token = UUID.randomUUID();
        Users user = new Users("user", "email", UserRole.USER, "password");
        VerificadorUsuario verificador = new VerificadorUsuario(1L, token, Instant.now().plusSeconds(3600), user);

        when(verificadoroUsuarioRepository.findByUuid(token)).thenReturn(Optional.of(verificador));

        ResponseEntity<?> response = controller.verifyEmail(token.toString());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("E-mail verificado com sucesso!", response.getBody());
        assertTrue(user.isEnabled());
    }

    @Test
    void testVerifyEmail_TokenExpired() {
        UUID token = UUID.randomUUID();
        Users user = new Users("user", "email", UserRole.USER, "password");
        VerificadorUsuario verificador = new VerificadorUsuario(1L, token, Instant.now().minusSeconds(3600), user);

        when(verificadoroUsuarioRepository.findByUuid(token)).thenReturn(Optional.of(verificador));

        ResponseEntity<?> response = controller.verifyEmail(token.toString());
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("O token expirou.", response.getBody());
    }

    @Test
    void testVerifyEmail_TokenInvalid() {
        UUID token = UUID.randomUUID();
        when(verificadoroUsuarioRepository.findByUuid(token)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.verifyEmail(token.toString());
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Token inválido ou expirado.", response.getBody());
    }
}
