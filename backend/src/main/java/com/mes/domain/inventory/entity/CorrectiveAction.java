package com.mes.domain.inventory.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrectiveAction extends BaseEntity {

    private UUID actionId;
    private UUID factoryId;
    private UUID ccpMonitoringRecordId;
    private String deviationDescription;
    private String correctiveProcedure;
    private String correctiveActionTaken;
    private String impactAssessment;
    private java.time.LocalDateTime actionTakenAt;
    private UUID takenBy;

    public CorrectiveAction(UUID actionId, UUID factoryId, UUID ccpMonitoringRecordId,
                            String deviationDescription, String correctiveProcedure,
                            UUID takenBy) {
        super(actionId, java.time.LocalDateTime.now());
        this.actionId = actionId;
        this.factoryId = factoryId;
        this.ccpMonitoringRecordId = ccpMonitoringRecordId;
        this.deviationDescription = deviationDescription;
        this.correctiveProcedure = correctiveProcedure;
        this.correctiveActionTaken = null;
        this.impactAssessment = null;
        this.actionTakenAt = java.time.LocalDateTime.now();
        this.takenBy = takenBy;
    }

    public void completeAction(String correctiveActionTaken, String impactAssessment) {
        this.correctiveActionTaken = correctiveActionTaken;
        this.impactAssessment = impactAssessment;
    }

    @Override
    public CorrectiveAction copy(UUID newId) {
        CorrectiveAction copy = new CorrectiveAction(newId, factoryId, ccpMonitoringRecordId,
                deviationDescription, correctiveProcedure, takenBy);
        copy.setCorrectiveActionTaken(this.correctiveActionTaken);
        copy.setImpactAssessment(this.impactAssessment);
        copy.setActionTakenAt(this.actionTakenAt);
        return copy;
    }
}