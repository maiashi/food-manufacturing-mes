package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ZoneTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        Zone zone = new Zone(id, warehouseId, "ZONE-COLD", "Cold Storage", new BigDecimal("4.0"));

        assertEquals(id, zone.getId());
        assertEquals(id, zone.getZoneId());
        assertEquals(warehouseId, zone.getWarehouseId());
        assertEquals("ZONE-COLD", zone.getZoneCode());
        assertEquals("Cold Storage", zone.getZoneName());
        assertEquals(new BigDecimal("4.0"), zone.getTemperatureOverride());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        Zone original = new Zone(originalId, warehouseId, "ZONE-COPY", "Copy Zone", new BigDecimal("-18.0"));
        UUID newId = UUID.randomUUID();

        Zone copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getId());
        assertEquals(warehouseId, copied.getWarehouseId());
        assertEquals("Copy Zone", copied.getZoneName());
        assertEquals(new BigDecimal("-18.0"), copied.getTemperatureOverride());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Zone zone = new Zone(UUID.randomUUID(), UUID.randomUUID(), "ZONE-INIT",
                "Initial Zone", new BigDecimal("2.5"));

        // Update zoneCode
        zone.setZoneCode("ZONE-UPDATED");
        assertEquals("ZONE-UPDATED", zone.getZoneCode());

        // Update zoneName
        zone.setZoneName("Updated Zone Name");
        assertEquals("Updated Zone Name", zone.getZoneName());

        // Update temperatureOverride
        zone.setTemperatureOverride(new BigDecimal("-20.0"));
        assertEquals(new BigDecimal("-20.0"), zone.getTemperatureOverride());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        Zone a = new Zone(sameId, UUID.randomUUID(), "ZONE-A", "Zone A", new BigDecimal("4.0"));
        Zone b = new Zone(sameId, UUID.randomUUID(), "ZONE-B", "Zone B", null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
