package com.ucb.market.services;

import com.ucb.market.dto.AddCartItemRequest;
import com.ucb.market.dto.CartResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.BuyItem;
import com.ucb.market.model.Cart;
import com.ucb.market.model.Client;
import com.ucb.market.model.User;
import com.ucb.market.model.Product;
import com.ucb.market.repository.CartRepository;
import com.ucb.market.repository.ClientRepository;
import com.ucb.market.repository.UserRepository;
import com.ucb.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse addItem(Integer cartId, AddCartItemRequest req) {
        Cart cart = findCart(cartId);
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + req.productId()));

        // O mesmo usuário pode ser cliente e vendedor, mas não pode comprar seu próprio produto.
        if (product.getSeller().getId().equals(cart.getClient().getId())) {
            throw new BusinessException("Você não pode adicionar seu próprio produto ao carrinho.");
        }
        if (req.quantity() == null || req.quantity() <= 0) {
            throw new BusinessException("A quantidade deve ser maior que zero.");
        }

        BuyItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(null);

        int newQuantity = (item == null ? 0 : item.getQuantity()) + req.quantity();
        if (product.getQuantity() < newQuantity)
            throw new BusinessException("Estoque insuficiente para " + product.getName());

        if (item == null) {
            cart.getItems().add(new BuyItem(cart, product, req.quantity(), product.getValue()));
        } else {
            item.setQuantity(newQuantity);
        }
        cart.recalculate();
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeItem(Integer cartId, Integer productId) {
        Cart cart = findCart(cartId);
        boolean removed = cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        if (!removed) throw new ResourceNotFoundException("Produto não está no carrinho");
        cart.recalculate();
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse getByClient(Integer clientId) {
        // Contas antigas podem existir apenas em tb_user, sem tb_client ou tb_cart.
        Client client = clientRepository.findById(clientId).orElseGet(() -> {
            User user = userRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + clientId));
            Client newClient = new Client();
            newClient.setUser(user);
            return clientRepository.saveAndFlush(newClient);
        });
        Cart cart = cartRepository.findFirstByClientIdOrderByIdDesc(clientId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setClient(client);
            return cartRepository.saveAndFlush(newCart);
        });
        return CartResponse.from(cart);
    }

    private Cart findCart(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado: " + cartId));
    }
}