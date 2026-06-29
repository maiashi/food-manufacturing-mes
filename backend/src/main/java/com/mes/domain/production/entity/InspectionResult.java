package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.inventory.enums.InspectionResultType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InspectionResult extends BaseEntity {

    private UUID resultId;
    private UUID lotId;
    private UUID specId;
    private LocalDateTime inspectionDate;
    private UUID inspectorUserId;
    private InspectionResultType overallResult;

    public InspectionResult(UUID resultId, UUID lotId, UUID specId, LocalDateTime inspectionDate,
                            UUID inspectorUserId, InspectionResultType overallResult) {
        super(resultId, java.time.LocalDateTime.now());
        this.resultId = resultId;
        this.lotId = lotId;
        this.specId = specId;
        this.inspectionDate = inspectionDate;
        this.inspectorUserId = inspectorUserId;
        this.overallResult = overallResult;
    }

    @Override
    public InspectionResult copy(UUID newId) {
        return new InspectionResult(
                newId, lotId, specId, inspectionDate, inspectorUserId, overallResult
        );
    }
}
