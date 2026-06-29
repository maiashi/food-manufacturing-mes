package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CcpMonitoringRecord extends BaseEntity {

    private UUID recordId;
    private UUID factoryId;
    private UUID ccpMapId;
    private UUID criticalLimitId;
    private BigDecimal monitoredValue;
    private String unit;
    private java.time.LocalDateTime monitoredAt;
    private UUID monitoredBy;
    private boolean isWithinLimit;
    private String deviationReason;
    private String correctiveActionTaken;

    public CcpMonitoringRecord(UUID recordId, UUID factoryId, UUID ccpMapId, UUID criticalLimitId,
                               BigDecimal monitoredValue, String unit, java.time.LocalDateTime monitoredAt,
                               UUID monitoredBy, boolean isWithinLimit) {
        super(recordId, java.time.LocalDateTime.now());
        this.recordId = recordId;
        this.factoryId = factoryId;
        this.ccpMapId = ccpMapId;
        this.criticalLimitId = criticalLimitId;
        this.monitoredValue = monitoredValue;
        this.unit = unit;
        this.monitoredAt = monitoredAt;
        this.monitoredBy = monitoredBy;
        this.isWithinLimit = isWithinLimit;
        this.deviationReason = null;
        this.correctiveActionTaken = null;
    }

    public void recordDeviation(String deviationReason, String correctiveActionTaken) {
        this.deviationReason = deviationReason;
        this.correctiveActionTaken = correctiveActionTaken;
        this.isWithinLimit = false;
    }

    @Override
    public CcpMonitoringRecord copy(UUID newId) {
        CcpMonitoringRecord copy = new CcpMonitoringRecord(newId, factoryId, ccpMapId, criticalLimitId,
                monitoredValue, unit, monitoredAt, monitoredBy, isWithinLimit);
        copy.setDeviationReason(this.deviationReason);
        copy.setCorrectiveActionTaken(this.correctiveActionTaken);
        return copy;
    }
}