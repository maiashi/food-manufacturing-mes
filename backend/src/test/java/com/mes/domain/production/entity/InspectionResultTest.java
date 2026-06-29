package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.InspectionResultType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InspectionResultTest {

    private final UUID testId = UUID.randomUUID();
    private final UUID lotId = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private final UUID specId = UUID.fromString("00000000-0000-0000-0000-000000000005");
    private final LocalDateTime inspectionDate = LocalDateTime.of(2025, 6, 1, 10, 30);
    private final UUID inspectorUserId = UUID.fromString("00000000-0000-0000-0000-000000000006");

    private InspectionResult createSample() {
        return new InspectionResult(
                testId, lotId, specId, inspectionDate, inspectorUserId,
                InspectionResultType.PASS
        );
    }

    @Test
    void constructor_setsAllFields() {
        InspectionResult result = createSample();

        assertEquals(testId, result.getId());
        assertEquals(lotId, result.getLotId());
        assertEquals(specId, result.getSpecId());
        assertEquals(inspectionDate, result.getInspectionDate());
        assertEquals(inspectorUserId, result.getInspectorUserId());
        assertEquals(InspectionResultType.PASS, result.getOverallResult());
    }

    @Test
    void copy_returnsNewWithDifferentId() {
        InspectionResult original = createSample();
        UUID newId = UUID.randomUUID();

        BaseEntity copied = original.copy(newId);

        assertNotEquals(original.getId(), copied.getId());
        assertEquals(lotId, ((InspectionResult) copied).getLotId());
        assertEquals(specId, ((InspectionResult) copied).getSpecId());
        assertEquals(inspectionDate, ((InspectionResult) copied).getInspectionDate());
        assertEquals(inspectorUserId, ((InspectionResult) copied).getInspectorUserId());
        assertEquals(InspectionResultType.PASS, ((InspectionResult) copied).getOverallResult());
    }

    @Test
    void setters_updateFieldsCorrectly() {
        InspectionResult result = createSample();

        result.setLotId(UUID.randomUUID());
        assertNotNull(result.getLotId());

        result.setInspectionDate(LocalDateTime.of(2026, 1, 15, 8, 0));
        assertEquals(LocalDateTime.of(2026, 1, 15, 8, 0), result.getInspectionDate());

        result.setOverallResult(InspectionResultType.FAIL);
        assertEquals(InspectionResultType.FAIL, result.getOverallResult());
    }

    @Test
    void equals_inheritedFromBaseEntity_comparesById() {
        InspectionResult result1 = createSample();
        InspectionResult result2 = new InspectionResult(
                testId, // same id as result1
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2099, 12, 31, 23, 59),
                UUID.randomUUID(),
                InspectionResultType.FAIL
        );

        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }
}
