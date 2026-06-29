package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();

        Shelf shelf = new Shelf(id, zoneId, "SHELF-A01", new BigDecimal("50"),
                new BigDecimal("200"), true);

        assertEquals(id, shelf.getId());
        assertEquals(id, shelf.getShelfId());
        assertEquals(zoneId, shelf.getZoneId());
        assertEquals("SHELF-A01", shelf.getShelfCode());
        assertEquals(new BigDecimal("50"), shelf.getWeightLimitKg());
        assertEquals(new BigDecimal("200"), shelf.getVolumeLimitL());
        assertTrue(shelf.getIsActive());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        Shelf original = new Shelf(originalId, zoneId, "SHELF-COPY", new BigDecimal("30"),
                new BigDecimal("150"), false);
        UUID newId = UUID.randomUUID();

        Shelf copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getId());
        assertEquals(zoneId, copied.getZoneId());
        assertEquals("SHELF-COPY", copied.getShelfCode());
        assertFalse(copied.getIsActive());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Shelf shelf = new Shelf(UUID.randomUUID(), UUID.randomUUID(), "SHELF-INIT",
                new BigDecimal("10"), new BigDecimal("50"), true);

        // Update shelfCode
        shelf.setShelfCode("SHELF-UPDATED");
        assertEquals("SHELF-UPDATED", shelf.getShelfCode());

        // Update weightLimitKg
        shelf.setWeightLimitKg(new BigDecimal("80"));
        assertEquals(new BigDecimal("80"), shelf.getWeightLimitKg());

        // Update volumeLimitL
        shelf.setVolumeLimitL(new BigDecimal("300"));
        assertEquals(new BigDecimal("300"), shelf.getVolumeLimitL());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        Shelf a = new Shelf(sameId, UUID.randomUUID(), "SHELF-A",
                new BigDecimal("50"), new BigDecimal("200"), true);
        Shelf b = new Shelf(sameId, UUID.randomUUID(), "SHELF-B",
                new BigDecimal("100"), new BigDecimal("400"), false);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
