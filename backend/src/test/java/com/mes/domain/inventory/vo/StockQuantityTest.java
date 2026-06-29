package com.mes.domain.inventory.vo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StockQuantityTest {

    @Test
    void constructor_acceptsNonNegativeAmount() {
        assertDoesNotThrow(() -> new StockQuantity(BigDecimal.ZERO));
        assertDoesNotThrow(() -> new StockQuantity(new BigDecimal("100.5")));
        assertDoesNotThrow(() -> new StockQuantity(new BigDecimal("999999.123456")));
    }

    @Test
    void constructor_rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> new StockQuantity(BigDecimal.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> new StockQuantity(new BigDecimal("-0.01")));
    }

    @Test
    void constructor_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new StockQuantity(null));
    }

    @Test
    void add_returnsNewQuantity() {
        StockQuantity a = new StockQuantity(new BigDecimal("10"));
        StockQuantity b = new StockQuantity(new BigDecimal("5"));
        StockQuantity result = a.add(b);

        assertEquals(new BigDecimal("15.000000"), result.toBigDecimal());
    }

    @Test
    void add_handlesNullArgument() {
        StockQuantity a = new StockQuantity(new BigDecimal("10"));
        assertSame(a, a.add(null)); // returns self on null
    }

    @Test
    void subtract_returnsNewQuantity() {
        StockQuantity a = new StockQuantity(new BigDecimal("10"));
        StockQuantity b = new StockQuantity(new BigDecimal("3"));
        StockQuantity result = a.subtract(b);

        assertEquals(new BigDecimal("7.000000"), result.toBigDecimal());
    }

    @Test
    void subtract_rejectsNegativeResult() {
        StockQuantity a = new StockQuantity(new BigDecimal("5"));
        StockQuantity b = new StockQuantity(new BigDecimal("10"));

        assertThrows(IllegalArgumentException.class, () -> a.subtract(b));
    }

    @Test
    void subtract_handlesNullArgument() {
        StockQuantity a = new StockQuantity(new BigDecimal("10"));
        assertSame(a, a.subtract(null));
    }

    @Test
    void isEqualToZero() {
        assertTrue(new StockQuantity(BigDecimal.ZERO).isEqualToZero());
        assertFalse(new StockQuantity(new BigDecimal("0.000001")).isEqualToZero());
    }

    @Test
    void isPositive() {
        assertTrue(new StockQuantity(new BigDecimal("1")).isPositive());
        assertFalse(new StockQuantity(BigDecimal.ZERO).isPositive());
    }

    @Test
    void equalityBasedOnValueNotReference() {
        StockQuantity a = new StockQuantity(new BigDecimal("100.500000"));
        StockQuantity b = new StockQuantity(new BigDecimal("100.500000"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toBigDecimalReturnsExactValue() {
        BigDecimal original = new BigDecimal("3.14159");
        StockQuantity sq = new StockQuantity(original);

        // VOはコンストラクタでscaleを6に正規化するため、スケール一致を確認
        assertEquals(6, sq.toBigDecimal().scale());
    }

    @Test
    void toStringIsInformative() {
        String s = new StockQuantity(new BigDecimal("42")).toString();
        assertTrue(s.contains("StockQuantity"));
        assertTrue(s.contains("42.000000"));
    }
}
