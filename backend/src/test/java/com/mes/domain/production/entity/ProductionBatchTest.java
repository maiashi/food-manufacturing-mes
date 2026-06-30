package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductionBatchTest {

    private static final UUID TEST_FACTORY = UUID.randomUUID();
    private static final UUID TEST_LINE = UUID.randomUUID();

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 1, 8, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 1, 14, 30);

        ProductionBatch batch = new ProductionBatch(
                id, TEST_FACTORY, TEST_LINE, "BATCH-2025-001",
                UUID.randomUUID(), startDate, endDate,
                new BigDecimal("100.5"), new BigDecimal("98.2"),
                new BigDecimal("0.977"), new BigDecimal("1.0"), new BigDecimal("0.3"));

        assertEquals(id, batch.getId());
        assertEquals(id, batch.getBatchId());
        assertEquals(TEST_FACTORY, batch.getFactoryId());
        assertEquals(TEST_LINE, batch.getLineId());
        assertEquals("BATCH-2025-001", batch.getBatchNumber());
        assertNotNull(batch.getProductionOrderId());
        assertEquals(startDate, batch.getStartDate());
        assertEquals(endDate, batch.getEndDate());
        assertEquals(new BigDecimal("100.5"), batch.getInputQty());
        assertEquals(new BigDecimal("98.2"), batch.getOutputQty());
        assertEquals(new BigDecimal("0.977"), batch.getYieldRate());
        assertEquals(new BigDecimal("1.0"), batch.getScrapQty());
        assertEquals(new BigDecimal("0.3"), batch.getWasteQty());
    }

    @Test
    void copy_returnsNewBatchWithDifferentId() {
        ProductionBatch original = new ProductionBatch(
                UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "BATCH-COPY-01",
                UUID.randomUUID(), LocalDateTime.now(), null,
                BigDecimal.TEN, new BigDecimal("9"), new BigDecimal("0.9"), BigDecimal.ONE, BigDecimal.ZERO);
        UUID newId = UUID.randomUUID();

        ProductionBatch copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals("BATCH-COPY-01", copied.getBatchNumber());
    }

    @Test
    void copy_preservesNullEndDate() {
        ProductionBatch original = new ProductionBatch(
                UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "BATCH-RUNNING",
                UUID.randomUUID(), LocalDateTime.now(), null,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO);

        ProductionBatch copied = original.copy(UUID.randomUUID());

        assertNull(copied.getEndDate());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProductionBatch batch = new ProductionBatch(
                UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "BATCH-UPDATE",
                UUID.randomUUID(), LocalDateTime.now(), null,
                BigDecimal.TEN, BigDecimal.ZERO, null, BigDecimal.ONE, BigDecimal.ZERO);

        // Update output quantity and yield rate
        batch.setOutputQty(new BigDecimal("9.5"));
        assertEquals(new BigDecimal("9.5"), batch.getOutputQty());

        batch.setYieldRate(new BigDecimal("0.95"));
        assertEquals(new BigDecimal("0.95"), batch.getYieldRate());
        
        // Update scrap and waste quantities
        batch.setScrapQty(new BigDecimal("0.5"));
        assertEquals(new BigDecimal("0.5"), batch.getScrapQty());
        
        batch.setWasteQty(new BigDecimal("0.2"));
        assertEquals(new BigDecimal("0.2"), batch.getWasteQty());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        ProductionBatch a = new ProductionBatch(
                sameId, TEST_FACTORY, TEST_LINE, "BATCH-A", UUID.randomUUID(),
                LocalDateTime.now(), null, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO);
        ProductionBatch b = new ProductionBatch(
                sameId, UUID.randomUUID(), UUID.randomUUID(), "BATCH-B", UUID.randomUUID(),
                LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2026, 1, 2, 0, 0),
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void allBatchStatuses_valid() {
        ProductionBatch[] batches = new ProductionBatch[] {
            new ProductionBatch(UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "B1", UUID.randomUUID(),
                LocalDateTime.now(), null, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO),
            new ProductionBatch(UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "B2", UUID.randomUUID(),
                LocalDateTime.now(), null, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO),
            new ProductionBatch(UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "B3", UUID.randomUUID(),
                LocalDateTime.now(), null, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO),
            new ProductionBatch(UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "B4", UUID.randomUUID(),
                LocalDateTime.now(), LocalDateTime.now(), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO),
            new ProductionBatch(UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "B5", UUID.randomUUID(),
                LocalDateTime.now(), null, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO)
        };

        for (int i = 0; i < batches.length; i++) {
            assertNotNull(batches[i]);
        }
    }

    @Test
    void copyReturnsCorrectType() {
        UUID newId = UUID.randomUUID();
        BaseEntity base = buildValid().copy(newId);

        assertTrue(base instanceof ProductionBatch);
        assertEquals(newId, base.getId());
    }

    private ProductionBatch buildValid() {
        return new ProductionBatch(
                UUID.randomUUID(), TEST_FACTORY, TEST_LINE, "BATCH-VALID",
                UUID.randomUUID(), LocalDateTime.now(), null,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO);
    }
}