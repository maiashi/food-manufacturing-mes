package com.mes.domain.haccp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NcrTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID FACTORY_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");
    private static final UUID LOT_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440005");

    @Test
    void constructor_setsAllFields() {
        LocalDateTime reportedDate = LocalDateTime.of(2026, 5, 10, 14, 20);
        UUID reporterId = UUID.randomUUID();

        Ncr ncr = new Ncr(ID, FACTORY_ID, "NCR-2026-0042", LOT_ID,
                "Equipment Malfunction", "Temperature sensor reading outside range during cooking step",
                reportedDate, reporterId);

        assertEquals(ID, ncr.getId());
        assertEquals(ID, ncr.getNcrId());
        assertEquals(FACTORY_ID, ncr.getFactoryId());
        assertEquals("NCR-2026-0042", ncr.getNcrNumber());
        assertEquals(LOT_ID, ncr.getLotId());
        assertEquals("Equipment Malfunction", ncr.getCauseCategory());
        assertEquals("Temperature sensor reading outside range during cooking step", ncr.getDescription());
        assertEquals(reportedDate, ncr.getReportedDate());
        assertEquals(reporterId, ncr.getReportedByUserId());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        Ncr original = new Ncr(ID, FACTORY_ID, "NCR-2026-0041", LOT_ID,
                "Raw Material", "Foreign object found in incoming shipment",
                LocalDateTime.of(2026, 5, 9, 10, 0), UUID.randomUUID());

        UUID newId = UUID.randomUUID();
        Ncr copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getNcrId(), copy.getNcrId());
        assertEquals(FACTORY_ID, copy.getFactoryId());
        assertEquals("NCR-2026-0041", copy.getNcrNumber());
        assertEquals(LOT_ID, copy.getLotId());
        assertEquals("Raw Material", copy.getCauseCategory());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Ncr ncr = new Ncr(ID, FACTORY_ID, "NCR-2026-0042", LOT_ID,
                "Equipment Malfunction", "Initial description",
                LocalDateTime.of(2026, 5, 10, 14, 20), UUID.randomUUID());

        assertEquals("Initial description", ncr.getDescription());
        ncr.setDescription("Updated: motor failure confirmed");
        assertEquals("Updated: motor failure confirmed", ncr.getDescription());

        ncr.setCauseCategory("Personnel Error");
        assertEquals("Personnel Error", ncr.getCauseCategory());

        UUID newReporter = UUID.randomUUID();
        ncr.setReportedByUserId(newReporter);
        assertEquals(newReporter, ncr.getReportedByUserId());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        Ncr ncr1 = new Ncr(sameId, FACTORY_ID, "NCR-2026-0042", LOT_ID,
                "Equipment Malfunction", "Desc A",
                LocalDateTime.of(2026, 5, 10, 14, 20), UUID.randomUUID());
        Ncr ncr2 = new Ncr(sameId, UUID.randomUUID(), "NCR-2026-0099", UUID.randomUUID(),
                "Environmental", "Desc B",
                LocalDateTime.of(2026, 6, 1, 8, 0), UUID.randomUUID());

        assertEquals(ncr1, ncr2);
        assertEquals(ncr1.hashCode(), ncr2.hashCode());
    }
}
