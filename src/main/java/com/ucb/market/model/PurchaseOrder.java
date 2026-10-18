package com.ucb.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Integer id;

    @Column(name = "date_order", nullable = false)
    private LocalDateTime dateOrder;

    @Column(name = "totalvalue", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("id.dateStatus ASC")
    private List<OrderStatus> statuses = new ArrayList<>();
    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void recalculateTotal() {
        totalValue = items.stream()
                .map(i -> i.getUnitValue().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addStatus(OrderStatusType type) {
        statuses.add(new OrderStatus(this, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), type));
    }

    public OrderStatusType currentStatus() {
        return statuses.get(statuses.size() - 1).getStatus();
    }
}
