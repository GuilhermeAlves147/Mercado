package com.ucb.market.services;

import com.ucb.market.dto.UserDTO;
import com.ucb.market.model.User;
import com.ucb.market.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CADASTRO
    public User register(UserDTO userDTO) {

        // Verifica se o e-mail já está cadastrado
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        User user = new User(
                userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword()
        );

        return userRepository.save(user);
    }

    // LOGIN
    public User login(UserDTO userDTO) {

        return userRepository.findByEmail(userDTO.getEmail())
                .filter(user -> user.getPassword().equals(userDTO.getPassword()))
                .orElse(null);
    }
}