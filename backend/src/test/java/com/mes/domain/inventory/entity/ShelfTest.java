package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();

        Shelf shelf = new Shelf(id, factoryId, zoneId, "SHELF-A01", new BigDecimal("50"),
                new BigDecimal("200"), "使用中");

        assertEquals(id, shelf.getId());
        assertEquals(id, shelf.getShelfId());
        assertEquals(factoryId, shelf.getFactoryId());
        assertEquals(zoneId, shelf.getZoneId());
        assertEquals("SHELF-A01", shelf.getShelfCode());
        assertEquals(new BigDecimal("50"), shelf.getMaxWeight());
        assertEquals(new BigDecimal("200"), shelf.getMaxVolume());
        assertEquals("使用中", shelf.getStatus());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        Shelf original = new Shelf(originalId, factoryId, zoneId, "SHELF-COPY", new BigDecimal("30"),
                new BigDecimal("150"), "使用中");
        UUID newId = UUID.randomUUID();

        Shelf copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getShelfId());
        assertEquals(factoryId, copied.getFactoryId());
        assertEquals(zoneId, copied.getZoneId());
        assertEquals("SHELF-COPY", copied.getShelfCode());
        assertEquals("使用中", copied.getStatus());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Shelf shelf = new Shelf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "SHELF-INIT",
                new BigDecimal("10"), new BigDecimal("50"), "使用中");

        // Update shelfCode
        shelf.setShelfCode("SHELF-UPDATED");
        assertEquals("SHELF-UPDATED", shelf.getShelfCode());

        // Update maxWeight
        shelf.setMaxWeight(new BigDecimal("80"));
        assertEquals(new BigDecimal("80"), shelf.getMaxWeight());

        // Update maxVolume
        shelf.setMaxVolume(new BigDecimal("300"));
        assertEquals(new BigDecimal("300"), shelf.getMaxVolume());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        Shelf a = new Shelf(sameId, factoryId, UUID.randomUUID(), "SHELF-A",
                new BigDecimal("50"), new BigDecimal("200"), "使用中");
        Shelf b = new Shelf(sameId, factoryId, UUID.randomUUID(), "SHELF-B",
                new BigDecimal("100"), new BigDecimal("400"), "点検中");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
