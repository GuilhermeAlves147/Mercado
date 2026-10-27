package com.ucb.market.services;

import com.ucb.market.dto.*;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.Cart;
import com.ucb.market.model.Client;
import com.ucb.market.model.Seller;
import com.ucb.market.model.User;
import com.ucb.market.repository.CartRepository;
import com.ucb.market.repository.ClientRepository;
import com.ucb.market.repository.SellerRepository;
import com.ucb.market.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterUserRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw new BusinessException("E-mail já cadastrado");
        if (userRepository.existsByUsername(req.username()))
            throw new BusinessException("Nome de usuário já cadastrado");

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .filter(u -> passwordEncoder.matches(req.password(), u.getPassword()))
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos"));
        return UserResponse.from(user);
    }
    public boolean isSeller(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException("Usuário não encontrado: " + userId);
        return sellerRepository.existsById(userId);
    }

    @Transactional
    public void becomeSeller(Integer userId, SellerRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));
        if (sellerRepository.existsById(userId))
            throw new BusinessException("Usuário já é vendedor");
        if (sellerRepository.existsByCnpj(req.cnpj()))
            throw new BusinessException("CNPJ já cadastrado");

        Seller seller = new Seller();
        seller.setUser(user);
        seller.setCnpj(req.cnpj());
        sellerRepository.save(seller);
    }

    @Transactional
    public void becomeClient(Integer userId, ClientRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userId));
        if (clientRepository.existsById(userId))
            throw new BusinessException("Usuário já é cliente");

        Client client = new Client();
        client.setUser(user);
        client.setPhone(req.phone());
        clientRepository.save(client);
        Cart cart = new Cart();
        cart.setClient(client);
        cartRepository.save(cart);
    }

}