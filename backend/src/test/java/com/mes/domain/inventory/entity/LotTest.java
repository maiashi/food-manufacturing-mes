package com.mes.domain.inventory.entity;

import com.mes.domain.inventory.enums.LotStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LotTest {

    private static final UUID TEST_FACTORY = UUID.randomUUID();
    private static final UUID TEST_MATERIAL = UUID.randomUUID();
    private static final LocalDate NOW_DATE = LocalDate.now();

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID supplierId = UUID.randomUUID();
        UUID parentLotId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        UUID shelfId = UUID.randomUUID();
        UUID binId = UUID.randomUUID();

        Lot lot = new Lot(id, TEST_FACTORY, "LOT-2025-001", TEST_MATERIAL, supplierId,
                parentLotId, NOW_DATE, NOW_DATE.plusDays(365), new BigDecimal("1000.50"),
                LotStatus.RECEIVED, warehouseId, zoneId, shelfId, binId, false);

        assertEquals(id, lot.getId());
        assertEquals(id, lot.getLotId());
        assertEquals(TEST_FACTORY, lot.getFactoryId());
        assertEquals("LOT-2025-001", lot.getLotNumber());
        assertEquals(TEST_MATERIAL, lot.getMaterialId());
        assertEquals(supplierId, lot.getSupplierId());
        assertEquals(parentLotId, lot.getParentLotId());
        assertEquals(NOW_DATE, lot.getManufacturingDate());
        assertEquals(NOW_DATE.plusDays(365), lot.getExpiryDate());
        assertEquals(new BigDecimal("1000.50"), lot.getQuantity());
        assertEquals(LotStatus.RECEIVED, lot.getStatus());
        assertEquals(warehouseId, lot.getWarehouseId());
        assertEquals(zoneId, lot.getZoneId());
        assertEquals(shelfId, lot.getShelfId());
        assertEquals(binId, lot.getBinId());
        assertFalse(lot.getIsQuarantined());
    }

    @Test
    void copy_returnsNewLotWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID supplierId = UUID.randomUUID();
        UUID parentLotId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID zoneId = UUID.randomUUID();
        UUID shelfId = UUID.randomUUID();
        UUID binId = UUID.randomUUID();
        
        Lot original = new Lot(originalId, TEST_FACTORY, "LOT-001", TEST_MATERIAL, supplierId,
                parentLotId, NOW_DATE, NOW_DATE.plusDays(180), BigDecimal.TEN, LotStatus.RESERVED,
                warehouseId, zoneId, shelfId, binId, false);
        UUID newId = UUID.randomUUID();

        Lot copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals("LOT-001", copied.getLotNumber());
    }
}