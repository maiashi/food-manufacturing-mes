package com.mes.domain.haccp.entity;

import com.mes.domain.haccp.enums.HazardType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HazardAnalysisTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID PLAN_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");
    private static final UUID STEP_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440003");

    @Test
    void constructor_setsAllFields() {
        HazardAnalysis analysis = new HazardAnalysis(ID, PLAN_ID, STEP_ID, HazardType.BIOLOGICAL,
                "Salmonella contamination risk", 5, 3, false);

        assertEquals(ID, analysis.getId());
        assertEquals(ID, analysis.getHazardId());
        assertEquals(PLAN_ID, analysis.getHaccpPlanId());
        assertEquals(STEP_ID, analysis.getStepId());
        assertEquals(HazardType.BIOLOGICAL, analysis.getHazardType());
        assertEquals("Salmonella contamination risk", analysis.getDescription());
        assertEquals(Integer.valueOf(5), analysis.getSeverityScore());
        assertEquals(Integer.valueOf(3), analysis.getProbabilityScore());
        assertFalse(analysis.getIsAcceptable());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        HazardAnalysis original = new HazardAnalysis(ID, PLAN_ID, STEP_ID, HazardType.CHEMICAL,
                "Cleaning agent residue", 4, 2, true);

        UUID newId = UUID.randomUUID();
        HazardAnalysis copy = original.copy(newId);

        assertNotSame(original, copy);
        assertNotSame(original.getHazardId(), copy.getHazardId());
        assertEquals(PLAN_ID, copy.getHaccpPlanId());
        assertEquals(HazardType.CHEMICAL, copy.getHazardType());
        assertEquals("Cleaning agent residue", copy.getDescription());
        assertEquals(Integer.valueOf(4), copy.getSeverityScore());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        HazardAnalysis analysis = new HazardAnalysis(ID, PLAN_ID, STEP_ID, HazardType.PHYSICAL,
                "Glass fragments", 3, 1, true);

        assertTrue(analysis.getIsAcceptable());
        analysis.setIsAcceptable(false);
        assertFalse(analysis.getIsAcceptable());

        analysis.setDescription("Metal shard risk");
        assertEquals("Metal shard risk", analysis.getDescription());

        analysis.setSeverityScore(8);
        assertEquals(Integer.valueOf(8), analysis.getSeverityScore());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        UUID sameId = UUID.randomUUID();

        HazardAnalysis a1 = new HazardAnalysis(sameId, PLAN_ID, STEP_ID, HazardType.BIOLOGICAL,
                "Hazard A", 5, 3, false);
        HazardAnalysis a2 = new HazardAnalysis(sameId, UUID.randomUUID(), UUID.randomUUID(),
                HazardType.CHEMICAL, "Hazard B", 1, 1, true);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void allHazardTypes_valid() {
        for (HazardType type : Arrays.asList(HazardType.values())) {
            HazardAnalysis analysis = new HazardAnalysis(ID, PLAN_ID, STEP_ID, type,
                    "Test hazard", 3, 2, true);
            assertEquals(type, analysis.getHazardType());
        }
    }
}
