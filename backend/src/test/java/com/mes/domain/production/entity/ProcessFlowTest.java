package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessFlowTest {

    private final UUID testId = UUID.randomUUID();
    private final UUID factoryId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID productId = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private ProcessFlow createSample() {
        return new ProcessFlow(
                testId, factoryId, productId, "Cooking Flow",
                new BigDecimal("2.0"), LocalDate.of(2025, 6, 1), new BigDecimal("30.0")
        );
    }

    @Test
    void constructor_setsAllFields() {
        ProcessFlow flow = createSample();

        assertEquals(testId, flow.getId());
        assertEquals(factoryId, flow.getFactoryId());
        assertEquals(productId, flow.getProductId());
        assertEquals("Cooking Flow", flow.getFlowName());
        assertEquals(new BigDecimal("2.0"), flow.getVersion());
        assertEquals(LocalDate.of(2025, 6, 1), flow.getEffectiveDate());
        assertEquals(new BigDecimal("30.0"), flow.getStandardProcessingTimeMinutes());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        ProcessFlow original = createSample();
        UUID newId = UUID.randomUUID();

        BaseEntity copied = original.copy(newId);

        assertNotEquals(original.getId(), copied.getId());
        assertEquals(factoryId, ((ProcessFlow) copied).getFactoryId());
        assertEquals(productId, ((ProcessFlow) copied).getProductId());
        assertEquals("Cooking Flow", ((ProcessFlow) copied).getFlowName());
        assertEquals(new BigDecimal("2.0"), ((ProcessFlow) copied).getVersion());
        assertEquals(LocalDate.of(2025, 6, 1), ((ProcessFlow) copied).getEffectiveDate());
        assertEquals(new BigDecimal("30.0"), ((ProcessFlow) copied).getStandardProcessingTimeMinutes());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProcessFlow flow = createSample();

        flow.setFlowName("Pasteurization Flow");
        assertEquals("Pasteurization Flow", flow.getFlowName());

        flow.setVersion(new BigDecimal("3.1"));
        assertEquals(new BigDecimal("3.1"), flow.getVersion());
        
        flow.setStandardProcessingTimeMinutes(new BigDecimal("45.0"));
        assertEquals(new BigDecimal("45.0"), flow.getStandardProcessingTimeMinutes());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        ProcessFlow flow1 = createSample();
        ProcessFlow flow2 = new ProcessFlow(
                testId, // same id as flow1
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Completely Different Flow",
                BigDecimal.ONE,
                LocalDate.of(2099, 12, 31),
                BigDecimal.ONE
        );

        assertEquals(flow1, flow2);
        assertEquals(flow1.hashCode(), flow2.hashCode());
    }
}