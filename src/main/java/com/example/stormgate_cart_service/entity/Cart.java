package com.example.stormgate_cart_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cart entity representing a shopping cart for a tenant-user pair.
 * Manages cart state and cart items with soft delete support.
 */
@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    /**
     * Unique identifier for the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartId;

    /**
     * Tenant identifier that owns this cart.
     */
    @Column(nullable = false)
    private String tenantId;

    /**
     * User identifier who owns this cart.
     */
    @Column(nullable = false)
    private String userId;

    /**
     * List of items in the cart.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /**
     * Total amount for all items in the cart.
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Currency code for the cart.
     */
    @Column(nullable = false)
    @Builder.Default
    private String currency = "USD";

    /**
     * Soft delete flag for the cart.
     */
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    /**
     * Timestamp when the cart was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when the cart was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Sets creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the update timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calculates the total number of items in the cart.
     *
     * @return total quantity of all non-deleted items
     */
    public int getItemCount() {
        return items.stream()
                .filter(item -> !item.getIsDeleted())
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Gets the items list.
     * Returns the actual list without defensive copying to allow JPA and tests to modify it.
     * Defensive copying is applied only during construction and assignment.
     *
     * @return items list
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Sets items with defensive copying.
     *
     * @param items the items to set
     */
    public void setItems(final List<CartItem> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    /**
     * Calculates the total amount for all items in the cart.
     *
     * @return sum of subtotals for all non-deleted items
     */
    public BigDecimal calculateTotal() {
        return items.stream()
                .filter(item -> !item.getIsDeleted())
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
