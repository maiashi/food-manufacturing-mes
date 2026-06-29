package com.mes.domain.haccp.entity;

import com.mes.domain.haccp.enums.HaccpPlanType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HaccpPlanTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID FACTORY_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");

    @Test
    void constructor_setsAllFields() {
        HaccpPlan plan = new HaccpPlan(ID, FACTORY_ID, "v2.1", LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1), HaccpPlanType.CCP, true);

        assertEquals(ID, plan.getId());
        assertEquals(ID, plan.getHaccpPlanId());
        assertEquals(FACTORY_ID, plan.getFactoryId());
        assertEquals("v2.1", plan.getPlanVersion());
        assertEquals(LocalDate.of(2026, 1, 1), plan.getEffectiveDate());
        assertEquals(LocalDate.of(2027, 1, 1), plan.getExpiryDate());
        assertEquals(HaccpPlanType.CCP, plan.getType());
        assertTrue(plan.getIsActive());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        HaccpPlan original = new HaccpPlan(ID, FACTORY_ID, "v1.0", LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1), HaccpPlanType.OPRP, false);

        UUID newId = UUID.randomUUID();
        HaccpPlan copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getHaccpPlanId(), copy.getHaccpPlanId());
        assertEquals(FACTORY_ID, copy.getFactoryId());
        assertEquals("v1.0", copy.getPlanVersion());
        assertEquals(HaccpPlanType.OPRP, copy.getType());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        HaccpPlan plan = new HaccpPlan(ID, FACTORY_ID, "v1.0", LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1), HaccpPlanType.CCP, false);

        assertEquals(false, plan.getIsActive());
        plan.setIsActive(true);
        assertTrue(plan.getIsActive());

        plan.setPlanVersion("v3.0");
        assertEquals("v3.0", plan.getPlanVersion());

        LocalDate newExpiry = LocalDate.of(2028, 12, 31);
        plan.setExpiryDate(newExpiry);
        assertEquals(newExpiry, plan.getExpiryDate());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        HaccpPlan plan1 = new HaccpPlan(sameId, FACTORY_ID, "v1.0", LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1), HaccpPlanType.CCP, true);
        HaccpPlan plan2 = new HaccpPlan(sameId, UUID.randomUUID(), "v2.0", LocalDate.of(2026, 6, 1),
                LocalDate.of(2027, 6, 1), HaccpPlanType.PRP, false);

        assertEquals(plan1, plan2);
        assertEquals(plan1.hashCode(), plan2.hashCode());
    }

    @Test
    void allHaccpPlanTypes_valid() {
        for (HaccpPlanType type : Arrays.asList(HaccpPlanType.values())) {
            HaccpPlan plan = new HaccpPlan(ID, FACTORY_ID, "v1.0", LocalDate.of(2026, 1, 1),
                    null, type, true);
            assertEquals(type, plan.getType());
        }
    }
}
