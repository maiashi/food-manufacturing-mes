package com.mes.domain.haccp.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.haccp.enums.HazardType;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class HazardAnalysis extends BaseEntity {

    private UUID hazardId;
    private UUID haccpPlanId;
    private UUID stepId;
    private HazardType hazardType;
    private String description;
    private Integer severityScore;
    private Integer probabilityScore;
    private Boolean isAcceptable;

    public HazardAnalysis(UUID hazardId, UUID haccpPlanId, UUID stepId, HazardType hazardType,
                          String description, Integer severityScore, Integer probabilityScore,
                          Boolean isAcceptable) {
        super(hazardId, java.time.LocalDateTime.now());
        this.hazardId = hazardId;
        this.haccpPlanId = haccpPlanId;
        this.stepId = stepId;
        this.hazardType = hazardType;
        this.description = description;
        this.severityScore = severityScore;
        this.probabilityScore = probabilityScore;
        this.isAcceptable = isAcceptable;
    }

    @Override
    public HazardAnalysis copy(UUID newId) {
        return new HazardAnalysis(newId, haccpPlanId, stepId, hazardType, description,
                severityScore, probabilityScore, isAcceptable);
    }
}
