package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CcpMonitoringRecordTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID FACTORY_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");
    private static final UUID LINE_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440002");
    private static final UUID STEP_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440003");
    private static final UUID BATCH_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440004");

    @Test
    void constructor_setsAllFields() {
        LocalDateTime monitoringDate = LocalDateTime.of(2026, 5, 15, 8, 30);
        Map<String, Double> values = new HashMap<>();
        values.put("temperature", 72.5);

        CcpMonitoringRecord record = new CcpMonitoringRecord(ID, FACTORY_ID, LINE_ID, STEP_ID,
                BATCH_ID, monitoringDate, values, "PASS", false);

        assertEquals(ID, record.getId());
        assertEquals(ID, record.getRecordId());
        assertEquals(FACTORY_ID, record.getFactoryId());
        assertEquals(LINE_ID, record.getLineId());
        assertEquals(STEP_ID, record.getStepId());
        assertEquals(BATCH_ID, record.getBatchId());
        assertEquals(monitoringDate, record.getMonitoringDate());
        assertNotNull(record.getMeasuredValues());
        assertEquals(1, record.getMeasuredValues().size());
        assertEquals(72.5, record.getMeasuredValues().get("temperature"));
        assertEquals("PASS", record.getResult());
        assertFalse(record.getDeviationTaken());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        Map<String, Double> values = new HashMap<>();
        values.put("pressure", 101.3);

        CcpMonitoringRecord original = new CcpMonitoringRecord(ID, FACTORY_ID, LINE_ID, STEP_ID,
                BATCH_ID, LocalDateTime.of(2026, 5, 15, 8, 30), values, "PASS", true);

        UUID newId = UUID.randomUUID();
        CcpMonitoringRecord copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getRecordId(), copy.getRecordId());
        assertEquals(FACTORY_ID, copy.getFactoryId());
        assertEquals(LINE_ID, copy.getLineId());
        assertEquals(BATCH_ID, copy.getBatchId());
        assertEquals("PASS", copy.getResult());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        CcpMonitoringRecord record = new CcpMonitoringRecord(ID, FACTORY_ID, LINE_ID, STEP_ID,
                BATCH_ID, LocalDateTime.of(2026, 5, 15, 8, 30), null, "PASS", false);

        assertEquals("PASS", record.getResult());
        record.setResult("FAIL");
        assertEquals("FAIL", record.getResult());

        assertFalse(record.getDeviationTaken());
        record.setDeviationTaken(true);
        assertTrue(record.getDeviationTaken());

        LocalDateTime newDate = LocalDateTime.of(2026, 6, 1, 9, 0);
        record.setMonitoringDate(newDate);
        assertEquals(newDate, record.getMonitoringDate());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        CcpMonitoringRecord rec1 = new CcpMonitoringRecord(sameId, FACTORY_ID, LINE_ID, STEP_ID,
                BATCH_ID, LocalDateTime.of(2026, 5, 15, 8, 30), null, "PASS", false);
        CcpMonitoringRecord rec2 = new CcpMonitoringRecord(sameId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.of(2026, 7, 1, 10, 0), null, "FAIL", true);

        assertEquals(rec1, rec2);
        assertEquals(rec1.hashCode(), rec2.hashCode());
    }
}
