package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.production.enums.ProductionOrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductionOrderTest {

    private static final UUID TEST_FACTORY = UUID.randomUUID();
    private static final UUID TEST_PRODUCT = UUID.randomUUID();
    private static final UUID TEST_LINE = UUID.randomUUID();
    private static final LocalDateTime PLANNED_START = LocalDateTime.of(2025, 6, 1, 8, 0);

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductionOrder order = new ProductionOrder(
                id, TEST_FACTORY, "PO-2025-001", TEST_PRODUCT,
                TEST_LINE, UUID.randomUUID(), PLANNED_START,
                null, null, new BigDecimal("500"),
                ProductionOrderStatus.PLANNED);

        assertEquals(id, order.getId());
        assertEquals(id, order.getProductionOrderId());
        assertEquals(TEST_FACTORY, order.getFactoryId());
        assertEquals("PO-2025-001", order.getOrderNumber());
        assertEquals(TEST_PRODUCT, order.getProductId());
        assertEquals(TEST_LINE, order.getLineId());
        assertNotNull(order.getWarehouseId());
        assertEquals(PLANNED_START, order.getPlannedStartDate());
        assertNull(order.getActualStartDate());
        assertNull(order.getCompletedDate());
        assertEquals(new BigDecimal("500"), order.getTotalQty());
        assertEquals(ProductionOrderStatus.PLANNED, order.getStatus());
    }

    @Test
    void copy_returnsNewOrderWithDifferentId() {
        ProductionOrder original = new ProductionOrder(
                UUID.randomUUID(), TEST_FACTORY, "PO-BATCH-01", TEST_PRODUCT,
                TEST_LINE, UUID.randomUUID(), PLANNED_START,
                PLANNED_START.plusHours(2), PLANNED_START.plusDays(3),
                new BigDecimal("1000"), ProductionOrderStatus.COMPLETED);
        UUID newId = UUID.randomUUID();

        ProductionOrder copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals("PO-BATCH-01", copied.getOrderNumber());
        assertEquals(ProductionOrderStatus.COMPLETED, copied.getStatus());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProductionOrder order = new ProductionOrder(
                UUID.randomUUID(), TEST_FACTORY, "PO-INIT", TEST_PRODUCT,
                TEST_LINE, UUID.randomUUID(), PLANNED_START,
                null, null, BigDecimal.TEN, ProductionOrderStatus.PLANNED);

        // Test basic setters
        LocalDateTime actualStart = LocalDateTime.of(2025, 6, 1, 8, 30);
        order.setActualStartDate(actualStart);
        assertEquals(actualStart, order.getActualStartDate());

        // Test status transition
        order.setStatus(ProductionOrderStatus.RELEASED);
        assertEquals(ProductionOrderStatus.RELEASED, order.getStatus());

        // Test quantity update
        order.setTotalQty(new BigDecimal("750"));
        assertEquals(new BigDecimal("750"), order.getTotalQty());
    }

    @Test
    void copy_preservesNullFields() {
        ProductionOrder original = new ProductionOrder(
                UUID.randomUUID(), TEST_FACTORY, "PO-NULL-FIELDS", TEST_PRODUCT,
                TEST_LINE, null, PLANNED_START,
                null, null, BigDecimal.ZERO, ProductionOrderStatus.PLANNED);

        ProductionOrder copied = original.copy(UUID.randomUUID());

        assertNull(copied.getWarehouseId());
        assertNull(copied.getActualStartDate());
        assertNull(copied.getCompletedDate());
        assertEquals(BigDecimal.ZERO, copied.getTotalQty());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        ProductionOrder a = new ProductionOrder(
                sameId, TEST_FACTORY, "PO-A", TEST_PRODUCT, TEST_LINE,
                null, PLANNED_START, null, null, BigDecimal.TEN, ProductionOrderStatus.PLANNED);
        ProductionOrder b = new ProductionOrder(
                sameId, UUID.randomUUID(), "PO-B", UUID.randomUUID(), UUID.randomUUID(),
                null, LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 5, 0, 0), LocalDateTime.of(2026, 1, 3, 0, 0),
                new BigDecimal("9999"), ProductionOrderStatus.COMPLETED);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void touchUpdatesTimestamp() {
        LocalDateTime original = LocalDateTime.of(2025, 1, 1, 0, 0);
        TestOrder order = new TestOrder(UUID.randomUUID(), "test");

        // Verify initial updatedAt matches createdAt
        assertEquals(order.getCreatedAt(), order.getUpdatedAt());

        try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        order.touch();
        assertTrue(order.getUpdatedAt().isAfter(original));
    }

    @Test
    void deleteMarksSoftDeleted() {
        TestOrder order = new TestOrder(UUID.randomUUID(), "test");
        assertFalse(order.isDeleted());

        order.delete();
        assertTrue(order.isDeleted());
        assertTrue(order.getUpdatedAt().isAfter(order.getCreatedAt()));
    }

    /** Concrete test subclass for protected method testing. */
    static class TestOrder extends ProductionOrder {
        public TestOrder(UUID id, String orderNumber) {
            super(id, TEST_FACTORY, orderNumber, TEST_PRODUCT,
                    TEST_LINE, null, PLANNED_START, null, null, BigDecimal.TEN,
                    ProductionOrderStatus.PLANNED);
        }
    }

    @Test
    void copyReturnsCorrectType() {
        UUID newId = UUID.randomUUID();
        BaseEntity base = buildValid().copy(newId);

        assertTrue(base instanceof ProductionOrder);
        assertEquals(newId, base.getId());
    }

    private ProductionOrder buildValid() {
        return new ProductionOrder(
                UUID.randomUUID(), TEST_FACTORY, "PO-VALID", TEST_PRODUCT,
                TEST_LINE, UUID.randomUUID(), PLANNED_START,
                null, null, new BigDecimal("100"),
                ProductionOrderStatus.PLANNED);
    }
}