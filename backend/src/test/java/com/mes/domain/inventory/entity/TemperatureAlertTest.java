package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureAlertTest {

    @Test
    void testTemperatureAlertCreation() {
        UUID alertId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        String alertType = "逸脱";
        String severity = "高";
        String message = "温度逸脱エラー";
        java.time.LocalDateTime occurredAt = java.time.LocalDateTime.now();

        TemperatureAlert alert = new TemperatureAlert(alertId, factoryId, warehouseId, zoneId, alertType, severity, message, occurredAt);

        assertEquals(alertId, alert.getAlertId());
        assertEquals(factoryId, alert.getFactoryId());
        assertEquals(warehouseId, alert.getWarehouseId());
        assertEquals(zoneId, alert.getZoneId());
        assertEquals(alertType, alert.getAlertType());
        assertEquals(severity, alert.getSeverity());
        assertEquals(message, alert.getMessage());
        assertFalse(alert.isResolved());
        assertEquals(occurredAt, alert.getOccurredAt());
        assertNull(alert.getResolvedAt());
        assertNull(alert.getResolvedBy());
    }

    @Test
    void testResolveAlert() {
        UUID alertId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        String alertType = "逸脱";
        String severity = "高";
        String message = "温度逸脱エラー";
        java.time.LocalDateTime occurredAt = java.time.LocalDateTime.now();

        TemperatureAlert alert = new TemperatureAlert(alertId, factoryId, warehouseId, zoneId, alertType, severity, message, occurredAt);
        UUID resolvedBy = UUID.randomUUID();

        alert.resolve(resolvedBy);

        assertTrue(alert.isResolved());
        assertNotNull(alert.getResolvedAt());
        assertEquals(resolvedBy, alert.getResolvedBy());
    }

    @Test
    void testCopy() {
        UUID alertId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        String alertType = "逸脱";
        String severity = "高";
        String message = "温度逸脱エラー";
        java.time.LocalDateTime occurredAt = java.time.LocalDateTime.now();

        TemperatureAlert alert = new TemperatureAlert(alertId, factoryId, warehouseId, zoneId, alertType, severity, message, occurredAt);
        UUID newId = UUID.randomUUID();

        TemperatureAlert copy = alert.copy(newId);

        assertEquals(newId, copy.getAlertId());
        assertEquals(factoryId, copy.getFactoryId());
        assertFalse(copy.isResolved());
    }
}