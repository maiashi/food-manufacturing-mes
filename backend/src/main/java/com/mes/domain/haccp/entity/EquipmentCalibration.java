package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EquipmentCalibration extends BaseEntity {

    private UUID calId;
    private UUID stepId;
    private LocalDate calibrationDueDate;
    private LocalDate lastCalibratedDate;
    private LocalDate nextCalibrationDate;
    private String result;
    private UUID calibratedByUserId;

    public EquipmentCalibration(UUID calId, UUID stepId, LocalDate calibrationDueDate,
                                LocalDate lastCalibratedDate, LocalDate nextCalibrationDate,
                                String result, UUID calibratedByUserId) {
        super(calId, java.time.LocalDateTime.now());
        this.calId = calId;
        this.stepId = stepId;
        this.calibrationDueDate = calibrationDueDate;
        this.lastCalibratedDate = lastCalibratedDate;
        this.nextCalibrationDate = nextCalibrationDate;
        this.result = result;
        this.calibratedByUserId = calibratedByUserId;
    }

    @Override
    public EquipmentCalibration copy(UUID newId) {
        return new EquipmentCalibration(newId, stepId, calibrationDueDate, lastCalibratedDate,
                nextCalibrationDate, result, calibratedByUserId);
    }
}
