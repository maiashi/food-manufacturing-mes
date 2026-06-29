package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BinTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID shelfId = UUID.randomUUID();

        Bin bin = new Bin(id, shelfId, "BIN-A01", new BigDecimal("50"), true);

        assertEquals(id, bin.getId());
        assertEquals(id, bin.getBinId());
        assertEquals(shelfId, bin.getShelfId());
        assertEquals("BIN-A01", bin.getBinCode());
        assertEquals(new BigDecimal("50"), bin.getWeightCapacityKg());
        assertTrue(bin.getIsOccupied());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID shelfId = UUID.randomUUID();
        Bin original = new Bin(originalId, shelfId, "BIN-COPY", new BigDecimal("25"), false);
        UUID newId = UUID.randomUUID();

        Bin copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getId());
        assertEquals(shelfId, copied.getShelfId());
        assertEquals("BIN-COPY", copied.getBinCode());
        assertEquals(new BigDecimal("25"), copied.getWeightCapacityKg());
        assertFalse(copied.getIsOccupied());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Bin bin = new Bin(UUID.randomUUID(), UUID.randomUUID(), "BIN-INIT",
                new BigDecimal("10"), false);

        // Update binCode
        bin.setBinCode("BIN-UPDATED");
        assertEquals("BIN-UPDATED", bin.getBinCode());

        // Update weightCapacityKg
        bin.setWeightCapacityKg(new BigDecimal("75"));
        assertEquals(new BigDecimal("75"), bin.getWeightCapacityKg());

        // Update isOccupied (Lombok generates getIsOccupied for non-primitive Boolean)
        bin.setIsOccupied(true);
        assertTrue(bin.getIsOccupied());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        Bin a = new Bin(sameId, UUID.randomUUID(), "BIN-A",
                new BigDecimal("50"), true);
        Bin b = new Bin(sameId, UUID.randomUUID(), "BIN-B",
                new BigDecimal("100"), false);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
