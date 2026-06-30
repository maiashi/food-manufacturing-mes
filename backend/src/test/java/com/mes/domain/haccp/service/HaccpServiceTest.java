package com.mes.domain.haccp.service;

import com.mes.domain.haccp.entity.Capa;
import com.mes.domain.haccp.entity.CcpMonitoringRecord;
import com.mes.domain.haccp.entity.HazardAnalysis;
import com.mes.domain.haccp.enums.CapaStatus;
import com.mes.domain.haccp.enums.HazardType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HaccpServiceTest {

    private final HaccpService service = new HaccpService();

    // --- assessHazard tests ---

    @Test
    void assessHazard_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.assessHazard(null));
    }

    @Test
    void assessHazard_lowRisk_returnsLow() {
        HazardAnalysis analysis = buildAnalysis(2, 2); // severity=2, prob=2 => score=4 => LOW
        assertEquals("LOW", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_mediumRisk_returnsMedium() {
        HazardAnalysis analysis = buildAnalysis(3, 3); // score=9 => MEDIUM
        assertEquals("MEDIUM", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_highRisk_returnsHigh() {
        HazardAnalysis analysis = buildAnalysis(4, 4); // score=16 => HIGH
        assertEquals("HIGH", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_criticalRisk_returnsCritical() {
        HazardAnalysis analysis = buildAnalysis(5, 5); // score=25 => CRITICAL
        assertEquals("CRITICAL", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_nullScores_treatedAsZero() {
        HazardAnalysis analysis = new HazardAnalysis(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                HazardType.BIOLOGICAL, "Test", null, null, false);
        assertEquals("LOW", service.assessHazard(analysis)); // 0 * 0 = 0 => LOW
    }

    @Test
    void assessHazard_zeroSeverity_returnsLow() {
        HazardAnalysis analysis = buildAnalysis(0, 5); // score=0 => LOW
        assertEquals("LOW", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_boundaryMedium() {
        HazardAnalysis analysis = buildAnalysis(1, 5); // score=5 => MEDIUM boundary
        assertEquals("MEDIUM", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_boundaryHigh() {
        HazardAnalysis analysis = buildAnalysis(2, 5); // score=10 => HIGH boundary
        assertEquals("HIGH", service.assessHazard(analysis));
    }

    @Test
    void assessHazard_boundaryCritical() {
        HazardAnalysis analysis = buildAnalysis(4, 5); // score=20 => CRITICAL boundary
        assertEquals("CRITICAL", service.assessHazard(analysis));
    }

    // --- validateCcpRecord tests ---

    @Test
    void validateCcpRecord_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.validateCcpRecord(null));
    }

    @Test
    void validateCcpRecord_pass_returnsTrue() {
        CcpMonitoringRecord record = buildCcpRecord("PASS");
        assertTrue(service.validateCcpRecord(record));
    }

    @Test
    void validateCcpRecord_na_returnsTrue() {
        CcpMonitoringRecord record = buildCcpRecord("NA");
        assertTrue(service.validateCcpRecord(record));
    }

    @Test
    void validateCcpRecord_fail_returnsFalse() {
        CcpMonitoringRecord record = buildCcpRecord("FAIL");
        assertFalse(service.validateCcpRecord(record));
    }

    @Test
    void validateCcpRecord_caseInsensitivePass() {
        CcpMonitoringRecord record = buildCcpRecord("pass");
        assertTrue(service.validateCcpRecord(record));
    }

    // --- triggerCorrectiveAction tests ---

    @Test
    void triggerCorrectiveAction_pass_returnsNull() {
        CcpMonitoringRecord record = buildCcpRecord("PASS");
        assertNull(service.triggerCorrectiveAction(record));
    }

    @Test
    void triggerCorrectiveAction_na_returnsNull() {
        CcpMonitoringRecord record = buildCcpRecord("NA");
        assertNull(service.triggerCorrectiveAction(record));
    }

    @Test
    void triggerCorrectiveAction_fail_createsCapa() {
        UUID recordId = UUID.randomUUID();
        CcpMonitoringRecord record = new CcpMonitoringRecord(
                recordId, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(),
                Map.of("temperature", 75.0), "FAIL", false, Map.of("temperature", 75.0));

        Capa capa = service.triggerCorrectiveAction(record);

        assertNotNull(capa);
        assertEquals("CCP_MONITORING_RECORD", capa.getReferenceType());
        assertEquals(recordId, capa.getReferenceId());
        assertTrue(capa.getDescription().contains("Deviation detected"));
        assertEquals(CapaStatus.OPEN, capa.getStatus());
    }

    @Test
    void triggerCorrectiveAction_fail_capaHasDueDate() {
        CcpMonitoringRecord record = buildCcpRecord("FAIL");
        Capa capa = service.triggerCorrectiveAction(record);

        assertNotNull(capa.getDueDate());
        // Due date should be ~2 weeks from now
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(
                java.time.LocalDate.now(), capa.getDueDate());
        assertTrue(daysBetween >= 13 && daysBetween <= 15, "Due date should be approximately 2 weeks");
    }

    // --- helpers ---

    private HazardAnalysis buildAnalysis(int severity, int probability) {
        return new HazardAnalysis(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                HazardType.BIOLOGICAL, "Test hazard", severity, probability, false);
    }

    private CcpMonitoringRecord buildCcpRecord(String result) {
        return new CcpMonitoringRecord(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(),
                Map.of("temperature", 75.0), result, false, Map.of("temperature", 75.0));
    }
}
