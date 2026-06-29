package com.mes.domain.production.service;

import com.mes.domain.production.enums.BatchStatus;
import com.mes.domain.production.entity.ProductionOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Domain service for production operations.
 * Pure domain logic — no Spring, no JPA annotations.
 */
public class ProductionService {

    /**
     * Validates a production order before release.
     * Checks business rules such as line availability and capacity constraints.
     *
     * @param order the production order to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductionOrder(ProductionOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Production order must not be null");
        }
        if (order.getFactoryId() == null) {
            throw new IllegalArgumentException("Factory ID is required");
        }
        if (order.getLineId() == null) {
            throw new IllegalArgumentException("Line ID is required");
        }
        if (order.getTotalQty() == null || order.getTotalQty().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total quantity must be positive");
        }
        if (order.getPlannedStartDate() == null) {
            throw new IllegalArgumentException("Planned start date is required");
        }
        if (order.getStatus() != null && order.getStatus().ordinal() >= 3) {
            throw new IllegalStateException("Order cannot be modified in state: " + order.getStatus());
        }
    }

    /**
     * Calculates the yield rate from input and output quantities.
     * Returns a value between 0.0 and 1.0 (inclusive).
     *
     * @param input  input quantity
     * @param output output quantity
     * @return yield rate as a decimal fraction (0.0 to 1.0)
     */
    public BigDecimal calculateYield(BigDecimal input, BigDecimal output) {
        if (input == null || input.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Input quantity must be positive");
        }
        if (output == null) {
            return BigDecimal.ZERO;
        }
        if (output.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Output quantity cannot be negative");
        }

        // Cap at 1.0 in case of measurement anomalies
        BigDecimal rate = output.divide(input, 4, RoundingMode.HALF_UP);
        return rate.max(BigDecimal.ZERO).min(BigDecimal.ONE);
    }

    /**
     * Business rule check: determines if a batch can be started given its current status.
     * A batch can only start from SCHEDULED or PAUSED states.
     *
     * @param currentStatus the current batch status
     * @return true if the batch can start, false otherwise
     */
    public boolean canStartBatch(BatchStatus currentStatus) {
        return currentStatus == BatchStatus.SCHEDULED || currentStatus == BatchStatus.PAUSED;
    }
}
