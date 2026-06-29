package com.mes.domain.inventory.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();

        ReceiptItem item = new ReceiptItem(id, factoryId, UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("50"), "https://example.com/coa.pdf");

        assertEquals(id, item.getId());
        assertEquals(id, item.getReceiptItemId());
        assertEquals(factoryId, item.getFactoryId());
        assertNotNull(item.getReceiptId());
        assertNotNull(item.getMaterialId());
        assertNotNull(item.getLotId());
        assertEquals(new BigDecimal("50"), item.getReceivedQty());
        assertEquals("https://example.com/coa.pdf", item.getCoaFileUrl());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        UUID originalId = UUID.randomUUID();
        UUID factoryId = UUID.randomUUID();
        ReceiptItem original = new ReceiptItem(originalId, factoryId, UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, "https://example.com/old.pdf");
        UUID newId = UUID.randomUUID();

        ReceiptItem copied = original.copy(newId);

        assertNotSame(original, copied);
        assertNotSame(originalId, copied.getId());
        assertEquals(newId, copied.getId());
        assertEquals(factoryId, copied.getFactoryId());
        assertEquals(BigDecimal.TEN, copied.getReceivedQty());
        assertEquals("https://example.com/old.pdf", copied.getCoaFileUrl());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ReceiptItem item = new ReceiptItem(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE, null);

        // Update receivedQty
        item.setReceivedQty(new BigDecimal("200"));
        assertEquals(new BigDecimal("200"), item.getReceivedQty());

        // Update coaFileUrl
        item.setCoaFileUrl("https://example.com/new-coa.pdf");
        assertEquals("https://example.com/new-coa.pdf", item.getCoaFileUrl());

        // Update factoryId
        UUID newFactory = UUID.randomUUID();
        item.setFactoryId(newFactory);
        assertEquals(newFactory, item.getFactoryId());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        ReceiptItem a = new ReceiptItem(sameId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, "https://example.com/a.pdf");
        ReceiptItem b = new ReceiptItem(sameId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE, null);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
