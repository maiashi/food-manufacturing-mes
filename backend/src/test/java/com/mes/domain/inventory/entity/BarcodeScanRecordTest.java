package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class BarcodeScanRecordTest {

    @Test
    void testBarcodeScanRecordCreation() {
        UUID scanId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        String scanType = "LOT_NUMBER";
        String scanValue = "LOT-2026-001";
        String actionType = "CREATE_TRANSFER";
        UUID scannedBy = UUID.randomUUID();

        BarcodeScanRecord record = new BarcodeScanRecord(scanId, factoryId, scanType, scanValue, actionType, scannedBy);

        assertEquals(scanId, record.getScanId());
        assertEquals(factoryId, record.getFactoryId());
        assertEquals(scanType, record.getScanType());
        assertEquals(scanValue, record.getScanValue());
        assertEquals(actionType, record.getActionType());
        assertEquals("処理中", record.getStatus());
        assertEquals(scannedBy, record.getScannedBy());
        assertNotNull(record.getScannedAt());
        assertNull(record.getErrorMessage());
        assertNull(record.getRelatedTransferId());
    }

    @Test
    void testCompleteRecord() {
        UUID scanId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        String scanType = "LOT_NUMBER";
        String scanValue = "LOT-2026-001";
        String actionType = "CREATE_TRANSFER";
        UUID scannedBy = UUID.randomUUID();

        BarcodeScanRecord record = new BarcodeScanRecord(scanId, factoryId, scanType, scanValue, actionType, scannedBy);
        record.complete();

        assertEquals("完了", record.getStatus());
        assertNull(record.getErrorMessage());
    }

    @Test
    void testErrorRecord() {
        UUID scanId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        String scanType = "LOT_NUMBER";
        String scanValue = "INVALID-LOT";
        String actionType = "CREATE_TRANSFER";
        UUID scannedBy = UUID.randomUUID();

        BarcodeScanRecord record = new BarcodeScanRecord(scanId, factoryId, scanType, scanValue, actionType, scannedBy);
        record.error("No lot found for lot number: INVALID-LOT");

        assertEquals("エラー", record.getStatus());
        assertEquals("No lot found for lot number: INVALID-LOT", record.getErrorMessage());
    }

    @Test
    void testCopy() {
        UUID scanId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        String scanType = "LOT_NUMBER";
        String scanValue = "LOT-2026-001";
        String actionType = "CREATE_TRANSFER";
        UUID scannedBy = UUID.randomUUID();

        BarcodeScanRecord record = new BarcodeScanRecord(scanId, factoryId, scanType, scanValue, actionType, scannedBy);
        UUID newId = UUID.randomUUID();

        BarcodeScanRecord copy = record.copy(newId);

        assertEquals(newId, copy.getScanId());
        assertEquals(factoryId, copy.getFactoryId());
        assertEquals("処理中", copy.getStatus());
    }
}