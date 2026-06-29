package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.haccp.enums.CapaStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CapaTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID REF_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");

    @Test
    void constructor_setsAllFields() {
        CapaStatus status = CapaStatus.OPEN;
        LocalDate dueDate = LocalDate.of(2026, 12, 31);
        LocalDateTime completed = LocalDateTime.of(2026, 6, 15, 10, 30);
        UUID refId = REF_ID;

        Capa capa = new Capa(ID, "INCIDENT", refId, "Defective packaging found",
                "Seal integrity failure", "Replace sealant equipment Q4", dueDate, completed, status);

        assertEquals(ID, capa.getId());
        assertEquals(ID, capa.getCapaId());
        assertEquals("INCIDENT", capa.getReferenceType());
        assertEquals(refId, capa.getReferenceId());
        assertEquals("Defective packaging found", capa.getDescription());
        assertEquals("Seal integrity failure", capa.getRootCauseAnalysis());
        assertEquals("Replace sealant equipment Q4", capa.getCorrectiveActionPlan());
        assertEquals(dueDate, capa.getDueDate());
        assertEquals(completed, capa.getCompletedDate());
        assertEquals(status, capa.getStatus());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        Capa original = new Capa(ID, "INCIDENT", REF_ID, "Original description",
                "Root cause analysis text", "Corrective plan here",
                LocalDate.of(2026, 9, 1), LocalDateTime.now(), CapaStatus.OPEN);

        UUID newId = UUID.randomUUID();
        Capa copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getCapaId(), copy.getCapaId());
        assertEquals("INCIDENT", copy.getReferenceType());
        assertEquals(REF_ID, copy.getReferenceId());
        assertEquals("Original description", copy.getDescription());
        assertEquals("Root cause analysis text", copy.getRootCauseAnalysis());
        assertEquals("Corrective plan here", copy.getCorrectiveActionPlan());
        assertEquals(LocalDate.of(2026, 9, 1), copy.getDueDate());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Capa capa = new Capa(ID, "INCIDENT", REF_ID, "Old description",
                "Old root cause", "Old plan", LocalDate.of(2026, 1, 1),
                null, CapaStatus.OPEN);

        assertEquals("Old description", capa.getDescription());
        capa.setDescription("Updated defect report");
        assertEquals("Updated defect report", capa.getDescription());

        assertEquals(CapaStatus.OPEN, capa.getStatus());
        capa.setStatus(CapaStatus.IN_PROGRESS);
        assertEquals(CapaStatus.IN_PROGRESS, capa.getStatus());

        LocalDate newDue = LocalDate.of(2027, 3, 15);
        capa.setDueDate(newDue);
        assertEquals(newDue, capa.getDueDate());

        LocalDateTime completed = LocalDateTime.now();
        capa.setCompletedDate(completed);
        assertEquals(completed, capa.getCompletedDate());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        Capa capa1 = new Capa(sameId, "INCIDENT", REF_ID, "Desc A",
                "Root A", "Plan A", LocalDate.of(2026, 1, 1), null, CapaStatus.OPEN);
        Capa capa2 = new Capa(sameId, "NON_CONFORMANCE", UUID.randomUUID(), "Desc B",
                "Root B", "Plan B", LocalDate.of(2027, 1, 1), null, CapaStatus.CLOSED);

        assertEquals(capa1, capa2);
        assertEquals(capa1.hashCode(), capa2.hashCode());
    }
}
