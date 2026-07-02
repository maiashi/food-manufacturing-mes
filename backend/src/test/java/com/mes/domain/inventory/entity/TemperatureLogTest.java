package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureLogTest {

    @Test
    void testTemperatureLogCreation() {
        UUID logId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        BigDecimal temperature = new BigDecimal("5.0");
        BigDecimal humidity = new BigDecimal("60.0");

        TemperatureLog log = new TemperatureLog(logId, factoryId, warehouseId, zoneId, temperature, humidity, null);

        assertEquals(logId, log.getLogId());
        assertEquals(factoryId, log.getFactoryId());
        assertEquals(warehouseId, log.getWarehouseId());
        assertEquals(zoneId, log.getZoneId());
        assertEquals(temperature, log.getTemperature());
        assertEquals(humidity, log.getHumidity());
        assertEquals("正常", log.getStatus());
        assertNull(log.getErrorMessage());
    }

    @Test
    void testMarkViolation() {
        UUID logId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        BigDecimal temperature = new BigDecimal("5.0");
        BigDecimal humidity = new BigDecimal("60.0");

        TemperatureLog log = new TemperatureLog(logId, factoryId, warehouseId, zoneId, temperature, humidity, null);
        log.markViolation("温度逸脱エラー");

        assertEquals("逸脱", log.getStatus());
        assertEquals("温度逸脱エラー", log.getErrorMessage());
    }

    @Test
    void testMarkWarning() {
        UUID logId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        BigDecimal temperature = new BigDecimal("5.0");
        BigDecimal humidity = new BigDecimal("60.0");

        TemperatureLog log = new TemperatureLog(logId, factoryId, warehouseId, zoneId, temperature, humidity, null);
        log.markWarning("温度警告");

        assertEquals("警告", log.getStatus());
        assertEquals("温度警告", log.getErrorMessage());
    }

    @Test
    void testCopy() {
        UUID logId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        BigDecimal temperature = new BigDecimal("5.0");
        BigDecimal humidity = new BigDecimal("60.0");

        TemperatureLog log = new TemperatureLog(logId, factoryId, warehouseId, zoneId, temperature, humidity, null);
        UUID newId = UUID.randomUUID();

        TemperatureLog copy = log.copy(newId);

        assertEquals(newId, copy.getLogId());
        assertEquals(factoryId, copy.getFactoryId());
        assertEquals("正常", copy.getStatus());
    }
}