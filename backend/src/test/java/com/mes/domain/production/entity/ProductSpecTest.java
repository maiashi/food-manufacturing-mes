package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductSpecTest {

    private final UUID testId = UUID.randomUUID();
    private final UUID factoryId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final LocalDateTime now = LocalDateTime.now();

    private ProductSpec createSample() {
        return new ProductSpec(
                testId, factoryId, "SKU-001",
                new BigDecimal("10"), new BigDecimal("20"), Integer.valueOf(30),
                new BigDecimal("2"), new BigDecimal("8")
        );
    }

    @Test
    void constructor_setsAllFields() {
        ProductSpec spec = createSample();

        assertEquals(testId, spec.getId());
        assertEquals(factoryId, spec.getFactoryId());
        assertEquals("SKU-001", spec.getSkuCode());
        assertEquals(new BigDecimal("10"), spec.getMinWeightG());
        assertEquals(new BigDecimal("20"), spec.getMaxWeightG());
        assertEquals(Integer.valueOf(30), spec.getShelfLifeDays());
        assertEquals(new BigDecimal("2"), spec.getStorageTempMinC());
        assertEquals(new BigDecimal("8"), spec.getStorageTempMaxC());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        ProductSpec original = createSample();
        UUID newId = UUID.randomUUID();

        BaseEntity copied = original.copy(newId);

        assertNotEquals(original.getId(), copied.getId());
        assertEquals(factoryId, ((ProductSpec) copied).getFactoryId());
        assertEquals("SKU-001", ((ProductSpec) copied).getSkuCode());
        assertEquals(new BigDecimal("10"), ((ProductSpec) copied).getMinWeightG());
        assertEquals(new BigDecimal("20"), ((ProductSpec) copied).getMaxWeightG());
        assertEquals(Integer.valueOf(30), ((ProductSpec) copied).getShelfLifeDays());
        assertEquals(new BigDecimal("2"), ((ProductSpec) copied).getStorageTempMinC());
        assertEquals(new BigDecimal("8"), ((ProductSpec) copied).getStorageTempMaxC());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProductSpec spec = createSample();

        spec.setMinWeightG(new BigDecimal("5"));
        assertEquals(new BigDecimal("5"), spec.getMinWeightG());

        spec.setMaxWeightG(new BigDecimal("25"));
        assertEquals(new BigDecimal("25"), spec.getMaxWeightG());

        spec.setShelfLifeDays(Integer.valueOf(60));
        assertEquals(Integer.valueOf(60), spec.getShelfLifeDays());

        spec.setStorageTempMinC(new BigDecimal("-10"));
        assertEquals(new BigDecimal("-10"), spec.getStorageTempMinC());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        ProductSpec spec1 = createSample();
        ProductSpec spec2 = new ProductSpec(
                testId, // same id as spec1
                UUID.randomUUID(),
                "DIFFERENT-SKU",
                new BigDecimal("99"), new BigDecimal("88"), Integer.valueOf(1),
                new BigDecimal("0"), new BigDecimal("100")
        );

        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    void copyReturnsCorrectType() {
        ProductSpec original = createSample();
        BaseEntity result = original.copy(UUID.randomUUID());

        assertInstanceOf(ProductSpec.class, result);
    }
}
