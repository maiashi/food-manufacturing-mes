package com.mes.domain.haccp.service;

import com.mes.domain.haccp.entity.Capa;
import com.mes.domain.haccp.entity.CcpMonitoringRecord;
import com.mes.domain.haccp.entity.HazardAnalysis;
import com.mes.domain.haccp.enums.CapaStatus;

import java.util.UUID;

/**
 * Domain service for HACCP (Hazard Analysis and Critical Control Points) operations.
 * Pure domain logic — no Spring, no JPA annotations.
 */
public class HaccpService {

    /**
     * Assesses hazard risk based on severity and probability scores.
     * Returns a descriptive risk level string.
     *
     * Risk matrix:
     *   1–4: LOW
     *   5–9: MEDIUM
     *   10–16: HIGH
     *   17+: CRITICAL
     *
     * @param analysis the hazard analysis to assess
     * @return risk level string
     */
    public String assessHazard(HazardAnalysis analysis) {
        if (analysis == null) {
            throw new IllegalArgumentException("Hazard analysis must not be null");
        }

        Integer severity = analysis.getSeverityScore() != null ? analysis.getSeverityScore() : 0;
        Integer probability = analysis.getProbabilityScore() != null ? analysis.getProbabilityScore() : 0;

        int riskScore = severity * probability;

        if (riskScore <= 4) {
            return "LOW";
        } else if (riskScore <= 9) {
            return "MEDIUM";
        } else if (riskScore <= 16) {
            return "HIGH";
        } else {
            return "CRITICAL";
        }
    }

    /**
     * Validates a CCP monitoring record to determine whether measured values are within
     * acceptable critical limits.
     * A record is valid if its result indicates PASS or NA.
     *
     * @param record the CCP monitoring record to validate
     * @return true if within acceptable range, false otherwise
     */
    public boolean validateCcpRecord(CcpMonitoringRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("CCP monitoring record must not be null");
        }

        String result = record.getResult();
        return "PASS".equalsIgnoreCase(result) || "NA".equalsIgnoreCase(result);
    }

    /**
     * Triggers a corrective action when a CCP deviation is detected.
     * If the monitoring record indicates FAIL or another non-conforming state,
     * a new CAPA (Corrective and Preventive Action) entity is created.
     *
     * @param record the CCP monitoring record that triggered the deviation
     * @return a CAPA entity with reference to the original deviation, or null if no action needed
     */
    public Capa triggerCorrectiveAction(CcpMonitoringRecord record) {
        if (!validateCcpRecord(record)) {
            Capa capa = new Capa(
                    UUID.randomUUID(),
                    "CCP_MONITORING_RECORD",
                    record.getRecordId(),
                    "Deviation detected in CCP monitoring record: " + record.getResult(),
                    null, // rootCauseAnalysis - to be filled during investigation
                    null, // correctiveActionPlan - to be defined
                    java.time.LocalDate.now().plusWeeks(2),
                    null, // completedDate - not yet completed
                    CapaStatus.OPEN
            );
            return capa;
        }

        return null;
    }
}
