package com.mes.domain.production.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMasterTest {

    private static final UUID TEST_PRODUCT_ID = UUID.randomUUID();

    @Test
    void constructor_setsAllFields() {
        String skuCode = "SKU-PROD-001";
        ProductMaster product = new ProductMaster(TEST_PRODUCT_ID, skuCode,
                "Premium Butter", "Dairy", "Refrigerated 2-8°C");

        assertEquals(TEST_PRODUCT_ID, product.getId());
        assertEquals(skuCode, product.getSkuCode());
        assertEquals("Premium Butter", product.getProductName());
        assertEquals("Dairy", product.getCategory());
        assertEquals("Refrigerated 2-8°C", product.getStorageCondition());
    }

    @Test
    void copy_returnsNewProductWithDifferentId() {
        ProductMaster original = new ProductMaster(
                UUID.randomUUID(), "SKU-COPY-01", "Copy Butter", "Dairy", "Refrigerated");
        UUID newId = UUID.randomUUID();

        ProductMaster copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals("Copy Butter", copied.getProductName());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProductMaster product = new ProductMaster(
                UUID.randomUUID(), "SKU-UPDATE", "Old Name", "Other", "Ambient");

        product.setProductName("Updated Name");
        assertEquals("Updated Name", product.getProductName());

        product.setCategory("Dairy");
        assertEquals("Dairy", product.getCategory());

        product.setStorageCondition("Frozen -18°C");
        assertEquals("Frozen -18°C", product.getStorageCondition());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        ProductMaster a = new ProductMaster(sameId, "SKU-A", "Product A", "Dairy", "2-8C");
        ProductMaster b = new ProductMaster(sameId, "SKU-B", "Product B", "Bakery", "Ambient");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void copyReturnsCorrectType() {
        UUID newId = UUID.randomUUID();
        ProductMaster copied = new ProductMaster(
                UUID.randomUUID(), "SKU-X", "X", "Other", null).copy(newId);

        assertEquals(newId, copied.getId());
        assertEquals("X", copied.getProductName());
    }
}
