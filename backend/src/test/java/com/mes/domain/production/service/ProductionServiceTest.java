package com.mes.domain.production.service;

import com.mes.domain.production.enums.BatchStatus;
import com.mes.domain.production.entity.ProductionOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductionServiceTest {

    private ProductionService service;

    @BeforeEach
    void setUp() {
        service = new ProductionService();
    }

    // --- validateProductionOrder tests ---

    @Test
    void validateProductionOrder_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(null));
    }

    @Test
    void validateProductionOrder_validOrder_passes() {
        ProductionOrder order = buildValidOrder();
        assertDoesNotThrow(() -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_nullFactory_throws() {
        ProductionOrder order = buildValidOrder();
        order.setFactoryId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_nullLineId_throws() {
        ProductionOrder order = buildValidOrder();
        order.setLineId(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_zeroQty_throws() {
        ProductionOrder order = buildValidOrder();
        order.setTotalQty(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_negativeQty_throws() {
        ProductionOrder order = buildValidOrder();
        order.setTotalQty(BigDecimal.valueOf(-10));
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_nullStartDate_throws() {
        ProductionOrder order = buildValidOrder();
        order.setPlannedStartDate(null);
        assertThrows(IllegalArgumentException.class, () -> service.validateProductionOrder(order));
    }

    @Test
    void validateProductionOrder_completedState_throws() {
        ProductionOrder order = buildValidOrder();
        order.setStatus(com.mes.domain.production.enums.ProductionOrderStatus.COMPLETED);
        assertThrows(IllegalStateException.class, () -> service.validateProductionOrder(order));
    }

    // --- calculateYield tests ---

    @Test
    void calculateYield_normalCase_returnsCorrectRate() {
        BigDecimal yield = service.calculateYield(BigDecimal.valueOf(100), BigDecimal.valueOf(95));
        assertEquals(new BigDecimal("0.9500"), yield);
    }

    @Test
    void calculateYield_100Percent_yieldIsOne() {
        BigDecimal yield = service.calculateYield(BigDecimal.TEN, BigDecimal.TEN);
        assertEquals(new BigDecimal("1.0000"), yield);
    }

    @Test
    void calculateYield_zeroOutput_returnsZero() {
        BigDecimal yield = service.calculateYield(BigDecimal.valueOf(100), BigDecimal.ZERO);
        assertEquals(new BigDecimal("0.0000"), yield);
    }

    @Test
    void calculateYield_nullOutput_returnsZero() {
        BigDecimal yield = service.calculateYield(BigDecimal.valueOf(100), null);
        assertEquals(0, yield.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateYield_inputExceedsOutput_capsAtOne() {
        BigDecimal yield = service.calculateYield(BigDecimal.valueOf(100), BigDecimal.valueOf(150));
        assertEquals(0, yield.compareTo(BigDecimal.ONE));
    }

    @Test
    void calculateYield_nullInput_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateYield(null, BigDecimal.TEN));
    }

    @Test
    void calculateYield_zeroInput_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateYield(BigDecimal.ZERO, BigDecimal.TEN));
    }

    @Test
    void calculateYield_negativeOutput_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.calculateYield(BigDecimal.TEN, BigDecimal.valueOf(-1)));
    }

    // --- canStartBatch tests ---

    @Test
    void canStartBatch_scheduled_returnsTrue() {
        assertTrue(service.canStartBatch(BatchStatus.SCHEDULED));
    }

    @Test
    void canStartBatch_paused_returnsTrue() {
        assertTrue(service.canStartBatch(BatchStatus.PAUSED));
    }

    @Test
    void canStartBatch_active_returnsFalse() {
        assertFalse(service.canStartBatch(BatchStatus.ACTIVE));
    }

    @Test
    void canStartBatch_completed_returnsFalse() {
        assertFalse(service.canStartBatch(BatchStatus.COMPLETED));
    }

    @Test
    void canStartBatch_aborted_returnsFalse() {
        assertFalse(service.canStartBatch(BatchStatus.ABORTED));
    }

    // --- helpers ---

    private ProductionOrder buildValidOrder() {
        return new ProductionOrder(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "PO-2025-001",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now().plusDays(1),
                null,
                null,
                new BigDecimal("500"),
                com.mes.domain.production.enums.ProductionOrderStatus.PLANNED
        );
    }
}
