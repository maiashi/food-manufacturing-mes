package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MaterialMasterTest {

    @Test
    void fullConstructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        MaterialMaster entity = new MaterialMaster(
                id, "SKU-001", "Wheat Flour", "Grain", true,
                List.of("Gluten"), "Cool and dry", "kg", now, "365", "http://example.com/coa.pdf");

        assertEquals(id, entity.getId());
        assertEquals(id, entity.getMaterialId());
        assertEquals("SKU-001", entity.getSkuCode());
        assertEquals("Wheat Flour", entity.getMaterialName());
        assertEquals("Grain", entity.getCategory());
        assertTrue(entity.getIsAllergen());
        assertEquals(List.of("Gluten"), entity.getAllergenTypes());
        assertEquals("Cool and dry", entity.getStorageCondition());
        assertEquals("kg", entity.getUnit());
        assertEquals("365", entity.getShelfLifeDays());
        assertEquals("http://example.com/coa.pdf", entity.getCoaFileUrl());
    }

    @Test
    void copy_createsNewInstanceWithDifferentId() {
        MaterialMaster original = new MaterialMaster(
                UUID.randomUUID(), "SKU-001", "Sugar", null, false, List.of(), "Dry", "kg", LocalDateTime.now(), "180", "http://example.com/coa.pdf"
        );
        UUID newId = UUID.randomUUID();

        MaterialMaster copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals(newId, copied.getMaterialId());
        assertEquals("SKU-001", copied.getSkuCode());
        assertEquals("180", copied.getShelfLifeDays());
        assertEquals("http://example.com/coa.pdf", copied.getCoaFileUrl());
    }

    @Test
    void equalsAndHashCode_worksViaBaseEntity() {
        UUID sameId = UUID.randomUUID();
        MaterialMaster a = new MaterialMaster(sameId, "A", "Prod A", "X", false, null, "", "", LocalDateTime.now(), "90", "http://example.com/a.pdf");
        MaterialMaster b = new MaterialMaster(sameId, "B", "Prod B", "Y", true, List.of(), "", "", LocalDateTime.of(2025, 1, 1, 0, 0), "180", "http://example.com/b.pdf");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}