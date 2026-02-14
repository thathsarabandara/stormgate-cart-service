package com.example.stormgate_cart_service.repository;

import com.example.stormgate_cart_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for Cart entity.
 * Provides database access methods for cart operations.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    /**
     * Finds an active cart by tenant and user identifiers.
     *
     * @param tenantId the tenant identifier
     * @param userId the user identifier
     * @return optional containing the cart if found
     */
    @Query("SELECT c FROM Cart c WHERE c.tenantId = :tenantId "
            + "AND c.userId = :userId AND c.isDeleted = false")
    Optional<Cart> findByTenantIdAndUserId(
            @Param("tenantId") String tenantId,
            @Param("userId") String userId);

    /**
     * Checks if an active cart exists for the given tenant and user.
     *
     * @param tenantId the tenant identifier
     * @param userId the user identifier
     * @return true if cart exists, false otherwise
     */
    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.tenantId = :tenantId "
            + "AND c.userId = :userId AND c.isDeleted = false")
    boolean existsByTenantIdAndUserId(
            @Param("tenantId") String tenantId,
            @Param("userId") String userId);
}
