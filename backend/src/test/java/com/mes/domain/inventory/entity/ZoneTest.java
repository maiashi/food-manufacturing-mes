package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ZoneTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        Zone zone = new Zone(id, factoryId, warehouseId, "ZONE-COLD", "Cold Storage",
                "Cold", true, "原料Zone");

        assertEquals(id, zone.getId());
        assertEquals(id, zone.getZoneId());
        assertEquals(factoryId, zone.getFactoryId());
        assertEquals(warehouseId, zone.getWarehouseId());
        assertEquals("ZONE-COLD", zone.getZoneCode());
        assertEquals("Cold Storage", zone.getZoneName());
        assertEquals("Cold", zone.getTemperatureZone());
        assertTrue(zone.isInheritTemperatureFromWarehouse());
        assertEquals("原料Zone", zone.getZoneType());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        Zone original = new Zone(originalId, factoryId, warehouseId, "ZONE-COPY", "Copy Zone",
                "Ambient", false, "完成品Zone");
        UUID newId = UUID.randomUUID();

        Zone copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getZoneId());
        assertEquals(factoryId, copied.getFactoryId());
        assertEquals(warehouseId, copied.getWarehouseId());
        assertEquals("Copy Zone", copied.getZoneName());
        assertEquals("Ambient", copied.getTemperatureZone());
        assertFalse(copied.isInheritTemperatureFromWarehouse());
        assertEquals("完成品Zone", copied.getZoneType());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Zone zone = new Zone(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "ZONE-INIT",
                "Initial Zone", "Ambient", true, "調合Zone");

        // Update zoneCode
        zone.setZoneCode("ZONE-UPDATED");
        assertEquals("ZONE-UPDATED", zone.getZoneCode());

        // Update zoneName
        zone.setZoneName("Updated Zone Name");
        assertEquals("Updated Zone Name", zone.getZoneName());

        // Update temperatureZone
        zone.setTemperatureZone("Cold");
        assertEquals("Cold", zone.getTemperatureZone());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        Zone a = new Zone(sameId, factoryId, UUID.randomUUID(), "ZONE-A", "Zone A",
                "Cold", true, "原料Zone");
        Zone b = new Zone(sameId, factoryId, UUID.randomUUID(), "ZONE-B", "Zone B",
                "Ambient", false, "完成品Zone");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
