package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CcpMonitoringRecord extends BaseEntity {

    private UUID recordId;
    private UUID factoryId;
    private UUID lineId;
    private UUID stepId;
    private UUID batchId;
    private LocalDateTime monitoringDate;
    private Map<String, Double> measuredValues;
    private String result; // "PASS", "FAIL", "NA"
    private Boolean deviationTaken;
    private Map<String, Double> criticalLimitMap; // CL-001/CL-002: クリティカルリミット値（温度/時間/pH等）

    public CcpMonitoringRecord(UUID recordId, UUID factoryId, UUID lineId, UUID stepId,
                               UUID batchId, LocalDateTime monitoringDate,
                               Map<String, Double> measuredValues, String result,
                               Boolean deviationTaken, Map<String, Double> criticalLimitMap) {
        super(recordId, java.time.LocalDateTime.now());
        this.recordId = recordId;
        this.factoryId = factoryId;
        this.lineId = lineId;
        this.stepId = stepId;
        this.batchId = batchId;
        this.monitoringDate = monitoringDate;
        this.measuredValues = measuredValues;
        this.result = result;
        this.deviationTaken = deviationTaken;
        this.criticalLimitMap = criticalLimitMap;
    }

    @Override
    public CcpMonitoringRecord copy(UUID newId) {
        return new CcpMonitoringRecord(newId, factoryId, lineId, stepId, batchId,
                monitoringDate, measuredValues, result, deviationTaken, criticalLimitMap);
    }

    /**
     * Checks if any measured value is outside its critical limit range.
     */
    public boolean hasDeviation() {
        if (deviationTaken == null || !deviationTaken) {
            return false;
        }
        if (measuredValues == null || criticalLimitMap == null) {
            return true; // No data = deviation
        }
        return measuredValues.entrySet().stream()
                .anyMatch(entry -> {
                    Double limit = criticalLimitMap.get(entry.getKey());
                    return limit != null && !Double.isNaN(limit)
                            && Math.abs(entry.getValue() - limit) > 0.01;
                });
    }

    /**
     * Compares measured value against its critical limit.
     */
    public boolean isWithinCriticalLimit(String parameterName, Double measuredValue) {
        if (criticalLimitMap == null || !criticalLimitMap.containsKey(parameterName)) {
            return true; // No limit defined for this parameter
        }
        Double limit = criticalLimitMap.get(parameterName);
        return limit != null && Math.abs(measuredValue - limit) <= 0.01;
    }
}
