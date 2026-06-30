package com.mes.domain.traceability.entity;

import com.mes.domain.traceability.enums.ShipmentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentOrderTest {

    @Test
    void constructor_allFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        var order = new ShipmentOrder(
                id, factoryId, "SH-2025-001", UUID.randomUUID(),
                "123 Main St", now, ShipmentStatus.PENDING);

        assertEquals(id, order.getId());
        assertEquals(order.getShipmentId(), id);
        assertEquals(order.getFactoryId(), factoryId);
        assertEquals(order.getShipmentNumber(), "SH-2025-001");
        assertEquals(order.getStatus(), ShipmentStatus.PENDING);
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID oldId = UUID.randomUUID();
        var order = new ShipmentOrder(
                oldId, UUID.randomUUID(), "SH-001", UUID.randomUUID(),
                "Address", LocalDateTime.now(), ShipmentStatus.PENDING);

        UUID newId = UUID.randomUUID();
        var copy = order.copy(newId);

        assertNotSame(order, copy);
        assertEquals(copy.getShipmentId(), newId);
        assertNotEquals(copy.getShipmentId(), oldId);
        assertEquals(copy.getShipmentNumber(), "SH-001");
    }

    @Test
    void copyPreservesAllFields() {
        UUID origId = UUID.randomUUID();
        var orig = new ShipmentOrder(
                origId, UUID.randomUUID(), "SH-2025-042", UUID.randomUUID(),
                "456 Oak Ave", LocalDateTime.now().minusDays(3),
                ShipmentStatus.SHIPPED);

        var copy = orig.copy(UUID.randomUUID());

        assertEquals(orig.getFactoryId(), copy.getFactoryId());
        assertEquals(orig.getShipmentNumber(), copy.getShipmentNumber());
        assertEquals(orig.getCustomerId(), copy.getCustomerId());
        assertEquals(orig.getDeliveryAddress(), copy.getDeliveryAddress());
        assertEquals(orig.getShipmentDate(), copy.getShipmentDate());
        assertEquals(orig.getStatus(), copy.getStatus());
    }

    @Test
    void setters() {
        LocalDateTime shipDate = LocalDateTime.now().plusDays(5);
        var order = new ShipmentOrder(UUID.randomUUID(), UUID.randomUUID(), "SH-INIT",
                UUID.randomUUID(), "Initial Address", LocalDateTime.now(), ShipmentStatus.PENDING);

        order.setShipmentNumber("SH-NEW");
        order.setDeliveryAddress("Updated Address");
        order.setShipmentDate(shipDate);
        order.setStatus(ShipmentStatus.CANCELLED);

        assertEquals("SH-NEW", order.getShipmentNumber());
        assertEquals(ShipmentStatus.CANCELLED, order.getStatus());
        assertEquals(shipDate, order.getShipmentDate());
    }
}