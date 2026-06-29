package com.mes.domain.inventory.entity;

import com.mes.domain.inventory.enums.InspectionResultType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        LocalDateTime receivedAt = LocalDateTime.of(2025, 6, 1, 9, 30);

        Receipt receipt = new Receipt(id, factoryId, UUID.randomUUID(),
                "REC-2025-001", InspectionResultType.PASS, receivedAt, UUID.randomUUID());

        assertEquals(id, receipt.getId());
        assertEquals(id, receipt.getReceiptId());
        assertEquals(factoryId, receipt.getFactoryId());
        assertEquals("REC-2025-001", receipt.getReceiptNumber());
        assertEquals(InspectionResultType.PASS, receipt.getInspectionResult());
        assertEquals(receivedAt, receipt.getReceivedAt());
        assertNotNull(receipt.getReceivedByUserId());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        LocalDateTime receivedAt = LocalDateTime.of(2025, 6, 1, 9, 30);
        Receipt original = new Receipt(originalId, factoryId, UUID.randomUUID(),
                "REC-COPY", InspectionResultType.FAIL, receivedAt, UUID.randomUUID());
        UUID newId = UUID.randomUUID();

        Receipt copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getId());
        assertEquals(factoryId, copied.getFactoryId());
        assertEquals("REC-COPY", copied.getReceiptNumber());
        assertEquals(InspectionResultType.FAIL, copied.getInspectionResult());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        Receipt receipt = new Receipt(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "REC-INIT", InspectionResultType.PENDING, LocalDateTime.now(), null);

        // Update receiptNumber
        receipt.setReceiptNumber("REC-UPDATED");
        assertEquals("REC-UPDATED", receipt.getReceiptNumber());

        // Update inspectionResult
        receipt.setInspectionResult(InspectionResultType.FAIL);
        assertEquals(InspectionResultType.FAIL, receipt.getInspectionResult());

        // Update receivedAt
        LocalDateTime newDate = LocalDateTime.of(2025, 7, 15, 14, 0);
        receipt.setReceivedAt(newDate);
        assertEquals(newDate, receipt.getReceivedAt());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        LocalDateTime dateA = LocalDateTime.of(2025, 6, 1, 9, 30);
        LocalDateTime dateB = LocalDateTime.of(2025, 8, 20, 11, 45);

        Receipt a = new Receipt(sameId, UUID.randomUUID(), UUID.randomUUID(),
                "REC-A", InspectionResultType.PASS, dateA, UUID.randomUUID());
        Receipt b = new Receipt(sameId, UUID.randomUUID(), UUID.randomUUID(),
                "REC-B", InspectionResultType.FAIL, dateB, null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
