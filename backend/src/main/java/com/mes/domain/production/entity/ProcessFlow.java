package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProcessFlow extends BaseEntity {

    private UUID processFlowId;
    private UUID factoryId;
    private UUID productId;
    private String flowName;
    private BigDecimal version;
    private LocalDate effectiveDate;
    private BigDecimal standardProcessingTimeMinutes; // PF-003: 標準作業時間（分）

    public ProcessFlow(UUID processFlowId, UUID factoryId, UUID productId, String flowName,
                       BigDecimal version, LocalDate effectiveDate,
                       BigDecimal standardProcessingTimeMinutes) {
        super(processFlowId, java.time.LocalDateTime.now());
        this.processFlowId = processFlowId;
        this.factoryId = factoryId;
        this.productId = productId;
        this.flowName = flowName;
        this.version = version;
        this.effectiveDate = effectiveDate;
        this.standardProcessingTimeMinutes = standardProcessingTimeMinutes;
    }

    @Override
    public ProcessFlow copy(UUID newId) {
        return new ProcessFlow(newId, factoryId, productId, flowName, version,
                effectiveDate, standardProcessingTimeMinutes);
    }
}
