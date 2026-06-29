package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockTransactionTest {

    @Test
    void constructor_initializesAllFields() {
        UUID lotId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        var tx = new StockTransaction(
                lotId, factoryId,
                com.mes.domain.inventory.enums.StockTransactionType.RECEIPT,
                new BigDecimal("100"), new BigDecimal("500"),
                "PO-2025-001", userId
        );

        assertNull(tx.getId()); // auto-increment PK not set until persisted
        assertEquals(lotId, tx.getLotId());
        assertEquals(factoryId, tx.getFactoryId());
        assertEquals(com.mes.domain.inventory.enums.StockTransactionType.RECEIPT, tx.getType());
        assertEquals(new BigDecimal("100"), tx.getQuantityDelta());
        assertEquals(new BigDecimal("500"), tx.getRemainingQty());
        assertEquals("PO-2025-001", tx.getReferenceDocNo());
    }

    @Test
    void constructorSetsTransactionIdToNull() {
        var tx = new StockTransaction(
                UUID.randomUUID(), UUID.randomUUID(),
                com.mes.domain.inventory.enums.StockTransactionType.SHIPMENT,
                new BigDecimal("10"), new BigDecimal("490"),
                "SO-2025-001", UUID.randomUUID()
        );

        assertNull(tx.getTransactionId());
    }
}
