package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import com.mes.domain.production.enums.ProcessStepType;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProcessStep extends BaseEntity {

    private UUID stepId;
    private UUID processFlowId;
    private Integer stepOrder;
    private String stepName;
    private ProcessStepType stepType;
    private Boolean isCcp;
    private Boolean isOprp;
    private Map<String, Object> ccpParameters;

    public ProcessStep(UUID stepId, UUID processFlowId, Integer stepOrder, String stepName,
                       ProcessStepType stepType, Boolean isCcp, Boolean isOprp,
                       Map<String, Object> ccpParameters) {
        super(stepId, java.time.LocalDateTime.now());
        this.stepId = stepId;
        this.processFlowId = processFlowId;
        this.stepOrder = stepOrder;
        this.stepName = stepName;
        this.stepType = stepType;
        this.isCcp = isCcp;
        this.isOprp = isOprp;
        this.ccpParameters = ccpParameters;
    }

    @Override
    public ProcessStep copy(UUID newId) {
        return new ProcessStep(
                newId, processFlowId, stepOrder, stepName, stepType, isCcp, isOprp, ccpParameters
        );
    }
}
