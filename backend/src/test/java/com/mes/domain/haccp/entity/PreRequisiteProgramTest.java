package com.mes.domain.haccp.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PreRequisiteProgramTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @Test
    void constructor_setsAllFields() {
        UUID assignedToUserId = UUID.randomUUID();
        PreRequisiteProgram prp = new PreRequisiteProgram(ID, "Sanitation",
                "Daily floor cleaning and sanitization protocol", 1, true, assignedToUserId);

        assertEquals(ID, prp.getId());
        assertEquals(ID, prp.getPrpId());
        assertEquals("Sanitation", prp.getProgramType());
        assertEquals("Daily floor cleaning and sanitization protocol", prp.getDescription());
        assertEquals(Integer.valueOf(1), prp.getFrequencyDays());
        assertTrue(prp.getIsActive());
        assertEquals(assignedToUserId, prp.getAssignedToUserId());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID assignedToUserId = UUID.randomUUID();
        PreRequisiteProgram original = new PreRequisiteProgram(ID, "Pest Control",
                "Weekly pest inspection and treatment", 7, true, assignedToUserId);

        UUID newId = UUID.randomUUID();
        PreRequisiteProgram copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getPrpId(), copy.getPrpId());
        assertEquals("Pest Control", copy.getProgramType());
        assertEquals("Weekly pest inspection and treatment", copy.getDescription());
        assertEquals(Integer.valueOf(7), copy.getFrequencyDays());
        assertEquals(assignedToUserId, copy.getAssignedToUserId());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        UUID assignedToUserId = UUID.randomUUID();
        PreRequisiteProgram prp = new PreRequisiteProgram(ID, "Sanitation",
                "Initial description", 1, true, assignedToUserId);

        assertTrue(prp.getIsActive());
        prp.setIsActive(false);
        assertFalse(prp.getIsActive());

        prp.setDescription("Updated cleaning schedule");
        assertEquals("Updated cleaning schedule", prp.getDescription());

        prp.setProgramType("Hand Washing");
        assertEquals("Hand Washing", prp.getProgramType());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        UUID assignedToUserId = UUID.randomUUID();

        PreRequisiteProgram prp1 = new PreRequisiteProgram(sameId, "Sanitation",
                "Protocol A", 1, true, assignedToUserId);
        PreRequisiteProgram prp2 = new PreRequisiteProgram(sameId, "Pest Control",
                "Protocol B", 30, false, assignedToUserId);

        assertEquals(prp1, prp2);
        assertEquals(prp1.hashCode(), prp2.hashCode());
    }
}