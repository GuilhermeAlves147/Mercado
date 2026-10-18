package com.ucb.market.services;

import com.ucb.market.dto.OrderResponse;
import com.ucb.market.exception.BusinessException;
import com.ucb.market.exception.ResourceNotFoundException;
import com.ucb.market.model.*;
import com.ucb.market.repository.CartRepository;
import com.ucb.market.repository.ProductRepository;
import com.ucb.market.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PurchaseOrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse checkout(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado: " + cartId));
        if (cart.getItems().isEmpty())
            throw new BusinessException("O carrinho está vazio");

        PurchaseOrder order = new PurchaseOrder();
        order.setClient(cart.getClient());
        order.setDateOrder(LocalDateTime.now());

        for (BuyItem bi : cart.getItems()) {
            int updated = productRepository.decreaseStock(bi.getProduct().getId(), bi.getQuantity());
            if (updated == 0)
                throw new BusinessException("Estoque insuficiente para " + bi.getProduct().getName());
            order.addItem(new OrderItem(order, bi.getProduct(), bi.getQuantity(), bi.getUnitValue()));
        }

        order.recalculateTotal();
        order.addStatus(OrderStatusType.ABERTO);
        orderRepository.save(order);

        cart.getItems().clear();
        cart.recalculate();
        return OrderResponse.from(order);
    }   // <- esta chave era a que faltava

    private static final Map<OrderStatusType, Set<OrderStatusType>> ALLOWED = Map.of(
            OrderStatusType.ABERTO,    Set.of(OrderStatusType.PAGO, OrderStatusType.CANCELADO),
            OrderStatusType.PAGO,      Set.of(OrderStatusType.ENVIADO, OrderStatusType.CANCELADO),
            OrderStatusType.ENVIADO,   Set.of(OrderStatusType.ENTREGUE),
            OrderStatusType.ENTREGUE,  Set.of(),
            OrderStatusType.CANCELADO, Set.of());

    @Transactional
    public OrderResponse changeStatus(Integer orderId, OrderStatusType newStatus) {
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + orderId));

        OrderStatusType current = order.currentStatus();
        if (!ALLOWED.get(current).contains(newStatus))
            throw new BusinessException("Não é possível ir de " + current + " para " + newStatus);

        if (newStatus == OrderStatusType.CANCELADO) {
            order.getItems().forEach(i ->
                    productRepository.increaseStock(i.getProduct().getId(), i.getQuantity()));
        }
        order.addStatus(newStatus);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Integer id) {
        return orderRepository.findById(id).map(OrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listByClient(Integer clientId) {
        return orderRepository.findByClientIdOrderByDateOrderDesc(clientId)
                .stream().map(OrderResponse::from).toList();
    }
}