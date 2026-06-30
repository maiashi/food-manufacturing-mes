package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();

        Warehouse wh = new Warehouse(id, factoryId, "WH-TOKYO", "Tokyo Warehouse",
                "Cold", new BigDecimal("1000"), new BigDecimal("5000"), "原材料倉庫");

        assertEquals(id, wh.getId());
        assertEquals(id, wh.getWarehouseId());
        assertEquals("WH-TOKYO", wh.getWarehouseCode());
        assertEquals("Tokyo Warehouse", wh.getWarehouseName());
        assertEquals("Cold", wh.getTemperatureZone());
    }

    @Test
    void copy_createsNewInstance() {
        UUID originalId = UUID.randomUUID();
        UUID copiedId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();

        Warehouse original = new Warehouse(originalId, factoryId, "WH-01", "Main", "Ambient",
                new BigDecimal("1000"), new BigDecimal("5000"), "完成品倉庫");
        Warehouse copied = original.copy(copiedId);

        assertNotSame(original, copied);
        assertEquals(copiedId, copied.getWarehouseId());
        assertEquals("WH-01", copied.getWarehouseCode());
    }
}
