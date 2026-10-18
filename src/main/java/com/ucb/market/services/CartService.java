package com.ucb.market.services;

import com.ucb.market.dto.AddCartItemRequest;
import com.ucb.market.dto.CartResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.BuyItem;
import com.ucb.market.model.Cart;
import com.ucb.market.model.Product;
import com.ucb.market.repository.CartRepository;
import com.ucb.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse addItem(Integer cartId, AddCartItemRequest req) {
        Cart cart = findCart(cartId);
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + req.productId()));

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

    @Transactional(readOnly = true)
    public CartResponse getByClient(Integer clientId) {
        Cart cart = cartRepository.findFirstByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o cliente " + clientId));
        return CartResponse.from(cart);
    }

    private Cart findCart(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado: " + cartId));
    }
}