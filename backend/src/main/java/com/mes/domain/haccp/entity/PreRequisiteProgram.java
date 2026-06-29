package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PreRequisiteProgram extends BaseEntity {

    private UUID prpId;
    private String programType;
    private String description;
    private Integer frequencyDays;
    private Boolean isActive;
    private UUID assignedToUserId; // PR-006/PR-008: 担当者（健康チェック/手指消毒等）

    public PreRequisiteProgram(UUID prpId, String programType, String description,
                               Integer frequencyDays, Boolean isActive,
                               UUID assignedToUserId) {
        super(prpId, java.time.LocalDateTime.now());
        this.prpId = prpId;
        this.programType = programType;
        this.description = description;
        this.frequencyDays = frequencyDays;
        this.isActive = isActive;
        this.assignedToUserId = assignedToUserId;
    }

    @Override
    public PreRequisiteProgram copy(UUID newId) {
        return new PreRequisiteProgram(newId, programType, description, frequencyDays,
                isActive, assignedToUserId);
    }
}
