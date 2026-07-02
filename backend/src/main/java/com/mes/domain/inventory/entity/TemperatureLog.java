package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureLog extends BaseEntity {

    private UUID logId;
    private UUID factoryId;
    private UUID warehouseId;
    private UUID zoneId;
    private BigDecimal temperature; // 温度(℃)
    private BigDecimal humidity; // 湿度(%)
    private String status; // 正常/逸脱/警告
    private String errorMessage; // 逸脱時のエラーメッセージ
    private java.time.LocalDateTime recordedAt;

    public TemperatureLog(UUID logId, UUID factoryId, UUID warehouseId, UUID zoneId,
                          BigDecimal temperature, BigDecimal humidity, java.time.LocalDateTime recordedAt) {
        super(logId, java.time.LocalDateTime.now());
        this.logId = logId;
        this.factoryId = factoryId;
        this.warehouseId = warehouseId;
        this.zoneId = zoneId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.recordedAt = recordedAt != null ? recordedAt : java.time.LocalDateTime.now();
        this.status = "正常";
        this.errorMessage = null;
    }

    public void markViolation(String errorMessage) {
        this.status = "逸脱";
        this.errorMessage = errorMessage;
    }

    public void markWarning(String errorMessage) {
        this.status = "警告";
        this.errorMessage = errorMessage;
    }

    @Override
    public TemperatureLog copy(UUID newId) {
        TemperatureLog copy = new TemperatureLog(newId, factoryId, warehouseId, zoneId,
                temperature, humidity, recordedAt);
        copy.setStatus(this.status);
        copy.setErrorMessage(this.errorMessage);
        return copy;
    }
}
