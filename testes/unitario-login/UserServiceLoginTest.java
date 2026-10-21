package com.ucb.market.services;

import com.ucb.market.dto.LoginRequest;
import com.ucb.market.dto.UserResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.model.User;
import com.ucb.market.repository.CartRepository;
import com.ucb.market.repository.ClientRepository;
import com.ucb.market.repository.SellerRepository;
import com.ucb.market.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Testes unitários do login (UserService.login) - sem banco e sem Spring: repositório simulado com Mockito. */
class UserServiceLoginTest {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private UserService service;
    private User usuario;

    @BeforeEach
    void preparar() {
        userRepository = mock(UserRepository.class);
        encoder = new BCryptPasswordEncoder();
        service = new UserService(mock(CartRepository.class), userRepository,
                mock(SellerRepository.class), mock(ClientRepository.class), encoder);

        usuario = new User();
        usuario.setId(1);
        usuario.setUsername("maria");
        usuario.setEmail("maria@email.com");
        usuario.setPassword(encoder.encode("senha123"));
    }

    @Test
    @DisplayName("LG-U-01: credenciais válidas devolvem os dados do usuário")
    void loginComCredenciaisValidas() {
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        UserResponse r = service.login(new LoginRequest("maria@email.com", "senha123"));

        assertEquals(1, r.id());
        assertEquals("maria", r.username());
        assertEquals("maria@email.com", r.email());
    }

    @Test
    @DisplayName("LG-U-02: senha incorreta é rejeitada")
    void loginComSenhaErrada() {
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        BusinessException e = assertThrows(BusinessException.class,
                () -> service.login(new LoginRequest("maria@email.com", "errada")));

        assertEquals("E-mail ou senha inválidos", e.getMessage());
    }

    @Test
    @DisplayName("LG-U-03: e-mail inexistente é rejeitado com a mesma mensagem")
    void loginComEmailInexistente() {
        when(userRepository.findByEmail("x@email.com")).thenReturn(Optional.empty());

        BusinessException e = assertThrows(BusinessException.class,
                () -> service.login(new LoginRequest("x@email.com", "senha123")));

        assertEquals("E-mail ou senha inválidos", e.getMessage());
    }

    @Test
    @DisplayName("LG-U-04: a senha é guardada com hash BCrypt, nunca em texto puro")
    void senhaNaoEstaEmTextoPuro() {
        assertNotEquals("senha123", usuario.getPassword());
        assertTrue(usuario.getPassword().startsWith("$2"));
        assertTrue(encoder.matches("senha123", usuario.getPassword()));
    }
}
