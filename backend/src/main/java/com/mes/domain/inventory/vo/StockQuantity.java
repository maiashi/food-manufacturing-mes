package com.mes.domain.inventory.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable value object representing a stock quantity.
 * All quantities are validated to be non-negative at construction.
 */
public final class StockQuantity {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int SCALE = 6;

    private final BigDecimal amount;

    public StockQuantity(BigDecimal amount) {
        if (amount == null || amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Stock quantity must be non-negative, got: " + amount);
        }
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public StockQuantity add(StockQuantity other) {
        if (other == null) return this;
        return new StockQuantity(this.amount.add(other.amount));
    }

    public StockQuantity subtract(StockQuantity other) {
        if (other == null) return this;
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Resulting quantity would be negative: " + result);
        }
        return new StockQuantity(result);
    }

    public boolean isEqualToZero() {
        return this.amount.compareTo(ZERO) == 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(ZERO) > 0;
    }

    public BigDecimal toBigDecimal() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockQuantity that = (StockQuantity) o;
        return amount.compareTo(that.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "StockQuantity{" + amount + "}";
    }
}
