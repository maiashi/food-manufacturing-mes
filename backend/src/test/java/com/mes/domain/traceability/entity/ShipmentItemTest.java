package com.mes.domain.traceability.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentItemTest {

    @Test
    void constructor_allFields() {
        UUID itemId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID shipmentId = UUID.randomUUID();
        UUID lotId = UUID.randomUUID();
        BigDecimal qty = new BigDecimal("50.5");
        LocalDateTime shippedDate = LocalDateTime.now().minusDays(2);

        var item = new ShipmentItem(itemId, factoryId, shipmentId, lotId, qty, shippedDate);

        assertEquals(itemId, item.getItemId());
        assertEquals(factoryId, item.getFactoryId());
        assertEquals(shipmentId, item.getShipmentId());
        assertEquals(lotId, item.getLotId());
        assertEquals(qty, item.getQuantity());
        assertEquals(shippedDate, item.getShippedDate());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID oldId = UUID.randomUUID();
        var item = new ShipmentItem(oldId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), new BigDecimal("10"), LocalDateTime.now().minusDays(1));

        UUID newId = UUID.randomUUID();
        var copy = item.copy(newId);

        assertEquals(newId, copy.getItemId());
        assertNotEquals(oldId, copy.getItemId());
        assertEquals(item.getShipmentId(), copy.getShipmentId());
        assertEquals(item.getQuantity(), copy.getQuantity());
    }
}
