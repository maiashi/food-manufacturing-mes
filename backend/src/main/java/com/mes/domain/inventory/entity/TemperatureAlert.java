package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureAlert extends BaseEntity {

    private UUID alertId;
    private UUID factoryId;
    private UUID warehouseId;
    private UUID zoneId;
    private String alertType; // 逸脱/警告/復旧
    private String severity; // 高/中/低
    private String message; // アラートメッセージ
    private boolean isResolved;
    private java.time.LocalDateTime occurredAt;
    private java.time.LocalDateTime resolvedAt;
    private UUID resolvedBy;

    public TemperatureAlert(UUID alertId, UUID factoryId, UUID warehouseId, UUID zoneId,
                            String alertType, String severity, String message, java.time.LocalDateTime occurredAt) {
        super(alertId, java.time.LocalDateTime.now());
        this.alertId = alertId;
        this.factoryId = factoryId;
        this.warehouseId = warehouseId;
        this.zoneId = zoneId;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.isResolved = false;
        this.occurredAt = occurredAt != null ? occurredAt : java.time.LocalDateTime.now();
        this.resolvedAt = null;
        this.resolvedBy = null;
    }

    public void resolve(UUID resolvedBy) {
        this.isResolved = true;
        this.resolvedAt = java.time.LocalDateTime.now();
        this.resolvedBy = resolvedBy;
    }

    @Override
    public TemperatureAlert copy(UUID newId) {
        TemperatureAlert copy = new TemperatureAlert(newId, factoryId, warehouseId, zoneId,
                alertType, severity, message, occurredAt);
        copy.setResolved(this.isResolved);
        copy.setResolvedAt(this.resolvedAt);
        copy.setResolvedBy(this.resolvedBy);
        return copy;
    }
}
