package com.mes.domain.haccp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentCalibrationTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID STEP_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440003");

    @Test
    void constructor_setsAllFields() {
        LocalDate dueDate = LocalDate.of(2026, 6, 1);
        LocalDate lastCal = LocalDate.of(2026, 5, 1);
        LocalDate nextCal = LocalDate.of(2026, 8, 1);
        UUID calUserId = UUID.randomUUID();

        EquipmentCalibration cal = new EquipmentCalibration(ID, STEP_ID, dueDate, lastCal, nextCal, "OK", calUserId);

        assertEquals(ID, cal.getId());
        assertEquals(ID, cal.getCalId());
        assertEquals(STEP_ID, cal.getStepId());
        assertEquals(dueDate, cal.getCalibrationDueDate());
        assertEquals(lastCal, cal.getLastCalibratedDate());
        assertEquals(nextCal, cal.getNextCalibrationDate());
        assertEquals("OK", cal.getResult());
        assertEquals(calUserId, cal.getCalibratedByUserId());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        EquipmentCalibration original = new EquipmentCalibration(ID, STEP_ID,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 8, 1), "OK", UUID.randomUUID());

        UUID newId = UUID.randomUUID();
        EquipmentCalibration copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getCalId(), copy.getCalId());
        assertEquals(STEP_ID, copy.getStepId());
        assertEquals(LocalDate.of(2026, 6, 1), copy.getCalibrationDueDate());
        assertEquals("OK", copy.getResult());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        EquipmentCalibration cal = new EquipmentCalibration(ID, STEP_ID,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 8, 1), "OK", UUID.randomUUID());

        assertEquals("OK", cal.getResult());
        cal.setResult("FAIL");
        assertEquals("FAIL", cal.getResult());

        LocalDate newNext = LocalDate.of(2027, 1, 1);
        cal.setNextCalibrationDate(newNext);
        assertEquals(newNext, cal.getNextCalibrationDate());

        UUID newUserId = UUID.randomUUID();
        cal.setCalibratedByUserId(newUserId);
        assertEquals(newUserId, cal.getCalibratedByUserId());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        EquipmentCalibration cal1 = new EquipmentCalibration(sameId, STEP_ID,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 8, 1), "OK", UUID.randomUUID());
        EquipmentCalibration cal2 = new EquipmentCalibration(sameId, UUID.randomUUID(),
                LocalDate.of(2027, 1, 1), LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 4, 1), "FAIL", UUID.randomUUID());

        assertEquals(cal1, cal2);
        assertEquals(cal1.hashCode(), cal2.hashCode());
    }
}
