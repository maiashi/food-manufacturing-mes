package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();

        Warehouse wh = new Warehouse(id, UUID.randomUUID(), "WH-TOKYO", "Tokyo Warehouse", "Cold");

        assertEquals(id, wh.getId());
        assertEquals(id, wh.getWarehouseId());
        assertEquals("WH-TOKYO", wh.getWhCode());
        assertEquals("Tokyo Warehouse", wh.getWhName());
        assertEquals("Cold", wh.getTemperatureZone());
    }

    @Test
    void copy_createsNewInstance() {
        Warehouse original = new Warehouse(UUID.randomUUID(), UUID.randomUUID(), "WH-01", "Main", "Ambient");
        Warehouse copied = original.copy(UUID.randomUUID());

        assertNotSame(original, copied);
        assertEquals("WH-01", copied.getWhCode());
    }
}
