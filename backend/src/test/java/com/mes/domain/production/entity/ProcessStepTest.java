package com.mes.domain.production.entity;

import com.mes.domain.production.enums.ProcessStepType;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProcessStepTest {

    @Test
    void constructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID flowId = UUID.randomUUID();
        Map<String, Object> params = Map.of("temperature", 180.0, "durationMin", 30);

        ProcessStep step = new ProcessStep(id, flowId, 1, "Mixing", ProcessStepType.MIXING,
                true, false, params);

        assertEquals(id, step.getId());
        assertEquals(flowId, step.getProcessFlowId());
        assertEquals(1, step.getStepOrder());
        assertEquals("Mixing", step.getStepName());
        assertEquals(ProcessStepType.MIXING, step.getStepType());
        assertTrue(step.getIsCcp());
        assertFalse(step.getIsOprp());
        assertNotNull(step.getCcpParameters());
        assertEquals(2, step.getCcpParameters().size());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        ProcessStep original = new ProcessStep(UUID.randomUUID(), UUID.randomUUID(), 1, "Mix",
                ProcessStepType.MIXING, true, false, Map.of("temp", 180.0));
        UUID newId = UUID.randomUUID();

        ProcessStep copied = original.copy(newId);

        assertNotSame(original, copied);
        assertEquals(newId, copied.getId());
        assertEquals(1, copied.getStepOrder());
        assertTrue(copied.getIsCcp());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        ProcessStep step = new ProcessStep(UUID.randomUUID(), UUID.randomUUID(), 1, "Mix",
                ProcessStepType.MIXING, false, false, null);

        step.setStepName("Heating");
        assertEquals("Heating", step.getStepName());

        step.setStepType(ProcessStepType.HEATING);
        assertEquals(ProcessStepType.HEATING, step.getStepType());

        step.setIsCcp(true);
        assertTrue(step.getIsCcp());

        step.setStepOrder(2);
        assertEquals(2, step.getStepOrder());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();
        ProcessStep a = new ProcessStep(sameId, UUID.randomUUID(), 1, "Mix",
                ProcessStepType.MIXING, true, false, null);
        ProcessStep b = new ProcessStep(sameId, UUID.randomUUID(), 2, "Heat",
                ProcessStepType.HEATING, false, true, Map.of("temp", 200.0));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void allProcessStepTypes_valid() {
        for (ProcessStepType type : ProcessStepType.values()) {
            ProcessStep step = new ProcessStep(UUID.randomUUID(), UUID.randomUUID(), 1, "Step",
                    type, false, false, null);
            assertNotNull(step);
            assertEquals(type, step.getStepType());
        }
    }

    @Test
    void copyReturnsCorrectType() {
        UUID newId = UUID.randomUUID();
        ProcessStep copied = new ProcessStep(UUID.randomUUID(), UUID.randomUUID(), 1, "X",
                ProcessStepType.OTHER, false, false, null).copy(newId);

        assertEquals(newId, copied.getId());
    }
}
